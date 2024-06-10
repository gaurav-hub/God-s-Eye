package Server.RemoteServer;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.io.IOException;

/*
 * class Name: Createframe
 * This class create the frame where the client side screen will be visible and can be controled.
 * 
 * This threads uses port "9998".
 */
class CreateFrame extends Thread {
	String width = "", height = "";
	private JFrame frame = new JFrame();

	// JDesktopPane represents the main container that will contain all connected
	// clients' screens
	private JDesktopPane desktop = new JDesktopPane();
	private Socket cSocket = null;
	private JInternalFrame interFrame = new JInternalFrame("Server Screen", true, true, true);
	private JPanel cPanel = new JPanel();
	boolean continueLoop = false;

	ReceiveScreen remoteReceive;
	SendEvents remoteSend;

	/*
	 * Constructor of the class CreateFrame
	 *
	 * Parameters:cSocket,width,height
	 *
	 * Parameter Type: cSocket->Socket , width->String, height->String
	 *
	 */
	public CreateFrame(Socket cSocket, String width, String height) {

		this.width = width;
		this.height = height;
		this.cSocket = cSocket;
		this.start();
	}

	// Draw GUI per each connected client
	public void drawGUI() {
		frame.add(desktop, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Show thr frame in maximized state
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH); // CHECK THIS LINE
		frame.setVisible(true);
		interFrame.setLayout(new BorderLayout());
		interFrame.getContentPane().add(cPanel, BorderLayout.CENTER);
		interFrame.setSize(100, 100);
		desktop.add(interFrame);

		try {

			// Initially show the internal frame maximized
			interFrame.setMaximum(true);
		} catch (PropertyVetoException ex) {
			ex.printStackTrace();
		}

		// This allows to handle KeyListener events
		cPanel.setFocusable(true);
		interFrame.setVisible(true);

	}

	public void run() {

		// Used to read screenshots
		InputStream in = null;

		// start drawing GUI
		drawGUI();

		try {
			in = cSocket.getInputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Start receiving screenshots
		remoteReceive = new ReceiveScreen(in, cPanel);

		// Start sending events to the client
		remoteSend = new SendEvents(cSocket, cPanel, width, height);

		while (continueLoop) {
			continue;
		}
	}

	public void close() {

		remoteReceive.close();
		remoteSend.close();
		frame.dispose();
	}
}
