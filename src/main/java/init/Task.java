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
    private long lastWriteConfSize = 0;
    private byte[] dataBuffer;
    private RandomAccessFile randomAccessFile;
    private String filePath;
    private File confFile;
    private File file;
    private int tempSize = 0; //当前缓存的实际大小
    private FileChannel channelOut;
    private MappedByteBuffer mappedBuffer;

    public Task(String fileName, String fileMd5, long fileSize, long nowSize) throws IOException {
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
        channelOut = randomAccessFile.getChannel();
    }

    public synchronized long numForAddAndGetNowSize(int size) {
        nowSize = nowSize + size;
        return nowSize;
    }

    /**
     * 向缓存中写数据
     */
    public long addByte(byte[] data) throws IOException {
        mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, nowSize, fileSize);
        mappedBuffer.put(data);
        nowSize = nowSize + data.length;
        if ((nowSize - lastWriteConfSize) > 1024 * 5) {
            lastWriteConfSize = nowSize;
            writeConf();
        }
        if (nowSize == fileSize) {
            channelOut.close();
            doOver();
            return -1;
        }
        return nowSize;
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
        if (!confFile.exists()) {
            confFile.createNewFile();
        }
        OutputStreamWriter pw = new OutputStreamWriter(new FileOutputStream(confFile), "UTF-8");
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
