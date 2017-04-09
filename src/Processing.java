import java.util.ArrayList;

import shiffman.box2d.*;

import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

import processing.core.PApplet;

public class Processing extends PApplet {
	// A reference to our box2d world
	public static int GRID_SIZE = 4;
	public static boolean START = false;

	public static final int SCREEN_SIZE = 800;
	public static final int PIPE_LENGTH = SCREEN_SIZE / GRID_SIZE;
	public static final int PIPE_WIDTH = 4;

	public Box2DProcessing box2d;
	// ArrayList<Cell> cells;
	ArrayList<Wall> walls;

	private Brain brain;
	private Wall wall;
	private Roomba roomba;
	private EndZone zone = new EndZone(SCREEN_SIZE - PIPE_LENGTH / 2, PIPE_LENGTH / 2, PIPE_LENGTH /4 );

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
		roomba = new Roomba(100, 100, PIPE_LENGTH / 6, box2d);
		brain = new Brain(roomba);
		setMaze();
	}

	public void draw() {
		background(255);
		// use this step method to sleep?
		box2d.step();
		roomba.display(this);
		if (START != true) {
			if (mousePressed) {
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
				if (!((j == 3 && i == 2) || (j == 2 && i == 2) || (j == 1 && i == 3) || (j == 1 && i == 0)))
					walls.add(
							new Wall(PIPE_LENGTH * i + offset, PIPE_LENGTH * j, PIPE_LENGTH, PIPE_WIDTH, bound, box2d));
				bound = i == 0 || i == GRID_SIZE;
				if (!((j == 1 && i == 2) || (j == 1 && i == 3) || (j == 1 && i == 2) || (j == 1 && i == 1)))
					walls.add(
							new Wall(PIPE_LENGTH * i, PIPE_LENGTH * j + offset, PIPE_WIDTH, PIPE_LENGTH, bound, box2d));
				// cells.add(Cell(offset*(i+1), offset*(j+1)));
			}
		}
	}

	void drawMaze() {
		for (int i = walls.size() - 1; i >= 0; i--) {
			Wall p = walls.get(i);
			p.display(this);
			if (START != true) {
				if (p.done(this)) {
					//walls.remove(i);
				}
			}
		}
	}

	public void beginContact(Contact cp) {
		brain.setBump(true);
	}

	public void endContact(Contact cp) {
		brain.setBump(false);
	}

}