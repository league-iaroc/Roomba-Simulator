package org.jointheleague;

import java.util.ArrayList;

import processing.core.PApplet;

public abstract class Challenge {
	private static Challenge challenge;

	public static Challenge getCurrent() {
		return challenge;
	}

	public Challenge() {
		challenge = this;

		PApplet.main("org.jointheleague.Processing");
	}

	private Roomba roomba;
	private ArrayList<String> commands = new ArrayList<>();
	private String lastIssuedCommand;

	void initialize(Roomba roomba) {
		this.roomba = roomba;
	}

	void update() {
		loop();
		if (commands.size() > 0) {
			String currentCommand = commands.get(0);
			executeCommand(currentCommand, true);
		}
	}

	public abstract void init();

	public abstract void loop();

	private void executeCommand(String command, boolean remove) {
		String[] cmdSplit = command.split(":");
		String cmdName = cmdSplit[0];
		String[] args = cmdSplit[1].split(",");

		if (cmdName.equals("driveDirect")) {
			roomba.driveDirect(Float.parseFloat(args[0]), Float.parseFloat(args[1]));
			lastIssuedCommand = command;
			if (remove)
				commands.remove(0);
		} else if (cmdName.equals("sleep")) {
			if ((System.currentTimeMillis() - Long.parseLong(args[0]) >= Long.parseLong(args[1]))) {
				lastIssuedCommand = command;

				if (remove)
					commands.remove(0);
			} else {
				if (lastIssuedCommand != null && !lastIssuedCommand.startsWith("sleep")) {
					executeCommand(lastIssuedCommand, false);
				}
			}
		}
	}

	public void driveDirect(float left, float right) {
		commands.add("driveDirect:" + left + "," + right + "");
	}

	public void sleep(long ms) {
		commands.add("sleep:" + System.currentTimeMillis() + "," + ms);
	}
}
