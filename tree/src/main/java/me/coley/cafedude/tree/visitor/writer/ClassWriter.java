package me.coley.cafedude.tree.visitor.writer;

import me.coley.cafedude.InvalidClassException;
import me.coley.cafedude.classfile.AttributeConstants;
import me.coley.cafedude.classfile.ClassFile;
import me.coley.cafedude.classfile.ConstPool;
import me.coley.cafedude.classfile.Descriptor;
import me.coley.cafedude.classfile.attribute.*;
import me.coley.cafedude.io.ClassBuilder;
import me.coley.cafedude.io.ClassFileWriter;
import me.coley.cafedude.tree.visitor.*;
import me.coley.cafedude.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static me.coley.cafedude.classfile.attribute.InnerClassesAttribute.InnerClass;

/**
 * Class visitor implementation for writing back to a {@link ClassFile}.
 *
 * @author Justus Garbe
 */
public class ClassWriter extends DeclarationWriter implements ClassVisitor {
	final ClassBuilder builder;
	private final List<InnerClass> innerClasses = new ArrayList<>();
	private final List<RecordAttribute.RecordComponent> recordComponents = new ArrayList<>();

	public ClassWriter(int versionMajor, int versionMinor) {
		super(new Symbols(new ConstPool()));
		this.builder = new ClassBuilder();
		// set version minor and major
		builder.setVersionMajor(versionMajor);
		builder.setVersionMinor(versionMinor);
		builder.setConstPool(symbols.pool);
	}

	@Override
	public void visitClass(@Nonnull String name, int access, @Nonnull String superName, String... interfaces) {
		builder.setThisClass(symbols.newClass(name));
		builder.setAccess(access);
		builder.setSuperClass(symbols.newClass(superName));
		for (String anInterface : interfaces) {
			builder.addInterface(symbols.newClass(anInterface));
		}
	}

	@Nonnull
	@Override
	public MethodVisitor visitMethod(@Nonnull String name, int access, @Nonnull Descriptor descriptor) {
		return new MethodWriter(symbols, access, symbols.newUtf8(name), symbols.newUtf8(descriptor.getDescriptor()),
				builder::addMethod);
	}

	@Nonnull
	@Override
	public FieldVisitor visitField(@Nonnull String name, int access, @Nonnull Descriptor descriptor) {
		return new FieldWriter(symbols, access, symbols.newUtf8(name), symbols.newUtf8(descriptor.getDescriptor()),
				builder::addField);
	}

	@Override
	@SuppressWarnings("DataFlowIssue")
	public void visitOuterClass(@Nonnull String owner, @Nullable String name, @Nullable Descriptor descriptor) {
		attributes.add(new EnclosingMethodAttribute(
				symbols.newUtf8(AttributeConstants.ENCLOSING_METHOD),
				symbols.newClass(owner),
				Optional.orNull(name, n -> symbols.newNameType(name, descriptor))));
	}

	@Override
	public void visitInnerClass(String name, @Nullable String outerName, @Nullable String innerName, int access) {
		innerClasses.add(new InnerClass(
				symbols.newClass(name),
				Optional.orNull(outerName, symbols::newClass),
				Optional.orNull(innerName, symbols::newUtf8),
				access));
	}

	@Override
	public void visitSource(@Nullable String source, @Nullable byte[] debug) {
		if (source != null) {
			attributes.add(new SourceFileAttribute(
					symbols.newUtf8(AttributeConstants.SOURCE_FILE),
					symbols.newUtf8(source)));
		}
		if (debug != null) {
			attributes.add(new SourceDebugExtensionAttribute(
					symbols.newUtf8(AttributeConstants.SOURCE_DEBUG_EXTENSION),
					debug));
		}
	}

	@Override
	public ModuleVisitor visitModule(@Nonnull String name, int access, @Nullable String version) {
		return new ModuleWriter(symbols, symbols.newModule(name), access, Optional.orNull(version, symbols::newUtf8),
				attributes::addAll);
	}

	@Override
	public RecordComponentVisitor visitRecordComponent(@Nonnull String name, @Nonnull Descriptor descriptor) {
		return new RecordComponentWriter(symbols, symbols.newUtf8(name), symbols.newUtf8(descriptor.getDescriptor()),
				recordComponents::add);
	}

	@Override
	public void visitClassEnd() {
		super.visitDeclarationEnd();
		if (!innerClasses.isEmpty()) {
			attributes.add(new InnerClassesAttribute(
					symbols.newUtf8(AttributeConstants.INNER_CLASSES),
					innerClasses));
		}
		if (!recordComponents.isEmpty()) {
			attributes.add(new RecordAttribute(
					symbols.newUtf8(AttributeConstants.RECORD),
					recordComponents));
		}
		for (Attribute attribute : attributes) {
			builder.addAttribute(attribute);
		}
	}

	public byte[] toByteArray() throws InvalidClassException {
		builder.setConstPool(symbols.pool);
		ClassFile file = builder.build();
		ClassFileWriter writer = new ClassFileWriter();
		return writer.write(file);
	}
}
