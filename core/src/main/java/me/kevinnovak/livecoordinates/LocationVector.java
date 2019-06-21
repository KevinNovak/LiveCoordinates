package me.kevinnovak.livecoordinates;

import org.bukkit.Location;

public class LocationVector {
    private int _x, _y, _z;

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public int getZ() {
        return _z;
    }

    public LocationVector(Location location) {
        _x = location.getBlockX();
        _y = location.getBlockY();
        _z = location.getBlockZ();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof LocationVector)) {
            return false;
        }

        LocationVector locationVector = (LocationVector) obj;
        return (_x == locationVector.getX())
                && (_y == locationVector.getY())
                && (_z == locationVector.getZ());
    }
}
