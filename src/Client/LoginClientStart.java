package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Header files for Java Swing
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/*
* class Name: LoginClientStart
* The class LoginClientStart asks the IP address from the user(client)
* of the server and if the IP address is correct, a new frame appears 
* asking for password set by server.
* If both IP Address and password is correct FunctionalListener class is called.
*
* This main thread works on port "9999".
*/
class LoginClientStart implements ActionListener {

	// stores IP address of the server
	String ipAddress;
	String portNum = "9999";

	Socket socketGUI;
	DataOutputStream sendData = null;
	DataInputStream receiveData = null;

	String verify = "false";
	JFrame frame;
	JButton btnSubmit;
	JPanel panel;
	JLabel label;
	JPasswordField passwordField;

	// constructor for class LoginClientStart
	LoginClientStart() {
		ipAddress = JOptionPane.showInputDialog("Please Enter The Server's IP: ");

		if (ipAddress == null) {
			System.exit(0);
		}

		try {
			socketGUI = new Socket(ipAddress, Integer.parseInt(portNum));

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, "Please enter the correct IP Address.");
			ex.printStackTrace();
			new LoginClientStart();
			System.exit(0);
		}

		frame = new JFrame("Client--Login");
		frame.setSize(300, 80);
		frame.setLocation(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		label = new JLabel("Password");
		passwordField = new JPasswordField(15);
		btnSubmit = new JButton("SubmiT");

		panel = new JPanel(new GridLayout(2, 1));
		panel.add(label, BorderLayout.CENTER);
		panel.add(passwordField);
		panel.add(new JLabel(""));
		panel.add(btnSubmit);

		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		btnSubmit.addActionListener(this);

	}

	/*
	 * The actionPerformed() method is invoked automatically whenever you click on
	 * the registered component
	 */

	public void actionPerformed(ActionEvent ae) {

		// receive the password from GUI window
		String value = passwordField.getText();
		try {
			sendData = new DataOutputStream(socketGUI.getOutputStream());
			receiveData = new DataInputStream(socketGUI.getInputStream());

			// receies the server verification status
			sendData.writeUTF(value);
			verify = (String) receiveData.readUTF();
			frame.dispose();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// checks whether client and server has entered the same password or not
		if (verify.equals("true")) {
			System.out.println("Connection Established...");
			new FunctionListener(ipAddress, socketGUI);
		} else {
			JOptionPane.showMessageDialog(frame, "Incorrect  password", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/*
	 * method name: main return type: void parameter : **no parameter**
	 *
	 * main method of class LoginClientStart and package Client. This main method
	 * call its own class i.e. LoginClientStart.
	 */
	public static void main(String args[]) {
		new LoginClientStart();
	}
}