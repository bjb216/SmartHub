package smarthub;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
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

    RemoteDevice[] devs;
    public static final Vector/*<RemoteDevice>*/ devicesDiscovered = new Vector();
    public static final String bjbid = "703EAC1A1042";
    public static final String mdbid = "10D542EFEC45";

    //For services discovered. from Bluecove java docs
    static final UUID OBEX_FILE_TRANSFER = new UUID(0x1106);
    public static final Vector/*<String>*/ serviceFound = new Vector();

    public BTConnection() {

    }

    public void discoverDevices() throws BluetoothStateException, InterruptedException {
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

    public void discoverServices() throws IOException, InterruptedException {
        // First run RemoteDeviceDiscovery and use discoved device
        //RemoteDeviceDiscovery.main(null);

        serviceFound.clear();

        UUID[] serviceUUID = new UUID[1];
        
        //0x1105 works for Matt's phone!!!!!!
        serviceUUID[0] = new UUID(0x1115);

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
                    DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                    if (serviceName != null) {
                        System.out.println("service " + serviceName.getValue() + " found " + url);
                    } else {
                        System.out.println("service found " + url);
                    }
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("service search completed!");
                synchronized (serviceSearchCompletedEvent) {
                    serviceSearchCompletedEvent.notifyAll();
                }
            }

        };

        UUID[] searchUuidSet = new UUID[]{serviceUUID[0]};
        int[] attrIDs = new int[]{
            0x0100 // Service name
        };

        for (Enumeration en = devicesDiscovered.elements(); en.hasMoreElements();) {
            RemoteDevice btDevice = (RemoteDevice) en.nextElement();

            synchronized (serviceSearchCompletedEvent) {
                System.out.println("search services on " + btDevice.getBluetoothAddress() + " " + btDevice.getFriendlyName(false));
                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(null, searchUuidSet, btDevice, listener);
                serviceSearchCompletedEvent.wait();
            }
        }

    }

    public void connect() throws IOException {
//        System.out.println("printing addresses of identified devices");
//        for (int i = 0; i < serviceFound.size(); i++) {
//            System.out.println("Address " + i + ": " + serviceFound.elementAt(i));
//        }

//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            //Handle exception
//        }

        ClientSession clientSession = (ClientSession) Connector.open((String) serviceFound.elementAt(1));
        HeaderSet hsConnectReply = clientSession.connect(null);
        if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
            System.out.println("Failed to connect");
        } else {
            System.out.println("connected");
        }

    }
}
