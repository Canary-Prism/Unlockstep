package canaryprism.unlockstep.swing;

import java.util.ArrayList;
import java.util.List;

public enum Animation {
    readytap(
        new CollapsedAnimationFrame(StepswitcherPose.tap3, 8),
        new CollapsedAnimationFrame(StepswitcherPose.tap2, 2),
        new CollapsedAnimationFrame(StepswitcherPose.tap1, 2),
        new CollapsedAnimationFrame(StepswitcherPose.tap0, 1)
    ), 
    tap(
        new CollapsedAnimationFrame(StepswitcherPose.tap3, 6),
        new CollapsedAnimationFrame(StepswitcherPose.tap2, 2),
        new CollapsedAnimationFrame(StepswitcherPose.tap1, 3),
        new CollapsedAnimationFrame(StepswitcherPose.tap0, 1)
    ), 
    hitleft(
        new CollapsedAnimationFrame(StepswitcherPose.left_extend, 2),
        new CollapsedAnimationFrame(StepswitcherPose.left_pose, 7),
        new CollapsedAnimationFrame(StepswitcherPose.left_return0, 1),
        new CollapsedAnimationFrame(StepswitcherPose.left_return1, 1),
        new CollapsedAnimationFrame(StepswitcherPose.idle, 1)
    ), 
    hitright(
        new CollapsedAnimationFrame(StepswitcherPose.right_extend, 2),
        new CollapsedAnimationFrame(StepswitcherPose.right_pose, 7),
        new CollapsedAnimationFrame(StepswitcherPose.right_return0, 1),
        new CollapsedAnimationFrame(StepswitcherPose.right_return1, 1),
        new CollapsedAnimationFrame(StepswitcherPose.idle, 1)
    ), 
    missleft(
        new CollapsedAnimationFrame(StepswitcherPose.left_miss_hit, 4),
        new CollapsedAnimationFrame(StepswitcherPose.left_miss_pose, 1)
    ), 
    missright(
        new CollapsedAnimationFrame(StepswitcherPose.right_miss_hit, 4),
        new CollapsedAnimationFrame(StepswitcherPose.right_miss_pose, 1)
    );

    public final List<StepswitcherPose> frames;

    Animation(CollapsedAnimationFrame... collapsed_frames) {
        this.frames = expandToFrames(collapsed_frames).stream().toList();
    }

    private static List<StepswitcherPose> expandToFrames(CollapsedAnimationFrame... collapsed_frames) {
        var expanded = new ArrayList<StepswitcherPose>();
        for (var frame : collapsed_frames) {
            for (int i = 0; i < frame.duration; i++) {
                expanded.add(frame.pose);
            }
        }
        return expanded;
    }

    record CollapsedAnimationFrame(StepswitcherPose pose, int duration) {}
}
