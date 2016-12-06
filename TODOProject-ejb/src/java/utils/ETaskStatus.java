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
    NOT_ASSIGNED ("Not assigned", "red"),
    IN_PROGRESS ("In progress", "orange"),
    COMPLETED ("Completed", "green");
    
    private final String message;
    private final String color;
    
    private ETaskStatus(String message, String color) {
        this.message = message;
        this.color = color;
    }    

    public String getMessage() {
        return message;
    }

    public String getColor() {
        return color;
    }
    
    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : message.equals(otherName);
    }
    
    public ETaskStatus findByName(String name) {
        switch (name) {
            case "NOT_ASSIGNED":
                return ETaskStatus.NOT_ASSIGNED;
            case "IN_PROGRESS":
                return ETaskStatus.IN_PROGRESS;
            case "COMPLETED":
                return ETaskStatus.COMPLETED;
            default:
                return ETaskStatus.NOT_ASSIGNED;
        }
    }
    
    @Override
    public String toString() {
       return this.message;
    }
}
