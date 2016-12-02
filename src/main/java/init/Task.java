package init;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.URLDecoder;

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

        randomAccessFile.seek(nowSize);

    }

    public synchronized long numForAddAndGetNowSize(int size) {
        nowSize = nowSize + size;
        return nowSize;
    }

    /**
     * 像缓存中写数据
     */
    public long addByte(byte[] data) {
        dataBuffer = ArrayUtils.addAll(dataBuffer, data);
        nowSize = nowSize + data.length;
        if (nowSize == fileSize) {
            writeByteBufferAndChangeConf();
            doOver();
            return -1;
        } else {
            if (dataBuffer.length > 1024 * 1024 * 10) {
                writeByteBufferAndChangeConf();
                return 0;
            }
            return nowSize;
        }

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

    private void writeByteBufferAndChangeConf() {
        try {
            randomAccessFile.write(dataBuffer);
            writeConf();
            dataBuffer = null;
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
