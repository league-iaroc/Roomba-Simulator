package level4;
import java.util.ArrayList;

public abstract class Head {
	Roomba roomba;
	float left = 0;
	float right = 0;
	int milliseconds = 0;
	ArrayList<Drive> commands;
	Drive current;
	boolean rightBump;
	boolean leftBump;

	public Head(Roomba roomba) {
		this.roomba = roomba;
		commands = new ArrayList<Drive>();
		commands.add(new Drive(0, 0));
		initialize();
		current = commands.get(0);
	}

	public abstract void initialize();

	public abstract void loop();

	void go() {
		roomba.driveDirect(current.getLeft(), current.getRight());
		if (!(current.isSleeping()) && commands.size() > 0) {
			current = commands.get(0);
			commands.remove(0);
		} else if (current.isSleeping()) {
			current.check();
		} else {
			this.loop();
		}
	}

	/**
	 * This command lets you control the forward and backward motion of Roomba’s
	 * drive wheels independently.  A positive velocity makes
	 * that wheel drive forward, while a negative velocity makes it drive
	 * backward.
	 * 
	 * @param Left wheel velocity
	 * @param Right wheel velocity
	 */
	void driveDirect(float l, float r) {
		Drive d = new Drive(l, r);
		commands.add(d);
	}

	void sleep(int mils) {
		Drive s = commands.get(commands.size() - 1);
		s.setSleep(mils);
	}

	int getMilliseconds() {
		driveDirect(left, right);
		milliseconds--;
		return milliseconds;
	}

	void setBump(boolean b) {
		leftBump = b;
		rightBump = b;
	}

	boolean isBumpedLeft() {
		return leftBump;
	}

	boolean isBumpedRight() {
		return rightBump;
	}

	void readSensors(int num) {

	}
	
	

}