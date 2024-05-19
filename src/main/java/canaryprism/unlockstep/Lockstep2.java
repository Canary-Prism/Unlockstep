package canaryprism.unlockstep;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import java.awt.Color;

import canaryprism.unlockstep.sequence.CollapsedRhythm;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Off;
import canaryprism.unlockstep.sequence.CollapsedRhythm.On;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Rest;
import canaryprism.unlockstep.sequence.CollapsedRhythm.Tap;
import canaryprism.unlockstep.sequence.Rhythm;
import canaryprism.unlockstep.sequence.Sound;
import canaryprism.unlockstep.swing.Animation;
import canaryprism.unlockstep.swing.CollapsedZoomSize;
import canaryprism.unlockstep.swing.ColorPalette;
import canaryprism.unlockstep.swing.ColorSequence;
import canaryprism.unlockstep.swing.ZoomSize;

public class Lockstep2 extends Lockstep {

    private static final List<CollapsedRhythm> beat_sequence = List.of(
        new Tap(1),
        new Rest(5),
        new Tap(1),
        new Rest(5),
        new Tap(1),
        new Rest(5),
        new Tap(1),
        new Rest(5),
        new Tap(1),
        new Rest(5),
        new Tap(1),
        new Rest(5),
        new Tap(1),
        new Rest(2),
        new Tap(1),
        new Rest(2),
        new Tap(1),
        new Rest(2),
        new Tap(1),
        new Rest(2),
        new On(47),
        new Off(48),
        new On(47),
        new Off(48),
        new On(23),
        new Off(25),
        new On(23),
        new Off(25),
        new On(11),
        new Off(13),
        new On(11),
        new Off(13),
        new On(11),
        new Off(25),
        new On(35),
        new Off(19),
        new On(29),
        new Off(19),
        new On(53),
        new Off(50),
        new On(75)
    );

    private static final List<CollapsedZoomSize> zoom_sequence = List.of(
        new CollapsedZoomSize(ZoomSize.l0, 191),
        new CollapsedZoomSize(ZoomSize.l1, 50),
        new CollapsedZoomSize(ZoomSize.l0, 23),
        new CollapsedZoomSize(ZoomSize.l1, 26),
        new CollapsedZoomSize(ZoomSize.l2, 23),
        new CollapsedZoomSize(ZoomSize.l1, 26),
        new CollapsedZoomSize(ZoomSize.l0, 11),
        new CollapsedZoomSize(ZoomSize.l1, 13),
        new CollapsedZoomSize(ZoomSize.l2, 11),
        new CollapsedZoomSize(ZoomSize.l3, 13),
        new CollapsedZoomSize(ZoomSize.l4, 11),
        new CollapsedZoomSize(ZoomSize.l3, 26),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l2, 2),
        new CollapsedZoomSize(ZoomSize.l1, 2),
        new CollapsedZoomSize(ZoomSize.l0, 2),
        new CollapsedZoomSize(ZoomSize.l3, 23),
        new CollapsedZoomSize(ZoomSize.l4, 13),
        new CollapsedZoomSize(ZoomSize.l0, 19),
        new CollapsedZoomSize(ZoomSize.l3, 29),
        new CollapsedZoomSize(ZoomSize.l4, 19),
        new CollapsedZoomSize(ZoomSize.l0, 6),
        new CollapsedZoomSize(ZoomSize.l1, 3),
        new CollapsedZoomSize(ZoomSize.l2, 3),
        new CollapsedZoomSize(ZoomSize.l3, 3),
        new CollapsedZoomSize(ZoomSize.l4, 15),
        new CollapsedZoomSize(ZoomSize.l1, 3),
        new CollapsedZoomSize(ZoomSize.l2, 3),
        new CollapsedZoomSize(ZoomSize.l3, 3),
        new CollapsedZoomSize(ZoomSize.l4, 14),
        new CollapsedZoomSize(ZoomSize.l1, 3),
        new CollapsedZoomSize(ZoomSize.l2, 3),
        new CollapsedZoomSize(ZoomSize.l3, 3),
        new CollapsedZoomSize(ZoomSize.l4, 15),
        new CollapsedZoomSize(ZoomSize.l1, 3),
        new CollapsedZoomSize(ZoomSize.l2, 3),
        new CollapsedZoomSize(ZoomSize.l3, 3),
        new CollapsedZoomSize(ZoomSize.l4, 14 + 75)
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
                    expanded.set(size - 3, Sound.n);
                    expanded.set(size - 4, Sound.ha);
                    expanded.set(size - 6, Sound.n);
                } else if (expanded.size() > 0 && sequence.get(k - 1) instanceof Rest) {
                    var size = expanded.size();
                    expanded.set(size - 3, Sound.cowbell);
                    expanded.set(size - 6, Sound.cowbell);
                    expanded.set(size - 9, Sound.cowbell);
                    expanded.set(size - 12, Sound.cowbell);
                }
                expanded.add(Sound.hai);
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Off r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof On) {
                    var size = expanded.size();
                    expanded.set(size - 2, Sound.hu);
                    expanded.set(size - 5, Sound.hai);
                    expanded.set(size - 8, Sound.hai);
                    expanded.set(size - 11, Sound.hai);
                }
                expanded.add(Sound.hoi);
                var size = expanded.size();
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
                for (int i = 2; i <= 15; i += 3) {
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
                    expanded.set(size - 3, ColorSequence.onbeat);
                    expanded.set(size - 4, ColorSequence.offbeat);
                    expanded.set(size - 6, ColorSequence.onbeat);
                }
                expanded.add(ColorSequence.onbeat);
                for (int i = 1; i < r.beats(); i++) {
                    expanded.add(null);
                }
            } else if (rhythm instanceof Off r) {
                if (expanded.size() > 0 && sequence.get(k - 1) instanceof On) {
                    var size = expanded.size();
                    expanded.set(size - 2, ColorSequence.onbeat);
                    expanded.set(size - 5, ColorSequence.offbeat);
                    expanded.set(size - 8, ColorSequence.onbeat);
                    expanded.set(size - 11, ColorSequence.offbeat);
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

    protected static final List<Rhythm> lockstep2_rhythm_sequence = expandToRhythm(beat_sequence);
    protected static final List<Sound> lockstep2_sound_sequence = expandToSound(beat_sequence);
    protected static final List<Animation> lockstep2_animation_sequence = expandToAnimations(beat_sequence);
    protected static final List<ColorSequence> lockstep2_color_sequence = expandToColor(beat_sequence);

    protected static final List<ZoomSize> lockstep2_size_sequence = expandToSize(zoom_sequence);


    @Override
    protected List<Rhythm> getRhythmSequence() {
        return lockstep2_rhythm_sequence;
    }
    
    @Override
    protected List<Sound> getSoundSequence() {
        return lockstep2_sound_sequence;
    }
    
    @Override
    protected List<Animation> getAnimationSequence() {
        return lockstep2_animation_sequence;
    }
    
    @Override
    protected List<ColorSequence> getColorSequence() {
        return lockstep2_color_sequence;
    }
    
    @Override
    protected List<ZoomSize> getSizeSequence() {
        return lockstep2_size_sequence;
    }


    public Lockstep2(JFrame frame, String music_path, String audio_path, String sprite_path, ColorPalette color_palette) {
        super(frame, music_path, audio_path, sprite_path, color_palette);
    }

    @Override
    protected long getConductorBpm() {
        return 135 * 3;
    }
    
}
