package resources;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundResources{
    public enum Sound{
        WARNING("warning_beep.wav"),
        SECOND_WARNING("second_warning_beep.wav");

        private String resourceName;

        Sound(String resourceName){
            this.resourceName = resourceName;
        }

        @Override
        public String toString(){
            return resourceName;
        }
    }

    private Clip clip;

    public SoundResources(Sound sound){
        try {
            URL url = SoundResources.class.getResource(sound.toString());
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            this.clip = AudioSystem.getClip();
            this.clip.open(audioIn);
            this.clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

