/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import utils.ETaskStatus;

/**
 *
 * @author thais
 */
@Entity
@NamedQueries({@NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")})
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private Person assignedPerson;
    private ETaskStatus status;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;

    public Task() {
        
    }

    public Task(String name, Person assignedPerson, ETaskStatus status) {
        this.name = name;
        this.assignedPerson = assignedPerson;
        this.status = status;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(Person assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public ETaskStatus getStatus() {
        return status;
    }

    public void setStatus(ETaskStatus status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Task[ id=" + id + " ]";
    }
    
}
