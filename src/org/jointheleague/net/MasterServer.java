package org.jointheleague.net;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import org.jnetwork.ClientData;
import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;
import org.jnetwork.TCPConnectionCallback;
import org.jnetwork.TCPServer;
import org.jointheleague.Roomba;

public class MasterServer implements TCPConnectionCallback {
	private static TCPServer server;

	public static void main(String[] args) {
		try {
			server = new TCPServer(1337, new MasterServer());
			server.start();
			server.waitUntilClose();
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private ArrayList<DataPackage> registered = new ArrayList<>();

	@Override
	public void clientConnected(ClientData event) {
		try {
			TCPConnection client = (TCPConnection) event.getConnection();
			for (DataPackage pkg : registered) {
				System.out.println("Sending new connection roomba " + ((Roomba) pkg.getObjects()[0]).getID());
				client.writeObject(pkg);
			}

			while (!client.isClosed()) {
				DataPackage pkgIn = (DataPackage) client.readObject();
				if (pkgIn.getMessage().equals("register_roomba")) {
					registered.add(pkgIn);
				}
				sendToClients(client, pkgIn);
			}
		} catch (SocketException | EOFException e) {
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendToClients(TCPConnection exclude, DataPackage pkg) throws IOException {
		for (ClientData client : server.getClients()) {
			if (!client.getConnection().getRemoteSocketAddress().toString()
					.equals(exclude.getRemoteSocketAddress().toString())) {
				client.getConnection().writeObject(pkg);
			}
		}
	}
}
