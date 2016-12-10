/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Person;
import entities.Task;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author thais
 */
@Stateless
@LocalBean
public class PeopleManager {
    @EJB
    private TasksManager tasksManager;
    @PersistenceContext(unitName = "TODOProject-ejbPU")
    private EntityManager em;   
    
    public void createPerson(String firstName, String lastName, List<Task> tasksList) {
        if(tasksList == null) {
            tasksList = new ArrayList<Task>();
        }
        persist(new Person(firstName, lastName, tasksList));
    }
    
    public void createPerson(Person person) {
        persist(person);
    }
    
    public List<Person> getAllPeople() {
        Query query = em.createNamedQuery("Person.findAll");  
        return query.getResultList();  
    }
    
    public List<Person> findRange(int start, int nb, String sortField, String sortOrder, Map<String, Object> filters) {
        String query = "select p from Person p";
        if(filters != null && !filters.isEmpty()) {
            query += " where";
            boolean isFirstFilter = true;
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                if(!isFirstFilter) {
                    query += " and";
                } else {
                    isFirstFilter = false;                    
                }
                query += " lower(p." + entry.getKey() + ") like lower(:" + entry.getKey() + ")";
            }
        }
        if(sortField != null) {
            query += " order by p." + sortField;
            if(sortOrder.equals("DESCENDING")) {
                query += " DESC";
            } else {
                query += " ASC";
            }
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
        Query q = em.createQuery("select count(p) from Person p");
        Number c = (Number)q.getSingleResult();
        return c.intValue();
    }
    
    public void createTestPeople() {
        List<Task> tasks = tasksManager.getAllTasks();
        createPerson("John", "Lennon", tasks);  
        createPerson("Paul", "McCartney", new ArrayList<>());  
        createPerson("Ringo", "Starr", new ArrayList<>());  
        createPerson("Georges", "Harrisson", new ArrayList<>());
        
        /*for (int i = 0; i < 200; i++) {        
            createPerson("Prenom" + i, "Nom" + i, new ArrayList<>());
        }*/
    }
    
    public Person getPersonById(int id) {
        return em.find(Person.class, id);
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
    public Person update(Person person) {
        return em.merge(person);  
    }

    public void delete(int personId) {
        Person p = em.find(Person.class, personId);
        em.remove(p);
    }

    public void delete(Person p) {
        for (Task t : p.getTasksList()) {
            t.setAssignedPerson(null);
            tasksManager.update(t);
        }
        em.remove(em.merge(p));
    }
}
