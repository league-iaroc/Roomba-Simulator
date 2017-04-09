
public class EndZone {
	private int red = 100;
	private int increment = 2;
	int x;
	int y;
	int r;

	public EndZone(int x, int y, int r) {

		this.x = x;
		this.y = y;
		this.r = r;
	}

	void display(Processing g) {
		red += increment;
		g.fill(red, 255, red);
		g.noStroke();
		if (red <= 0 || red >= 255) {
			increment = -increment;
		}
		g.ellipse(x, y, r * 2, r * 2);
	}
}
