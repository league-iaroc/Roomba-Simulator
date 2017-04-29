package org.jointheleague;

import org.jnetwork.DataPackage;

public class Root {
	Root() {
	}

	public void sendCommand(String roombaId, String cmd) {
		Challenge.getCurrent().netCommand(roombaId, cmd);
		Challenge.getCurrent().handlePacket(new DataPackage(roombaId, cmd).setMessage("roomba_cmd"));
	}

	public String getRoombaID(int x, int y) {
		Roomba roomba = Processing.getProcessing().getRoombaAtPosition(x, y);
		return roomba != null ? roomba.getID() : null;
	}
}
