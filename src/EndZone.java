
public class EndZone {
	private int red = 100;
	private int increment = 2;


void display(Processing g){
	red += increment;
	g.fill(red, 255, red);
	g.noStroke();
	if (red <= 0 || red >= 255) {
		increment = -increment;
	}
	g.ellipse(Processing.SCREEN_SIZE - Processing.PIPE_LENGTH / 2, Processing.PIPE_LENGTH / 2, Processing.PIPE_LENGTH / 2, Processing.PIPE_LENGTH / 2);
}
}
