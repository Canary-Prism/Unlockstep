package canaryprism.unlockstep.launcher;

public enum GameMode {
    lockstep1("Lockstep 1"),
    lockstep2("Lockstep 2"),
    remix6("Remix 6"),
    remix8("Remix 8"),
    remix9("Remix 9"),
    remix1("Remix 1"),
    endless_remix("Endless Remix"),
    wip1("mewo");

    public final String name;

    GameMode(String name) {
        this.name = name;
    }
}
