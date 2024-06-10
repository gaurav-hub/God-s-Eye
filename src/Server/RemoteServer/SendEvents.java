package Server.RemoteServer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JPanel;

/*
 * class Name: SendEvents
 * This class creates a new thread to send events that needs to be performed on the clien
 * 
 * This thread uses port "9998" to send screenshots to the server.
 */
class SendEvents implements KeyListener, MouseMotionListener, MouseListener {
	private Socket cSocket = null;
	private JPanel cPanel = null;
	private PrintWriter writer = null;
	String width = "", height = "";
	double w;
	double h;

	/*
	 * Constructor to the Class SendEvents
	 *
	 * Parameters:s,p,width,height
	 *
	 * Parameter Type: s->Socket, p->JPanel, width->String, height->String
	 *
	 * Functionalities: Analyzes the width and height of the Client Screen. It
	 * creates a Panel and adds the Various Listners needed to control and send
	 * events to the client.
	 *
	 */
	SendEvents(Socket s, JPanel p, String width, String height) {
		cSocket = s;
		cPanel = p;
		this.width = width;
		this.height = height;
		w = Double.valueOf(width.trim()).doubleValue();
		h = Double.valueOf(height.trim()).doubleValue();

		// Associate event listeners to the panel

		cPanel.addKeyListener(this);
		cPanel.addMouseListener(this);
		cPanel.addMouseMotionListener(this);

		try {
			// Prepare PrintWriter which will be used to send commands to the client
			writer = new PrintWriter(cSocket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// FunctionListner to Track the Movement of the MouseDragged
	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		double xScale = (double) w / cPanel.getWidth();
		double yScale = (double) h / cPanel.getHeight();
		writer.println(Commands.MOVE_MOUSE.getAbbrev());
		writer.println((int) (e.getX() * xScale));
		writer.println((int) (e.getY() * yScale));
		writer.flush();
	}

	// FunctionListner to Track the Movement of the mouseclicked
	public void mouseClicked(MouseEvent e) {
	}

	// FunctionListner to Track the Movement of the mousePressed
	public void mousePressed(MouseEvent e) {
		writer.println(Commands.PRESS_MOUSE.getAbbrev());
		int button = e.getButton();
		int xButton = 16;
		if (button == 3) {
			xButton = 4;
		}
		writer.println(xButton);
		writer.flush();
	}

	// FunctionListner to Track the Movement of the mouseReleased
	public void mouseReleased(MouseEvent e) {
		writer.println(Commands.RELEASE_MOUSE.getAbbrev());
		int button = e.getButton();
		int xButton = 16;
		if (button == 3) {
			xButton = 4;
		}
		writer.println(xButton);
		writer.flush();
	}

	// FunctionListner to Track the Movement of the mouseEntered
	public void mouseEntered(MouseEvent e) {
	}

	// FunctionListner to Track the Movement of the mouseExited
	public void mouseExited(MouseEvent e) {
	}

	// FunctionListner to Track the Movement of the KeyTyped
	public void keyTyped(KeyEvent e) {
	}

	// FunctionListner to Track the Movement of the kedKeyPressed
	public void keyPressed(KeyEvent e) {
		writer.println(Commands.PRESS_KEY.getAbbrev());
		writer.println(e.getKeyCode());
		writer.flush();
	}

	// FunctionListner to Track the Movement of the KeyReleased
	public void keyReleased(KeyEvent e) {
		writer.println(Commands.RELEASE_KEY.getAbbrev());
		writer.println(e.getKeyCode());
		writer.flush();
	}

	public void close() {

	}
}
