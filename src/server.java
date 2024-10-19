import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class server {


    public static void main(String[] args) throws IOException {


        ServerSocket listener = new ServerSocket(8000);
        ArrayList<Socket> sockets = new ArrayList<>();
        int i=0;
        while (true) {
            Socket socket = listener.accept();
            sockets.add(socket);
            OutputStream os = socket.getOutputStream();
            i = sockets.size();
            if(i==1){
                os.write(("X\n").getBytes());
            }
            else if(i==2){
                os.write(("O\n").getBytes());
            }

            new Thread(new JoueurHandler(sockets , socket)).start();

        }
    }
}
