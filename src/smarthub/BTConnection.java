package smarthub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.*;

public class BTConnection {

//    RemoteDevice[] devs;
    public ArrayList<RemoteDevice> devices;
    public ArrayList<String> urls;
    public ArrayList<RemoteDevice> devicesDiscovered;
    private Scanner scan;
    public static final Vector/*<String>*/ serviceFound = new Vector();

    public BTConnection() {
        scan = new Scanner(System.in);
        devices = new ArrayList<>();
        urls = new ArrayList<>();
        devicesDiscovered = new ArrayList<>();

    }

    //Catch these errors
    public User pair() throws BluetoothStateException, InterruptedException, IOException {
        discoverDevices();
        if (devicesDiscovered == null) {
            System.out.println("no devices in range");
            return null;
        }

        for (int i = 0; i < devicesDiscovered.size(); i++) {
            System.out.println("Would you like to pair the following device(yes or no)");
            try {
                System.out.println(devicesDiscovered.get(i).getFriendlyName(true));
            } catch (IOException ex) {
                //
            }
            if (scan.next().equals("yes")) {
                if (devices.contains(devicesDiscovered.get(i))) {
                    System.out.println("Already paired");
                } else {
                    discoverServices(devicesDiscovered.get(i));
                    return new User(devicesDiscovered.get(i), urls.get(0),devicesDiscovered.get(i).getFriendlyName(true));
                }
            } else {
                System.out.println("Item not added");
            }

        }
        return null;
    }

    private void discoverDevices() throws BluetoothStateException, InterruptedException {
        final Object inquiryCompletedEvent = new Object();
        //devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                //System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                //devicesDiscovered.addElement(btDevice);
                devicesDiscovered.add(btDevice);
                //devices.add(btDevice);
//                try {
//                    System.out.println("     name " + btDevice.getFriendlyName(false));
//                } catch (IOException cantGetDeviceName) {
//                }
            }

            public void inquiryCompleted(int discType) {
                //System.out.println("Device Inquiry completed!");
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
                //System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                //System.out.println(devicesDiscovered.size() + " device(s) found");
            }
        }

        //Returns an array of all remote devices that have been identified in a previous scan
        //LocalDevice ld = LocalDevice.getLocalDevice();
        //DiscoveryAgent agent = ld.getDiscoveryAgent();
        //devices = agent.retrieveDevices(DiscoveryAgent.CACHED);
    }

    private void discoverServices(RemoteDevice device) throws IOException, InterruptedException {
        // First run RemoteDeviceDiscovery and use discoved device
        //RemoteDeviceDiscovery.main(null);
        urls.clear();
        serviceFound.clear();

        UUID[] serviceUUID = new UUID[1];

        //0x1105 works for Matt's phone!!!!!!
        serviceUUID[0] = new UUID(0x1105);

        final Object serviceSearchCompletedEvent = new Object();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            }

            public void inquiryCompleted(int discType) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                for (int i = 0; i < servRecord.length; i++) {
                    String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (url == null) {
                        continue;
                    }
                    serviceFound.add(url);
                    urls.add(url);
                    DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                    if (serviceName != null) {
                        //System.out.println("service " + serviceName.getValue() + " found " + url);
                    } else {
                        //System.out.println("service found " + url);
                    }
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
                //System.out.println("service search completed!");
                synchronized (serviceSearchCompletedEvent) {
                    serviceSearchCompletedEvent.notifyAll();
                }
            }

        };

        UUID[] searchUuidSet = new UUID[]{serviceUUID[0]};
        int[] attrIDs = new int[]{
            0x0100 // Service name
        };

        synchronized (serviceSearchCompletedEvent) {
            //System.out.println("search services on " + device.getBluetoothAddress() + " " + device.getFriendlyName(false));
            LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(null, searchUuidSet, device, listener);
            serviceSearchCompletedEvent.wait();
        }

    }

    public boolean connect(User user) {
        ClientSession clientSession;
        HeaderSet hsConnectReply;
        try {
            clientSession = (ClientSession) Connector.open(user.address);
            hsConnectReply = clientSession.connect(null);
            
            if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
                return false;
            } else {
                clientSession.close();
                return true;
            }
        } catch (IOException ex) {
            return false;
        }

    }
}
