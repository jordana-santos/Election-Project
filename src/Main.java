import Client.*;
import Common.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Distributed Voting System");
        System.out.println("Choose mode: ");
        System.out.println("1 - Start Server");
        System.out.println("2 - Start Client");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // startServer();
                break;

            case 2:
                startClient();
                break;

            default:
                System.out.println("Invalid option. Exiting...");
                System.exit(0);
        }
    }

    private static void startClient() {
        //examples
        String serverAddress = "127.0.0.1";
        int serverPort = 12345;

        try {
            ClientConnection clientConnection = new ClientConnection(serverAddress, serverPort);
            clientConnection.connect();

            // Fetch election data and display in GUI in a separate thread
            javax.swing.SwingUtilities.invokeLater(() -> {
                ClientGUI gui = new ClientGUI(clientConnection);
                gui.setVisible(true);

                try {
                    // Fetch election data and display in GUI
                    ElectionData electionData = clientConnection.receiveElectionData();
                    gui.displayElectionData(electionData);
                } catch (IOException e) {
                    System.err.println("Failed to receive election data: " + e.getMessage());
                    gui.showError("Error receiving election data. Please try again.");
                } catch (ClassNotFoundException e) {
                    System.err.println("Failed to receive election data: " + e.getMessage());
                    gui.showError("Error: Invalid election data format.");
                }
            });

        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        }
    }


}
