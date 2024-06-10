package Server.RemoteServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
 * Class Name: ServerRemoteSetup
 * 
 * This Class Runs the Thread to get the details of the Client Screen.
 * 
 * This thread uses port "9998" 
 *
 */
public class ServerRemoteSetup extends Thread {
	private Socket cSocket = null;
	DataOutputStream psswrchk = null;
	DataInputStream verification = null;
	String width = "", height = "";
	boolean continueLoop = true;

	CreateFrame remoteFrame;

	/*
	 * Constructor of the class ServerRemoteSetup
	 *
	 * Parameters:cSocket
	 *
	 * Parameter Type: cSocket->Socket
	 *
	 */
	public ServerRemoteSetup(Socket cSocket) {
		this.cSocket = cSocket;
		this.start();
	}

	/*
	 * Runs the Thread. Reads the Dimensions of the Client Screen and Creates a New
	 * Frame Accordingly.
	 */
	public void run() {

		// Handles the Exception of getInputStream
		try {
			verification = new DataInputStream(cSocket.getInputStream());
			width = (String) verification.readUTF();
			height = (String) verification.readUTF();

		} catch (IOException e) {
			e.printStackTrace();
		}
		// Creates a new Frame.
		remoteFrame = new CreateFrame(cSocket, width, height);

		while (continueLoop) {
			continue;
		}

	}

	public void close() {

		remoteFrame.close();
		continueLoop = false;

	}

}
