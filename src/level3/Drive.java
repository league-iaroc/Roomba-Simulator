package level3;
public class Drive {
	public static final int MAX_SPEED = 1000;
	private float left;
	private float right;
	int mil = 0;
	boolean sleep = false;
	long start = -1;

	public Drive(float left, float right) {
		if (left >= 0) {
			this.left = Math.min(left, MAX_SPEED);
		} else {
			this.left = Math.max(left, -MAX_SPEED);
		}
		if (right >= 0) {
			this.right = Math.min(right, MAX_SPEED);
		} else {
			this.right = Math.max(right, -MAX_SPEED);
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