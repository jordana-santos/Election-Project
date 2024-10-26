package Client;

import Common.*;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String serverAddress;
    private int serverPort;

    //constructor
    public ClientConnection(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public String sendVote(Vote vote) {
        try {
            if (out != null) {
                out.writeObject(vote);
                out.flush();

                //await an answer from the server regarding vote acceptance
                String answer = (String) in.readObject();
                return answer; //can be "acepted" or "denied"
            } else {
                throw new IOException("Connection is not established.");
            }
        } catch (IOException | ClassNotFoundException e) {
            return "Error sending vote: " + e.getMessage();
        }
    }

    public ElectionData receiveElectionData() throws IOException, ClassNotFoundException {
        if (in != null) {
            return (ElectionData) in.readObject();
        } else {
            throw new IOException("Connection is not established.");
        }
    }

    public void close() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }
}
