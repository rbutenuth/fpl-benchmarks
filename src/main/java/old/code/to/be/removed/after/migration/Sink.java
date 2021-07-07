package old.code.to.be.removed.after.migration;

public class Sink<T> {
	private volatile T value;
	
	public void use(T value) {
		this.value = value;
	}
	
	public T get() {
		return value;
	}
}
