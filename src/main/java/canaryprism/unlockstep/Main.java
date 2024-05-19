package canaryprism.unlockstep;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        var lockstep = new Lockstep(
            new JFrame("Lockstep"), 
            "/unlockstep_assets/music/lockstep1.wav", 
            "/unlockstep_assets/audio", 
            "/unlockstep_assets/sprites/lockstep1");

        lockstep.start().join();
        
    }
}
