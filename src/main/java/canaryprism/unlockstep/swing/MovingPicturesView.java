package canaryprism.unlockstep.swing;

import java.awt.Image;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.JComponent;

import canaryprism.unlockstep.Conductor;

public class MovingPicturesView extends JComponent {
    private volatile List<Image> images;
    private volatile int index = 0;

    private volatile Conductor conductor;
    private final long fps;

    private final Object lock = new Object();

    public MovingPicturesView(long fps) {
        this.fps = fps;
    }

    public void setVideo(List<Image> images) {
        this.images = images;
        this.index = 0;
    }

    private volatile CompletableFuture<Void> future;

    public CompletableFuture<Void> start() {
        if (conductor != null) {
            return CompletableFuture.completedFuture(null);
        }

        conductor = new Conductor(1000, fps);
        conductor.submit((e) -> {
            synchronized (lock) {
                if (images == null || index + 1 >= images.size()) {
                    conductor.stop();
                    this.conductor = null;
                    future.complete(null);
                    return;
                }
                index++;
                repaint();
            }
        });
        conductor.start(0);

        future = new CompletableFuture<>();
        return future;
    }

    public enum FitMode {
        fit, fill, stretch
    }

    private volatile FitMode fitMode = FitMode.fit;

    public void setFitMode(FitMode fitMode) {
        this.fitMode = fitMode;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        synchronized (lock) {
            if (images == null || index >= images.size()) {
                return;
            }
            var image = images.get(index);
            int x, y, w, h;
            switch (fitMode) {
                case fit -> {
                    double scale = Math.min((double)getWidth() / image.getWidth(null), (double)getHeight() / image.getHeight(null));
                    w = (int)Math.round(image.getWidth(null) * scale);
                    h = (int)Math.round(image.getHeight(null) * scale);
                    x = (getWidth() - w) / 2;
                    y = (getHeight() - h) / 2;
                }
                case fill -> {
                    double scale = Math.max((double)getWidth() / image.getWidth(null), (double)getHeight() / image.getHeight(null));
                    w = (int)Math.round(image.getWidth(null) * scale);
                    h = (int)Math.round(image.getHeight(null) * scale);
                    x = (getWidth() - w) / 2;
                    y = (getHeight() - h) / 2;
                }
                case stretch -> {
                    x = 0;
                    y = 0;
                    w = getWidth();
                    h = getHeight();
                }
                case null -> throw new IllegalStateException("Unexpected value: " + fitMode);
                default -> throw new IllegalStateException("Unexpected value: " + fitMode);
            }
            image.getHeight(null);
            g.drawImage(images.get(index), x, y, w, h, null);
        }
    }
}
