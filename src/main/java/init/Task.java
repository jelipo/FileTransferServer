package init;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by 10441 on 2016/12/1.
 */
public class Task {

    private String fileName;
    private long fileSize;
    private String fileMd5;
    private long nowSize = 0;
    private byte[] dataBuffer;
    private RandomAccessFile randomAccessFile;
    private String filePath;
    private File confFile;
    private File file;
    //private byte[] dateTemp;
    private int tempSize = 0; //当前缓存的实际大小
    //int bufferMaxSize=1024*1024*100;
    //private ByteBuffer byteBuffer=ByteBuffer.allocate(bufferMaxSize);
    private FileChannel channelOut ;
    private MappedByteBuffer mappedBuffer;
    public Task(String fileName, String fileMd5, long fileSize, long nowSize) throws IOException {
        //dateTemp = new byte[bufferMaxSize]; //预设缓存的最大
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileMd5 = fileMd5;
        this.nowSize = nowSize;
        String jarFilePath = Task.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            jarFilePath = java.net.URLDecoder.decode(jarFilePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.filePath = new File(new File(jarFilePath).getParent(), "transfer/" + fileName + ".tran").getAbsolutePath();
        this.confFile = new File(filePath + ".conf");
        file = new File(filePath);
        if (!file.exists()) {
            creatNewFile();
        }
        randomAccessFile = new RandomAccessFile(filePath, "rw");
        channelOut= randomAccessFile.getChannel();
        try {
            mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, nowSize,fileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized long numForAddAndGetNowSize(int size) {
        nowSize = nowSize + size;
        return nowSize;
    }

    /**
     * 向缓存中写数据
     */
    public long addByte(byte[] data) throws IOException {
        mappedBuffer.put(data);
        nowSize = nowSize + data.length;
        if(nowSize==fileSize){
            channelOut.close();
            doOver();
        }
        return nowSize;

//        nowSize = nowSize + data.length;
//        int shengyu=byteBuffer.remaining();
//        if (shengyu<data.length){
//            byteBuffer.put(data,0,shengyu);
//            writeByteBufferAndChangeConf(byteBuffer);
//            byteBuffer.clear();
//            byteBuffer.put(data,shengyu,data.length-shengyu);
//        }else{
//            byteBuffer.put(data);
//        }
//        if (nowSize==fileSize){
//            doOver();
//            return -1; //返回-1说明任务完成
//        }
//        return nowSize;

//        nowSize = nowSize + data.length;
//        for(int i=0;i<data.length;i++){
//            if (tempSize==dateTemp.length){
//                writeByteBufferAndChangeConf(dateTemp);
//                tempSize=0;
//            }
//            dateTemp[tempSize]=data[i];
//            tempSize++;
//        }
//        if (nowSize==fileSize){
//            doOver();
//            return -1; //返回-1说明任务完成
//        }
//        return nowSize;
//        dataBuffer = ArrayUtils.addAll(dataBuffer, data);
//        nowSize = nowSize + data.length;
//        if (nowSize == fileSize) {
//            writeByteBufferAndChangeConf(dateTemp);
//            doOver();
//            return -1;
//        } else {
//
//            if (dataBuffer.length > 1024 * 1024 * 1) {
//                writeByteBufferAndChangeConf(dateTemp);
//                return 0;
//            }
//            return nowSize;
//        }

    }

    /**
     * 写入配置文件，
     */
    private void writeConf() throws IOException {
        JSONObject json = new JSONObject();
        json.put("fileName", fileName);
        json.put("fileSize", fileSize);
        json.put("fileMd5", fileMd5);
        json.put("nowSize", nowSize);
        confFile.createNewFile();
        OutputStreamWriter pw = null;
        pw = new OutputStreamWriter(new FileOutputStream(confFile), "UTF-8");
        pw.write(json.toJSONString());
        pw.close();
    }

    private void creatNewFile() {
        try {
            file.createNewFile();
            randomAccessFile = new RandomAccessFile(filePath, "rw");
            randomAccessFile.setLength(fileSize);
            confFile.createNewFile();
            writeConf();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeByteBufferAndChangeConf(ByteBuffer buffer) {

        try {
            //long a=System.currentTimeMillis();
            randomAccessFile.write(buffer.array());
            //System.out.println(System.currentTimeMillis()-a);
            writeConf();
            tempSize = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doOver() {
        confFile.delete();
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        File newFile = new File(filePath.replace(".tran", ""));
        int i = 0;
        while (!file.renameTo(newFile) && i < 100) {
            i++;
        }
        System.out.println(i);
    }
}
