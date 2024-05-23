package canaryprism.unlockstep.calibration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import canaryprism.unlockstep.AudioPlayer;
import canaryprism.unlockstep.Conductor;
import canaryprism.unlockstep.Lockstep;

public class LagCalibration {

    private final AudioCalibration audio_calibration;

    private final JPanel panel;

    private final AudioPlayer cowbell;
    private final AudioPlayer music;

    public LagCalibration(JFrame frame, String audio_path, String music_path) {
        this.audio_calibration = new AudioCalibration();
        
        try {
            this.music = new AudioPlayer(Lockstep.getResource(music_path), 2);
            music.setVolume(.5f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.panel = new JPanel();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        var info_label = new JLabel("""
                <html>
                <h1>Audio Calibration</h1>
                <p>Click the mouse or press space when you hear the cowbell</p>
                <p>if the music is off, or the cowbells lagged at the start, wait till the music reloops</p>
                </html>
                """);

        info_label.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(info_label);

        panel.add(audio_calibration);

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                panel.requestFocus();
                audio_calibration.playerInput();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
        });

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    audio_calibration.playerInput();
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
            }
        });

        frame.getContentPane().add(panel);

        this.cowbell = new AudioPlayer(Lockstep.getResource(audio_path + "/sfx/cowbell.wav"), 10);
    }
    
    private volatile CompletableFuture<Long> future;

    public CompletableFuture<Long> start() {
        audio_calibration.start();
        panel.requestFocus();

        this.future = new CompletableFuture<>();
        return future;
    }

    class AudioCalibration extends JComponent {
        private final Conductor frame_conductor;
        private final Conductor audio_conductor;
        private final Conductor music_conductor;
        public AudioCalibration() {
            this.frame_conductor = new Conductor(1000, 60);

            frame_conductor.submit((e) -> {
                advanceFrame();
                repaint();
            });

            this.audio_conductor = new Conductor(60_000, 125);

            audio_conductor.submit((e) -> {
                cowbell.play();
                last_cowbell = System.currentTimeMillis();
                x_index = 0;
            });
            audio_conductor.submit((e) -> {
                verifyRecords();
            });

            this.music_conductor = new Conductor(60_000 * 64, 125 * 2);

            music_conductor.submit((e) -> {
                if (first) {
                    first = false;
                    return;
                }
                music.play();
            });
        }


        private volatile boolean first = false;

        public void start() {
            first = true;
            music.play();
            music_conductor.start(0);
            
            frame_conductor.start(140);
            audio_conductor.start(140);
        }

        private volatile int remaining = 30;

        private volatile long last_cowbell;

        private volatile int x_index = 0;

        private int xpos(int x) {
            return x * getWidth() / 30;
        }

        private void advanceFrame() {
            x_index += 1;

            if (linger_timer > 0) {
                linger_timer--;
            }

        }

        private void verifyRecords() {
            synchronized (click_records) {
                if (click_records.isEmpty()) {
                    return;
                }
                var std_dev = getStandardDeviation();
                if (std_dev <= 30 && remaining == 0) {
                    stop();
                    future.complete(Math.round(getMean()));
                }
            }
        }

        private double getMean() {
            synchronized (click_records) {
                double sum = 0.0;
                for (ClickRecord record : click_records) {
                    sum += record.millis_offset;
                }
                return sum / click_records.size();
            }
        }

        private double getStandardDeviation() {

            synchronized (click_records) {
                // Calculate the mean
                double mean = getMean();
    
                // Calculate the sum of squared differences from the mean
                double squaredDiffSum = 0.0;
                for (ClickRecord record : click_records) {
                    double diff = record.millis_offset - mean;
                    squaredDiffSum += diff * diff;
                }
    
                // Calculate the variance and return the square root as the standard deviation
                double variance = squaredDiffSum / (click_records.size() - 1);
                return Math.sqrt(variance);
            }

        }

        record ClickRecord(int x_index, long millis_offset) {}

        private ArrayList<ClickRecord> click_records = new ArrayList<>();

        private volatile int linger_timer = 0;
        private volatile int linger_index = 0;

        public void playerInput() {
            var new_record = new ClickRecord(x_index, System.currentTimeMillis() - last_cowbell);
            if (new_record.millis_offset > 400) {
                return;
            }
            synchronized (click_records) {
                click_records.add(new_record);
            }

            linger_timer = 10;
            linger_index = x_index;

            if (remaining > 0) {
                remaining--;
            }
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            var x = xpos(x_index);

            g.setColor(new Color(100, 100, 255, 30));

            for (var record : click_records) {
                var x2 = xpos(record.x_index);
                g.fillRect(x2 - 10, 0, 20, 40);
            }

            if (linger_timer > 0) {
                var x2 = xpos(linger_index);
                g.setColor(new Color(100, 100, 255));
                g.fillRect(x2 - 10, 0, 20, 40);
            } else {
                g.setColor(getForeground());
                g.fillRect(x - 10, 0, 20, 40);
            } 
        }

        public void stop() {
            frame_conductor.stop();
            audio_conductor.stop();
            music_conductor.stop();
            music.stop();
        }

        @Override
        public Dimension getSize() {
            return new Dimension(640, 40);
        }

        @Override
        public Dimension getPreferredSize() {
            return getSize();
        }
    }
}
