package init;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by 10441 on 2016/12/1.
 */
public class TaskManager {
    private Map<String,Task> tasksmap;


    public void addByte(String fileMd5,byte[] data){
        Task task=tasksmap.get(fileMd5);
        if (task!=null){

        }else{

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
