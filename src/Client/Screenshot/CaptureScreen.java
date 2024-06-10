package Client.Screenshot;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

/*
 * class Name: CaptureScreen 
 * This class creates a seperate thread and capture 
 * the screenshot of the device and sends it to the server.
 * 
 * This port runs on port "9996".
 */
public class CaptureScreen extends Thread {

	Socket socket;
	OutputStream outputStream;

	/*
	 * Constructor of the class CaptureScreen
	 * 
	 * Parameter: socket
	 * 
	 * Parameter Type: socket -> Socket
	 * 
	 * This constructor assigns the socket fot this class thread.
	 */
	public CaptureScreen(Socket socket) {
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

		// Handles exception by ImageIO.write
		try {
			BufferedImage screenshot = new Robot()
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenshot, "PNG", outputStream);
		} catch (HeadlessException | AWTException | IOException e) {
			e.printStackTrace();
		}
	}
}
