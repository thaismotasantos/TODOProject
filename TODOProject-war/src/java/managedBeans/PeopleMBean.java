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
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author thais
 */
@Named(value = "peopleMBean")
@ViewScoped
public class PeopleMBean implements Serializable {
    @EJB
    private TasksManager tasksManager;
    @EJB
    private PeopleManager peopleManager;
    
    private List<Person> peopleList = new ArrayList();
    private String message;
    private LazyDataModel<Person> peopleModel;
    private Person selectedPerson;
    private String newPersonFirstName;
    private String newPersonLastName;
    private DualListModel<Task> availableTasks;
    
    /**
     * Creates a new instance of PeopleMBean
     */
    public PeopleMBean() {
        peopleModel = new LazyDataModel<Person>() {
            @Override
            public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return peopleManager.findRange(first, pageSize, sortField, sortOrder.toString(), filters);
            }

            @Override
            public int getRowCount() {
                return peopleManager.count();
            }
        };
    }
    
    @PostConstruct
    public void init() {
        this.peopleList = peopleManager.getAllPeople();
        //this.newPerson = new Person();
        
        List<Task> availableTasksSource = this.tasksManager.getAvailableTasks();
        List<Task> availableTasksTarget = new ArrayList<Task>();
         
        this.availableTasks = new DualListModel<Task>(availableTasksSource, availableTasksTarget);        
    }
    
    public String createTestData(){
        peopleManager.createTestPeople();
        refreshCache();
        
        return "index?faces-redirect=true";
    }
    
    public void updateAvailableTasks() {
        List<Task> availableTasksSource = this.tasksManager.getAvailableTasks();
        List<Task> availableTasksTarget = new ArrayList<Task>();
        if(selectedPerson != null) {
            availableTasksTarget = selectedPerson.getTasksList();
        }
         
        this.availableTasks = new DualListModel<Task>(availableTasksSource, availableTasksTarget);   
    }
    
    public void refreshCache(){
         this.peopleList = peopleManager.getAllPeople();
    }
    
    /*public String showPerson(int id) {  
        return "AddPerson?id=" + id + "&amp;faces-redirect=true";
    }*/
    
    public void addPerson() {
        Person newPerson = new Person(newPersonFirstName, newPersonLastName);
        peopleManager.createPerson(newPerson, availableTasks.getTarget());
        
        addMessage("Successful", "Person added");
    }
    
//    public String addPerson() {
//        return "AddPerson?amp;faces-redirect=true";
//    } 
    
    public String editPerson(int id) {  
        return "AddPerson?id=" + id + "&amp;faces-redirect=true";
    }
    
    public void editPerson() {       
        peopleManager.update(selectedPerson, availableTasks.getTarget());
        
        addMessage("Successful", "Person modified");
//        int id = selectedPerson.getId();
//        return "AddPerson?id=" + id + "&amp;faces-redirect=true";
    }
    
    public void onEdit(RowEditEvent event) {
        System.out.println("entrou AQUI ON EDIT");
        try {
            Person person = (Person)event.getObject();
            peopleManager.update(person);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void deletePerson(Person p) {
        peopleManager.delete(p);
        
        addMessage("Successful", "Person deleted");
        /*FacesContext context = FacesContext.getCurrentInstance();
        message = "Person deleted";
        context.addMessage(null, new FacesMessage("Successful", message));*/
    }
    
    public void deletePerson() {
        peopleManager.delete(selectedPerson);
        selectedPerson = null;
        
        addMessage("Successful", "Person deleted");
    } 
    
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<Person> getPeopleList() {
        if(peopleList.isEmpty()) {
           refreshCache();
        }
        return peopleList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LazyDataModel<Person> getPeopleModel() {
        return peopleModel;
    }

    public void setPeopleModel(LazyDataModel<Person> peopleModel) {
        this.peopleModel = peopleModel;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public String getNewPersonFirstName() {
        return newPersonFirstName;
    }

    public void setNewPersonFirstName(String newPersonFirstName) {
        this.newPersonFirstName = newPersonFirstName;
    }

    public String getNewPersonLastName() {
        return newPersonLastName;
    }

    public void setNewPersonLastName(String newPersonLastName) {
        this.newPersonLastName = newPersonLastName;
    }

    public DualListModel<Task> getAvailableTasks() {
        return availableTasks;
    }

    public void setAvailableTasks(DualListModel<Task> availableTasks) {
        this.availableTasks = availableTasks;
    }
    
    public String showPeopleList() {
        return "PeopleList?amp;faces-redirect=true";
    }
}
