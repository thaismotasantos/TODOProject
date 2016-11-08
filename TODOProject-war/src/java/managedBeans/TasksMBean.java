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
    private TasksManager tasksManager;
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
        this.tasksList = tasksManager.getAllTasks();
    }
    
}
