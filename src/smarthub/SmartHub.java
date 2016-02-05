package smarthub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SmartHub {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> ids = new ArrayList<String>(Arrays.asList("Brandon","70:3E:AC:1A:10:42","Matt","10:D5:42:EF:EC:45"));
        Runtime rt = Runtime.getRuntime();
        while (true) {
            System.out.println("executing scan");
            try {

                Process pr = rt.exec("hcitool scan");
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                while ((line = input.readLine()) != null) {
                    if(line.contains(ids.get(1)))
                    System.out.println("user: "+ids.get(0));
                    
                    if(line.contains(ids.get(3)))
                    System.out.println("user: "+ids.get(2));
                }
                input.close();
                //System.out.println(pr.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(SmartHub.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(SmartHub.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
