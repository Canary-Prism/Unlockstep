package canaryprism.unlockstep;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import canaryprism.unlockstep.sequence.CollapsedRhythm;
import canaryprism.unlockstep.sequence.Rhythm;
import canaryprism.unlockstep.sequence.Sound;
import canaryprism.unlockstep.sequence.CollapsedRhythm.*;

public class Main {
    private static AudioPlayer click_on;
    private static AudioPlayer click_off;
    private static final List<CollapsedRhythm> sequence = List.of(
        new Rest(32),
        new On(31),
        new Off(33),
        new On(31),
        new Off(33),
        new On(15),
        new Off(17),
        new On(15),
        new Off(17),
        new On(7),
        new Off(9),
        new On(7),
        new Off(9),
        new On(7),
        new Off(17),
        new On(23),
        new Off(13),
        new On(19),
        new Off(13),
        new On(35),
        new Off(33),
        new On(50)
    );

    private static Clip hai, hu, hoi, ho, n, ha, cowbell;


    public static void main(String[] args) {

        var lockstep = new Lockstep("/unlockstep_assets");

        lockstep.start().join();

        // try {
        //     var lockstep_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/music/lockstep1.wav"));
        //     var lockstep = AudioSystem.getClip();
        //     lockstep.open(lockstep_ais);

        //     var expanded = expandToRhythm(sequence);

        //     click_on = new AudioPlayer(getResource("/unlockstep_assets/audio/sfx/onbeat.wav"), 10);
        //     click_off = new AudioPlayer(getResource("/unlockstep_assets/audio/sfx/offbeat.wav"), 10);

        //     var conductor = new Conductor(60_000, 162 * 2);

        //     conductor.submit((e) -> {
        //         int i = e.beat();
        //         var on = (i & 1) == 0;
        //         if (i >= expanded.size()) {
        //             conductor.stop();
        //             return;
        //         }
        //         if (expanded.get(i) == Rhythm.on) {
        //             if (on) {
        //                 clickOn();
        //             }
        //         } else if (expanded.get(i) == Rhythm.off) {
        //             if (!on) {
        //                 clickOff();
        //             }
        //         }
        //     });

        //     var sound_sequence = expandToSound(sequence);

        //     conductor.submit((e) -> {
        //         int i = e.beat();

        //         if (i >= expanded.size()) {
        //             return;
        //         }

        //         switch (sound_sequence.get(i)) {
        //             case hai -> {
        //                 hai.setFramePosition(0);
        //                 hai.start();
        //             }
        //             case hu -> {
        //                 hu.setFramePosition(0);
        //                 hu.start();
        //             }
        //             case hoi -> {
        //                 hoi.setFramePosition(0);
        //                 hoi.start();
        //             }
        //             case ho -> {
        //                 ho.setFramePosition(0);
        //                 ho.start();
        //             }
        //             case n -> {
        //                 n.setFramePosition(0);
        //                 n.start();
        //             }
        //             case ha -> {
        //                 ha.setFramePosition(0);
        //                 ha.start();
        //             }

        //             case cowbell -> {
        //                 cowbell.setFramePosition(0);
        //                 cowbell.start();
        //             }

        //             case null -> {}
        //         }
        //     });

        //     lockstep.start();
        //     conductor.start(1500 * 60 / 162);

            // var timer = new Timer();
            // timer.scheduleAtFixedRate(new TimerTask() {
            //     boolean on = true;
            //     int i = 0;
            //     @Override
            //     public void run() {
            //         if (i >= expanded.size()) {
            //             timer.cancel();
            //             return;
            //         }
            //         if (expanded.get(i) == Rhythm.on || expanded.get(i) == Rhythm.hai || expanded.get(i) == Rhythm.hu) {
            //             if (on) {
            //                 clickOn();
            //             }
            //         } else if (expanded.get(i) == Rhythm.off || expanded.get(i) == Rhythm.hoi || expanded.get(i) == Rhythm.ho || expanded.get(i) == Rhythm.ha) {
            //             if (!on) {
            //                 clickOff();
            //             }
            //         }
            //         switch (expanded.get(i)) {
            //             case hai -> {
            //                 hai.setFramePosition(0);
            //                 hai.start();
            //             }
            //             case hu -> {
            //                 hu.setFramePosition(0);
            //                 hu.start();
            //             }
            //             case hoi -> {
            //                 hoi.setFramePosition(0);
            //                 hoi.start();
            //             }
            //             case ho -> {
            //                 ho.setFramePosition(0);
            //                 ho.start();
            //             }
            //             case n -> {
            //                 n.setFramePosition(0);
            //                 n.start();
            //             }
            //             case ha -> {
            //                 ha.setFramePosition(0);
            //                 ha.start();
            //             }

            //             default -> {}
            //         }
            //         on = !on;
            //         i++;
            //     }
            // }, 1500 * 60 / 162, 500 * 60 / 162);

            // var t = new Thread(() -> {
            //     try {
            //         var buffer = new byte[1024];
            //         double frame = 0;
            //         int read;

            //         var start = System.currentTimeMillis() - 1;
            //         while ((read = lockstep_ais.read(buffer)) != -1) {
            //             lockstep.write(buffer, 0, read);
            //             frame++;
            //             if (frame >= 44d * 4 * 60 / 162) {
            //                 frame = 0;
            //             }
            //         }
            //     } catch (IOException e) {
            //         // TODO Auto-generated catch block
            //         e.printStackTrace();
            //     }
            // });
            // t.start();
            // t.join();

            // Thread.currentThread().join();
            
        // } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

    private static ExecutorService ex = Executors.newCachedThreadPool();
    private static void clickOn() {
        click_on.play();
    }

    private static void clickOff() {
        click_off.play();
    }

    private static InputStream getResource(String name) {
        return Main.class.getClassLoader().getResourceAsStream(name);
    }

    private static List<Rhythm> expandToRhythm(List<CollapsedRhythm> sequence) {
        var expanded = new ArrayList<Rhythm>();
        for (var rhythm : sequence) {
            if (rhythm instanceof On r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(Rhythm.on);
                }
            } else if (rhythm instanceof Off r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(Rhythm.off);
                }
            } else if (rhythm instanceof Rest r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(Rhythm.rest);
                }
            }
        }
        return expanded;
    }

    private static List<Sound> expandToSound(List<CollapsedRhythm> sequence) {
        var expanded = new ArrayList<Sound>();
        for (int k = 0; k < sequence.size(); k++) {
            var rhythm = sequence.get(k);
            if (rhythm instanceof On r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof Off) {
                    var size = expanded.size();
                    expanded.set(size - 1, Sound.ha);
                    expanded.set(size - 2, Sound.n);
                    expanded.set(size - 3, Sound.ha);
                    expanded.set(size - 4, Sound.n);
                } else if (expanded.size() > 0 && sequence.get(k - 1) instanceof Rest) {
                    var size = expanded.size();
                    expanded.set(size - 2, Sound.cowbell);
                    expanded.set(size - 4, Sound.cowbell);
                    expanded.set(size - 6, Sound.cowbell);
                    expanded.set(size - 8, Sound.cowbell);
                }
                expanded.add(Sound.hai);
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Off r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof On) {
                    var size = expanded.size();
                    expanded.set(size - 1, Sound.hu);
                    expanded.set(size - 3, Sound.hai);
                    expanded.set(size - 5, Sound.hai);
                    expanded.set(size - 7, Sound.hai);
                }
                expanded.add(Sound.hoi);
                var size = expanded.size();
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
                for (int i = 1; i <= 7; i += 2) {
                    expanded.set(size + i, Sound.ho);
                }
            } else if (rhythm instanceof Rest r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(null);
                }
            }
        }
        return expanded;
    }

    // static {
    //     try {
    //         var hai_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/sfx/hai.wav"));
    //         hai = AudioSystem.getClip();
    //         hai.open(hai_ais);

    //         var hu_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/sfx/hu.wav"));
    //         hu = AudioSystem.getClip();
    //         hu.open(hu_ais);

    //         var hoi_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/sfx/hoi.wav"));
    //         hoi = AudioSystem.getClip();
    //         hoi.open(hoi_ais);

    //         var ho_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/sfx/ho.wav"));
    //         ho = AudioSystem.getClip();
    //         ho.open(ho_ais);

    //         var n_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/sfx/n.wav"));
    //         n = AudioSystem.getClip();
    //         n.open(n_ais);

    //         var ha_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/sfx/ha.wav"));
    //         ha = AudioSystem.getClip();
    //         ha.open(ha_ais);

    //         var cowbell_ais = AudioSystem.getAudioInputStream(getResource("/unlockstep_assets/audio/sfx/cowbell.wav"));
    //         cowbell = AudioSystem.getClip();
    //         cowbell.open(cowbell_ais);
    //     } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }
}
