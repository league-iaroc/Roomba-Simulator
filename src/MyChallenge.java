import java.awt.Point;
import java.util.Base64;
import java.util.Random;

import org.jointheleague.Challenge;
import org.jointheleague.Processing;
import org.jointheleague.Root;

public class MyChallenge extends Challenge {
	public static void main(String[] args) {
		new MyChallenge();
	}

	private Random random = new Random();
	private Root root;

	@Override
	public void init() {
		root = this.root(new String(Base64.getDecoder().decode(getBase64EncodedPassword().getBytes())));
		driveDirect(100, 100);
	}

	@Override
	public void loop() {
		Point p = new Point(Processing.getProcessing().mouseX, Processing.getProcessing().mouseY);
		String id = root.getRoombaID(p.x, p.y);
		if (id != null) {
			root.sendCommand(id, "driveDirect:0,0");
		}
	}
}
