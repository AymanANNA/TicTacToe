import javax.swing.*;
import javax.xml.crypto.dsig.XMLObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Scanner;

public class joueur2 {


    private static TicTacToeGUI game;
    private static OutputStream out;  // Network output stream global for access in the ActionListener
    public String[][] XO = new String[3][3];

    private String X0;

    public joueur2() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                XO[i][j] = " ";
            }
        }
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out = socket.getOutputStream();  // Keep a reference to the OutputStream to use in the ActionListener
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream inp = null;
        try {
            inp = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp));


        try {
            X0 = reader.readLine();
            System.out.println(X0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(X0);


        new Thread(() -> {

            SwingUtilities.invokeLater(() -> {
                game = new TicTacToeGUI();// Initialize the GUI on the EDT

                setupButtonListeners();    // Set up button listeners after GUI is created

            });


        }).start();




        // Thread to listen for server messages
        new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Handle incoming server messages appropriately
                    String finalLine = line;
                    String[] a = finalLine.split(" ");
                    int i = Integer.parseInt(a[0]);
                    int j = Integer.parseInt(a[1]);
                    SwingUtilities.invokeLater(() -> {


                        toggleButton(true);
                        if(X0.equals("X")){
                            game.buttons[i][j].setText("O");
                            XO[i][j] = "O";
                        }
                        else{
                            game.buttons[i][j].setText("X");
                            XO[i][j] = "X";
                        }
                        game.textAreaWinner.setText(checkWinner(XO));
                        game.textArea.setText("it's your turn");
                    });
                }
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {

        new joueur2();


    }//zeofjqsdjfqpidfpieof


    private void setupButtonListeners () {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = game.buttons[i][j];
                int finalJ = j;
                int finalI = i;
                btn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedButton = (JButton) e.getSource();
                        if (clickedButton.getText().equals("")) {
                            System.out.println(X0);

                            clickedButton.setText(X0);
                            XO[finalI][finalJ] =X0;
                            game.textAreaWinner.setText(checkWinner(XO));


                            sendMoveToServer(finalI, finalJ);
                            game.textArea.setText("wait it's the other player turn !!!");
                            toggleButton(false);
                        }
                    }
                });
            }
        }
    }

    private static void sendMoveToServer ( int i, int j){
        try {
            out.write((i + " " + j + "\n").getBytes());
            out.flush();
            System.out.println("hii");
        } catch (IOException e) {
            System.out.println("Error sending move to server: " + e.getMessage());
        }
    }
    private static void toggleButton ( boolean state){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game.buttons[i][j].setEnabled(state);
            }
        }
    }

    public String checkWinner(String[][] board) {
        // Check for horizontal and vertical wins
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i][0] != null && board[i][1] != null && board[i][2] != null &&
                    board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals(" ")) {
                return board[i][0] + " wins!";
            }
            // Check columns
            if (board[0][i] != null && board[1][i] != null && board[2][i] != null &&
                    board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && !board[0][i].equals(" ")) {
                return board[0][i] + " wins!";
            }
        }

        // Check diagonals
        if (board[0][0] != null && board[1][1] != null && board[2][2] != null &&
                board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].equals(" ")) {
            return board[0][0] + " wins!";
        }
        if (board[0][2] != null && board[1][1] != null && board[2][0] != null &&
                board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].equals(" ")) {
            return board[0][2] + " wins!";
        }

        // Check if the board is full
        boolean isFull = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null || board[i][j].equals(" ")) {
                    isFull = false;
                    break;
                }
            }
            if (!isFull) {
                break;
            }
        }

        // If no winner and the board is full
        if (isFull) {
            return "No one wins.";
        }

        // If no winner is found and the board is not full
        return "No winner yet.";
    }



}
