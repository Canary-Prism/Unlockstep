package canaryprism.unlockstep;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    private final Clip[] clips;
    private volatile int rotation = 0;
    public AudioPlayer(InputStream data, int concurrents) {
        var baos = new ByteArrayOutputStream();
        this.clips = new Clip[concurrents];
        try {
            data.transferTo(baos);
            for (int i = 0; i < concurrents; i++) {
                var clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(baos.toByteArray())));
                clips[i] = clip;
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    public void setVolume(float volume) {
        for (var clip : clips) {
            var gain = (javax.sound.sampled.FloatControl) clip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
            gain.setValue(20f * (float) Math.log10(volume));
        }
    }

    public void play() {
        var clip = clips[rotation];
        if (clip.isActive()) {
            System.err.println("Warning: player rotation saturated");
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
        rotation++;
        if (rotation == clips.length)
            rotation = 0;
    }

    public void stop() {
        for (var clip : clips) {
            clip.stop();
        }
    }
}
