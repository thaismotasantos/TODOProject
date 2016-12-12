/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entities.Person;
import entities.Task;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import managers.PeopleManager;
import managers.TasksManager;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import utils.Status;

/**
 *
 * @author cojoc
 */
@Named(value = "tasksMBean")
@ViewScoped
public class TasksMBean implements Serializable {
    @EJB
    private PeopleManager peopleManager;
    @EJB
    private TasksManager tasksManager;
    
    private List<Task> tasksList = new ArrayList();
    private String message;
    private LazyDataModel<Task> tasksModel;
    private Task selectedTask;
    
    private List<Status> status = new ArrayList<Status>(EnumSet.allOf(Status.class));
    private Person selectedPersonOnEdit;
    /**
     * Creates a new instance of TasksMBean
     */
    public TasksMBean() {
        tasksModel = new LazyDataModel<Task>() {
            @Override
            public List<Task> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return tasksManager.findRange(first, pageSize, sortField, sortOrder.toString(), filters);
            }

            @Override
            public int getRowCount() {
                return tasksManager.count();
            }
        };
    }
    
    @PostConstruct
    public void init() {
        this.tasksList = tasksManager.getAllTasks();
        
    }

    public void refreshCache(){
         this.tasksList = tasksManager.getAllTasks();
    }
    
    public List<Task> getTasksList() {
        if(tasksList.isEmpty()) {
           refreshCache();
        }
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

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public Person getSelectedPersonOnEdit() {
        return selectedPersonOnEdit;
    }

    public void setSelectedPersonOnEdit(Person selectedPersonOnEdit) {
        this.selectedPersonOnEdit = selectedPersonOnEdit;
    }
    
    
    public void onEdit(RowEditEvent event) {
        System.out.println("je suis dedans");
        try {
            Task newTask = (Task) event.getObject();
//            newTask.setAssignedPersonId(this.selectedPersonOnEdit.getId());
            tasksManager.update(newTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String deleteTask(Task t) {
            int deletedTaskId = tasksManager.delete(t);
            this.tasksList = tasksManager.getAllTasks();
            if (deletedTaskId != 0) {
                System.out.println("Task " + deletedTaskId + " deleted.");
                message = "Task " + deletedTaskId + " deleted.";
            } else {
                System.out.println("Unable to find task " + deletedTaskId);
                message = "Unable to find task " + deletedTaskId;
            }
        return "index?amp;faces-redirect=true";
    }
    
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String showTasksList() {
        return "index?amp;faces-redirect=true";
    }
    
    public List<Person> completePerson(String query) {
        List<Person> allPeople = peopleManager.getAllPeople();
        List<Person> filteredPeople = new ArrayList<Person>();
         
        for (int i = 0; i < allPeople.size(); i++) {
            Person t = allPeople.get(i);
            if(t.getFirstName().toLowerCase().startsWith(query.toLowerCase())) {
                filteredPeople.add(t);
            }
        }
         
        return filteredPeople;
    }
}

/*
                                        <p:autoComplete value="#{tasksMBean.selectedPersonOnEdit}" completeMethod="#{tasksMBean.completePerson}"
                                                        var="person" itemLabel="#{person.firstName}" itemValue="#{person.id}" converter="personConverter" forceSelection="true" />
*/