import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI extends JFrame {
    public  JButton[][] buttons = new JButton[3][3];
    public JTextArea textArea;
    public JTextArea textAreaWinner;

    public TicTacToeGUI() {
        super("Tic Tac Toe Game");
        initializeGUI();
    }

    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 350);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(true);
        textAreaWinner = new JTextArea();
        textAreaWinner.setEditable(true);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(75, 75));
                boardPanel.add(buttons[i][j]);
            }
        }

        add(textArea, BorderLayout.NORTH);
        add(textAreaWinner, BorderLayout.CENTER);
        add(boardPanel, BorderLayout.SOUTH);

        setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}


