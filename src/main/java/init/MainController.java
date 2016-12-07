package init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10441 on 2016/11/29.
 */
public class MainController {

    private TaskManager taskManager;
    private String softwarePath;

    public MainController() throws IOException {
        this.softwarePath=MainController.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        taskManager=new TaskManager((HashMap) readConfAndInit());
    }
    public TaskManager getTaskManager(){
        return taskManager;
    }

    public String getSoftwarePath(){
        return softwarePath;
    }
    private Map readConfAndInit() throws IOException {
        String transferPath=softwarePath+"/transfer";
        File transferDe=new File(transferPath);
        Map map=new HashMap();
        if (transferDe.exists()){
            File[] fileList=transferDe.listFiles();
            for(int i=0;i<fileList.length;i++){
                if (FilenameUtils.getExtension(fileList[i].getAbsolutePath()).equals("conf")){
                    JSONObject json= JSON.parseObject(FileUtils.readFileToString(fileList[i],"UTF-8"));
                    map.put(json.get("fileMd5"),new Task(json.getString("fileName"),json.getString("fileMd5"),json.getLong("fileSize"),json.getLong("nowSize")));
                }
            }

        }else{
            transferDe.mkdirs();
        }
        return map;
    }
}
