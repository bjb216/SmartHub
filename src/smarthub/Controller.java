package smarthub;

import com.google.api.services.calendar.model.Event;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import javax.swing.*;

/**
 * *GENERAL NOTES*** Box layout causes size of panel to be that of contents.
 * flow layout does not.
 *
 *
 *
 *
 */
public class Controller {

    BTConnection con;
    JFrame frame;
    JPanel body;
    JPanel user;

    ArrayList<User> users;
    String[] menuItems = {"Pair users", "Current paired users", "Scan area", "Weather", "Calendar",
        "Financial", "Speech", "exit"};
    Scanner scan;
    final int height = 500;
    final int width = 1200;

    public Controller() throws InterruptedException, IOException {
        TTS test = new TTS();
        test.doSomething();
        con = new BTConnection();
        scan = new Scanner(System.in);
        users = new ArrayList<>();

        init();
        frame = new JFrame();

        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout((new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(topPanel(width, height / 8));

        body = bodyPanel(width, height * 7 / 8);

        frame.getContentPane().add(body);

        user = bodyPanel(width * 7 / 8, height * 7 / 8);
        body.add(user);
        body.add(sidePanel(width / 8, height * 7 / 8));

        //frame.setBackground(Color.blue);
        //frame.pack();
        frame.setVisible(true);

    }

    private void init() throws InterruptedException, IOException {
//        User user = con.pair();
//        if (user != null) {
//            users.add(user);
//        }
        users.add(new User("Brandon"));
        users.add(new User("Matt"));
        users.add(new User("Jane"));
        users.add(new User("John"));
        users.add(new User("Jenna"));
    }

    public void run() throws IOException {

        boolean change = false;

        //wont work if higher order user enters the room
        //check if user is the same as from the previous scan
        int i = 0;
        JPanel userPanel = null;
        while (true) {
            i = (i + 1) % 6;
            //User current = scan();
            User current = scanTest(i);
            if (current == null) {
                System.out.println("removing");
                user.remove(userPanel);
                user.revalidate();
                user.repaint();
                change = false;
            } else if (!change) {
                System.out.println("creating jpanel");
                userPanel = userPanel(current, width * 7 / 8, height * 7 / 8);
                user.add(userPanel);
                user.revalidate();
                user.repaint();
                change = true;
            } else {
                System.out.println("still in range");
            }
            delay(2);
        }
    }

    private User scanTest(int i) {
        if (i < 4) {
            return users.get(0);
        } else {
            return null;
        }
    }

    private JPanel userPanel(User user, int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(width, height));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //JPanel weatherPanel = weatherPanel(user, width, height/2);
        //panel.add(weatherPanel);
        JPanel calendarPanel = calendarPanel(user, width, height / 2);
        panel.add(calendarPanel);
        JPanel stockPanel = stockPanel(user, width, height / 2);
        panel.add(stockPanel);

        return panel;

    }

    private JPanel topPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Dark blue
        panel.setBackground(new Color(33, 83, 129));

        JLabel title = new JLabel("SmartHub");
        title.setPreferredSize(new Dimension(width / 3, height));

        JLabel date = new JLabel("xx/xx/xxxx");
        date.setPreferredSize(new Dimension(width / 2, height));

        panel.add(title);
        panel.add(date);

        return panel;
    }

    private JPanel weatherPanel(User user, int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.MAGENTA);

        return panel;
    }

    //Yellow
    private JPanel bodyPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(width, height));

        //Light gray
        panel.setBackground(new Color(217, 217, 217));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel sidePanel(int width, int height) {
        JPanel panel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));

        //Slightly lighter blue than top
        panel.setBackground(new Color(46, 117, 182));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("initial");
        panel.add(label);

        for (int i = 0; i < users.size(); i++) {
            JButton button = new JButton(users.get(i).name);
            panel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    label.setText(button.getText() + " pressed");
                }
            });
        }
        JButton button = new JButton("Pair");
        panel.add(button);

        return panel;
    }

    //Orange
    private JPanel calendarPanel(User user, int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.ORANGE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GCalendar cal = new GCalendar(user);
        ArrayList<Event> meetings = cal.meetings;

        JLabel label = new JLabel("Time");
        label.setPreferredSize(new Dimension(width /4, height/10));
        panel.add(label);

        label = new JLabel("Title");
        label.setPreferredSize(new Dimension(width / 4, height/10));
        panel.add(label);
        
        label = new JLabel("Location");
        label.setPreferredSize(new Dimension(width / 4, height/10));
        panel.add(label);
        
        for (int i = 0; i < meetings.size(); i++) {
            //JLabel label = new JLabel(cal.getTime(meetings.get(i))+meetings.get(i).getSummary()+" "+meetings.get(i).getLocation());
            label = new JLabel(cal.getTime(meetings.get(i)));
            label.setPreferredSize(new Dimension(width / 4, height/10));
            panel.add(label);
            
            label = new JLabel(meetings.get(i).getSummary());
            label.setPreferredSize(new Dimension(width / 4, height/10));
            panel.add(label);
            
            label = new JLabel(meetings.get(i).getLocation());
            label.setPreferredSize(new Dimension(width / 4, height/10));
            panel.add(label);
        }

        return panel;
    }

    private JPanel stockPanel(User user, int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));

        //slightly darker gray
        panel.setBackground(new Color(148, 148, 148));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < user.stocks.length; i++) {
            JLabel label = new JLabel(user.stocks[i]);
            panel.add(label);
        }

        return panel;
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
        //w.getWeekly();
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
