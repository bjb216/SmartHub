
package smarthub;

import java.io.*;
import com.sun.speech.freetts.*;


public class TextToSpeech {
    String message;
    private static final String VOICENAME = "kevin16";
    Voice voice;
    VoiceManager vm;
    
    public TextToSpeech(String msg){
        message=msg;
        vm=VoiceManager.getInstance();
        voice=vm.getVoice(VOICENAME);
        voice.allocate();
        
    }
    
    public void speak(){
        voice.speak(message);
    }
    
    
}
