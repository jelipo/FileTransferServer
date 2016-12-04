package ctrl.server;

import MySocket.send.SendSocket;
import com.alibaba.fastjson.JSONObject;
import init.MainController;
import init.TaskManager;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by 10441 on 2016/12/1.
 */
public class ServerFileCtrl {

    public void cteatTask(JSONObject parm,byte[] data, Socket socket, MainController mainController) throws IOException {
        String filename=parm.getString("filename");
        long fileSize=parm.getLong("fileSize");
        String md5=parm.getString("md5");
        TaskManager taskManager=mainController.getTaskManager();
        long blockStart=taskManager.addNewTask(filename,md5,fileSize);
        parm.put("block",blockStart);
        parm.put("blockSize",1024*100);
        parm.remove("filename");
        parm.remove(fileSize);
        SendSocket.sendCustomMsg(SendSocket.SUCCESS_STATUS,"transfer",parm,socket);

    }
    public void addByte(JSONObject parm,byte[] data, Socket socket, MainController mainController){
        String md5=parm.getString("md5");
        TaskManager taskManager=mainController.getTaskManager();
        long sizeOrStatus=taskManager.addByte(parm.getString("fileMd5"),data);
        if (sizeOrStatus>0){
            SendSocket.sendCustomMsg(SendSocket.SUCCESS_STATUS,"transfer",parm,socket);
        }
    }
}
