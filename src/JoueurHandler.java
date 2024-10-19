import java.io.*;
import java.net.Socket;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;



public class JoueurHandler extends Thread {
    public static int lock ;
    Socket socket;
    ArrayList<Socket> sockets;


    public JoueurHandler(ArrayList<Socket> sockets, Socket socket) {
        this.sockets = sockets;
        this.socket = socket;
    }

    public void run() {
        if(socket == null) {
            System.exit(0);
        }
        try{

            OutputStream outp = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String input;
            while (true) {

                if (sockets.size() > 2) {
                    outp.write("impossible de se connecter à cette salon".getBytes());
                    outp.flush();
                    socket.close();
                }

                else {
                    input = reader.readLine();




                    for (int i=0; i < 2; i++) {
                        if (sockets.get(i) != socket) {
                            OutputStream out = sockets.get(i).getOutputStream();

                            out.write((input+"\n").getBytes());
                            out.flush();
                        }
                    }
                }

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}