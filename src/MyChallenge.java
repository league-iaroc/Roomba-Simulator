import java.util.Random;

import org.jointheleague.Challenge;

public class MyChallenge extends Challenge {
	public static void main(String[] args) {
		new MyChallenge();
	}

	private Random random = new Random();
	
	@Override
	public void init() {
		driveDirect(-random.nextInt(1000), random.nextInt(1000));
	}

	@Override
	public void loop() {
	}
}
