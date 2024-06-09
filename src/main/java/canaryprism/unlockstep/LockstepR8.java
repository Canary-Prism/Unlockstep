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

public class LockstepR8 extends Lockstep {

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
// 32
        new On(7),
        new Off(25),

        new On(8),


        new Rest(8),

        new Tap(1),
        new Rest(1),
        new Tap(1),
        new Rest(1),
        new Tap(1),
        new Rest(1),
        new Tap(1),
        new Rest(1),
        // new Rest(16),

        new On(7),
        // new On(31),
        new Off(17),
        new On(31),

        new Off(5),
        new On(12),
        // new Off(17),

        new Rest(14),

        new On(11),
        new Off(7),
        // new On(15),
        // new Off(17),
        new On(31),
        new Off(9),
        new On(7),
        new Off(9),
        new On(23),
        new Off(13),

        new On(4),

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
        // new Rest(16),

        new On(15),
        new Off(13),
        new On(19),
        // new On(19),
        // new Off(13),
        // new On(35),
        new Off(13),
        new On(19),
        new Off(25),
        new On(7),
        new Off(5),
        new On(27),

        new Off(17),
        new On(23),
        // new Off(25),
        // new On(15),
        new Off(25),
        new On(63),
        new Off(17),
        new On(2)
    );

    private static final List<CollapsedZoomSize> zoom_sequence = List.of(
        new CollapsedZoomSize(ZoomSize.l0, 88),
        new CollapsedZoomSize(ZoomSize.l1, 40),
        // new CollapsedZoomSize(ZoomSize.l0, 128),
        new CollapsedZoomSize(ZoomSize.l1, 15),
        new CollapsedZoomSize(ZoomSize.l0, 17),

        new CollapsedZoomSize(ZoomSize.l1, 14),
        new CollapsedZoomSize(ZoomSize.l2, 11),
        new CollapsedZoomSize(ZoomSize.l3, 7),
        new CollapsedZoomSize(ZoomSize.l4, 31),
        // new CollapsedZoomSize(ZoomSize.l2, 15),
        // new CollapsedZoomSize(ZoomSize.l3, 48),
        new CollapsedZoomSize(ZoomSize.l4, 9),
        new CollapsedZoomSize(ZoomSize.l3, 7),
        new CollapsedZoomSize(ZoomSize.l4, 9),
        new CollapsedZoomSize(ZoomSize.l2, 8),
        new CollapsedZoomSize(ZoomSize.l3, 8),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 1),
        new CollapsedZoomSize(ZoomSize.l4, 13),

        new CollapsedZoomSize(ZoomSize.l3, 1),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 1),
        // new CollapsedZoomSize(ZoomSize.l0, 4),

        new CollapsedZoomSize(ZoomSize.l0, 16),
        // new CollapsedZoomSize(ZoomSize.l1, 2),
        // new CollapsedZoomSize(ZoomSize.l2, 2),
        // new CollapsedZoomSize(ZoomSize.l3, 2),
        // new CollapsedZoomSize(ZoomSize.l4, 9),

        new CollapsedZoomSize(ZoomSize.l1, 15),
        new CollapsedZoomSize(ZoomSize.l0, 13),
        // new CollapsedZoomSize(ZoomSize.l0, 12),
        // new CollapsedZoomSize(ZoomSize.l1, 2),
        // new CollapsedZoomSize(ZoomSize.l0, 2),
        // new CollapsedZoomSize(ZoomSize.l1, 2),
        // new CollapsedZoomSize(ZoomSize.l0, 2),
        // new CollapsedZoomSize(ZoomSize.l1, 2),
        // new CollapsedZoomSize(ZoomSize.l0, 2),
        // new CollapsedZoomSize(ZoomSize.l1, 2),
        // new CollapsedZoomSize(ZoomSize.l0, 2),

        new CollapsedZoomSize(ZoomSize.l4, 1),
        new CollapsedZoomSize(ZoomSize.l3, 1),
        new CollapsedZoomSize(ZoomSize.l2, 1),
        new CollapsedZoomSize(ZoomSize.l1, 1),
        // new CollapsedZoomSize(ZoomSize.l1, 2),
        // new CollapsedZoomSize(ZoomSize.l0, 2),

        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 1),
        new CollapsedZoomSize(ZoomSize.l4, 13),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l4, 2),
        new CollapsedZoomSize(ZoomSize.l1, 15),
        new CollapsedZoomSize(ZoomSize.l4, 25),
        new CollapsedZoomSize(ZoomSize.l3, 7),
        new CollapsedZoomSize(ZoomSize.l4, 1),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 27),
        new CollapsedZoomSize(ZoomSize.l0, 25),

        new CollapsedZoomSize(ZoomSize.l1, 8),
        new CollapsedZoomSize(ZoomSize.l2, 8),
        new CollapsedZoomSize(ZoomSize.l3, 8),
        new CollapsedZoomSize(ZoomSize.l4, 24),

        new CollapsedZoomSize(ZoomSize.l3, 1),
        new CollapsedZoomSize(ZoomSize.l2, 1),
        new CollapsedZoomSize(ZoomSize.l1, 1),
        new CollapsedZoomSize(ZoomSize.l0, 1),
        new CollapsedZoomSize(ZoomSize.l3, 1),
        new CollapsedZoomSize(ZoomSize.l2, 1),
        new CollapsedZoomSize(ZoomSize.l1, 1),
        new CollapsedZoomSize(ZoomSize.l0, 1),

        new CollapsedZoomSize(ZoomSize.l4, 8),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l4, 16),

        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l4, 7),

        new CollapsedZoomSize(ZoomSize.l3, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 4),

        new CollapsedZoomSize(ZoomSize.l4, 9)
        // new CollapsedZoomSize(ZoomSize.l1, 15),
        // new CollapsedZoomSize(ZoomSize.l4, 26),
        // new CollapsedZoomSize(ZoomSize.l4, 150)
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
        var first = true;
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
                    if (first) {
                        first = false;
                        var size = expanded.size();
                        expanded.set(size - 2, Sound.cowbell);
                        expanded.set(size - 4, Sound.cowbell);
                        expanded.set(size - 6, Sound.cowbell);
                        expanded.set(size - 8, Sound.cowbell);
                    } else {
                        var size = expanded.size();
                        expanded.set(size - 1, Sound.ha);
                        expanded.set(size - 2, Sound.n);
                        expanded.set(size - 3, Sound.ha);
                        expanded.set(size - 4, Sound.n);
                    }
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
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof On) {
                    var size = expanded.size();
                    expanded.set(size - 1, Sound.cowbell);
                    expanded.set(size - 2, Sound.cowbell);
                    expanded.set(size - 3, Sound.cowbell);
                    expanded.set(size - 4, Sound.cowbell);
                }
                for (int i = 0; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Tap r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof On) {
                    var size = expanded.size();
                    expanded.set(size - 1, Sound.cowbell);
                    expanded.set(size - 2, Sound.cowbell);
                    expanded.set(size - 3, Sound.cowbell);
                    expanded.set(size - 4, Sound.cowbell);
                }
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

    protected static final List<Rhythm> lockstepr8_rhythm_sequence = expandToRhythm(beat_sequence);
    protected static final List<Sound> lockstepr8_sound_sequence = expandToSound(beat_sequence);
    protected static final List<Animation> lockstepr8_animation_sequence = expandToAnimations(beat_sequence);
    protected static final List<ColorSequence> lockstepr8_color_sequence = expandToColor(beat_sequence);

    protected static final List<ZoomSize> lockstepr8_size_sequence = expandToSize(zoom_sequence);

    @Override
    protected List<Rhythm> getRhythmSequence() {
        return lockstepr8_rhythm_sequence;
    }

    @Override
    protected List<Sound> getSoundSequence() {
        return lockstepr8_sound_sequence;
    }

    @Override
    protected List<Animation> getAnimationSequence() {
        return lockstepr8_animation_sequence;
    }

    @Override
    protected List<ColorSequence> getColorSequence() {
        return lockstepr8_color_sequence;
    }

    @Override
    protected List<ZoomSize> getSizeSequence() {
        return lockstepr8_size_sequence;
    }

    public LockstepR8(JFrame frame, String music_path, String audio_path, String sprite_path,
            ColorPalette color_palette,
            boolean player_input_sound) {
        super(frame, music_path, audio_path, sprite_path, color_palette, player_input_sound);
    }

    @Override
    protected long getConductorBpm() {
        return 171 * 2;
    }

    @Override
    protected long getInitialDelay() {
        return 119 * 600 / 171;
    }

    @Override
    protected void setup() {
        final var first_change = 223 - 8;
        audio_conductor.submit((e) -> {
            if (e.beat() == (143 - 32) * 2 + 1) {
                // audio_conductor.setTempo(600_000, 1715 * 2);
            } else if (e.beat() == (143 + 8) * 2 + 1) {
                audio_conductor.setTempo(60_000_000, 171184 * 2);
            } else if (e.beat() == first_change * 2 + 1) {
                audio_conductor.setTempo(60_000, 182 * 2);
            } else if (e.beat() == (first_change + 4 * 6) * 2 + 1) {
                audio_conductor.setTempo(60_000, 185 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2) * 2 + 1) {
                audio_conductor.setTempo(60_000, 190 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2) * 2 + 1) {
                audio_conductor.setTempo(60_000, 198 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2) * 2 + 1) {
                audio_conductor.setTempo(60_000, 213 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2) * 2 + 1) {
                audio_conductor.setTempo(60_000, 216 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2) * 2 + 1) {
                audio_conductor.setTempo(60_000, 218 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4) * 2 + 1) {
                audio_conductor.setTempo(60_000, 220 * 2);
            }
        });
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
            if (e.beat() == (143 - 32) * 2 + 1) {
                // visuals_conductor.setTempo(600_000, 1715 * 2);
                // input_handler.setTempo(600_000, 1715 * 2);
            } else if (e.beat() == (143 + 8) * 2 + 1) {
                visuals_conductor.setTempo(60_000_000, 171184 * 2);
                input_handler.setTempo(60_000_000, 171184 * 2);
            } else if (e.beat() == first_change * 2 + 1) {
                visuals_conductor.setTempo(60_000, 182 * 2);
                input_handler.setTempo(60_000, 182 * 2);
            } else if (e.beat() == (first_change + 4 * 6) * 2 + 1) {
                visuals_conductor.setTempo(60_000, 185 * 2);
                input_handler.setTempo(60_000, 185 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2) * 2 + 1) {
                visuals_conductor.setTempo(60_000, 190 * 2);
                input_handler.setTempo(60_000, 190 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2) * 2 + 1) {
                visuals_conductor.setTempo(60_000, 198 * 2);
                input_handler.setTempo(60_000, 198 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2) * 2 + 1) {
                visuals_conductor.setTempo(60_000, 213 * 2);
                input_handler.setTempo(60_000, 213 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2) * 2 + 1) {
                visuals_conductor.setTempo(60_000, 216 * 2);
                input_handler.setTempo(60_000, 216 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2) * 2 + 1) {
                visuals_conductor.setTempo(60_000, 218 * 2);
                input_handler.setTempo(60_000, 218 * 2);
            } else if (e.beat() == (first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4) * 2 + 1) {
                visuals_conductor.setTempo(60_000, 220 * 2);
                input_handler.setTempo(60_000, 220 * 2);
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

            if (size_sequence.get(i) != null) {
                view.zoom(size_sequence.get(i));
                // System.out.println("size change " + size_sequence.get(i));
            }
        });
    }

}
