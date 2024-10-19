import javax.swing.*;
import javax.xml.crypto.dsig.XMLObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Scanner;

public class joueur3 {


    private static TicTacToeGUI game;
    private static OutputStream out;  // Network output stream global for access in the ActionListener

    private String X0;

    public joueur3() {
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
                        System.out.println(X0);
                        toggleButton(true);
                        if(X0.equals("X")){
                            game.buttons[i][j].setText("O");
                        }
                        else{
                            game.buttons[i][j].setText("X");
                        }




                        game.textArea.setText("it's your turn");
                    });
                }
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {

        new joueur3();


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


}
