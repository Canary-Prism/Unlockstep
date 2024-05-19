package canaryprism.unlockstep;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

import canaryprism.unlockstep.scoring.PlayerInputHandler;
import canaryprism.unlockstep.sequence.CollapsedRhythm;
import canaryprism.unlockstep.sequence.PlayerSound;
import canaryprism.unlockstep.sequence.CollapsedRhythm.*;
import canaryprism.unlockstep.sequence.Rhythm;
import canaryprism.unlockstep.sequence.Sound;
import canaryprism.unlockstep.swing.Animation;
import canaryprism.unlockstep.swing.CollapsedZoomSize;
import canaryprism.unlockstep.swing.LockstepView;
import canaryprism.unlockstep.swing.ZoomSize;

public class Lockstep {

    protected static final long barely_range = 135;
    protected static final long hit_range = 70;

    protected static final Color onbeat_color = new Color(0xf84898), offbeat_color = new Color(0xe858a0);

    protected static final List<CollapsedRhythm> beat_sequence = List.of(
        new Tap(1),
        new Rest(3),
        new Tap(1),
        new Rest(3),
        new Tap(1),
        new Rest(3),
        new Tap(1),
        new Rest(3),
        new Tap(1),
        new Rest(3),
        new Tap(1),
        new Rest(3),
        new Tap(1),
        new Rest(1),
        new Tap(1),
        new Rest(1),
        new Tap(1),
        new Rest(1),
        new Tap(1),
        new Rest(1),
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

    protected static final List<CollapsedZoomSize> zoom_sequence = List.of(
        new CollapsedZoomSize(ZoomSize.l0, 127),
        new CollapsedZoomSize(ZoomSize.l1, 33),
        new CollapsedZoomSize(ZoomSize.l0, 15),
        new CollapsedZoomSize(ZoomSize.l1, 17),
        new CollapsedZoomSize(ZoomSize.l2, 15),
        new CollapsedZoomSize(ZoomSize.l1, 17),
        new CollapsedZoomSize(ZoomSize.l0, 7),
        new CollapsedZoomSize(ZoomSize.l1, 9),
        new CollapsedZoomSize(ZoomSize.l2, 7),
        new CollapsedZoomSize(ZoomSize.l3, 9),
        new CollapsedZoomSize(ZoomSize.l4, 7),
        new CollapsedZoomSize(ZoomSize.l3, 17),
        new CollapsedZoomSize(ZoomSize.l2, 1),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 1),
        new CollapsedZoomSize(ZoomSize.l2, 1),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 1),
        new CollapsedZoomSize(ZoomSize.l3, 15),
        new CollapsedZoomSize(ZoomSize.l4, 13),
        new CollapsedZoomSize(ZoomSize.l0, 4),
        new CollapsedZoomSize(ZoomSize.l3, 15),
        new CollapsedZoomSize(ZoomSize.l4, 13),
        new CollapsedZoomSize(ZoomSize.l0, 4),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l4, 10),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l4, 9),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l4, 10),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l4, 9 + 51)
    );

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
            } else if (rhythm instanceof Tap r) {
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
            } else if (rhythm instanceof Tap r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(null);
                }
            }
        }
        return expanded;
    }

    private static List<Animation> expandToAnimations(List<CollapsedRhythm> sequence) {
        var expanded = new ArrayList<Animation>();
        boolean onbeat = true;
        for (int k = 0; k < sequence.size(); k++) {
            var rhythm = sequence.get(k);

            if (rhythm instanceof On r) {
                for (int i = 0; i < r.beats(); i++) {
                    if (onbeat)
                        expanded.add(Animation.hitleft);
                    else
                        expanded.add(null);
                    onbeat = !onbeat;
                }
            } else if (rhythm instanceof Off r) {
                for (int i = 0; i < r.beats(); i++) {
                    if (!onbeat)
                        expanded.add(Animation.hitright);
                    else
                        expanded.add(null);
                    onbeat = !onbeat;
                }
            } else if (rhythm instanceof Rest r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(null);
                    onbeat = !onbeat;
                }
            } else if (rhythm instanceof Tap r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(Animation.tap);
                    onbeat = !onbeat;
                }
            }
        }

        return expanded;
    }

    private static List<Color> expandToColor(List<CollapsedRhythm> sequence) {
        var expanded = new ArrayList<Color>();
        for (int k = 0; k < sequence.size(); k++) {
            var rhythm = sequence.get(k);
            if (rhythm instanceof On r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof Off) {
                    var size = expanded.size();
                    expanded.set(size - 1, offbeat_color);
                    expanded.set(size - 2, onbeat_color);
                    expanded.set(size - 3, offbeat_color);
                    expanded.set(size - 4, onbeat_color);
                }
                expanded.add(onbeat_color);
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Off r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof On) {
                    var size = expanded.size();
                    expanded.set(size - 1, onbeat_color);
                    expanded.set(size - 3, offbeat_color);
                    expanded.set(size - 5, onbeat_color);
                    expanded.set(size - 7, offbeat_color);
                }
                expanded.add(offbeat_color);
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Rest r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Tap r) {
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(null);
                }
            }
        }
        return expanded;
    }

    private static List<ZoomSize> expandToSize(List<CollapsedZoomSize> sequence) {
        var expanded = new ArrayList<ZoomSize>();
        for (var size : sequence) {
            expanded.add(size.size());
            for (int i = 1; i < size.duration(); i++) {
                expanded.add(null);
            }
        }
        return expanded;
    }

    protected static final List<Rhythm> rhythm_sequence = expandToRhythm(beat_sequence);
    protected static final List<Sound> sound_sequence = expandToSound(beat_sequence);
    protected static final List<Animation> animation_sequence = expandToAnimations(beat_sequence);
    protected static final List<Color> color_sequence = expandToColor(beat_sequence);

    protected static final List<ZoomSize> size_sequence = expandToSize(zoom_sequence);


    protected long getConductorBpm() {
        return 162 * 2;
    }

    protected long getInitialDelay() {
        return 1500 * 60 / 162;
    }

    protected final Conductor conductor = new Conductor(60_000, getConductorBpm());
    private final String sprite_path;
    private final JFrame frame;

    private final PlayerInputHandler input_handler;

    private final Clip music;

    public Lockstep(JFrame frame, String music_path, String sound_path, String sprite_path) {
        this.sprite_path = sprite_path;

        this.frame = frame;

        this.input_handler = new PlayerInputHandler(this, 60_000, getConductorBpm(), animation_sequence, barely_range, hit_range);

        try {
            this.music = AudioSystem.getClip();
            music.open(AudioSystem.getAudioInputStream(getResource(music_path)));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }


        for (var sound : Sound.values()) {
            sounds.put(sound, new AudioPlayer(getResource(STR."\{sound_path}/sfx/\{sound.name()}.wav"), 2));
        }

        for (var sound : PlayerSound.values()) {
            player_sounds.put(sound, new AudioPlayer(getResource(STR."\{sound_path}/player/\{sound.name()}.wav"), 10));
        }

        player_sounds.get(PlayerSound.onbeat).setVolume(.7f);

        music.addLineListener((e) -> {
            if (e.getType().equals(LineEvent.Type.STOP) && future != null) {
                future.complete(null);
            }
        });
    }

    private final HashMap<Sound, AudioPlayer> sounds = new HashMap<>();
    private final HashMap<PlayerSound, AudioPlayer> player_sounds = new HashMap<>();

    private volatile LockstepView view = null;

    private volatile CompletableFuture<Void> future = null;

    public CompletableFuture<Void> start() {
        if (view != null) {
            return future;
        }
        view = new LockstepView(60, this, sprite_path);
        view.setBackgroundColor(onbeat_color);
        frame.getContentPane().add(view);

        setup();

        var initial_delay = getInitialDelay();

        music.start();
        conductor.start(initial_delay);
        input_handler.start(initial_delay);

        frame.setSize(200 * 3, 256 * 3);
        frame.setVisible(true);

        view.allAnimate(Animation.readytap);

        this.future = new CompletableFuture<>();
        return this.future;
    }

    protected void setup() {
        conductor.submit((e) -> {
            int i = e.beat();

            if (i >= sound_sequence.size()) {
                conductor.stop();
            }
            if (sound_sequence.get(i) != null)
                sounds.get(sound_sequence.get(i)).play();
        });
        conductor.submit((e) -> {
            int i = e.beat();
            var on = (i & 1) == 0;
            if (i >= rhythm_sequence.size()) {
                conductor.stop();
                return;
            }
            if (rhythm_sequence.get(i) == Rhythm.on) {
                if (on) {
                    player_sounds.get(PlayerSound.onbeat).play();
                }
            } else if (rhythm_sequence.get(i) == Rhythm.off) {
                if (!on) {
                    player_sounds.get(PlayerSound.offbeat).play();
                }
            }
        });
        conductor.submit((e) -> {
            int i = e.beat();

            if (i >= animation_sequence.size()) {
                conductor.stop();
                return;
            }

            if (animation_sequence.get(i) != null) {
                if (animation_sequence.get(i) == Animation.tap)
                    view.allAnimate(Animation.tap);
                else
                    view.crowdAnimate(animation_sequence.get(i));
            }
        });
        conductor.submit((e) -> {
            int i = e.beat();

            if (i >= color_sequence.size()) {
                conductor.stop();
                return;
            }

            if (color_sequence.get(i) != null) {
                view.setBackgroundColor(color_sequence.get(i));
            }
        });
        conductor.submit((e) -> {
            int i = e.beat();

            if (i >= size_sequence.size()) {
                conductor.stop();
                return;
            }

            if (size_sequence.get(i) != null)
                view.zoom(size_sequence.get(i));
        });
    }

    public void stop() {
        if (view == null) {
            return;
        }
        conductor.stop();
        frame.dispose();;
        view = null;
    }

    public void playerTap() {
        input_handler.playerInput();
    }

    public void animatePlayer(Animation animation) {
        view.playerAnimate(animation);
    }

    public void playSound(Sound sound) {
        sounds.get(sound).play();
    }

    public void playSound(PlayerSound sound) {
        player_sounds.get(sound).play();
    }

    public static InputStream getResource(String name) {
        return new BufferedInputStream(Lockstep.class.getResourceAsStream(name));
    }
}
