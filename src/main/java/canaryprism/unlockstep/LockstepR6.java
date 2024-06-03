package canaryprism.unlockstep;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import canaryprism.unlockstep.sequence.CollapsedRhythm;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Off;
import canaryprism.unlockstep.sequence.CollapsedRhythm.On;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Rest;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Tap;
import canaryprism.unlockstep.sequence.PlayerSound;
import canaryprism.unlockstep.sequence.Rhythm;
import canaryprism.unlockstep.sequence.Sound;
import canaryprism.unlockstep.swing.Animation;
import canaryprism.unlockstep.swing.CollapsedZoomSize;
import canaryprism.unlockstep.swing.ColorPalette;
import canaryprism.unlockstep.swing.ColorSequence;
import canaryprism.unlockstep.swing.ZoomSize;

public class LockstepR6 extends Lockstep {

    private static final List<CollapsedRhythm> beat_sequence = List.of(
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
        new On(7),
        new Off(25),
        new On(31),
        new Off(17),
        new On(31),
        new Off(17),
        new On(15),
        new Off(17),
        new On(31),
        new Off(9),
        new On(7),
        new Off(9),
        new On(23),
        new Off(13),
        new On(19), 
        new Off(13),
        new On(35),
        new Off(13),
        new On(19),
        new Off(25), 
        new On(7),
        new Off(5),
        new On(27), 
        new Off(25),
        new On(15),
        new Off(26)
    );

    private static final List<CollapsedZoomSize> zoom_sequence = List.of(
        new CollapsedZoomSize(ZoomSize.l0, 128),
        new CollapsedZoomSize(ZoomSize.l1, 15),
        new CollapsedZoomSize(ZoomSize.l0, 17),
        new CollapsedZoomSize(ZoomSize.l2, 15),
        new CollapsedZoomSize(ZoomSize.l3, 48),
        new CollapsedZoomSize(ZoomSize.l4, 9),
        new CollapsedZoomSize(ZoomSize.l0, 7),
        new CollapsedZoomSize(ZoomSize.l4, 9),
        new CollapsedZoomSize(ZoomSize.l1, 8),
        new CollapsedZoomSize(ZoomSize.l0, 8),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 1),
        new CollapsedZoomSize(ZoomSize.l4, 13),
        new CollapsedZoomSize(ZoomSize.l0, 4),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l4, 9),
        new CollapsedZoomSize(ZoomSize.l0, 13),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l0, 1),
        new CollapsedZoomSize(ZoomSize.l4, 13),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 15),
        new CollapsedZoomSize(ZoomSize.l4, 25),
        new CollapsedZoomSize(ZoomSize.l3, 7),
        new CollapsedZoomSize(ZoomSize.l4, 1),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 27),
        new CollapsedZoomSize(ZoomSize.l0, 25),
        new CollapsedZoomSize(ZoomSize.l1, 15),
        new CollapsedZoomSize(ZoomSize.l4, 26)
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

    public LockstepR6(JFrame frame, String music_path, String audio_path, String sprite_path, ColorPalette color_palette,
            boolean player_input_sound) {
        super(frame, music_path, audio_path, sprite_path, color_palette, player_input_sound);
    }

    @Override
    protected long getConductorBpm() {
        return 135 * 2;
    }

    @Override
    protected long getInitialDelay() {
        return 17 * 6_000 / 135;
    }

    @Override
    protected void setup() {
        audio_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= sound_sequence.size()) {
                audio_conductor.stop();
                return;
            }
            if (sound_sequence.get(i) != null)
                sounds.get(sound_sequence.get(i)).play();
        });
        audio_conductor.submit((e) -> {
            int i = e.beat();
            var on = (i & 1) == 0;
            if (i >= rhythm_sequence.size()) {
                audio_conductor.stop();
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
        visuals_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= animation_sequence.size()) {
                visuals_conductor.stop();
                return;
            }

            if (animation_sequence.get(i) != null) {
                if (animation_sequence.get(i) == Animation.tap)
                    view.allAnimate(Animation.tap);
                else
                    view.crowdAnimate(animation_sequence.get(i));
            }
        });
        visuals_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= color_sequence.size()) {
                visuals_conductor.stop();
                return;
            }

            if (color_sequence.get(i) != null) {
                view.setBackgroundColor(color_palette.getColor(color_sequence.get(i)));
            }
            if (i + 4 == rhythm_sequence.size()) {
                view.fadeOut();
            }
        });
        visuals_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= size_sequence.size()) {
                visuals_conductor.stop();
                return;
            }

            if (size_sequence.get(i) != null)
                view.zoom(size_sequence.get(i));
        });
    }

}
