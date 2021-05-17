package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.displayable.PBSign;

import java.util.Collection;
import java.util.HashMap;

public class SignManager extends PBManager {

    private final HashMap<String, PBSign> SIGNS = new HashMap<>();

    public SignManager(ProBending plugin) {
        super(plugin);
    }

    public void addSign(PBSign sign) {
        SIGNS.put(sign.getID(), sign);
    }

    public PBSign getSign(String ID) {
        return SIGNS.getOrDefault(ID, null);
    }

    public Collection<PBSign> getSigns() {
        return SIGNS.values();
    }
}
