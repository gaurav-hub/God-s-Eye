package Client.Webcam;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

// Libraries containing Webcam functionalities in java
//https://github.com/sarxos/webcam-capture 
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

/*
 * class Name: CaptureCam
 * This class access the webcam and capture the picture and sends it to server
 * 
 * This port runs on port "9997".
 */
public class CaptureCam extends Thread {

	Socket socket;
	Webcam webcam;
	OutputStream outputStream;

	/*
	 * Constructor of the class CaptureCam
	 * 
	 * Parameter: socket
	 * 
	 * Parameter Type: socket -> Socket
	 * 
	 * This constructure assign the socket for current thread.
	 */
	public CaptureCam(Socket socket) {
		this.socket = socket;

	}

	// Runs the Thread
	public void run() {
		// Handles the exception caused by getOutputStream
		try {
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		webcam = Webcam.getDefault();

		// Can't find camera
		if (webcam == null)
			return;

		webcam.close();
		webcam.setViewSize(WebcamResolution.VGA.getSize());

		// Open the camera
		webcam.open();

		try {
			// sends the captured picture to the server
			ImageIO.write(webcam.getImage(), "PNG", outputStream);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// Close the camera
		webcam.close();
	}

}
