package canaryprism.unlockstep;

public class Main {

    public static void main(String[] args) {

        var lockstep = new Lockstep("/unlockstep_assets/lockstep1");

        lockstep.start().join();
        
    }
}
