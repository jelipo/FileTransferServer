import MySocket.receive.MySocketProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 10441 on 2016/11/28.
 */
public class Main {

    private static int PORT = 12345;

    public static void main(String[] args) throws IOException {
        startServer();

    }

    private static void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        while (true) {
            Socket socket = serverSocket.accept();
            new MySocketProtocol(socket);
        }
    }


}
