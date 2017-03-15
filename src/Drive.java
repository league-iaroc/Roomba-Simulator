public class Drive {
	float left;
	float right;

	int mil = 0;
	boolean sleep = false;
	long start = -1;
	public Drive(float left, float right) {
		this.left = left;
		this.right = right;
	}

	float getLeft() {
		return left;
	}

	float getRight() {
		return right;
	}
	void setLeft(float left) {
		this.left = left;
	}
	void setRight(float right) {
		this.right = right;
	}
	boolean isSleeping() {
		return sleep;
	}

	void check() {
		if (start == -1)
			start = System.currentTimeMillis();

		if (System.currentTimeMillis() - start >= mil) {
			sleep = false;
		}
	}
	void setSleep(int mil) {
		this.mil = mil;
		this.sleep = true;
	}
}