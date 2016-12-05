package ctrl.server;

import MySocket.send.SendSocket;
import com.alibaba.fastjson.JSONObject;
import init.MainController;
import init.TaskManager;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by 10441 on 2016/12/1.
 */
public class ServerFileCtrl {


    public void addByte(JSONObject parm,byte[] data, Socket socket, MainController mainController) throws IOException {
        String md5=parm.getString("md5");
        parm.put("blockSize",1024*5);
        TaskManager taskManager=mainController.getTaskManager();
        long sizeOrStatus=taskManager.addByte(md5,data);
        if (sizeOrStatus>0){
            parm.put("nowSize",sizeOrStatus);
            SendSocket.sendCustomMsg(SendSocket.SUCCESS_STATUS,"pleaseAddByte",parm,socket);
        }
    }
}
