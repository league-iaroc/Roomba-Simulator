import java.util.Base64;
import java.util.Random;

import org.jointheleague.Challenge;
import org.jointheleague.Root;

public class MyChallenge extends Challenge {
	public static void main(String[] args) {
		new MyChallenge();
	}

	private Random random = new Random();
	
	@Override
	public void init() {
		Root root = this.root(new String(Base64.getDecoder().decode(getBase64EncodedPassword().getBytes())));
		driveDirect(-random.nextInt(1000), random.nextInt(1000));
	}

	@Override
	public void loop() {
	}
}
