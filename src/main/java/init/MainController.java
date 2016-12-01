package init;

/**
 * Created by 10441 on 2016/11/29.
 */
public class MainController {

    private TaskManager taskManager;

    public MainController(){
        taskManager=new TaskManager();
    }
    public TaskManager getTaskManager(){
        return taskManager;
    }

}
