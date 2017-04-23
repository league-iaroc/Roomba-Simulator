package org.jointheleague;

import java.util.ArrayList;

import processing.core.PApplet;

public class Challenge {
	private static Challenge challenge;

	public static Challenge getCurrent() {
		return challenge;
	}

	public static void main(String[] args) {
		PApplet.main("org.jointheleague.Processing");
		challenge = new Challenge();
	}

	private Roomba roomba;
	private ArrayList<String> commands = new ArrayList<>();
	private String lastIssuedCommand;

	public void initialize(Roomba roomba) {
		this.roomba = roomba;

		driveDirect(100, 100);
		sleep(5000);
		driveDirect(0, 0);
	}

	public void loop(Roomba roomba) {
		if (commands.size() > 0) {
			String currentCommand = commands.get(0);
			executeCommand(currentCommand, true);
		}
	}

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

	private void driveDirect(float left, float right) {
		commands.add("driveDirect:" + left + "," + right + "");
	}

	private void sleep(long ms) {
		commands.add("sleep:" + System.currentTimeMillis() + "," + ms);
	}
}
