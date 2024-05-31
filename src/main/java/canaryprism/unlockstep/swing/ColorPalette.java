package canaryprism.unlockstep.swing;

import java.awt.Color;

@FunctionalInterface
public interface ColorPalette {

    public static final ColorPalette lockstep1 = (e) -> switch (e) {
        case onbeat -> new Color(0xf84898);
        case offbeat -> new Color(0xe858a0);
    };

    public static final ColorPalette lockstep2 = (e) -> switch (e) {
        case onbeat -> new Color(0x30a8e0);
        case offbeat -> new Color(0x1090c8);
    };

    public static final ColorPalette remix6 = (e) -> switch (e) {
        case onbeat -> new Color(0x88f820);
        case offbeat -> new Color(0x60c008);
    };


    Color getColor(ColorSequence sequence);
}
