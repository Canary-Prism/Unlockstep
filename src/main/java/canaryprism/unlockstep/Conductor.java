package canaryprism.unlockstep;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Conductor {
    private Timer timer = new Timer();
    public Conductor(long millis, long beats) {
        var gcd = gcd(millis, beats);
        millis /= gcd;
        beats /= gcd;


        this.small_loop = beats;
        this.long_delay = millis;
        this.short_delay = Math.round(((double)millis) / beats);

        // delay_sequence = new long[(int) beats];

        // var avg_delay = millis / beats;

        // for (int i = 0; i < beats - 1; i++) {
        //     delay_sequence[i] = avg_delay;
        // }
        // delay_sequence[(int) beats - 1] = millis - avg_delay * (beats - 1);

        // System.out.println(Arrays.toString(delay_sequence));
    }

    private volatile long small_loop;
    private volatile long long_delay;
    private volatile long short_delay;
    
    private ArrayList<ConductorTimerTask> tasks = new ArrayList<>();
    private volatile boolean running = false;
    
    
    private volatile int beat = 0;
    
    private volatile boolean tempo_changed = false;
    private volatile int target_beat;

    public void setTempo(long millis, long beats) {
        var gcd = gcd(millis, beats);
        millis /= gcd;
        beats /= gcd;

        this.small_loop = beats;
        this.long_delay = millis;
        this.short_delay = Math.round(((double)millis) / beats);

        tempo_changed = true;
        var target_beat = this.beat + 1;
        // for (var task : tasks) {
        //     task.tempo_block = true;
        //     task.tempo_block_beat = target_beat;
        // }
        this.target_beat = target_beat;
    }

    private void changeTempo() {
        running = false;
        timer.cancel();
        timer = new Timer();
        start(0);
    }

    // private volatile ConductorTimerTask tempo_change = new ConductorTimerTask((e) -> {
    //     if (tempo_changed && e.beat == target_beat) {
    //         tempo_changed = false;
    //         System.out.println("Tempo changed");
    //         changeTempo();
    //     }
    // }, 0);

    public void start(long initial_delay) {
        if (running) {
            return;
        }
        running = true;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < tasks.size(); i++) {
                    tasks.set(i, tasks.get(i).clone());
                }

                // tempo_change = tempo_change.clone();
                // timer.scheduleAtFixedRate(tempo_change, 0, short_delay);

                for (int i = 0; i < tasks.size(); i++) {
                    var task = tasks.get(i);
                    timer.scheduleAtFixedRate(task, 0, short_delay);
                }
            }
        }, initial_delay, long_delay);
        // for (int i = 0; i < tasks.size(); i++) {
        //     var task = tasks.get(i);
        //     timer.scheduleAtFixedRate(task, 0, short_delay);
        // }
    }

    public void submit(Consumer<ConductorEvent> r) {
        if (running) {
            throw new IllegalStateException("Cannot submit tasks while the conductor is running");
        }
        tasks.add(new ConductorTimerTask(r, 0));
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        timer.cancel();
        timer = new Timer();
    }


    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // private long[] delay_sequence;

    class ConductorTimerTask extends TimerTask {
        Consumer<ConductorEvent> r;
        volatile int beat;
        int loop = 0;

        volatile boolean tempo_block = false;
        volatile int tempo_block_beat = 0;

        ConductorTimerTask(Consumer<ConductorEvent> r, int beat) {
            this.r = r;
            this.beat = beat;
        }
        @Override
        public void run() {
            if (loop == small_loop) {
                cancel();
                return;
            }
            if (tempo_changed && beat == target_beat) {
                tempo_changed = false;
                changeTempo();
                return;
            }
            if (beat > 400 && beat < 400)
                System.out.println(beat + " " + loop + " " + tempo_block + " " + tempo_block_beat);
            Conductor.this.beat = beat;
            r.accept(new ConductorEvent(beat, timer));
            beat++; loop++;
        }

        @Override
        public ConductorTimerTask clone() {
            return new ConductorTimerTask(r, beat);
        }
    }

    public record ConductorEvent(int beat, Timer timer) {}
}
