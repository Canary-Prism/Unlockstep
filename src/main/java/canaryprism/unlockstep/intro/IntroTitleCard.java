package canaryprism.unlockstep.intro;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl.Type;
import javax.swing.JComponent;
import javax.swing.JFrame;
import canaryprism.unlockstep.Lockstep;
import canaryprism.unlockstep.swing.MovingPicturesView;
import canaryprism.unlockstep.swing.MovingPicturesView.FitMode;

public class IntroTitleCard {

    private Clip music;
    private Image background_image;
    private List<Image> title_frames;

    private MovingPicturesView title_view;

    public IntroTitleCard(String path, JFrame frame, float volume) {

        try {
            music = AudioSystem.getClip();
            music.open(AudioSystem.getAudioInputStream(Lockstep.getResource(STR."\{path}/music.wav")));

            var control = (FloatControl)music.getControl(Type.MASTER_GAIN);
            control.setValue(20f * (float)Math.log10(volume));

            music.addLineListener((e) -> {
                if (e.getType() == LineEvent.Type.STOP && future != null) {
                    future.complete(null);
                }
            });
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            background_image = ImageIO.read(Lockstep.getResource(STR."\{path}/introbackground.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            int count = Lockstep.getResource(STR."\{path}/title/count").read();

            title_frames = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                title_frames.add(ImageIO.read(Lockstep.getResource(STR."\{path}/title/title\{i}.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        title_view = new MovingPicturesView(20);

        title_view.setVideo(title_frames);

        var content = new JComponent() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                var scale = Math.max((double)getWidth() / background_image.getWidth(null), (double)getHeight() / background_image.getHeight(null));
                var x = (int)Math.round((getWidth() - background_image.getWidth(null) * scale) / 2);
                var y = (int)Math.round((getHeight() - background_image.getHeight(null) * scale) / 2);
                var w = (int)Math.round(background_image.getWidth(null) * scale);
                var h = (int)Math.round(background_image.getHeight(null) * scale);
                
                g.drawImage(background_image, x, y, w, h, null);
            }
        };

        frame.getContentPane().add(content, BorderLayout.CENTER);

        content.setLayout(new BorderLayout());

        title_view.setFitMode(FitMode.fit);

        content.add(title_view);
    }

    private volatile CompletableFuture<Void> future;

    public CompletableFuture<Void> start() {
        if (future != null) {
            return future;
        }

        var timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                music.start();
            }
        }, 100);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                title_view.start();
            }
        }, 200);

        future = new CompletableFuture<>();

        return future;
    }
}
