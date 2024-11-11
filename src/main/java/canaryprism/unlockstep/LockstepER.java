package canaryprism.unlockstep;

import canaryprism.unlockstep.sequence.CollapsedRhythm;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Off;
import canaryprism.unlockstep.sequence.CollapsedRhythm.On;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Rest;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Tap;
import canaryprism.unlockstep.sequence.PlayerSound;
import canaryprism.unlockstep.sequence.Rhythm;
import canaryprism.unlockstep.sequence.Sound;
import canaryprism.unlockstep.swing.*;
import com.adonax.audiocue.AudioCue;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public class LockstepER extends Lockstep {

    private static final List<CollapsedRhythm> beat_sequence = List.of(
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

            new On(15),
            new Off(13),
            new On(4 + 7),
            new Off(25),

            new On(15),
            new Off(13),
            new On(4 + 3),
            new Off(11),
            new On(7),
            new Off(9),
            new On(2 + 9),
            new Off(21),
            new On(2 + 23),
            new Off(7),
            new On(2)
    );

    private static final List<CollapsedZoomSize> zoom_sequence = List.of(
            new CollapsedZoomSize(ZoomSize.l0, 208)
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
                for (int i = 1; i <= 7 && i < r.beats(); i += 2) {
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

    private static List<ColorSequence> expandToColor(List<CollapsedRhythm> sequence) {
        var expanded = new ArrayList<ColorSequence>();
        for (int k = 0; k < sequence.size(); k++) {
            var rhythm = sequence.get(k);
            if (rhythm instanceof On r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof Off) {
                    var size = expanded.size();
                    expanded.set(size - 1, ColorSequence.offbeat);
                    expanded.set(size - 2, ColorSequence.onbeat);
                    expanded.set(size - 3, ColorSequence.offbeat);
                    expanded.set(size - 4, ColorSequence.onbeat);
                }
                expanded.add(ColorSequence.onbeat);
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Off r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof On) {
                    var size = expanded.size();
                    expanded.set(size - 1, ColorSequence.onbeat);
                    expanded.set(size - 3, ColorSequence.offbeat);
                    expanded.set(size - 5, ColorSequence.onbeat);
                    expanded.set(size - 7, ColorSequence.offbeat);
                }
                expanded.add(ColorSequence.offbeat);
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

    protected static final List<Rhythm> lockstepr6_rhythm_sequence = expandToRhythm(beat_sequence);
    protected static final List<Sound> lockstepr6_sound_sequence = expandToSound(beat_sequence);
    protected static final List<Animation> lockstepr6_animation_sequence = expandToAnimations(beat_sequence);
    protected static final List<ColorSequence> lockstepr6_color_sequence = expandToColor(beat_sequence);

    protected static final List<ZoomSize> lockstepr6_size_sequence = expandToSize(zoom_sequence);

    @Override
    protected List<Rhythm> getRhythmSequence() {
        return lockstepr6_rhythm_sequence;
    }

    @Override
    protected List<Sound> getSoundSequence() {
        return lockstepr6_sound_sequence;
    }

    @Override
    protected List<Animation> getAnimationSequence() {
        return lockstepr6_animation_sequence;
    }

    @Override
    protected List<ColorSequence> getColorSequence() {
        return lockstepr6_color_sequence;
    }

    @Override
    protected List<ZoomSize> getSizeSequence() {
        return lockstepr6_size_sequence;
    }

    public LockstepER(JFrame frame, String music_path, String audio_path, String sprite_path,
            ColorPalette color_palette,
            boolean player_input_sound) {
        super(frame, music_path, audio_path, sprite_path, color_palette, player_input_sound);
    }

    private volatile AudioCue music_loop;

    @Override
    protected long getConductorBpm() {
        return 119 * 2;
    }

    @Override
    protected long getInitialDelay() {
        return 3 * 6_000 / 119 - 30;
    }

    private void lineListener(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            ((Clip) event.getSource()).close();
        }
    }

    private volatile int music_bpm = 119;
    private volatile int audio_bpm = 119;
    private volatile int visuals_bpm = 119;


    private void nextMusic() {
        var instance = music_loop.obtainInstance();

        music_loop.setVolume(instance, 1);
        music_loop.setRecycleWhenDone(instance, true);
        music_loop.setSpeed(instance, music_bpm / 119d);





        music_loop.start(instance);
    }

    private final Queue<Optional<Animation>> animation_queue = new ConcurrentLinkedQueue<>(this.getAnimationSequence().stream().map(Optional::ofNullable).toList());
    private final Queue<Optional<ColorSequence>> color_queue = new ConcurrentLinkedQueue<>(this.getColorSequence().stream().map(Optional::ofNullable).toList());
    private final Queue<Optional<Sound>> sound_queue = new ConcurrentLinkedQueue<>(this.getSoundSequence().stream().map(Optional::ofNullable).toList());
    private final Queue<Rhythm> rhythm_queue = new ConcurrentLinkedQueue<>(this.getRhythmSequence());
    private final Queue<Optional<ZoomSize>> size_queue = new ConcurrentLinkedQueue<>(this.getSizeSequence().stream().map(Optional::ofNullable).toList());

    private final Object generation_lock = new Object();

    private volatile boolean now_onbeat = true;

    private void generate() {
        // generate more rhythms for endlessness i guess
        synchronized (generation_lock) {
            if (now_onbeat) {
                int target = randomIntInclusive(1, 12);
                if (target == 1)
                    target = 0;

                for (int i = 0; i < target; i++) {
                    rhythm_queue.add(Rhythm.on);
                    rhythm_queue.add(Rhythm.on);

                    animation_queue.add(Optional.of(Animation.hitleft));
                    animation_queue.add(Optional.empty());

                    input_handler.load(Animation.hitleft);
                    input_handler.load(((Animation) null));

                    if (i == 0) {
                        sound_queue.add(Optional.of(Sound.hai));
                        sound_queue.add(Optional.empty());

                        color_queue.add(Optional.of(ColorSequence.onbeat));
                        color_queue.add(Optional.empty());

                        if (randomIntInclusive(0, 1) == 0) {
                            size_queue.add(Optional.of(ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)]));
                            size_queue.add(Optional.empty());
                        } else {
                            size_queue.add(Optional.empty());
                            size_queue.add(Optional.empty());
                        }
                    } else {
                        sound_queue.add(Optional.empty());
                        sound_queue.add(Optional.empty());

                        color_queue.add(Optional.empty());
                        color_queue.add(Optional.empty());

                        if (randomIntInclusive(0, 19) == 0) {
                            size_queue.add(Optional.of(ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)]));
                            size_queue.add(Optional.empty());
                        } else {
                            size_queue.add(Optional.empty());
                            size_queue.add(Optional.empty());
                        }
                    }
                }

                rhythm_queue.addAll(List.of(
                        Rhythm.on, Rhythm.on, Rhythm.on, Rhythm.on, Rhythm.on, Rhythm.on, Rhythm.on, Rhythm.off
                ));

                // TODO: extract to static final fields probably
                animation_queue.addAll(Stream.of(
                        Animation.hitleft,
                        null,
                        Animation.hitleft,
                        null,
                        Animation.hitleft,
                        null,
                        Animation.hitleft,
                        Animation.hitright
                ).map(Optional::ofNullable).toList());

                input_handler.load(Stream.of(
                        Animation.hitleft,
                        null,
                        Animation.hitleft,
                        null,
                        Animation.hitleft,
                        null,
                        Animation.hitleft,
                        Animation.hitright
                ).map(Optional::ofNullable).toList());

                color_queue.addAll(Stream.of(
                        ColorSequence.offbeat,
                        null,
                        ColorSequence.onbeat,
                        null,
                        ColorSequence.offbeat,
                        null,
                        ColorSequence.onbeat,
                        ColorSequence.offbeat
                ).map(Optional::ofNullable).toList());
                
                sound_queue.addAll(Stream.of(
                        Sound.hai,
                        null,
                        Sound.hai,
                        null,
                        Sound.hai,
                        null,
                        Sound.hu,
                        Sound.hoi
                ).map(Optional::ofNullable).toList());

                if (randomIntInclusive(0, 2) == 0) {
                    size_queue.addAll(Stream.of(
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)],
                            null,
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)],
                            null,
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)],
                            null,
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)],
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)]
                    ).map(Optional::ofNullable).toList());
                } else {
                    // wow, this sucks
                    size_queue.addAll(List.of(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
                }

                now_onbeat = false;
            } else {
                int target = randomIntInclusive(0, 12);

                for (int i = 0; i < target; i++) {
                    rhythm_queue.add(Rhythm.off);
                    rhythm_queue.add(Rhythm.off);

                    animation_queue.add(Optional.empty());
                    animation_queue.add(Optional.of(Animation.hitright));

                    input_handler.load(((Animation) null));
                    input_handler.load(Animation.hitright);

                    if (i <= 2) {
                        sound_queue.add(Optional.empty());
                        sound_queue.add(Optional.of(Sound.ho));

                        color_queue.add(Optional.of(ColorSequence.offbeat));
                        color_queue.add(Optional.empty());
                    } else {
                        sound_queue.add(Optional.empty());
                        sound_queue.add(Optional.empty());

                        color_queue.add(Optional.empty());
                        color_queue.add(Optional.empty());

                        if (randomIntInclusive(0, 19) == 0) {
                            size_queue.add(Optional.empty());
                            size_queue.add(Optional.of(ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)]));
                        } else {
                            size_queue.add(Optional.empty());
                            size_queue.add(Optional.empty());
                        }
                    }
                }

                rhythm_queue.addAll(List.of(
                        Rhythm.off, Rhythm.off, Rhythm.off, Rhythm.off
                ));

                // TODO: extract to static final fields probably
                animation_queue.addAll(Stream.of(
                        null,
                        Animation.hitright,
                        null,
                        Animation.hitright
                ).map(Optional::ofNullable).toList());

                input_handler.load(Stream.of(
                        null,
                        Animation.hitright,
                        null,
                        Animation.hitright
                ).map(Optional::ofNullable).toList());

                color_queue.addAll(Stream.of(
                        ColorSequence.onbeat,
                        ColorSequence.offbeat,
                        ColorSequence.onbeat,
                        ColorSequence.offbeat
                ).map(Optional::ofNullable).toList());


                sound_queue.addAll(Stream.of(
                        Sound.n,
                        Sound.ha,
                        Sound.n,
                        Sound.ha
                ).map(Optional::ofNullable).toList());

                if (randomIntInclusive(0, 2) == 0) {
                    size_queue.addAll(Stream.of(
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)],
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)],
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)],
                            ZoomSize.values()[randomIntInclusive(0, ZoomSize.values().length - 1)]
                    ).map(Optional::ofNullable).toList());
                } else {
                    // wow, this sucks
                    size_queue.addAll(List.of(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
                }

                now_onbeat = true;
            }
        }
    }

    private int randomIntInclusive(int lower, int upper) {
        return (int)((Math.random() * (upper - lower + 1)) + lower);
    }

    @Override
    protected void setup() {
        final var bpm_inc = 5;
//        audio_conductor.setTempo(60 * 1000 * 10, 1190 * 2);
//        visuals_conductor.setTempo(60 * 1000 * 10, 1190 * 2);
        try {
            music_loop = AudioCue.makeStereoCue(Objects.requireNonNull(this.getClass().getResource("/unlockstep_assets/music/endless_remix_loop.wav")), 2);

            music_loop.open();

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        audio_conductor.submit((e) -> {
            int i = e.beat() - 16;
            if (i % 448 == 0) {
                nextMusic();
            }
        });
        music_bpm -= bpm_inc;
        audio_conductor.submit((e) -> {
            int i = e.beat() - 16 + 1;
            if (i % 448 == 0) {

                music_bpm += bpm_inc;

            }
        });
        audio_conductor.submit((e) -> {
            int i = e.beat() - 16 + 1;
            if (i % 448 == 0) {
//                nextMusic();

                audio_conductor.setTempo(60 * 1000, audio_bpm * 2);

                audio_bpm += bpm_inc;
            }
        });
        audio_conductor.submit((e) -> {
            int i = e.beat();


            if (sound_queue.isEmpty()) {

                audio_conductor.stop();
                return;
            }

            if (sound_queue.element().isPresent())
                sounds.get(sound_queue.element().get()).play();

            sound_queue.remove();
        });
        audio_conductor.submit((e) -> {
            int i = e.beat();
            var on = (i & 1) == 0;
            if (rhythm_queue.isEmpty()) {
                audio_conductor.stop();
                return;
            }

            if (rhythm_queue.element() == Rhythm.on) {
                if (on) {
                    player_sounds.get(PlayerSound.onbeat).play();
                }
            } else if (rhythm_queue.element() == Rhythm.off) {
                if (!on) {
                    player_sounds.get(PlayerSound.offbeat).play();
                }
            }
            rhythm_queue.poll();

            if (rhythm_queue.size() < 16) {
                Thread.ofVirtual().start(this::generate);
            }
        });
        visuals_conductor.submit((e) -> {
            int i = e.beat() - 16 + 1;
            if (i % 448 == 0) {

                visuals_conductor.setTempo(60 * 1000, visuals_bpm * 2);
                input_handler.queueSetTempo(60_000, visuals_bpm * 2);

                visuals_bpm += bpm_inc;
            }
        });

        visuals_conductor.submit((e) -> {
            int i = e.beat();

            if (animation_queue.isEmpty()) {
                visuals_conductor.stop();
                return;
            }

            if (animation_queue.element().isPresent()) {
                if (animation_queue.element().get() == Animation.tap)
                    view.allAnimate(Animation.tap);
                else {
                    if (auto) // auto mode hack
                        playerTap();
                    view.crowdAnimate(animation_queue.element().get());
                }
            }
            animation_queue.remove();
        });
        visuals_conductor.submit((e) -> {
            int i = e.beat();

            if (color_queue.isEmpty()) {
                visuals_conductor.stop();
                return;
            }

            if (color_queue.element().isPresent()) {
                view.setBackgroundColor(color_palette.getColor(color_queue.element().get()));
            }
            color_queue.remove();

        });
        visuals_conductor.submit((e) -> {
            int i = e.beat();

            if (size_queue.isEmpty()) {
                visuals_conductor.stop();
                return;
            }

            if (size_queue.element().isPresent())
                view.zoom(size_queue.element().get());
        });

    }

    protected volatile boolean auto = false;

    @Override
    public void setAuto(boolean auto) {
        this.auto = auto;
        super.setAuto(auto);
    }
}
