package Client.RemoteClient;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


import javax.imageio.ImageIO;

/*
 * class Name: SendScreen 
 * This class creates a new thread to send screenshots of
 * the clients screen continuesly.
 * 
 * This thread uses port "9998" to send screenshots to the server.
 */
class SendScreen extends Thread {

	Socket socket = null;
	Robot robot = null;
	Rectangle rectangle = null;
	boolean continueLoop = true;

	OutputStream oos = null;

	/*
	 * Constructor of the class SendScreen.
	 * 
	 * Parameters: socket,robot,rect
	 * 
	 * Parameter Type: socket ->Socket, robot -> Robot, rect -> Rectangle
	 * 
	 * Functionality:Creates a Socket Connection of the objects.
	 */
	public SendScreen(Socket socket, Robot robot, Rectangle rect) {
		this.socket = socket;
		this.robot = robot;
		rectangle = rect;
		this.start();
	}

	/*
	 * Runs The Thread. Captures the screen Continuously and Sends it to server in
	 * the JPG Format.
	 */

	public void run() {
		// Handles The Exception caused by getOutputStream
		try {
			oos = socket.getOutputStream();

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		while (continueLoop) {

			BufferedImage image = robot.createScreenCapture(rectangle);

			try {
				ImageIO.write(image, "JPG", oos);

			} catch (IOException ex) {
				ex.printStackTrace();
				continueLoop = false;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continueLoop = false;
			}
		}
	}

	// to close the this thread
	public void close() {
		this.continueLoop = false;
	}
}