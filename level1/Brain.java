package level1;

import processing.core.PApplet;

public class Brain extends Head {
	Roomba roomba;

	public static void main(String[] args) {
		PApplet.main("level1.Processing");
	}

	public Brain(Roomba roomba) {
		super(roomba);
	}

	public void initialize() {
		driveDirect(500, 500);
		sleep(8000);
		driveDirect(100, 200);
		sleep(3800);
		driveDirect(400, 400);
		sleep(9000);
		driveDirect(100, 200);
		sleep(3800);
		driveDirect(400, 400);
	}

	public void loop() {
	
	}
}
