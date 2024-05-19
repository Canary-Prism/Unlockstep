package canaryprism.unlockstep.sequence;

public sealed interface CollapsedRhythm permits CollapsedRhythm.On, CollapsedRhythm.Off, CollapsedRhythm.Rest, CollapsedRhythm.Tap {
    public record On(int beats) implements CollapsedRhythm {
    }
    public record Off(int beats) implements CollapsedRhythm {
    }
    public record Rest(int beats) implements CollapsedRhythm {
    }
    public record Tap(int beats) implements CollapsedRhythm {
    }
}
