package old.code.to.be.removed.after.migration;

public abstract class AbstractRunner implements Runner {
	protected int problemSize;
	
	@Override
	public void prepare(int problemSize) {
		this.problemSize = problemSize;
	}

	@Override
	public void cleanup() {
		problemSize = 0;
	}
}
