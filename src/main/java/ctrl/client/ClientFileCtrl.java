package ctrl.client;

import MySocket.send.SendSocket;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * Created by 10441 on 2016/12/4.
 */
public class ClientFileCtrl {

    public void addByte(Socket socket, JSONObject parm) throws IOException {
        int byteSize=parm.getInteger("blockSize");
        byte[] data=new byte[byteSize];
        long nowSize=parm.getLong("nowSize");
        parm=new JSONObject();
        parm.put("md5","CB6BAE7581DCD2434E77C42F35F009FB");
        RandomAccessFile randomAccessFile=new RandomAccessFile("C:\\Users\\10441\\Desktop\\The.Last.Naruto2014 BD720P.mp4","rw");
        randomAccessFile.seek(nowSize);
        randomAccessFile.readFully(data);
        SendSocket.sendCustomFile(parm,data,socket);
    }
}
