package smarthub;

import com.amazonaws.AmazonClientException;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.CreateSpeechRequest;
import com.ivona.services.tts.model.CreateSpeechResult;
import com.ivona.services.tts.model.Input;
import com.ivona.services.tts.model.Voice;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class TTS {

    private static String secretKey = "2IqTnmUkdlama7WkTUdYODCuYuIKqxb2BmcQPdcI";
    private static String accessKey = "GDNAJA5USEBJNWSMGXFA";

    private static IvonaSpeechCloudClient speechCloud;

    public TTS() {
        speechCloud = new IvonaSpeechCloudClient(new IvonaCredentials(secretKey, accessKey));
        speechCloud.setEndpoint("https://tts.eu-west-1.ivonacloud.com");
    }
    
    public void speak(String textIn)  {

        String outputFileName = "/users/bartonb/speech.mp3";
        //String outputFileName = "/home/pi/music/sound.mp3";
        CreateSpeechRequest createSpeechRequest = new CreateSpeechRequest();
        Input input = new Input();
        Voice voice = new Voice();

        voice.setName("Emma");
        //voice.setName("Celine");
        //input.setData("Good morning Brandon");
        input.setData(textIn);
        createSpeechRequest.setInput(input);
        createSpeechRequest.setVoice(voice);
        InputStream in = null;
        FileOutputStream outputStream = null;

        try {

            CreateSpeechResult createSpeechResult = speechCloud.createSpeech(createSpeechRequest);

            in = createSpeechResult.getBody();
            outputStream = new FileOutputStream(new File(outputFileName));

            byte[] buffer = new byte[2 * 1024];
            int readBytes;

            while ((readBytes = in.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readBytes);
            }

            FileInputStream fis = new FileInputStream(outputFileName);
            Player playMP3 = new Player(fis);

            playMP3.play();

        }
        catch(AmazonClientException | IOException | JavaLayerException e){
            
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(TTS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(TTS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
