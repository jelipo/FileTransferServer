package init;

/**
 * Created by 10441 on 2016/12/10.
 */
public class TaskCache {
    private long fileBlockHasReceivedSize;
    private long fileBlockStartSize;
    private long fileBlockSize;

    /**此类抽象为文件多线程传输文件的某个文件块
     * @param fileBlockSize 文件块总大小
     * @param fileBlockStartSize 文件块从整个文件开始的位置
     */
    public TaskCache(long fileBlockSize,long fileBlockStartSize){
        this.fileBlockSize=fileBlockSize;
        this.fileBlockStartSize=fileBlockStartSize;
    }

}
