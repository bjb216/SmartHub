/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthub;

import java.util.ArrayList;

/**
 *
 * @author bartonb
 */
public class GCalendar {
    private ArrayList<String> meetings;
    private String username;
    private String password;
    
    public GCalendar(String username, String password){
        this.username=username;
        this.password=password;
        doStuff();
    }
    
    private void doStuff(){
        //do stuff to generate meeting array
    }
    
    public ArrayList<String> getMeetings(){
        return meetings;
    }
    
}
