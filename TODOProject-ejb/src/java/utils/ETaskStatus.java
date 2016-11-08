/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author thais
 */
public enum ETaskStatus {
    NOT_ASSIGNED ("Not assigned"),
    IN_PROGRESS ("In progress"),
    COMPLETED ("Completed");
    
    private final String name;       

    private ETaskStatus(String s) {
        name = s;
    }    

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }
    
    @Override
    public String toString() {
       return this.name;
    }
}
