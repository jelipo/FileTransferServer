package MySocket.receive;

import MySocket.send.SendSocket;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ctrl.server.ServerFileCtrl;
import ctrl.server.ServerMsgCtrl;
import init.MainController;
import init.TaskTemp;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 10441 on 2016/10/15.
 */
public class MySocketProtocol implements Runnable {
    private Socket socket;

    private String firsFlag = "/0!F/";
    private byte[] FIRST_BYTE;
    private int FIRST_LENGTH;
    private int[] first;
    private String endFlag = "/0!H/";
    private byte[] END_BYTE;
    private int END_LENGTH;
    private byte[] end;
    private String overFlag = "/0!E/";
    private TaskTemp taskTemp;

    public MySocketProtocol(TaskTemp taskTemp, MainController mainController) {
        this.socket = taskTemp.getSocket();
        this.taskTemp=taskTemp;
        new Thread(this).start();
    }

    private void init() {
        FIRST_BYTE = firsFlag.getBytes();
        FIRST_LENGTH = FIRST_BYTE.length;
        first = new int[FIRST_LENGTH - 1];
        for (int i = 0; i < (FIRST_LENGTH - 1); i++) {
            first[i] = FIRST_BYTE[i + 1];
        }
        END_BYTE = endFlag.getBytes();
        END_LENGTH = END_BYTE.length;
        end = new byte[END_LENGTH - 1];
        for (int i = 0; i < (END_LENGTH - 1); i++) {
            end[i] = END_BYTE[i + 1];
        }
    }

    @Override
    public void run() {
        init();
        try {
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

            int r = -1;
            Boolean isFindFirst = false;
            List<Byte> temp = new LinkedList<Byte>();
            byte[] endTemp = new byte[END_LENGTH - 1];
            while ((r = in.read()) != -1) {
                if (r == FIRST_BYTE[0] && (!isFindFirst)) {
                    for (int i = 0; i < FIRST_LENGTH - 1; i++) {
                        r = in.read();
                        if (first[i] != r) {
                            break;
                        } else {
                            if (i == (FIRST_LENGTH - 2))
                                isFindFirst = true;
                        }
                    }
                } else if (isFindFirst) {
                    temp.add((byte) r);
                    while ((r = in.read()) != -1) {
                        if (r == END_BYTE[0]) {
                            in.read(endTemp, 0, END_LENGTH - 1);
                            if (Arrays.equals(end, endTemp)) {
                                whatNeedToDO(temp, in);
                                temp.clear();
                                isFindFirst = false;
                                break;
                            } else {
                                temp.add((byte) END_BYTE[0]);
                                for (int i = 0; i < endTemp.length; i++) {
                                    temp.add(endTemp[i]);
                                }
                            }
                        } else {
                            temp.add((byte) r);
                        }
                    }
                }
            }
            //System.out.println(new String((new BASE64Decoder()).decodeBuffer(new String(st)), "utf-8"));
            socket.close();
        } catch (IOException ex) {
            System.out.println("连接可能已经断开，此连接中断" + socket.getInetAddress() + ":" + socket.getPort());

        } finally {
            System.out.println("连接断开");
        }
    }

    private void whatNeedToDO(List<Byte> data, BufferedInputStream in) throws IOException {
        List<Byte> list = new LinkedList();
        list.addAll(data);
        int size = list.size();
        byte[] by = new byte[size];
        Iterator<Byte> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            by[i] = it.next();
            i++;
        }
        byte[] s = Base64.decodeBase64(by);
        String ss = new String(s);
        JSONObject headJson = JSON.parseObject(ss);
        int leng = headJson.getInteger("dataSize");
        byte[] da = new byte[leng];
        in.read(da, 0, leng);
        byte[] overCut = new byte[overFlag.length()];
        in.read(overCut, 0, overFlag.length());
        JSONObject returnJson = null;
        if (Arrays.equals(overFlag.getBytes(), overCut)) {
            ctrl(headJson,da);

        } else {
            SendSocket.sendTipMsg("文件块数据不完整", socket);
        }
    }
    private void ctrl(JSONObject head,byte[] data) throws IOException {
        ServerFileCtrl serverFileCtrl=taskTemp.getServerFileCtrl();
        ServerMsgCtrl serverMsgCtrl=taskTemp.getServerMsgCtrl();
        if (head.getString("msg").equals("file")){
            if(head.getString("method").equals("addByte")){
                serverFileCtrl.addByte(head.getJSONObject("parm"),data,socket,taskTemp.getMainController());
            } else if (head.getString("method").equals("cteatTask")){
                serverFileCtrl.cteatTask(head.getJSONObject("parm"),data,socket,taskTemp.getMainController());
            }
        }
    }

}

