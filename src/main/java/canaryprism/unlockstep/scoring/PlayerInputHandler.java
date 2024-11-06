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


    private volatile boolean player_input_sound;

    public PlayerInputHandler(Lockstep lockstep, long millis, long beats, List<Animation> animation_sequence, long barely_range, long hit_range, boolean player_input_sound) {
        this.lockstep = lockstep;

        this.player_input_sound = player_input_sound;

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
                    this.player_animation = animation_sequence.get(i);
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
                    this.player_animation = switch (player_animation) {
                        case hitleft -> Animation.missleft;
                        case hitright -> Animation.missright;
                        default -> player_animation;
                    };
                    if (!has_tapped && !has_missed) {
                        missHit();
                        has_missed = true;
                    }
                    this.player_animation = switch (player_animation) {
                        case hitleft -> Animation.missright;
                        case hitright -> Animation.missleft;
                        default -> player_animation;
                    };
                }
                has_tapped = false;

                if (i + 1 >= animation_sequence.size()) {
                    this.running = false;
                }

                if (will_change) {

                    start_barely_conductor.setTempo(change_millis, change_beats);
                    start_hit_conductor.setTempo(change_millis, change_beats);
                    end_hit_conductor.setTempo(change_millis, change_beats);
                    end_barely_conductor.setTempo(change_millis, change_beats);

                    will_change = false;
                }
            }
        });

        this.barely_start = Math.floorDiv(barely_range, 2);
        this.hit_start = Math.floorDiv(hit_range, 2);
        this.hit_end = Math.ceilDiv(hit_range, 2);
        this.barely_end = Math.ceilDiv(barely_range, 2);
    }

    private volatile Animation player_animation = Animation.missleft;
    // private volatile boolean onbeat = true;
    private volatile Scoring current_score = Scoring.miss;

    private final Object ticklock = new Object();

    private volatile boolean has_tapped = false, has_missed = false;

    private volatile Animation last_animation;

    private volatile boolean running = false;

    private void missHit() {
        synchronized (ticklock) {
            lockstep.animatePlayer(player_animation);
            last_animation = (player_animation == Animation.missleft) ? Animation.hitleft : Animation.hitright;
            lockstep.playSound(PlayerSound.miss);
            lockstep.fail();
        }
    }

    public void start(long initial_delay) {
        start_barely_conductor.start(initial_delay - barely_start);
        start_hit_conductor.start(initial_delay - hit_start);
        end_hit_conductor.start(initial_delay + hit_end);
        end_barely_conductor.start(initial_delay + barely_end);
        running = true;
    }

    private volatile boolean will_change = false;
    private volatile long change_millis = 0, change_beats = 0;

    public void setTempo(long millis, long beats) {
        synchronized (ticklock) {
            if (!running || current_score == Scoring.miss) {
                start_barely_conductor.setTempo(millis, beats);
                start_hit_conductor.setTempo(millis, beats);
                end_hit_conductor.setTempo(millis, beats);
                end_barely_conductor.setTempo(millis, beats);
            } else {
                will_change = true;
                change_millis = millis;
                change_beats = beats;
            }
        }
    }

    public void queueSetTempo(long millis, long beats) {
        synchronized (ticklock) {
            will_change = true;
            change_millis = millis;
            change_beats = beats;
        }
    }

    public void playerInput() {
        if (!running) {
            return;
        }
        synchronized (ticklock) {
            var score = this.current_score;
            if (has_tapped) 
                return;
    
            has_tapped = true;
            

            switch (score) {
                case hit -> {
                    has_missed = false;
                    lockstep.animatePlayer(player_animation);
                    lockstep.indicatorTrigger();
                    if (player_input_sound) {
                        if (player_animation == Animation.hitleft) {
                            lockstep.playSound(PlayerSound.onbeat);
                        } else {
                            lockstep.playSound(PlayerSound.offbeat);
                        }
                    }
                }
                case barely -> {
                    has_missed = false;
                    lockstep.animatePlayer(player_animation);
                    lockstep.playSound(PlayerSound.barely);
                }
                case miss -> {
                    missHit();
                    has_missed = true;
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
