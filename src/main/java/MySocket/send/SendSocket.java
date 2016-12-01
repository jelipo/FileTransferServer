package MySocket.send;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by 10441 on 2016/12/1.
 */
public class SendSocket {
    private static JSONObject fileProtocol;
    private static JSONObject msgProtocol;
    private static byte[] firstCut;
    private static byte[] endCut;
    private static byte[] headCut;
    public final static int SUCCESS_STATUS=1;
    public final static int TIP_STATUS=2;
    public final static int WORNG_STATUS=3;
    static {
        fileProtocol=new JSONObject();
        fileProtocol.put("flag","file");
        msgProtocol=new JSONObject();
        msgProtocol.put("flag","msg");
        firstCut="/0!F/".getBytes();
        headCut="/0!H/".getBytes();
        endCut="/0!E/".getBytes();
    }
/** /0!F/ head /0!H/ filedata /0!E/
 *
 * head:
 * {
 *     "flag":"msg/file",
 *     dataSize:(int)...,
 *     "status":1/2/3,(状态：1：成功，2：提示，3：错误 需要断开连接)
 *     "method":"......"
 *     "parm":{ "...":"...“ , "...":"..." }，
 *
 * }
 **/
    public static void sendCustomMsg(int STATUS,String method,JSONObject parm,Socket socket){
        JSONObject newMsgProtocol= (JSONObject) msgProtocol.clone();
        newMsgProtocol.put("status",STATUS);
        newMsgProtocol.put("parm",parm);
        newMsgProtocol.put("method",method);
        send(socket,newMsgProtocol.toJSONString(),null);
    }
    public static void sendCustomFile(JSONObject parm,byte[] fileData,Socket socket){
        JSONObject newFileProtocol= (JSONObject) fileProtocol.clone();
        newFileProtocol.put("status",SUCCESS_STATUS);
        newFileProtocol.put("parm",parm);
        newFileProtocol.put("dataSize",fileData.length);
        newFileProtocol.put("method","receivefile");
        send(socket,newFileProtocol.toJSONString(),fileData);
    }
    public static void sendTipMsg(String tip,Socket socket){
        JSONObject newMsgProtocol= (JSONObject) msgProtocol.clone();
        newMsgProtocol.put("status",TIP_STATUS);
        JSONObject js=new JSONObject();
        js.put("tip",tip);
        newMsgProtocol.put("parm",js);
        newMsgProtocol.put("method","tip");
        send(socket,newMsgProtocol.toJSONString(),null);
    }
    public static void sendWorngMsg(String worng,Socket socket){
        JSONObject newMsgProtocol= (JSONObject) msgProtocol.clone();
        newMsgProtocol.put("status",WORNG_STATUS);
        JSONObject wo=new JSONObject();
        wo.put("worng",worng);
        newMsgProtocol.put("parm",wo);
        newMsgProtocol.put("method","worng");
        send(socket,newMsgProtocol.toJSONString(),null);
    }


    private static void send(Socket socket,String headAndMsg,byte[] fileData){
        byte[] valueByte= Base64.encodeBase64(headAndMsg.toString().getBytes());
        byte[] head=ArrayUtils.addAll(ArrayUtils.addAll(firstCut,valueByte),headCut);
        byte[] dataAndEnd=ArrayUtils.addAll(fileData,endCut);
        byte[] allBytes= ArrayUtils.addAll(head,dataAndEnd);
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(allBytes);
        } catch (IOException e) {
            System.out.println("socket发送失败");
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
    }
}
