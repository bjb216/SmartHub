
package smarthub;

import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import static smarthub.SmartHub.bjbid;
import static smarthub.SmartHub.devicesDiscovered;

public class BTConnection {
    RemoteDevice[] devs;
    public static final Vector/*<RemoteDevice>*/ devicesDiscovered = new Vector();
    public static final String bjbid = "703EAC1A1042";
    public static final String mdbid = "10D542EFEC45";
    
    
    public BTConnection(){
        
    }
    
    public void discover() throws BluetoothStateException, InterruptedException{
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
                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };
        synchronized (inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() + " device(s) found");
            }
        }
        
        //Returns an array of all remote devices that have been identified in a previous scan
        LocalDevice ld = LocalDevice.getLocalDevice();
        DiscoveryAgent agent = ld.getDiscoveryAgent();
        devs = agent.retrieveDevices(DiscoveryAgent.CACHED);
    
    }
    
    public void connect() throws IOException{
        String url="";
        ClientSession cs = (ClientSession) Connector.open(bjbid);
        
        RemoteDevice bjb = devs[2];
        System.out.println(bjb.getFriendlyName(true));
        System.out.println(bjb.getBluetoothAddress());
        if (bjb.isTrustedDevice()) {
            System.out.println("is trusted device");
    }
    
}
}
