package Client.RemoteClient;

import java.awt.Robot;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
* class Name: ReciveEvents
* This class creates a thread which recieves screen-events from 
* from the server and execute it on the client side.
*/
class ReceiveEvents extends Thread {
	Socket socket = null;
	Robot robot = null;
	boolean continueLoop = true;

	/**
	 * Constructor to class RecieveEvents
	 * 
	 * Parameteres: socket,robot
	 * 
	 * Parameter Type:socket->Socket , robot->Robot
	 */
	public ReceiveEvents(Socket socket, Robot robot) {

		this.socket = socket;
		this.robot = robot;
		this.start(); // Start the thread and hence calling run method
	}

	// Starts the thread
	// receives events send by server and executes it
	public void run() {
		Scanner scanner = null;

		// handles exception caused by getInputStream class
		try {
			scanner = new Scanner(socket.getInputStream());

			while (continueLoop) {

				// recieve commands and respond accordingly
				int command = scanner.nextInt();

				switch (command) {
					case -1:
						robot.mousePress(scanner.nextInt());
						break;
					case -2:
						robot.mouseRelease(scanner.nextInt());
						break;
					case -3:
						robot.keyPress(scanner.nextInt());
						break;
					case -4:
						robot.keyRelease(scanner.nextInt());
						break;
					case -5:
						robot.mouseMove(scanner.nextInt(), scanner.nextInt());
						break;
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
			continueLoop = false;
		}
	}// end function

	// method to close this thread
	public void close() {
		this.continueLoop = false;
	}

}// end class
