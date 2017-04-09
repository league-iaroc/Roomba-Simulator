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
		driveDirect(500,500);
	}

	public void loop() {
		driveDirect(800,1000);
		if(isBumpedLeft()&& isBumpedRight()){
	
	}
	}
}
