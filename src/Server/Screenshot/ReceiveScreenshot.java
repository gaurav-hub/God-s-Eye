package Server.Screenshot;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//Header files for java swing
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * class Name: ReceiveScreenshot 
 * This class creates a seperate thread and receive 
 * the screenshot of the device from the client.
 * 
 * This port runs on port "9996".
 */
public class ReceiveScreenshot extends Thread {

	Socket socket;
	InputStream inputStream;
	BufferedImage image;

	/*
	 * Constructor of the class ReceiveScreenshot
	 * 
	 * Parameter: socket
	 * 
	 * Parameter Type: socket -> Socket
	 * 
	 * This constructor assigns the socket fot this class thread.
	 */
	public ReceiveScreenshot(Socket socket) {
		this.socket = socket;
	}

	// runs teh thread
	public void run() {

		// Handle exception caused by getInputStream, ImageIO.read
		try {
			inputStream = socket.getInputStream();

			byte[] bytes = new byte[1024 * 1024];
			int count = 0;
			do {
				count += inputStream.read(bytes, count, bytes.length - count);
			} while (!(count > 4 && bytes[count - 4] == (byte) 174 && bytes[count - 3] == (byte) 66
					&& bytes[count - 2] == (byte) 96 && bytes[count - 1] == (byte) 130));

			image = ImageIO.read(new ByteArrayInputStream(bytes));

			JFrame frame = new JFrame();
			frame.getContentPane().add(new JLabel(new ImageIcon(image)));
			frame.pack();
			frame.setVisible(true);

			ImageIO.write(image, "PNG", new File("ScreenShot_"
					+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
