import processing.core.PApplet;

public class Brain extends Head {
	Roomba roomba;
	public static void main(String[] args) {
		PApplet.main("App");
	}
	
	
	public Brain(Roomba roomba) {
		super(roomba);
		this.roomba = roomba;
	}

	void initialize() {
		driveDirect(-500, -500);
		sleep(1000);

	}

	void loop() {
		readSensors(0);
		if (isBumpedRight()) {
			driveDirect(-500, -100);
			System.out.println(roomba.getAngle());
			sleep(500);
		} else {
			driveDirect(500, 0);
		}

	}

}