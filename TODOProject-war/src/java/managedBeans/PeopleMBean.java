/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entities.Person;
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
    private PeopleManager peopleManager;
    private List<Person> peopleList = new ArrayList();
    private String message;
    private LazyDataModel<Person> peopleModel;
    
    /**
     * Creates a new instance of PeopleMBean
     */
    public PeopleMBean() {
        peopleModel = new LazyDataModel<Person>() {
            @Override
            public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return peopleManager.findRange(first, pageSize, sortField, sortOrder.toString());
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
    }
    
    public String createTestData(){
        peopleManager.createTestPeople();
        refreshCache();
        
        return "index?faces-redirect=true";
    }
    
    public void refreshCache(){
         peopleList = peopleManager.getAllPeople();
    }
    
    public String showPerson(int id) {  
        return "ShowPerson?id=" + id + "&amp;faces-redirect=true";
    }
    
    public String editPerson(int id) {  
        return "EditPerson?id=" + id + "&amp;faces-redirect=true";
    }
    
    public void deletePerson(Person p) {
        peopleManager.delete(p);
        FacesContext context = FacesContext.getCurrentInstance();
        message = "Person deleted";
        context.addMessage(null, new FacesMessage("Successful", message));
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
    
}