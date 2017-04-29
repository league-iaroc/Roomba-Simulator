package org.jointheleague;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.dynamics.contacts.Contact;
import org.jnetwork.DataPackage;

import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

class Processing extends PApplet {
	public static Box2DProcessing WORLD;

	private static Processing processing;

	public static Processing getProcessing() {
		return processing;
	}

	public static int GRID_SIZE = 4;
	public static boolean START = false;
	public static final int SCREEN_SIZE = 900;
	public static int PIPE_LENGTH = SCREEN_SIZE / GRID_SIZE;
	public static int PIPE_WIDTH = 4;
	public static boolean END = false;
	public static boolean WIN = false;
	private ArrayList<Path> verticalPaths = new ArrayList<Path>();
	private ArrayList<Path> horizontalPaths = new ArrayList<Path>();
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Roomba> roombas = new ArrayList<>();
	private EndZone zone;

	public Processing() {
		verticalPaths.add(new Path(0, 1));
		verticalPaths.add(new Path(3, 1));
		horizontalPaths.add(new Path(2, 1));
		horizontalPaths.add(new Path(3, 1));
		horizontalPaths.add(new Path(2, 1));
		horizontalPaths.add(new Path(1, 1));
	}

	public void addRoomba(Roomba roomba) {
		roombas.add(roomba);
	}

	public void settings() {
		size(SCREEN_SIZE, SCREEN_SIZE);
	}

	public void setup() {
		processing = this;
		WORLD = new Box2DProcessing(this);
		WORLD.createWorld();
		WORLD.setGravity(0, 0);
		WORLD.listenForCollisions();

		Random random = new Random();
		Roomba roomba = new Roomba(random.nextInt(500), random.nextInt(500), PIPE_LENGTH / 6);
		roombas.add(roomba);
		System.out.println("Registering roomba " + roomba.getID() + "...");
		try {
			Challenge.getCurrent().getNetClient().writeObject(new DataPackage(roomba).setMessage("register_roomba"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Registered roomba.");

		zone = new EndZone(SCREEN_SIZE - PIPE_LENGTH / 2, PIPE_LENGTH / 2, PIPE_LENGTH / 4);
		// setMaze();

		Challenge.getCurrent().initialize(roomba);
	}

	public void draw() {
		background(255);

		WORLD.step();
		for (Roomba roomba : roombas) {
			roomba.display(this);
		}
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
				Challenge.getCurrent().start();
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
					walls.add(new Wall(PIPE_LENGTH * i + offset, PIPE_LENGTH * j, PIPE_LENGTH, PIPE_WIDTH));
				}

				for (Path p : horizontalPaths) {
					if (p.getRow() == i && p.getColumn() == j) {
						setHorz = false;
					}
				}
				if (setHorz) {
					walls.add(new Wall(PIPE_LENGTH * i, PIPE_LENGTH * j + offset, PIPE_WIDTH, PIPE_LENGTH));
				}
			}
		}
	}

	void drawMaze() {
		for (int i = walls.size() - 1; i >= 0; i--) {
			walls.get(i).display(this);
		}
	}

	public void beginContact(Contact cp) {
		if (!(cp.getFixtureA().getBody() == zone.getBody()) && !(cp.getFixtureB().getBody() == zone.getBody())) {
			for (Roomba roomba : roombas) {
				if ((cp.getFixtureA().getBody() == roomba.getBody())
						|| (cp.getFixtureB().getBody() == roomba.getBody())) {
					roomba.setBump(true);
					fill(0);
					END = true;
					START = false;
					roomba.killBody();
				}
			}
		} else {
			END = true;
			WIN = true;
		}
	}

	public void endContact(Contact cp) {
		for (Roomba roomba : roombas) {
			if ((cp.getFixtureA().getBody() == roomba.getBody()) || (cp.getFixtureB().getBody() == roomba.getBody())) {
				roomba.setBump(false);
			}
		}
	}
}