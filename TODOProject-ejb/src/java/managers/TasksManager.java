/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Person;
import entities.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utils.Status;

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
    
    public Task createTask(String name, Person assignedPerson, String description) {
        Task task = new Task(name, assignedPerson, description);
        if (assignedPerson == null) {
            task.setStatus(Status.NOT_ASSIGNED);
        } else {
            task.setStatus(Status.IN_PROGRESS);
            assignedPerson.addTask(task);
            peopleManager.update(assignedPerson);
        }
        persist(task);
        return task;
    }
    
    public List<Task> getAllTasks() {
        Query query = em.createNamedQuery("Task.findAll");  
        return query.getResultList();  
    }
    
    public List<Task> findRange(int start, int nb, String sortField, String sortOrder, Map<String, Object> filters) {
        String query = "select t from Task t";
        if(filters != null && !filters.isEmpty()) {
            query += " where";
            boolean isFirstFilter = true;
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                if(!isFirstFilter) {
                    query += " and";
                } else {
                    isFirstFilter = false;                    
                }
                query += " lower(t." + entry.getKey() + ") like lower(:" + entry.getKey() + ")";
            }
        }
        if(sortField != null) {
            query += " order by t." + sortField;
            if(sortOrder.equals("DESCENDING")) {
                query += " DESC";
            } else {
                query += " ASC";
            }
        } else {
            query += " order by t.creationDate DESC";
        }
        System.out.println("FIND RANGE GESTIONNAIRE QUERY " + query + " start " + start + " nb " + nb);
        Query q = em.createQuery(query);
        if(filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                q.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
            }
        }
        q.setFirstResult(start);
        q.setMaxResults(nb);
        return q.getResultList();
    }
    
    public int count(){
        Query q = em.createQuery("select count(t) from Task t");
        Number c = (Number)q.getSingleResult();
        return c.intValue();
    }
    
    public void createTestTasks() {
        createTask("Corriger bug XYZ", peopleManager.getPersonById(5), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse felis justo, euismod eget tincidunt faucibus, venenatis nec arcu. Suspendisse potenti.");
        createTask("Implémtenter fonction X", peopleManager.getPersonById(5), "Sed nibh enim, varius vel dictum eget, auctor id ante. Nunc diam felis, euismod vel gravida vel, euismod vel augue. ");
        createTask("Finir test ZZZ", peopleManager.getPersonById(2), "Curabitur pulvinar, metus pretium mattis lacinia, ligula elit condimentum neque, sed pharetra mi magna euismod purus.");
        createTask("Tache bla bla", peopleManager.getPersonById(10), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse felis justo, euismod eget tincidunt faucibus, venenatis nec arcu. Suspendisse potenti.");
        createTask("Test unitaire 9999", peopleManager.getPersonById(3), "Sed nibh enim, varius vel dictum eget, auctor id ante. Nunc diam felis, euismod vel gravida vel, euismod vel augue. ");
        createTask("CRUD Entité", null, "Curabitur pulvinar, metus pretium mattis lacinia, ligula elit condimentum neque, sed pharetra mi magna euismod purus.");
        for(int i = 0; i < 100; i++) {
            if(i % 3 == 0)
                createTask("Task"+i, null, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse felis justo, euismod eget tincidunt faucibus, venenatis nec arcu. Suspendisse potenti.");
            else if(i % 3 == 1)
                createTask("Task"+i, null, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse felis justo, euismod eget tincidunt faucibus, venenatis nec arcu. Suspendisse potenti.");
            else
                createTask("Task"+i, null, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse felis justo, euismod eget tincidunt faucibus, venenatis nec arcu. Suspendisse potenti.");
        }
    }
    
    public Task getTaskById(int id) {
        return em.find(Task.class, id);
    }
    
    public Task update(Task task) {
        System.out.println("Name" + task.getName());
        System.out.println("Description" + task.getDescription());
        System.out.println("AssignedPerson" + task.getAssignedPerson());
        System.out.println("AssignedPersonID" + task.getAssignedPersonId());
        
        
        
        //Si la tâche avait une personne assignée
        if (task.getAssignedPerson() != null) {
            //Si la personne assignée a changé
            if (task.getAssignedPerson().getId() == task.getAssignedPersonId()) {
                Person oldAssignedPerson = task.getAssignedPerson();
                oldAssignedPerson.removeTask(task);
                peopleManager.update(oldAssignedPerson);
            } else {
                //Ne rien faire
            }
        }
        
        if (task.getAssignedPersonId() != 0) {
            Person newAssignedPerson = peopleManager.getPersonById(task.getAssignedPersonId());
            if (newAssignedPerson != null) {
                newAssignedPerson.addTask(task);
                peopleManager.update(newAssignedPerson);
                
                task.setAssignedPerson(newAssignedPerson);
                if (task.getStatus() == Status.NOT_ASSIGNED) {
                    task.setStatus(Status.IN_PROGRESS);
                }
                
            } else {
                task.setAssignedPerson(null);
            }
        } else {
            task.setAssignedPerson(null);
            task.setStatus(Status.NOT_ASSIGNED);
        }
        
        
        return em.merge(task);
    }
    
    public int delete(Task t) {
        if (t.getAssignedPerson() != null) {
            Person assignedPerson = t.getAssignedPerson();
            assignedPerson.removeTask(t);
            peopleManager.update(assignedPerson);
        }
        int tId = t.getId();
        try {
            em.remove(em.merge(t));
            return tId;
        } catch (Exception e) {
            return 0;
        }
        
    }
    
    public List<Task> getAvailableTasks() {
        //List<Task> availableTasks = new ArrayList<>();
        
        String query = "select t from Task t where t.assignedPerson = null";
        Query q = em.createQuery(query);
        return q.getResultList();
        
        //return availableTasks;
    }
    
    public void persist(Object object) {
        em.persist(object);
    }
}
