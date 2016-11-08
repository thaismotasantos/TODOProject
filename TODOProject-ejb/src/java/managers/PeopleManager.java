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
    @PersistenceContext(unitName = "TODOProject-ejbPU")
    private EntityManager em;   
    
    public void createPerson(String firstName, String lastName, List<Task> tasksList) {
        if(tasksList == null) {
            tasksList = new ArrayList<Task>();
        }
        persist(new Person(firstName, lastName, tasksList));
    }
    
    public List<Person> getAllPeople() {
        Query query = em.createNamedQuery("Person.findAll");  
        return query.getResultList();  
    }
    
    public List<Person> findRange(int start, int nb, String sortField, String sortOrder) {
        String query = "select p from Person p";
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
        createPerson("John", "Lennon", new ArrayList<>());  
        createPerson("Paul", "McCartney", new ArrayList<>());  
        createPerson("Ringo", "Starr", new ArrayList<>());  
        createPerson("Georges", "Harrisson", new ArrayList<>());
        
        /*for (int i = 0; i < 200; i++) {        
            createPerson("Nom" + i, 150000*Math.random());
        }*/
    }
    
    public Person getPersonById(int id) {
        return em.find(Person.class, id);
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public void delete(Person p) {
        em.remove(em.merge(p));
    }
}
