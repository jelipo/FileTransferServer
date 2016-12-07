package MySocket.receive;

import com.alibaba.fastjson.JSONObject;
import ctrl.client.ClientFileCtrl;
import ctrl.client.ClientMsgCtrl;
import init.MainController;
import init.TaskTemp;

import java.io.IOException;

/**
 * Created by 10441 on 2016/12/5.
 */
public class ClientMySocketProtocol extends MySocketProtocol {
    public ClientMySocketProtocol(TaskTemp taskTemp, MainController mainController) {
        super(taskTemp, mainController);
    }

    public void ctrl(JSONObject head, byte[] data) {
        try {
            ClientFileCtrl clientFileCtrl = super.taskTemp.getClientFileCtrl();
            ClientMsgCtrl clientMsgCtrl = super.taskTemp.getClientMsgCtrl();
            if (head.getString("flag").equals("file")) {
                System.out.print(head);
            } else {
                if (head.getString("method").equals("pleaseAddByte")) {
                    clientMsgCtrl.addByte(socket, head.getJSONObject("parm"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
