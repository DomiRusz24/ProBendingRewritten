package com.probending.probending.core.displayable;

import com.probending.probending.ProBending;
import com.probending.probending.core.displayable.Displayable;
import com.probending.probending.core.interfaces.PlaceholderObject;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;

public class PBHologram extends Displayable {

    private final String identification;

    private Location location;

    private boolean visible = true;

    private ArmorStand armorStand;

    public PBHologram(String identification, PlaceholderObject... objects) {
        super(objects);
        this.identification = identification;
        ProBending.hologramM.addHologram(this);
    }


    @Override
    protected void onUpdate(ArrayList<String> values) {
        if (isVisible()) {
            StringBuilder s = new StringBuilder();
            for (String value : values) {
                s.append(value).append('\n');
            }
            s.deleteCharAt(s.length());
            armorStand.setCustomName(s.toString());
        }
    }

    private void createArmorStand(Location location) {
        armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setInvulnerable(true);
        armorStand.setBasePlate(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
    }

    public void teleport(Location location) {
        location.clone().add(0, -0.5, 0);
        this.location = location;
        if (isVisible()) {
            if (armorStand == null) {
                createArmorStand(location);
            } else {
                armorStand.teleport(location);
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (isVisible() && location != null) {
            teleport(location);
        } else if (armorStand != null) {
            armorStand.remove();
            armorStand = null;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public String getID() {
        return identification;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }
}
