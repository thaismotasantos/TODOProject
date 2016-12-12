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
        Person person = new Person(firstName, lastName);
        for(Task t : tasksList) {
            t.setAssignedPerson(person);
        }
        person.setTasksList(tasksList);
        persist(person);
    }
    
    public void createPerson(Person person, List<Task> tasks) {
        List l = new ArrayList<Task>();
        for(Task t : tasks) {
            t = tasksManager.getTaskById(t.getId());
            t.setAssignedPerson(person);
            l.add(t);
        }
        person.setTasksList(l);
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
            for (Entry<String, Object> entry : filters.entrySet()) {
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
        } else {
            query += " order by p.id ASC";
        }
        //System.out.println("FIND RANGE GESTIONNAIRE QUERY " + query + " start " + start + " nb " + nb);
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
        /*Task task1 = tasksManager.getTaskById(1);
        Task task2 = tasksManager.getTaskById(2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);*/
        createPerson("John", "Lennon", new ArrayList<>());  
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
    
    public Person update(Person person, List<Task> tasks) {
        List l = new ArrayList<Task>();
        for(Task t : tasks) {
            t = tasksManager.getTaskById(t.getId());
            t.setAssignedPerson(person);
            l.add(t);
        }
        List<Task> notAssignedToPersonAnymore = new ArrayList<>();
        if(person.getTasksList().removeAll(l) || !person.getTasksList().isEmpty()) {
            notAssignedToPersonAnymore = person.getTasksList();
            for(Task t : notAssignedToPersonAnymore) {
                t = tasksManager.getTaskById(t.getId());
                t.setAssignedPerson(null);
            }
        }
        
        person.setTasksList(l);
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
