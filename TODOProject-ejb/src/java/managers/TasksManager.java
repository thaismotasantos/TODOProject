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
    
    public void createTask(String name, Person assignedPerson, ETaskStatus status, String description) {
        Task task = new Task(name, assignedPerson, status, description);
        persist(task);
    }
    
    public void createTestTasks() {
        createTask("Corriger bug XYZ", null, ETaskStatus.NOT_ASSIGNED, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse felis justo, euismod eget tincidunt faucibus, venenatis nec arcu. Suspendisse potenti.");
        createTask("Implémtenter fonction X", null, ETaskStatus.IN_PROGRESS, "Sed nibh enim, varius vel dictum eget, auctor id ante. Nunc diam felis, euismod vel gravida vel, euismod vel augue. ");
        createTask("Finir test ZZZ", null, ETaskStatus.COMPLETED, "Curabitur pulvinar, metus pretium mattis lacinia, ligula elit condimentum neque, sed pharetra mi magna euismod purus.");
        createTask("Tache bla bla", null, ETaskStatus.NOT_ASSIGNED, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse felis justo, euismod eget tincidunt faucibus, venenatis nec arcu. Suspendisse potenti.");
        createTask("Test unitaire 9999", null, ETaskStatus.COMPLETED, "Sed nibh enim, varius vel dictum eget, auctor id ante. Nunc diam felis, euismod vel gravida vel, euismod vel augue. ");
        createTask("CRUD Entité", null, ETaskStatus.COMPLETED, "Curabitur pulvinar, metus pretium mattis lacinia, ligula elit condimentum neque, sed pharetra mi magna euismod purus.");
    }
    
    public List<Task> getAllTasks() {
        Query query = em.createNamedQuery("Task.findAll");  
        return query.getResultList();  
    }
    
    public Task getTask(int taskId) {
        return em.find(Task.class, taskId);
    }
    
    public int delete(Task t) {
        int tId = t.getId();
        try {
            em.remove(em.merge(t));
            return tId;
        } catch (Exception e) {
            return 0;
        }
        
    }
    
    public void persist(Object object) {
        em.persist(object);
    }
}
