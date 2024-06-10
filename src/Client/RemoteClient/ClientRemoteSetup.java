package Client.RemoteClient;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/*
 * class Name: ClientRemoteSetup
 * This class creates a thread which call the class SendScreen
 * and ReceiveEvents. And also it secnds the dimension of client
 * screen to the server.
 * 
 * This thread uses port "9998" to interact with Server
 */
public class ClientRemoteSetup extends Thread {

	Socket socket = null;
	DataInputStream password = null;
	DataOutputStream verify = null;
	String width = "";
	String height = "";
	boolean continueLoop = true;
	Robot robot = null;
	Rectangle rectangle = null;
	SendScreen remoteSend;
	ReceiveEvents remoteReceive;

	/*
	 * Constructor of class ClientRemoteSetup 
	 * 
	 * Parameters:s
	 * 
	 * Parameter Type: s -> Socket
	 */
	public ClientRemoteSetup(Socket s) {
		System.out.println("Awaiting Connection from Client");
		socket = s;
	}

	/*
	* Runs a Thread
	* Gets The Dimension of the Client Side Screen and Send to the Server Side.
	 */
	public void run() {

		//Handles exception that might be created while using Robot class
		try {
			GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gDev = gEnv.getDefaultScreenDevice();

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

			verify = new DataOutputStream(socket.getOutputStream());

			width = "" + dim.getWidth();
			height = "" + dim.getHeight();
			verify.writeUTF(width);
			verify.writeUTF(height);
			rectangle = new Rectangle(dim);
			robot = new Robot(gDev);

			drawGUI();

			remoteSend = new SendScreen(socket, robot, rectangle);
			remoteReceive = new ReceiveEvents(socket, robot);

			//loop to keep this thread alive
			while (continueLoop) {
				continue;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void drawGUI() {
	}

	//mehod to close this thread along with all other threads called by this thread
	public void close() {
		remoteSend.close();
		remoteReceive.close();
		continueLoop = false;
	}
}

/**
 * ************************
 * Important Classes Used
 * ************************
 * 
 * GraphicsEnvironment:The GraphicsEnvironment class describes the collection of GraphicsDevice objects
 *					  	and Font objects available to a Java(tm) application on a particular platform. 

 * 
 * GraphicsDevices:The GraphicsDevice class describes the graphics devices that might be available in 
				    a particular graphics environment.
 * 
 * Rectangle:A Rectangle specifies an area in a coordinate space that is enclosed by the Rectangle object's
 			 upper-left point (x,y) in the coordinate space, its width, and its height.
 * 
 * Dimension:The Dimension class encapsulates the width and height of a component (in integer precision) in a single object.
 * 
 * Robot:Robot Class->The primary purpose of Robot is to facilitate automated testing of 
 *					  Java platform implementations.
 * 
 */

