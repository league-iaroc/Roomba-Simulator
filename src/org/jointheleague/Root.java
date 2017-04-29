package org.jointheleague;

public class Root {
	Root() {
	}

	public void sendCommand(String roombaId, String cmd) {
		Challenge.getCurrent().netCommand(roombaId, cmd);
	}
}
