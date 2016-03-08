package smarthub;

import com.google.api.services.calendar.model.Event;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import javax.swing.*;

public class Controller {

    BTConnection con;
    JFrame frame;

    ArrayList<User> users;
    String[] menuItems = {"Pair users", "Current paired users", "Scan area", "Weather", "Calendar",
        "Financial", "Speech", "exit"};
    Scanner scan;
    int height = 600;
    int width = 600;

    public Controller() throws InterruptedException, IOException {
        con = new BTConnection();
        scan = new Scanner(System.in);
        users = new ArrayList<>();

        init();
        frame = new JFrame();
        frame.setSize(height, width);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //JFrame 
        //For testing purposes
    }

    private void init() throws InterruptedException, IOException {
        User user = con.pair();
        if (user != null) {
            users.add(user);
        }
        //users.add(new User("Brandon's Phone"));
    }

    public void run() throws IOException {

//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout());
//        JLabel label = new JLabel("This is a label!");
//
//        JButton button = new JButton();
//        button.setText("Press me");
//        panel.add(label);
//        panel.add(button);
//        frame.add(panel);
        //boolean to check whether or not we need to change the frame
        boolean change = false;

        //wont work if higher order user enters the room
        int i = 0;
        JPanel userPanel = null;
        while (true) {
            //i = (i + 1) % 6;
            User current = scan();
            if (current == null) {
                System.out.println("removing");
                frame.getContentPane().remove(userPanel);
                frame.revalidate();
                frame.repaint();
                change = false;
            } else if (!change) {
                System.out.println("creating jpanel");
                userPanel = userPanel(current, height, width);
                frame.getContentPane().add(userPanel);
                frame.revalidate();
                frame.repaint();
                change = true;
            }
            else{
                System.out.println("still in range");
            }
            delay(5);
        }
    }

    private JPanel userPanel(User user, int height, int width) throws IOException {
        JPanel panel = new JPanel();
        //panel.setLayout(new FlowLayout());
        JLabel title = new JLabel("SmartHub");
        JLabel name = new JLabel(user.name);
        panel.add(title);
        panel.add(name);

        //JPanel weatherPanel = weatherPanel(user,height/2,width);
        JPanel calendarPanel = calendarPanel(user, height / 2, width);
        //panel.add(weatherPanel);
        panel.add(calendarPanel);

        return panel;

    }

    private JPanel weatherPanel(User user, int length, int width) {
        JPanel panel = new JPanel();

        return null;
    }

    private JPanel calendarPanel(User user, int height, int width) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        GCalendar cal = new GCalendar(user);
        ArrayList<Event> meetings = cal.meetings;

        for (int i = 0; i < meetings.size(); i++) {
            System.out.println("adding meeting: " + meetings.get(i).getSummary());
            JLabel label = new JLabel(meetings.get(i).getSummary());
            //label.setLocation(i*height/meetings.size()+10, 10);
            panel.add(label);
        }

        return panel;
    }

    private JPanel stockPanel() {
        return null;
    }

    public void run2() throws InterruptedException, IOException, JSONException {

        while (true) {
            System.out.println("Welcome to SmartHub");
            for (int i = 0; i < menuItems.length; i++) {
                System.out.println((i + 1) + ": " + menuItems[i]);
            }
            int choice = scan.nextInt();
            switch (choice) {
                case 1:
                    User user = con.pair();
                    if (user != null) {
                        users.add(user);
                    }
                    break;
                case 2:
                    printCurrentUsers();
                    break;
                case 3:
                    scan();
                    break;
                case 4:
                    weather();
                    break;
                case 5:
                    GCalendar cal = new GCalendar(users.get(0));
                    cal.printCalendar();
                    break;
                case 6:
                    financial();
                    break;
                case 7:
                    //TextToSpeech tts=new TextToSpeech("We are going to graduate");
                    //tts.speak();
                    break;
                case 8:
                    System.out.println("goodbye");
                    System.exit(0);
                    break;
            }
        }

    }

    private void financial() throws IOException {
        FinancialData data = new FinancialData(users.get(0));
        data.printInfo();

    }

    private void weather() throws IOException, JSONException {
        System.out.println("testing weather function");
        MyWeather w = new MyWeather("New York");
        w.doSomething();
        w.getWeekly();
    }

    private User scan() {
//        return users.get(0);
        for (int i = 0; i < users.size(); i++) {
            if (con.connect(users.get(i))) {
                return users.get(i);
            }
        }
        return null;
    }

    private void delay(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            //Handle exception
        }
    }

    private void printCurrentUsers() {
        if (users.isEmpty()) {
            System.out.println("No current users paired");
        }
        System.out.println("Current paired users");
        for (int i = 0; i < users.size(); i++) {
            System.out.println("Device: " + users.get(i).device);
            System.out.println("Address: " + users.get(i).address);
            System.out.println("Name: " + users.get(i).name);
            System.out.println("\n");
        }

    }

}
