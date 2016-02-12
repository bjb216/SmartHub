package smarthub;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;

public class Controller {
    BTConnection con;
    
    
    public Controller(){
        con = new BTConnection();
        initialize();
    }
    
    private void initialize() {
        try {
            con.discover();
        } catch (BluetoothStateException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void run() {
        if(con.devs!=null){
            for(int i=0;i<con.devs.length;i++){
                System.out.println("ID "+i+": "+con.devs[i]);
            }
        }
        GCalendar cal = new GCalendar("xxxx","yyy");
        ArrayList<String> events = cal.getMeetings();
        
        //Print out events
        //for...
        
        
    }
    
    
}
