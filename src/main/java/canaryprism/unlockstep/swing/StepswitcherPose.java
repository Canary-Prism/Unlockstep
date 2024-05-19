package canaryprism.unlockstep.swing;

public enum StepswitcherPose {
    idle, 
    left_extend, left_pose, left_return0, left_return1, left_miss_hit, left_miss_pose,
    right_extend, right_pose, right_return0, right_return1, right_miss_hit, right_miss_pose,
    tap0, tap1, tap2, tap3;

    public int spriteOffset(ZoomSize zoom_size) {
        return switch (zoom_size) {
            case l0 -> switch (this) {
                case idle -> 15;

                case left_pose, left_extend -> 37;
                case right_pose, right_extend -> 9;

                case left_miss_pose, left_miss_hit -> 31;
                case right_miss_pose, right_miss_hit -> 15;
                
                case left_return0 -> 18;
                case right_return0 -> 12;
                
                case right_return1, left_return1 -> 15;

                case tap0, tap1, tap2, tap3 -> 15;
            };
            case l1 -> switch (this) {
                case idle -> 11;

                case left_pose, left_extend -> 29;
                case right_pose, right_extend -> 9;

                case left_miss_pose, left_miss_hit -> 21;
                case right_miss_pose, right_miss_hit -> 10;

                case left_return0 -> 24;
                case right_return0 -> 10;

                case left_return1 -> 21;
                case right_return1 -> 7;

                case tap0, tap1, tap2, tap3 -> 0;
            };
            case l2 -> switch (this) {
                case idle -> 7;

                case left_pose, left_extend -> 22;
                case right_pose, right_extend -> 8;

                case left_miss_pose, left_miss_hit -> 15;
                case right_miss_pose, right_miss_hit -> 7;

                case left_return0 -> 7;
                case right_return0 -> 7;

                case left_return1 -> 7;
                case right_return1 -> 7;

                case tap0, tap1, tap2, tap3 -> 0;
            };
            case l3 -> switch (this) {
                case idle -> 7;

                case left_pose, left_extend -> 13;
                case right_pose, right_extend -> 1;

                case left_miss_pose, left_miss_hit -> 11;
                case right_miss_pose -> 3;
                case right_miss_hit -> 6;

                case left_return0 -> 6;
                case right_return0 -> 7;

                case left_return1 -> 6;
                case right_return1 -> 7;

                case tap0, tap1, tap2, tap3 -> 0;
            };
            case l4 -> switch (this) {
                case idle -> 3;

                case left_pose, left_extend -> 9;
                case right_pose, right_extend -> 5;

                case left_miss_pose, left_miss_hit -> 4;
                case right_miss_pose -> 2;
                case right_miss_hit -> 7;

                case left_return0 -> 6;
                case right_return0 -> 1;

                case left_return1 -> 6;
                case right_return1 -> 1;

                case tap0, tap1, tap2, tap3 -> 0;
            };
            
            // this is just to make the compiler happy
            // without this the compiler crashes for some reason
            default -> -1;
        };
    }
}
