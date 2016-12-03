import MySocket.receive.MySocketProtocol;
import init.Task;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 10441 on 2016/11/28.
 */
public class Main {

    private static int PORT = 12345;

    public static void main(String[] args) throws Exception {
        writeTest();
    }

    private static void writeTest() throws Exception {
        //startServer();
        File file=new File("C:/Users/10441/Desktop/HA-Proxifier328-LDR.rar");
        RandomAccessFile clientFile=new RandomAccessFile("C:/Users/10441/Desktop/HA-Proxifier328-LDR.rar","rw");
        Task task=new Task(file.getName(), "CB6BAE7581DCD2434E77C42F35F009FB",file.length(),0);
        clientFile.seek(0);
        long nowSize=0;
        int blockSize=1024*500;
        byte[] data = new byte[blockSize];
        long a=System.currentTimeMillis();
        while(nowSize<file.length()){
            if ((file.length()-nowSize)<(blockSize)){
                data=new byte[(int)(file.length()-nowSize)];
                blockSize=(int)(file.length()-nowSize);
            }

            clientFile.readFully(data);
            task.addByte(data);
            nowSize=nowSize+(blockSize);
        }

        System.out.println((System.currentTimeMillis()-a));
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
