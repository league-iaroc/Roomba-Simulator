package org.jointheleague;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;

import processing.core.PApplet;

public abstract class Challenge {
	private static Challenge challenge;

	public static Challenge getCurrent() {
		return challenge;
	}

	public Challenge() {
		challenge = this;

		try {
			netClient = new TCPConnection("localhost", 1337);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		PApplet.main("org.jointheleague.Processing");

		Thread objectHandler = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					while (!netClient.isClosed()) {
						DataPackage in = (DataPackage) netClient.readObject();
						System.out.println("Received package: " + in.getMessage());
						if (in.getMessage().equals("register_roomba")) {
							Roomba roomba = (Roomba) in.getObjects()[0];
							roomba.makeBody();

							Processing.getProcessing().addRoomba(roomba);
							registerRoomba(roomba);
							System.out.println("Registered roomba " + roomba.getID());
						} else if (in.getMessage().equals("roomba_cmd")) {
							commandsMap.get((String) in.getObjects()[0]).add((String) in.getObjects()[1]);
						} else if (in.getMessage().equals("game_start")) {
							Processing.START = true;
							init();
							System.out.println("Started game from remote request.");
						} else {
							System.err.println("No handle for package: " + in.getMessage());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});
		objectHandler.start();
	}

	private String thisId;
	private HashMap<String, Roomba> roombas = new HashMap<>();
	private HashMap<String, ArrayList<String>> commandsMap = new HashMap<>();
	private HashMap<String, String> lastIssuedCommands = new HashMap<>();
	private TCPConnection netClient;

	public TCPConnection getNetClient() {
		return netClient;
	}

	void initialize(Roomba roomba) {
		thisId = roomba.getID();
		registerRoomba(roomba);
	}

	private void registerRoomba(Roomba roomba) {
		roombas.put(roomba.getID(), roomba);
		commandsMap.put(roomba.getID(), new ArrayList<>());
		lastIssuedCommands.put(roomba.getID(), null);
	}

	void update() {
		loop();
		for (Entry<String, ArrayList<String>> commands : commandsMap.entrySet()) {
			if (commands.getValue().size() > 0) {
				String currentCommand = commands.getValue().get(0);
				executeCommand(commands.getKey(), currentCommand, true);
			} else {
				String str = lastIssuedCommands.get(commands.getKey());
				if (str != null && !str.startsWith("sleep")) {
					executeCommand(commands.getKey(), str, false);
				}
			}
		}
	}

	void start() {
		init();
		try {
			System.out.println("Starting game because of local request.");
			netClient.getOutputStream().writeObject(new DataPackage().setMessage("game_start"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract void init();

	protected abstract void loop();

	private void executeCommand(String roombaId, String command, boolean remove) {
		String[] cmdSplit = command.split(":");
		String cmdName = cmdSplit[0];
		String[] args = cmdSplit[1].split(",");

		if (cmdName.equals("driveDirect")) {
			roombas.get(roombaId).driveDirect(Float.parseFloat(args[0]), Float.parseFloat(args[1]));
			lastIssuedCommands.put(roombaId, command);
			if (remove)
				commandsMap.get(roombaId).remove(0);
		} else if (cmdName.equals("sleep")) {
			if ((System.currentTimeMillis() - Long.parseLong(args[0]) >= Long.parseLong(args[1]))) {
				lastIssuedCommands.put(roombaId, command);

				if (remove)
					commandsMap.get(roombaId).remove(0);
			} else {
				String cmd = lastIssuedCommands.get(roombaId);
				if (cmd != null && !cmd.startsWith("sleep")) {
					executeCommand(roombaId, cmd, false);
				}
			}
		}
	}

	public void driveDirect(float left, float right) {
		commandsMap.get(thisId).add(netCommand("driveDirect:" + left + "," + right + ""));
	}

	public void sleep(long ms) {
		commandsMap.get(thisId).add(netCommand("sleep:" + System.currentTimeMillis() + "," + ms));
	}

	private String netCommand(String cmd) {
		try {
			netClient.writeObject(new DataPackage(thisId, cmd).setMessage("roomba_cmd"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cmd;

	}

	public boolean bumpersTriggered() {
		return roombas.get(thisId).isBump();
	}
}
