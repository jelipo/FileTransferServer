package init;

import ctrl.client.ClientFileCtrl;
import ctrl.client.ClientMsgCtrl;
import ctrl.server.ServerFileCtrl;
import ctrl.server.ServerMsgCtrl;

import java.net.Socket;

/**
 * Created by 10441 on 2016/12/4.
 */
public class TaskTemp {

    private Socket socket;
    private ServerFileCtrl serverFileCtrl;
    private ServerMsgCtrl serverMsgCtrl;
    private ClientFileCtrl clientFileCtrl;
    private ClientMsgCtrl clientMsgCtrl;
    private MainController mainController;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerFileCtrl getServerFileCtrl() {
        return serverFileCtrl;
    }

    public void setServerFileCtrl(ServerFileCtrl serverFileCtrl) {
        this.serverFileCtrl = serverFileCtrl;
    }

    public ServerMsgCtrl getServerMsgCtrl() {
        return serverMsgCtrl;
    }

    public void setServerMsgCtrl(ServerMsgCtrl serverMsgCtrl) {
        this.serverMsgCtrl = serverMsgCtrl;
    }

    public ClientFileCtrl getClientFileCtrl() {
        return clientFileCtrl;
    }

    public void setClientFileCtrl(ClientFileCtrl clientFileCtrl) {
        this.clientFileCtrl = clientFileCtrl;
    }

    public ClientMsgCtrl getClientMsgCtrl() {
        return clientMsgCtrl;
    }

    public void setClientMsgCtrl(ClientMsgCtrl clientMsgCtrl) {
        this.clientMsgCtrl = clientMsgCtrl;
    }
}
