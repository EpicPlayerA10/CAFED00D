package me.coley.cafedude.instruction;

/**
 * Represent method handle.
 *
 * @author xDark
 */
public class MethodHandle {

	private int type;
	private String owner;
	private String name;
	private String desc;

	/**
	 * @param type
	 * 		Method type.
	 * @param owner
	 * 		Method owner.
	 * @param name
	 * 		Method name.
	 * @param desc
	 * 		Method desc.
	 */
	public MethodHandle(int type, String owner, String name, String desc) {
		this.type = type;
		this.owner = owner;
		this.name = name;
		this.desc = desc;
	}

	/**
	 * @return method type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets method type.
	 *
	 * @param type
	 * 		New type.
	 */
	public void setType(int type) {
		this.type = type;
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
		if (!(o instanceof MethodHandle)) return false;

		MethodHandle that = (MethodHandle) o;

		if (type != that.type) return false;
		if (!owner.equals(that.owner)) return false;
		if (!name.equals(that.name)) return false;
		return desc.equals(that.desc);
	}

	@Override
	public int hashCode() {
		int result = type;
		result = 31 * result + owner.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + desc.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "handle(" + type + ", " + owner + ", " + name + ", " + desc + ')';
	}
}
