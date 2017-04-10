package level3;

import java.util.ArrayList;

import shiffman.box2d.*;

import org.jbox2d.dynamics.contacts.*;

import processing.core.PApplet;

public class Processing extends PApplet {
	// A reference to our box2d world
	public static int GRID_SIZE = 6;
	public static boolean START = false;
	public static final int SCREEN_SIZE = 900;
	public static int PIPE_LENGTH = SCREEN_SIZE / GRID_SIZE;
	public static int PIPE_WIDTH = 4;
	public static boolean END = false;
	public static boolean WIN = false;
	public ArrayList<Path> verticalPaths = new ArrayList<Path>();
	public ArrayList<Path> horizontalPaths = new ArrayList<Path>();
	public Box2DProcessing box2d;
	ArrayList<Wall> walls;

	private Challenge brain;
	private Wall wall;
	private Roomba roomba;
	private EndZone zone;

	public Processing() {
		verticalPaths.add(new Path(0, 5));
		verticalPaths.add(new Path(2, 4));
		verticalPaths.add(new Path(2, 3));
		verticalPaths.add(new Path(4, 2));
		verticalPaths.add(new Path(4, 1));
		horizontalPaths.add(new Path(2, 4));
		horizontalPaths.add(new Path(1, 4));
		horizontalPaths.add(new Path(3, 2));
		horizontalPaths.add(new Path(4, 2));
		horizontalPaths.add(new Path(1, 4));
		horizontalPaths.add(new Path(5, 0));
		// horizontalPaths.add(new Path(2,1));
		// horizontalPaths.add(new Path(1,1));
	}

	public void settings() {
		size(SCREEN_SIZE, SCREEN_SIZE);
		box2d = new Box2DProcessing(this);
	}

	public void setup() {
		// create box2d world
		box2d.createWorld();
		box2d.setGravity(0, 0);
		box2d.listenForCollisions();
		walls = new ArrayList<Wall>();
		roomba = new Roomba(PIPE_LENGTH / 2, PIPE_LENGTH / 2 * 9, PIPE_LENGTH / 6, box2d);
		zone = new EndZone(SCREEN_SIZE - PIPE_LENGTH / 2, PIPE_LENGTH / 2, PIPE_LENGTH / 4, box2d);
		brain = new Challenge(roomba);
		setMaze();
	}

	public void draw() {
		background(255);
		// use this step method to sleep?
		box2d.step();
		roomba.display(this);
		if (END) {
			textSize(40);
			if (WIN) {
				text("You Win!", 400, 300);

			} else {
				text("You touched a wall!", 200, 200);
			}
		} else if (START != true) {
			textSize(40);
			text("Click to start!", 200, 300);
			if (mousePressed && END != true) {
				START = true;
			}
		} else {
			brain.go();
		}
		zone.display(this);
		drawMaze();

	}

	void setMaze() {
		int offset = PIPE_LENGTH / 2;
		boolean bound;
		for (int i = 0; i < GRID_SIZE + 1; i++) {
			for (int j = 0; j < GRID_SIZE + 1; j++) {
				bound = j == 0 || j == GRID_SIZE;
				boolean setVert = true;
				boolean setHorz = true;
				for (Path p : verticalPaths) {
					if (p.row == i && p.col == j) {
						setVert = false;
					}
				}
				if (setVert) {
					walls.add(
							new Wall(PIPE_LENGTH * i + offset, PIPE_LENGTH * j, PIPE_LENGTH, PIPE_WIDTH, bound, box2d));
				}

				for (Path p : horizontalPaths) {
					if (p.row == i && p.col == j) {
						setHorz = false;
					}
				}
				if (setHorz) {
					walls.add(
							new Wall(PIPE_LENGTH * i, PIPE_LENGTH * j + offset, PIPE_WIDTH, PIPE_LENGTH, bound, box2d));
					// cells.add(Cell(offset*(i+1), offset*(j+1)));
				}
			}
		}
	}

	void drawMaze() {
		for (int i = walls.size() - 1; i >= 0; i--) {
			Wall p = walls.get(i);
			p.display(this);
			if (START != true) {
				if (p.done(this)) {
					// walls.remove(i);
				}
			}
		}
	}

	public void beginContact(Contact cp) {
		if (!(cp.getFixtureA().getBody() == zone.getBody()) && !(cp.getFixtureB().getBody() == (zone.getBody()))) {
			brain.setBump(true);
			fill(0);
			END = true;
			START = false;
			roomba.killBody();
		} else {
			END = true;
			WIN = true;
		}
	}

	public void endContact(Contact cp) {
		brain.setBump(false);
	}

}