package canaryprism.unlockstep.swing;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import canaryprism.unlockstep.Lockstep;

public record Indicator(Image[] frames, Image fail) {
    public static Indicator load(String path) {
        try {

            var count = Lockstep.getResource(path + "/count").read();

            var frames = new Image[count];
            for (int i = 0; i < count; i++) {
                frames[i] = ImageIO.read(Lockstep.getResource(path + "/" + i + ".png"));
            }
            var fail = ImageIO.read(Lockstep.getResource(path + "/fail.png"));
            return new Indicator(frames, fail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
