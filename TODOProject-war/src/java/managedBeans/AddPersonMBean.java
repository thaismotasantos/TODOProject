/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import entities.Person;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import managers.PeopleManager;

/**
 *
 * @author thais
 */
@Named(value = "addPersonMBean")
@ViewScoped
public class AddPersonMBean implements Serializable {
    @EJB
    private PeopleManager peopleManager;
    private int personId;
    private Person person;

    /**
     * Creates a new instance of AddPersonMBean
     */
    public AddPersonMBean() {
        this.person = new Person();
    }
    
    public String save() {
        if(personId != 0) {
            person = peopleManager.update(person);
        } else {
            peopleManager.createPerson(person, new ArrayList<>());            
        }
        return "PeopleList?amp;faces-redirect=true";
    }
    
    public String list() {
        return "PeopleList?amp;faces-redirect=true";
    }

    public void loadPerson() {
        this.person = peopleManager.getPersonById(personId);
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
}
