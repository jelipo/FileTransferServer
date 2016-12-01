package init;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by 10441 on 2016/12/1.
 */
public class Task {

    private String fileName;
    private long fileSize;
    private String fileMd5;
    private long nowSize;
    private byte[] dataBuffer;

    public Task(String fileName, String fileMd5, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileMd5 = fileMd5;

    }

    public synchronized long numForAddAndGetNowSize(int size) {
        nowSize = nowSize + size;
        return nowSize;
    }

    public long addByte(Boolean isOver, byte[] data) {
        if (isOver) {

            return 0;
        } else {
            if (dataBuffer.length < 1024 * 1024 * 10) {
                dataBuffer = ArrayUtils.addAll(dataBuffer, data);
                nowSize = nowSize + data.length;
                return nowSize;
            } else {

                return 0;
            }
        }
    }

}
