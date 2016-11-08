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
    
    public final String status;       

    private ETaskStatus(String s) {
        status = s;
    }    

    public String getStatus() {
        return status;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : status.equals(otherName);
    }
    
    @Override
    public String toString() {
       return this.status;
    }
}
