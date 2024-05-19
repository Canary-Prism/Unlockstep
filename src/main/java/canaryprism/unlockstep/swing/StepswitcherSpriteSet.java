package canaryprism.unlockstep.swing;

import java.util.HashMap;
import javax.imageio.ImageIO;

import canaryprism.unlockstep.Lockstep;

import java.awt.Image;
import java.io.IOException;

public class StepswitcherSpriteSet {
    private final ZoomSize size;
    private final HashMap<StepswitcherPose, Image> sprites = new HashMap<>();
    public StepswitcherSpriteSet(ZoomSize size, String path) {
        this.size = size;
        for (var pose : StepswitcherPose.values()) {
            try {
                sprites.put(pose, ImageIO.read(Lockstep.getResource(STR."\{path}/stepswitcher_\{pose.name()}.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Image getSprite(StepswitcherPose pose) {
        return sprites.get(pose);
    }

    public ZoomSize getSize() {
        return size;
    }
}
