/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entities.Person;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import managers.PeopleManager;
import managers.TasksManager;

/**
 *
 * @author cojoc
 */
@Named(value = "addTaskMBean")
@ViewScoped
public class AddTaskMBean implements Serializable {
    @EJB
    private TasksManager tasksManager;
    @EJB
    private PeopleManager peopleManager;
    private String name;
    private String description;
    private String assignedPersonId;
    /**
     * Creates a new instance of AddTaskMBean
     */
    public AddTaskMBean() {
    }

    public void save() throws IOException {
        int id = Integer.parseInt(assignedPersonId);
        Person assignedPerson = peopleManager.getPersonById(id);
        tasksManager.createTask(name, assignedPerson, description);
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedPersonId() {
        return assignedPersonId;
    }

    public void setAssignedPersonId(String assignedPersonId) {
        this.assignedPersonId = assignedPersonId;
    }
    
    
}
