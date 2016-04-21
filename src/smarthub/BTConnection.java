package smarthub;

import com.intel.bluetooth.btgoep.Connection;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BTConnection {

    public ArrayList<RemoteDevice> devices;
    public ArrayList<String> urls;
    public ArrayList<RemoteDevice> devicesDiscovered;
    private final Scanner scan;
    public static final Vector/*<String>*/ serviceFound = new Vector();

    public BTConnection() {
        scan = new Scanner(System.in);
        devices = new ArrayList<>();
        urls = new ArrayList<>();
        devicesDiscovered = new ArrayList<>();

    }

    //Catch these errors
    public User pair(JPanel user, JFrame frame) {
        try {
            System.out.println("pair called");
            discoverDevices();
            System.out.println("returned from discoverDevices");
            if (devicesDiscovered == null || devicesDiscovered.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No Devices in range");
                return null;
            }

            for (int i = 0; i < devicesDiscovered.size(); i++) {
                System.out.println("in for loop");
                String message = "Would you like to pair device: " + devicesDiscovered.get(i).getFriendlyName(true);
                int reply = JOptionPane.showConfirmDialog(frame, message, null, JOptionPane.YES_NO_OPTION);

                if (reply == JOptionPane.YES_OPTION) {
                    if (devices.contains(devicesDiscovered.get(i))) {
                        JOptionPane.showMessageDialog(frame, "Device already Paired");
                    } else {

                            discoverServices(devicesDiscovered.get(i));
                            System.out.println("adding new user");
                            if (urls.isEmpty()) {
                                System.out.println("No urls found");
                                JOptionPane.showMessageDialog(frame, "Error: Device not added");
                                return null;
                            }
                            System.out.println("url found");
                            devices.add(devicesDiscovered.get(i));
                            JOptionPane.showMessageDialog(frame, "Device added");
                            return new User(devicesDiscovered.get(i), urls.get(0), devicesDiscovered.get(i).getFriendlyName(true));
                        } 
                    
                } else {
                    JOptionPane.showMessageDialog(frame, "Device not added");
                    //return null;
                }

            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: Device not added");
            System.out.println("error mesage: "+ex.getMessage());
            return null;
        }
        return null;
    }

    private void discoverDevices() throws BluetoothStateException, InterruptedException {
        final Object inquiryCompletedEvent = new Object();
        devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
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
                System.out.println(devicesDiscovered.size() + " device(s) found");
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
        //OBEX Object Push Profile
        //serviceUUID[0] = new UUID(0x1105);
        
        //Personal area network user
        //serviceUUID[0] = new UUID(0x1115);
        
        //Handsfree profile
        serviceUUID[0] = new UUID(0x111E);

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
                        System.out.println("service " + serviceName.getValue() + " found " + url);
                    } else {
                        System.out.println("service found " + url);
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
            System.out.println("search services on " + device.getBluetoothAddress() + " " + device.getFriendlyName(false));
            LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(null, searchUuidSet, device, listener);
            serviceSearchCompletedEvent.wait();
        }

    }

    
        public boolean connect(User user) {
        try {
            System.out.println("connecting to user: "+user.name);
            Connector.open(user.address);
            return true;

            //Catching all exceptions. used to be an IO exception 
        } catch (Exception ex) {
            if(ex.getMessage().contains("Host is down")){
                System.out.println("device not found");
                return false;
            }
            else{
                System.out.println(ex.getMessage()+":::but returning true");
                return true;
            }
        }

    }

    
    
    public boolean connect2(User user) {
        ClientSession clientSession;
        HeaderSet hsConnectReply;
        try {
            System.out.println("connecting to user: "+user.name);
            clientSession = (ClientSession) Connector.open(user.address);
            hsConnectReply = clientSession.connect(null);
            clientSession.close();
            return true;
            

//            //old
//            //hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK
//            
//            if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
//                return false;
//            } else {
//                clientSession.close();
//                return true;
//            }
            //Catching all exceptions. used to be an IO exception 
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }
}
