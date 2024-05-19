package canaryprism.unlockstep.scoring;

import java.util.List;
import canaryprism.unlockstep.Conductor;
import canaryprism.unlockstep.Lockstep;
import canaryprism.unlockstep.sequence.PlayerSound;
import canaryprism.unlockstep.swing.Animation;

public class PlayerInputHandler {
    private final Conductor start_barely_conductor;
    private final Conductor start_hit_conductor;
    private final Conductor end_hit_conductor;
    private final Conductor end_barely_conductor;
    private final long barely_start, hit_start, hit_end, barely_end;

    private final Lockstep lockstep;

    public PlayerInputHandler(Lockstep lockstep, long millis, long beats, List<Animation> animation_sequence, long barely_range, long hit_range) {
        this.lockstep = lockstep;

        this.start_barely_conductor = new Conductor(millis, beats);
        this.start_hit_conductor = new Conductor(millis, beats);
        this.end_hit_conductor = new Conductor(millis, beats);
        this.end_barely_conductor = new Conductor(millis, beats);

        start_barely_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= animation_sequence.size()) {
                start_barely_conductor.stop();
                return;
            }

            synchronized (ticklock) {
                if (animation_sequence.get(i) == Animation.hitleft || animation_sequence.get(i) == Animation.hitright) {
                    if (animation_sequence.get(i) != last_animation) {
                        has_missed = false;
                        last_animation = animation_sequence.get(i);
                    }
                    current_score = Scoring.barely;
                }
            }
        });
        start_hit_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= animation_sequence.size()) {
                start_hit_conductor.stop();
                return;
            }

            synchronized (ticklock) {
                if (animation_sequence.get(i) == Animation.hitleft || animation_sequence.get(i) == Animation.hitright) {
                    current_score = Scoring.hit;
                }
            }
        });
        end_hit_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= animation_sequence.size()) {
                end_hit_conductor.stop();
                return;
            }

            synchronized (ticklock) {
                if (animation_sequence.get(i) == Animation.hitleft || animation_sequence.get(i) == Animation.hitright) {
                    current_score = Scoring.barely;
                }
            }
        });
        end_barely_conductor.submit((e) -> {
            int i = e.beat();

            if (i >= animation_sequence.size()) {
                end_barely_conductor.stop();
                return;
            }

            synchronized (ticklock) {
                if (animation_sequence.get(i) == Animation.hitleft || animation_sequence.get(i) == Animation.hitright) {
                    current_score = Scoring.miss;
                    if (!has_tapped && !has_missed) {
                        missHit();
                        has_missed = true;
                    }
                }
                has_tapped = false;
                onbeat = !onbeat;
            }
        });

        this.barely_start = Math.floorDiv(barely_range, 2);
        this.hit_start = Math.floorDiv(hit_range, 2);
        this.hit_end = Math.ceilDiv(hit_range, 2);
        this.barely_end = Math.ceilDiv(barely_range, 2);
    }

    private volatile boolean onbeat = true;
    private volatile Scoring current_score = Scoring.miss;

    private final Object ticklock = new Object();

    private volatile boolean has_tapped = false, has_missed = false;

    private volatile Animation last_animation;

    private void missHit() {
        synchronized (ticklock) {
            lockstep.animatePlayer((onbeat) ? Animation.missleft : Animation.missright);
            last_animation = (onbeat) ? Animation.hitleft : Animation.hitright;
            lockstep.playSound(PlayerSound.miss);
        }
    }

    public void start(long initial_delay) {
        start_barely_conductor.start(initial_delay - barely_start);
        start_hit_conductor.start(initial_delay - hit_start);
        end_hit_conductor.start(initial_delay + hit_end);
        end_barely_conductor.start(initial_delay + barely_end);
    }

    public void playerInput() {
        synchronized (ticklock) {
            var score = this.current_score;
            var onbeat = this.onbeat;
            if (has_tapped) 
                return;
    
            has_tapped = true;
            
            if (onbeat) {
                switch (score) {
                    case hit -> {
                        has_missed = false;
                        lockstep.animatePlayer(Animation.hitleft);
                        lockstep.playSound(PlayerSound.onbeat);
                    }
                    case barely -> {
                        has_missed = false;
                        lockstep.animatePlayer(Animation.hitleft);
                        lockstep.playSound(PlayerSound.barely);
                    }
                    case miss -> {
                        missHit();
                    }
                }
            } else {
                switch (score) {
                    case hit -> {
                        has_missed = false;
                        lockstep.animatePlayer(Animation.hitright);
                        lockstep.playSound(PlayerSound.offbeat);
                    }
                    case barely -> {
                        has_missed = false;
                        lockstep.animatePlayer(Animation.hitright);
                        lockstep.playSound(PlayerSound.barely);
                    }
                    case miss -> {
                        missHit();
                    }
                }
            }
        }
    }

    public void stop() {
        start_barely_conductor.stop();
        start_hit_conductor.stop();
        end_hit_conductor.stop();
        end_barely_conductor.stop();
    }
}
