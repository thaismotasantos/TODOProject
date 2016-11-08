/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
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
    private TasksManager tm;

    @PostConstruct
    public void init() {
        tm.createTestTasks();
    }
}
