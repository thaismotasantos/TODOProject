/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entities.Task;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import managers.TasksManager;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author thais
 */
@Named(value = "tasksMBean")
@ViewScoped
public class TasksMBean implements Serializable {
    @EJB
    private TasksManager tm;
    private List<Task> tasksList = new ArrayList();
    private String message;
    private LazyDataModel<Task> tasksModel;

    /**
     * Creates a new instance of TasksMBean
     */
    public TasksMBean() {
    }
    
    @PostConstruct
    public void init() {
        this.tasksList = tm.getAllTasks();
    }

    public List<Task> getTasksList() {
        return tasksList;
    }

    public void setTasksList(List<Task> tasksList) {
        this.tasksList = tasksList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LazyDataModel<Task> getTasksModel() {
        return tasksModel;
    }

    public void setTasksModel(LazyDataModel<Task> tasksModel) {
        this.tasksModel = tasksModel;
    }
    
    public String deleteTask(Task t) {
        int deletedTaskId = tm.delete(t);
        this.tasksList = tm.getAllTasks();
        if (deletedTaskId != 0) {
            System.out.println("Task " + deletedTaskId + " deleted.");
            message = "Task " + deletedTaskId + " deleted.";
        } else {
            System.out.println("Unable to find task " + deletedTaskId);
            message = "Unable to find task " + deletedTaskId;
        }
        return "index?faces-redirect=true";
    }
}
