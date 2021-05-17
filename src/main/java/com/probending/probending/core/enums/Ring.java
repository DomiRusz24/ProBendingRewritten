package com.probending.probending.core.enums;

public enum Ring {
    // Blue
    BLUE_BACK(false, 7, 1),
    BLUE_THIRD(true, 6, 5),
    BLUE_SECOND(true, 5, 9),
    BLUE_FIRST(true, 4, 3),
    BLUE_TIEBREAKER(true, 9, 11),


    // Red
    RED_TIEBREAKER(true, 8, 14),
    RED_FIRST(true, 3, 2),
    RED_SECOND(true, 2, 6),
    RED_THIRD(true, 1, 10),
    RED_BACK(false, 0, 12),

    // Other
    LINE(false, 15),
    OUTSIDE(false, 0),
    OFF_WOOL(false, 100);

    // ---

    private static final Ring[] BLUE_RINGS = {BLUE_BACK, BLUE_THIRD, BLUE_SECOND, BLUE_FIRST, BLUE_TIEBREAKER};

    private static final Ring[] RED_RINGS = {RED_BACK, RED_THIRD, RED_SECOND, RED_FIRST, RED_TIEBREAKER};

    private static final Ring[] OTHER_RINGS = {RED_BACK, RED_THIRD, RED_SECOND, RED_FIRST, RED_TIEBREAKER};


    public static Ring fromIndex(int index) {
        for (Ring ring : Ring.values()) {
            if (ring.getIndex() != -1) {
                if (ring.getIndex() == index) {
                    return ring;
                }
            }
        }
        return null;
    }

    public static Ring fromID(int id) {
        for (Ring ring : Ring.values()) {
            if (ring.getID() == id) {
                    return ring;
            }
        }
        return null;
    }

    public static Ring fromName(String name) {
        for (Ring ring : Ring.values()) {
            if (ring.name().equalsIgnoreCase(name)) {
                return ring;
            }
        }
        return null;
    }

    // ---

    private boolean teleport;

    private int index = -1;

    private byte id;

    // ---

    Ring(boolean teleport, int index, int id) {
        this.teleport = teleport;
        this.index = index;
        this.id = (byte)  id;
    }

    Ring(boolean teleport,  int id) {
        this.teleport = teleport;
        this.id = (byte) id;
    }

    // ---

    public boolean isTeleportRing() {
        return teleport;
    }

    // --- Indexes ---

    public int getIndex() {
        return index;
    }

    public byte getID() {
        return id;
    }

    // ---

    public TeamTag getTeam() {
        return getIndex() == -1 ? null : getIndex() > 4 ? TeamTag.BLUE : TeamTag.RED;
    }

    // ---

    public int offset(TeamTag team, Ring ring) {
        if (ring.getIndex() != -1) {
            if (team == TeamTag.RED) {
                return (getIndex() - ring.getIndex()) * -1;
            } else {
                return (getIndex() - ring.getIndex());
            }
        }
        return 0;
    }

    public Ring getFromOffset(TeamTag team, int offset) {
        int id = getIndex();
        if (team == TeamTag.BLUE) {
            offset*= -1;
        }
        return Ring.fromIndex(id + offset);
    }

    // ---


    public static Ring[] getBlueRings() {
        return BLUE_RINGS;
    }

    public static Ring[] getRedRings() {
        return RED_RINGS;
    }

    public static Ring[] getOtherRings() {
        return OTHER_RINGS;
    }
}
