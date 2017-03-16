import java.util.ArrayList;

import shiffman.box2d.*;

import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

import processing.core.PApplet;

public class Pgraphics extends PApplet {
	// A reference to our box2d world
	public static final int GRID_SIZE = 8;
	public static final int SCREEN_SIZE = 1000;
	public static final int PIPE_LENGTH = SCREEN_SIZE / GRID_SIZE;
	public static final int PIPE_WIDTH = 10;

	public Box2DProcessing box2d;
	// ArrayList<Cell> cells;
	ArrayList<Wall> walls;

	private Brain brain;
	private Wall wall;
	private Roomba roomba;

	private int red = 100;
	private int increment = 2;
	boolean start = false;

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
		roomba = new Roomba(20, 50, PIPE_LENGTH / 6, box2d);
		brain = new Brain(roomba);
		setMaze();
	}

	public void draw() {
		background(255);
		// use this step method to sleep?
		box2d.step();
		roomba.display(this);
		if (start != true) {
			if (mousePressed) {
				start = true;
				System.out.println("hel");
			}
		} else {
			brain.go();
		}
		drawMaze();
		drawFinish();
	}

	void setMaze() {
		int offset = PIPE_LENGTH / 2;
		boolean bound;
		for (int i = 0; i < GRID_SIZE + 1; i++) {
			for (int j = 0; j < GRID_SIZE + 1; j++) {
				bound = j == 0 || j == GRID_SIZE;
				walls.add(new Wall(PIPE_LENGTH * i + offset, PIPE_LENGTH * j, PIPE_LENGTH, PIPE_WIDTH, bound, box2d));
				bound = i == 0 || i == GRID_SIZE;
				walls.add(new Wall(PIPE_LENGTH * i, PIPE_LENGTH * j + offset, PIPE_WIDTH, PIPE_LENGTH, bound, box2d));
				// cells.add(Cell(offset*(i+1), offset*(j+1)));
			}
		}
	}

	void drawMaze() {
		for (int i = walls.size() - 1; i >= 0; i--) {
			Wall p = walls.get(i);
			p.display(this);
			if (start != true) {
				if (p.done(this)) {
					walls.remove(i);
				}
			}
		}
	}

	void drawFinish() {
		if (start) {
			red += increment;
		}
		fill(red, 255, red);
		noStroke();
		if (red <= 0 || red >= 255) {
			increment = -increment;
		}
		ellipse(width - PIPE_LENGTH / 2, PIPE_LENGTH / 2, PIPE_LENGTH / 2, PIPE_LENGTH / 2);
	}

	public void beginContact(Contact cp) {
		brain.setBump(true);
	}

	public void endContact(Contact cp) {
		brain.setBump(false);
	}

}