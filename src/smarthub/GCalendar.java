package smarthub;

import static com.amazonaws.services.waf.model.MatchFieldType.URI;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.URI;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class GCalendar {

    public ArrayList<Event> meetings;
    private final com.google.api.services.calendar.Calendar calendar;
    HttpTransport httpTransport;
    JacksonFactory jsonFactory;
    private final User user;
    
    //credentials for original method
    private final String clientID="1060921422022-3dvj5las14a263cgv6coaasaieaih85v.apps.googleusercontent.com";
    private final String clientSecret = "t8xxaUvbBR-yN6w4gWdmPeJL";
    
    //credentials for potential fwding
    //private final String clientID = "1060921422022-rlk5jlp0cqj2jvvjf5kdj5i1jam83ta6.apps.googleusercontent.com";
    //private final String clientSecret = "yPUOts6QVP_ZulvbMGnmHiV4";

    public GCalendar(User user) throws IOException, URISyntaxException {
        this.user = user;
        meetings = new ArrayList<>();
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
        if (user.credential == null) {
            System.out.println("Authenticating user: " + user.name);
            createCredential(user);
        } else {
            System.out.println(user.name + " already is authenticated");
        }
        System.out.println("Attempting to create calendar");
        calendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, jsonFactory, user.credential)
                .setApplicationName("SmartHub")
                .build();

        buildEvents();
    }

    //To to handle errors, check for bad input
    private void createCredential(User user) throws IOException, URISyntaxException {
        //ServerSocket serverSocket = new ServerSocket(4444);
        //Socket socket = serverSocket.accept();
        //System.out.println("past server accept");

        String redirectUri = "urn:ietf:wg:oauth:2.0:oob";
        //String redirectUri = "http://localhost:8080";
        //String redirectUri = "http://localhost:8080/oath2callback";

        String scope = "https://www.googleapis.com/auth/calendar";

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientID, clientSecret,
                Arrays.asList(scope)).setAccessType("online")
                .setApprovalPrompt("auto").build();

        //String url = flow.newAuthorizationUrl().setState("/linkaccount").setRedirectUri(redirectUrl).build();
        String url = flow.newAuthorizationUrl().setRedirectUri(redirectUri).build();
        Desktop.getDesktop().browse(new URI(url));
        System.out.println("Please open the following URL in your browser then type the authorization code:");

        System.out.println("  " + url);
        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //System.out.println("code is: "+in.readLine());
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        //Catch bad response
        GoogleTokenResponse response = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri).execute();

        GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
        user.setCredential(credential);
    }

    private double roundUp(long number, long multiple) {
        return Math.ceil((number + multiple / 2) / multiple) * multiple;
    }

    private void buildEvents() throws IOException {
        //System.out.println("Printing " + user.name + " Calendar");

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        long todayNum = now.getValue();
        long dayNum = (1000 * 60 * 60 * 24);
        long tomorrowNum = (long) (roundUp(todayNum, dayNum) + 18000000);
        DateTime tomorrow = new DateTime(tomorrowNum);
        //System.out.println("dayNum: " + dayNum);
        //System.out.println("tomorrowNum: " + tomorrowNum);
        //System.out.println("Get Value now: " +now.getValue());
        //System.out.println("Get Value tomorrow: " + tomorrow.getValue());

        String pageToken = null;
        do {
            com.google.api.services.calendar.model.Events events = calendar.events().list("primary")
                    .setPageToken(pageToken)
                    .setTimeMin(now)
                    .setTimeMax(tomorrow)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            meetings.addAll(events.getItems());
            printCalendar();
            pageToken = events.getNextPageToken();
        } while (pageToken != null);

    }

    public void printCalendar() {
        for (int i = 0; i < meetings.size(); i++) {
            System.out.println(meetings.get(i).getSummary());
        }
    }

    public String getTime(Event evt, boolean cond) {
        System.out.println("in gettime");
        Date start = new Date(evt.getStart().getDateTime().getValue());
        Date end = new Date(evt.getEnd().getDateTime().getValue());

        String startTime = new SimpleDateFormat("hh:mm a", Locale.US).format(start);
        String endTime = new SimpleDateFormat("hh:mm a", Locale.US).format(end);

        System.out.println(startTime + " - " + endTime);

        if(cond)
            return startTime + " - " + endTime;
        else
            return startTime;
    }

}
