package canaryprism.unlockstep;

import javax.swing.JFrame;

import canaryprism.unlockstep.swing.ColorPalette;

public class Main {

    public static void main(String[] args) {

        var lockstep = new Lockstep(
            new JFrame("Lockstep"), 
            "/unlockstep_assets/music/lockstep1.wav", 
            "/unlockstep_assets/audio", 
            "/unlockstep_assets/sprites/lockstep1",
            ColorPalette.lockstep1
        );

        lockstep.start().join();
        
    }
}
