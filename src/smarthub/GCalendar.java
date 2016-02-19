package smarthub;

import java.util.ArrayList;

public class GCalendar {
    private ArrayList<String> meetings;
    private String username;
    private String password;
    
    public GCalendar(String username, String password, String name){
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
