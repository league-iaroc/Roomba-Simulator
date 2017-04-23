package org.jointheleague;

import java.util.ArrayList;

import org.jbox2d.dynamics.contacts.Contact;

import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

public class Processing extends PApplet {
	// A reference to our box2d world
	public static int GRID_SIZE = 4;
	public static boolean START = false;
	public static final int SCREEN_SIZE = 900;
	public static int PIPE_LENGTH = SCREEN_SIZE / GRID_SIZE;
	public static int PIPE_WIDTH = 4;
	public static boolean END = false;
	public static boolean WIN = false;
	private ArrayList<Path> verticalPaths = new ArrayList<Path>();
	private ArrayList<Path> horizontalPaths = new ArrayList<Path>();
	public Box2DProcessing box2d;
	private ArrayList<Wall> walls;

	private Roomba roomba;
	private EndZone zone;

	public Processing() {
		verticalPaths.add(new Path(0, 1));
		verticalPaths.add(new Path(3, 1));
		horizontalPaths.add(new Path(2, 1));
		horizontalPaths.add(new Path(3, 1));
		horizontalPaths.add(new Path(2, 1));
		horizontalPaths.add(new Path(1, 1));
	}

	public void settings() {
		size(SCREEN_SIZE, SCREEN_SIZE);
		box2d = new Box2DProcessing(this);
	}

	public void setup() {
		box2d.createWorld();
		box2d.setGravity(0, 0);
		box2d.listenForCollisions();
		walls = new ArrayList<Wall>();
		roomba = new Roomba(PIPE_LENGTH / 2, PIPE_LENGTH / 2, PIPE_LENGTH / 6, box2d);
		zone = new EndZone(SCREEN_SIZE - PIPE_LENGTH / 2, PIPE_LENGTH / 2, PIPE_LENGTH / 4, box2d);
		setMaze();

		Challenge.getCurrent().initialize(roomba);
	}

	public void draw() {
		background(255);
		
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
				Challenge.getCurrent().init();
				START = true;
			}
		} else {
			Challenge.getCurrent().update();
		}
		zone.display(this);
		drawMaze();

	}

	void setMaze() {
		int offset = PIPE_LENGTH / 2;
		for (int i = 0; i < GRID_SIZE + 1; i++) {
			for (int j = 0; j < GRID_SIZE + 1; j++) {
				boolean setVert = true;
				boolean setHorz = true;
				for (Path p : verticalPaths) {
					if (p.getRow() == i && p.getColumn() == j) {
						setVert = false;
					}
				}
				if (setVert) {
					walls.add(new Wall(PIPE_LENGTH * i + offset, PIPE_LENGTH * j, PIPE_LENGTH, PIPE_WIDTH, box2d));
				}

				for (Path p : horizontalPaths) {
					if (p.getRow() == i && p.getColumn() == j) {
						setHorz = false;
					}
				}
				if (setHorz) {
					walls.add(new Wall(PIPE_LENGTH * i, PIPE_LENGTH * j + offset, PIPE_WIDTH, PIPE_LENGTH, box2d));
					// cells.add(Cell(offset*(i+1), offset*(j+1)));
				}
			}
		}
	}

	void drawMaze() {
		for (int i = walls.size() - 1; i >= 0; i--) {
			Wall p = walls.get(i);
			p.display(this);
		}
	}

	public void beginContact(Contact cp) {
		if (!(cp.getFixtureA().getBody() == zone.getBody()) && !(cp.getFixtureB().getBody() == (zone.getBody()))) {
			roomba.setBump(true);
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
		roomba.setBump(false);
	}
}