/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthub;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;

/**
 *
 * @author bartonb
 */
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
        if(con!=null){
            for(int i=0;i<con.devs.length;i++){
                System.out.println("ID "+i+": "+con.devs[i]);
            }
        }
        //System.out.println("You have begun coding");
        
    }
    
    
}
