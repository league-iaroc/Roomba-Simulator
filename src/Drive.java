public class Drive {
	private float left;
	private float right;

	int mil = 0;
	boolean sleep = false;
	long start = -1;

	public Drive(float left, float right) {
		if (left > 0) {
			this.left = Math.min(left, 1000);
		} else {
			this.left = Math.max(left, -1000);
		}
		if (right > 0) {
			this.right = Math.min(right, 1000);
		} else {
			this.right = Math.max(right, -1000);
		}
		
	}

	public float getLeft() {
		return left;
	}

	public float getRight() {
		return right;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public boolean isSleeping() {
		return sleep;
	}

	public void check() {
		if (start == -1) {
			start = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - start >= mil) {
			sleep = false;
		}
	}

	public void setSleep(int mil) {
		this.mil = mil;
		this.sleep = true;
	}
}