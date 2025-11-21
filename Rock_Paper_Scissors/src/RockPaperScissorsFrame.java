import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RockPaperScissorsFrame extends JFrame implements ActionListener {
    private final JButton rockButton;
    private final JButton paperButton;
    private final JButton scissorsButton;
    private final JButton quitButton;
    private final JTextField playerWinsTextField;
    private final JTextField computerWinsTextField;
    private final JTextField tiesTextField;
    private final JTextArea resultsTextArea;
    private int playerWins = 0, computerWins = 0, ties = 0;
    private final Map<String, Integer> playerChoices = new HashMap<>();

    // ComputerStrategy Interface
    interface ComputerStrategy {
        String chooseGesture(Map<String, Integer> playerChoices);

    }

    // RandomStrategy class
    static class RandomStrategy implements ComputerStrategy {
        @Override
        public String chooseGesture(Map<String, Integer> playerChoices) {
            String[] gestures = {"Rock", "Paper", "Scissors"};
            Random random = new Random();
            return gestures[random.nextInt(gestures.length)];
        }
    }

    private ComputerStrategy computerStrategy;

    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating panels
        JPanel buttonPanel = new JPanel();
        JPanel statsPanel = new JPanel(new GridLayout(3, 2));
        JPanel resultsPanel = new JPanel(new BorderLayout());

        // Buttons
        rockButton = new JButton(new ImageIcon("Rock.png"));
        paperButton = new JButton(new ImageIcon("Paper.png"));
        scissorsButton = new JButton(new ImageIcon("Scissors.png"));
        quitButton = new JButton("Quit");
        rockButton.addActionListener(this);
        paperButton.addActionListener(this);
        scissorsButton.addActionListener(this);
        quitButton.addActionListener(this);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Choose"));
        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);

        // Stats panel
        JLabel playerWinsLabel = new JLabel("Player Wins:");
        JLabel computerWinsLabel = new JLabel("Computer Wins:");
        JLabel tiesLabel = new JLabel("Ties:");
        playerWinsTextField = new JTextField(5);
        computerWinsTextField = new JTextField(5);
        tiesTextField = new JTextField(5);
        playerWinsTextField.setEditable(false);
        computerWinsTextField.setEditable(false);
        tiesTextField.setEditable(false);
        statsPanel.add(playerWinsLabel);
        statsPanel.add(playerWinsTextField);
        statsPanel.add(computerWinsLabel);
        statsPanel.add(computerWinsTextField);
        statsPanel.add(tiesLabel);
        statsPanel.add(tiesTextField);

        // Results
        resultsTextArea = new JTextArea(10, 20);
        resultsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsTextArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Adding panels
        add(buttonPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.WEST);
        add(resultsPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Player choices map
        playerChoices.put("Rock", 0);
        playerChoices.put("Paper", 0);
        playerChoices.put("Scissors", 0);

        // Computer strategy
        setComputerStrategy(new RandomStrategy());
    }

    public void setComputerStrategy(ComputerStrategy strategy) {
        this.computerStrategy = strategy;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitButton) {
            dispose();
        } else {
            String playerChoice = "";
            if (e.getSource() == rockButton) {
                playerChoice = "Rock";
            } else if (e.getSource() == paperButton) {
                playerChoice = "Paper";
            } else if (e.getSource() == scissorsButton) {
                playerChoice = "Scissors";
            }

            String computerChoice = computerStrategy.chooseGesture(playerChoices);
            String result = determineWinner(playerChoice, computerChoice);

            resultsTextArea.append(result + "\n");
            updateStats(result);

            // Update player choices
            playerChoices.put(playerChoice, playerChoices.get(playerChoice) + 1);
        }
    }

    // Results
    private String determineWinner(String playerChoice, String computerChoice) {
        if (playerChoice.equals(computerChoice)) {
            ties++;
            return "Tie: Both chose " + playerChoice;
        } else if ((playerChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                (playerChoice.equals("Paper") && computerChoice.equals("Rock")) ||
                (playerChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
            playerWins++;
            return "Player wins: " + playerChoice + " beats " + computerChoice;
        } else {
            computerWins++;
            return "Computer wins: " + computerChoice + " beats " + playerChoice;
        }
    }

    private void updateStats(String result) {
        playerWinsTextField.setText(Integer.toString(playerWins));
        computerWinsTextField.setText(Integer.toString(computerWins));
        tiesTextField.setText(Integer.toString(ties));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RockPaperScissorsFrame::new);
    }
}
