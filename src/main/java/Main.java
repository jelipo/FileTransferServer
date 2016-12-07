import MySocket.receive.MySocketProtocol;
import ctrl.server.ServerFileCtrl;
import ctrl.server.ServerMsgCtrl;
import init.MainController;
import init.Task;
import init.TaskManager;
import init.TaskTemp;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 10441 on 2016/11/28.
 */
public class Main {

    private static int PORT = 12345;

    public static void main(String[] args) throws Exception {

        startServer();
        //Thread.sleep(10000);
        //writeTest();

    }

    private static void writeTest() throws Exception {
        //startServer();
        File file = new File("C:\\Users\\10441\\Desktop\\The.Last.Naruto2014 BD720P.mp4");
        RandomAccessFile clientFile = new RandomAccessFile(file.getPath(), "rw");
        Task task = new Task(file.getName(), "CB6BAE7581DCD2434E77C42F35F009FB", file.length(), 0);
        clientFile.seek(0);
        long nowSize = 0;
        int blockSize = 1024 * 500;
        byte[] data = new byte[blockSize];
        long a = System.currentTimeMillis();
        while (nowSize < file.length()) {
            if ((file.length() - nowSize) < (blockSize)) {
                data = new byte[(int) (file.length() - nowSize)];
                blockSize = (int) (file.length() - nowSize);
            }
            clientFile.readFully(data);
            task.addByte(data);
            nowSize = nowSize + (blockSize);
        }
        System.out.println((System.currentTimeMillis() - a));
    }

    private static void startServer() throws IOException {
        MainController mainController = new MainController();
        ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        while (true) {
            TaskTemp taskTemp = new TaskTemp();
            Socket socket = serverSocket.accept();
            taskTemp.setSocket(socket);
            taskTemp.setServerFileCtrl(new ServerFileCtrl());
            taskTemp.setServerMsgCtrl(new ServerMsgCtrl());
            taskTemp.setMainController(mainController);
            new MySocketProtocol(taskTemp,new MainController());
        }
    }


}
