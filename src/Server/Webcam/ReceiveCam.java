package Server.Webcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
*
* ClassName: RecieveCam
*
* RecieveCam Thread recieves the incoming images from client
*
* RecieveCam Listens to the Port no '9997'
*
 */
public class ReceiveCam extends Thread {
	InputStream inputStream = null;
	Socket socket = null;
	BufferedImage image;
	

	/**
	 * Constructor of the Class RecieveCam
	 *
	 * Parameters: socket
	 *
	 * Parameter Type: socket->Socket
	 *
	 */
	public ReceiveCam(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Runs the Thread Reads the Images of the Client and saves in ByteBuffer.
	 * Images are recieved by the Server in the form of ByteStream.
	 */
	public void run() {
		try {
			inputStream = socket.getInputStream();
			byte[] bytes = new byte[1024 * 1024];
			int count = 0;
			do {
				count += inputStream.read(bytes, count, bytes.length - count);
			} while (!(count > 4 && bytes[count - 4] == (byte) 174 && bytes[count - 3] == (byte) 66
					&& bytes[count - 2] == (byte) 96 && bytes[count - 1] == (byte) 130));

			// An InternalBuffer that contains bytes that may be read from the Stream.
			image = ImageIO.read(new ByteArrayInputStream(bytes));

			// Creates a new Frame which will showcase the image
			JFrame frame = new JFrame();
			frame.getContentPane().add(new JLabel(new ImageIcon(image)));
			frame.pack();
			frame.setVisible(true);

			// Writes the Image in the PNG Format in the Path/Filename Specified.
			ImageIO.write(image, "PNG", new File("Webcam_"
					+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".png"));

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
