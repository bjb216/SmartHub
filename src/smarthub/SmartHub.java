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
import static javax.bluetooth.DiscoveryAgent.PREKNOWN;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class SmartHub {

    public static final Vector/*<RemoteDevice>*/ devicesDiscovered = new Vector();
    public static final String bjbid="70:3E:AC:1A:10:42";
    public static final String mdbid="10:D5:42:EF:EC:45";

    public static void main(String[] args) throws BluetoothStateException, InterruptedException, IOException {
        System.out.println("new test 2-10-16");
        final Object inquiryCompletedEvent = new Object();
        devicesDiscovered.clear();
        
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
        
         LocalDevice ld=LocalDevice.getLocalDevice();
        DiscoveryAgent agent = ld.getDiscoveryAgent();
        RemoteDevice[] devs=agent.retrieveDevices(DiscoveryAgent.CACHED);
        if(devs==null)
            System.out.println("cached is null");
        else
            System.out.println("cached size: "+devs.length);
        
        RemoteDevice bjb=devs[0];
        System.out.println("starting to wait");
        try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(SmartHub.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        System.out.println(bjb.getFriendlyName(true));
        System.out.println(bjb.getBluetoothAddress());
        if(bjb.isTrustedDevice())
            System.out.println("is trusted device");

        
        
    

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

            
            
        }
     */
}

}
