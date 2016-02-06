package smarthub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class SmartHub {

    /**
     * @param args the command line arguments
     */
    public static final Vector/*<RemoteDevice>*/ devicesDiscovered = new Vector();

    public static void main(String[] args) throws BluetoothStateException, InterruptedException {
        System.out.println("hi dude");
        final Object inquiryCompletedEvent = new Object();
        devicesDiscovered.clear();
        
        //LocalDevice ld=LocalDevice.getLocalDevice();
        //DiscoveryAgent agent = ld.getDiscoveryAgent();
        //agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());
        
        DiscoveryListener listener = new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                devicesDiscovered.addElement(btDevice);
                try {
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
            }
        
                public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };
        synchronized(inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() +  " device(s) found");
            }
        }
    

    /*
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
     */
}

}
