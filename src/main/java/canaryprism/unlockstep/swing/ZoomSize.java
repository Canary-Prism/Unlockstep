package canaryprism.unlockstep.swing;

public enum ZoomSize {
    l0, l1, l2, l3, l4;

    public int startX() {
        return switch (this) {
            case l0 -> 9 - 23;
            case l1 -> 4;
            case l2 -> 0;
            case l3 -> -1;
            case l4 -> -1;
            default -> -24;
        };
    }

    public int startY() {
        return switch (this) {
            case l0 -> 31;
            case l1 -> 23;
            case l2 -> 21;
            case l3 -> 3;
            case l4 -> 7;
            default -> 31;
        };
    }

    public int distX() {
        return switch (this) {
            case l0 -> 45;
            case l1 -> 32;
            case l2 -> 22;
            case l3 -> 14;
            case l4 -> 8;
            default -> 45;
        };
    }

    public int offsetX() {
        return switch (this) {
            case l0 -> 23;
            case l1 -> 16;
            case l2 -> 11;
            case l3 -> 7;
            case l4 -> 4;
            default -> 23;
        };
    }

    public boolean startsOffset() {
        return switch (this) {
            case l0 -> false;
            case l1 -> false;
            case l2 -> true;
            case l3 -> true;
            case l4 -> true;
            default -> true;
        };
    }

    public int distY() {
        return switch (this) {
            case l0 -> 48;
            case l1 -> 36;
            case l2 -> 23;
            case l3 -> 14;
            case l4 -> 8;
            default -> 48;
        };
    }

    public int countX() {
        return switch (this) {
            case l0 -> 6;
            case l1 -> 7;
            case l2 -> 9;
            case l3 -> 15;
            case l4 -> 26;
            default -> 6;
        };
    }

    public int countY() {
        return switch (this) {
            case l0 -> 6;
            case l1 -> 8;
            case l2 -> 12;
            case l3 -> 20;
            case l4 -> 32;
            default -> 6;
        };
    }

    public int playerX() {
        return switch (this) {
            case l0 -> 2;
            case l1 -> 3;
            case l2 -> 4;
            case l3 -> 7;
            case l4 -> 13;
            default -> 0;
        };
    }

    public int playerY() {
        return switch (this) {
            case l0 -> 3;
            case l1 -> 4;
            case l2 -> 6;
            case l3 -> 11;
            case l4 -> 17;
            default -> 0;
        };
    }
}
