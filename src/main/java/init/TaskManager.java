package init;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10441 on 2016/12/1.
 */
public class TaskManager {
    private Map<String,Task> tasksmap;
    public TaskManager(){
        tasksmap=new HashMap();
    }

    /** 此方法接收传来文件的某部分的字节数组，并返回当前已经接收的字节的总长度
     * @param fileMd5 文件的md5值，同时也是任务的特征值
     * @param data  文件某部分的字节
     * @return 返回的数值为当前文件接受了多少字节，当返回值为负数时，代表状态值
     * 值为-1时：文件传输完成，-2：任务未创建
     */
    public long addByte(String fileMd5,byte[] data) throws IOException {
        Task task=tasksmap.get(fileMd5);
        if (task!=null){
            long sizeOrStatus=task.addByte(data);
            if (sizeOrStatus==-1){
                tasksmap.remove(fileMd5);
            }
            return sizeOrStatus;
        }else{
            return -2;
        }
    }

    public long addNewTask(String filename,String fileMd5,long fileSize) throws IOException {
        Task task=tasksmap.get(fileMd5);
        if (task==null){
            task=new Task(filename,fileMd5,fileSize,0);
            tasksmap.put(fileMd5,task);
            return 0;
        }else{
            return task.numForAddAndGetNowSize(0);
        }
    }
}
