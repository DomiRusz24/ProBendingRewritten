package com.probending.probending.core.enums;

public enum Ring {

    // Actual rings
    TIEBREAKER(true),
    FIRST(true),
    SECOND(true),
    THIRD(true),

    // Other
    LINE(false),
    OUTSIDE(false),
    BACK(false);

    private boolean teleport;

    Ring(boolean teleport) {
        this.teleport = teleport;
    }

    public boolean doesItNeedATeleport() {
        return teleport;
    }
}
