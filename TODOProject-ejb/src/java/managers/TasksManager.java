/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Person;
import entities.Task;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utils.ETaskStatus;

/**
 *
 * @author thais
 */
@Stateless
@LocalBean
public class TasksManager {
    @EJB
    private PeopleManager peopleManager;
    @PersistenceContext(unitName = "TODOProject-ejbPU")
    private EntityManager em;
    
    public void createTask(String name, int assignedPersonId, ETaskStatus status) {
        Person assignedPerson = null;
        if(assignedPersonId != 0) {
            assignedPerson = peopleManager.getPersonById(assignedPersonId);
        }
        Task task = new Task(name, assignedPerson, status);
        persist(task);
    }
    
    public List<Task> getAllTasks() {
        Query query = em.createNamedQuery("Task.findAll");  
        return query.getResultList();  
    }
    
    public void persist(Object object) {
        em.persist(object);
    }
}
