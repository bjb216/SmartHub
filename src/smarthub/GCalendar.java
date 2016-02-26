package smarthub;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class GCalendar {

    private ArrayList<String> meetings;
    private String username;
    private String password;
    private final String clientID = "239534582570-3rvd6phvksoe8mm028jm1u3h2eu1c3oo.apps.googleusercontent.com";
    private final String clientSecret = "6BvMtDTZm2AY0q370frrGpHa";

    public GCalendar(String username, String password, String name) {
        this.username = username;
        this.password = password;
    }

    public GCalendar() {

    }

    public void doStuff() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
        String scope = "https://www.googleapis.com/auth/calendar";

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientID, clientSecret,
                Arrays.asList(scope)).setAccessType("online")
                .setApprovalPrompt("auto").build();
        
        String url = flow.newAuthorizationUrl().setRedirectUri(redirectUrl).build();
        System.out.println("Please open the following URL in your browser then type the authorization code:");

        System.out.println("  " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        GoogleTokenResponse response = flow.newTokenRequest(code)
                .setRedirectUri(redirectUrl).execute();
        
        GoogleCredential credential = new GoogleCredential()
                .setFromTokenResponse(response);

// Create a new authorized API client
        //Calendar service = new Calendar.Builder(httpTransport, jsonFactory,credential).build();

    }

    public ArrayList<String> getMeetings() {
        return meetings;
    }

}
