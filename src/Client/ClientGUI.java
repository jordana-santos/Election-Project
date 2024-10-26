package Client;

import Common.*;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class ClientGUI extends JFrame {
    private JTextField cpfField;
    private JButton connectButton;
    private JPanel optionsPanel;
    private ButtonGroup optionGroup;
    private ClientConnection clientConnection;

    //expression to validate the CPF in the xxx.xxx.xxx-xx format
    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}");

    public ClientGUI(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        setupUI();
    }

    private void setupUI() {
        setTitle("Distributed Voting System");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel cpfLabel = new JLabel("Enter CPF:");
        cpfField = new JTextField(15);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> connectToServer());

        JPanel cpfPanel = new JPanel();
        cpfPanel.add(cpfLabel);
        cpfPanel.add(cpfField);
        cpfPanel.add(connectButton);

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        add(cpfPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
    }

    private void connectToServer() {
        String cpf = cpfField.getText().trim();

        if (!isValidCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "Invalid CPF format! Please use XXX.XXX.XXX-XX or XXXXXXXXXXX.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            //connects to server and send info to client
            clientConnection.connect();
            ElectionData electionData = clientConnection.receiveElectionData();
            displayElectionData(electionData);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //validated cpf
    private boolean isValidCPF(String cpf) {
        return CPF_PATTERN.matcher(cpf).matches();
    }


    public void displayElectionData(ElectionData electionData) {
        optionsPanel.removeAll(); //clean previous panel

        JLabel questionLabel = new JLabel("Question: " + electionData.getQuestion());
        optionsPanel.add(questionLabel);

        optionGroup = new ButtonGroup();
        for (String option : electionData.getOptions()) {
            JRadioButton optionButton = new JRadioButton(option);
            optionGroup.add(optionButton);
            optionsPanel.add(optionButton);
        }

        JButton submitVoteButton = new JButton("Submit Vote");
        submitVoteButton.addActionListener(e -> submitVote());
        optionsPanel.add(submitVoteButton);

        //upadte option panel
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void submitVote() {
        String selectedOption = null;

        for (Enumeration<AbstractButton> buttons = optionGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                selectedOption = button.getText();
                break;
            }
        }

        if (selectedOption == null) {
            JOptionPane.showMessageDialog(this, "Please select an option before voting.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String cpf = cpfField.getText().trim();
            Vote vote = new Vote(cpf, selectedOption);
            String response = clientConnection.sendVote(vote); // Get server response

            //show server response
            JOptionPane.showMessageDialog(this, response, "Vote Submission", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error submitting vote: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
