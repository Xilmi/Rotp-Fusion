package rotp;

import rotp.util.sound.OggClip;
import rotp.util.sound.WavClip;

import javax.sound.sampled.*;

/**
 * Executable file to test sound playback
 */
public class PlaySoundClip {

    public static void main(String arg[]) throws Exception {
        for (AudioFileFormat.Type t: AudioSystem.getAudioFileTypes()) {
            System.out.println("Supported file type "+t.getExtension());
        }
        for (Mixer.Info mi: AudioSystem.getMixerInfo()) {
            System.out.println("Mixer "+mi.getName()+" "+mi.getVendor()+" "+mi.getVersion()+" "+mi.getDescription());
            for (Line.Info s: AudioSystem.getMixer(mi).getSourceLineInfo()) {
                System.out.println("\tSource Line "+s.getLineClass()+" "+s.toString());
            }
            for (Line.Info t: AudioSystem.getMixer(mi).getTargetLineInfo()) {
                System.out.println("\tTarget Line "+t.getLineClass()+" "+t.toString());
            }
        }

        if (arg.length == 1 && "wav".equalsIgnoreCase(arg[0])) {
            System.out.println("Playing WAV clip");
            WavClip.play("data/sounds/combat_open.wav", 100, 100);
        } else {
            System.out.println("Playing OGG clip");
            OggClip.play("data/sounds/combat_open.ogg", 100, 100);
        }
        Thread.sleep(3000);
    }

}