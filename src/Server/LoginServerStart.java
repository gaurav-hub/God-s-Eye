package Server;

//https://www.javatpoint.com/java-actionlistener

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//Header files for swing
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * class Name: LoginServerStart The class LoginClientStart shows a frame to set
 * the password for authenticating connection with client. And this class also
 * checks whether the password entered by the client is correct or not. If
 * password of both client and server are same, then a GUI window is poped which
 * contains main frame of this program
 * 
 * This main thread works on port "9999".
 */
public class LoginServerStart implements ActionListener {

	String portNum = "9999";

	ServerSocket socketServer;
	Socket socket;
	DataOutputStream sendData = null;
	DataInputStream receiveData = null;

	JFrame frame;
	JPanel panel;
	JLabel label;
	JPasswordField passwordField;
	JButton btnSubmit;

	/**
	 * Constructor of the class LoginServerStart.
	 * 
	 * Parameter: Null
	 *
	 * Functionality: Creates a new Java Frame which contains a label to Enter the
	 * Password and a Submit Button.
	 */
	LoginServerStart() {
		frame = new JFrame("Server--Login");
		frame.setSize(350, 85);
		frame.setLocation(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		label = new JLabel("Set Password : ");
		passwordField = new JPasswordField(15);
		btnSubmit = new JButton("Submit");

		panel = new JPanel(new GridLayout(2, 2));
		panel.add(label);
		panel.add(passwordField);
		panel.add(new JLabel(""));
		panel.add(btnSubmit);

		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);

		btnSubmit.addActionListener(this);
	}

	/**
	 * Takes The Password as input and sets as a default password to communicate
	 * with Client. Once, The Button is Clicked,the password is set by server and
	 * then it waits for the the password entered by the client and Authenticates
	 * the connection if password entered is correct GUI window.
	 */
	public void actionPerformed(ActionEvent ae) {
		String passValue = passwordField.getText();
		String password = "";

		frame.dispose();

		// Handles the Exception caused by ServerSocket, getInputStream, getOuptupStream
		// etc.
		try {
			socketServer = new ServerSocket(Integer.parseInt(portNum));
			socket = socketServer.accept();
			System.out.println("Socket Created........");

			receiveData = new DataInputStream(socket.getInputStream());
			sendData = new DataOutputStream(socket.getOutputStream());

			password = (String) receiveData.readUTF();

			// validates password
			if (password.equals(passValue)) {
				System.out.println("Server--Connection Established...");
				sendData.writeUTF("true");
				new MainFrame(socket);
			} else {
				sendData.writeUTF("false");
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	// Execution of the Program Begin from here.
	public static void main(String args[]) {
		new LoginServerStart();
	}

}
