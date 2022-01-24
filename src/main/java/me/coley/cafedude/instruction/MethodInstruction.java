package me.coley.cafedude.instruction;

/**
 * Method instruction.
 *
 * @author xDark
 */
public class MethodInstruction extends BasicInstruction {

	private String owner;
	private String name;
	private String desc;

	/**
	 * @param opcode
	 * 		Instruction opcode.
	 * @param owner
	 * 		Method owner.
	 * @param name
	 * 		Method name.
	 * @param desc
	 * 		Method desc.
	 */
	public MethodInstruction(int opcode, String owner, String name, String desc) {
		super(opcode);
		this.owner = owner;
		this.name = name;
		this.desc = desc;
	}

	/**
	 * @return method owner.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Sets method owner.
	 *
	 * @param owner
	 * 		New owner.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return method name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets method name.
	 *
	 * @param name
	 * 		New name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return method descriptor.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets method descriptor.
	 *
	 * @param desc
	 * 		New descriptor.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MethodInstruction)) return false;
		if (!super.equals(o)) return false;

		MethodInstruction that = (MethodInstruction) o;

		if (!owner.equals(that.owner)) return false;
		if (!name.equals(that.name)) return false;
		return desc.equals(that.desc);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + owner.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + desc.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "method(" + getOpcode() + ", " + owner + ", " + name + ", " + desc + ')';
	}
}
