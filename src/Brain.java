import processing.core.PApplet;

public class Brain extends Head {
	Roomba roomba;

	public static void main(String[] args) {
		PApplet.main("Processing");
	}

	public Brain(Roomba roomba) {
		super(roomba);
	}

	public void initialize() {
		driveDirect(100,100);

	}

	public void loop() {
		
	}

}
