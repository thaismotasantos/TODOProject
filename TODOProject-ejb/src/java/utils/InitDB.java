/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import managers.PeopleManager;
import managers.TasksManager;

/**
 *
 * @author cojoc
 */
@Singleton
@LocalBean
@Startup
public class InitDB {
    @EJB
    private PeopleManager peopleManager;
    @EJB
    private TasksManager tm;

    @PostConstruct
    public void init() {
        
        peopleManager.createTestPeople();
        createPeople();
        tm.createTestTasks();
    }
    
    private void createPeople() {
        URL url = getClass().getResource("people.csv");
        String csvFile = url.getPath();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] person = line.split(cvsSplitBy);
                peopleManager.createPerson(person[1], person[0], new ArrayList<>());
                System.out.println("Person [lastname= " + person[0] + " , firstname=" + person[1] + "]");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    
}
