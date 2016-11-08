/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Person;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author thais
 */
@Stateless
@LocalBean
public class PeopleManager {
    @PersistenceContext(unitName = "TODOProject-ejbPU")
    private EntityManager em;
    
    public Person getPersonById(int id) {
        return em.find(Person.class, id);
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
