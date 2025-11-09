package net.popzi.utils;

import org.bukkit.Location;

public class Boundary {

    private final Location location;
    private final int radius;

    /**
     * Constructor
     * @param origin location to calculate the boundary from
     * @param radius of the boundary in blocks
     */
    public Boundary(Location origin, int radius) {
        this.location = origin;
        this.radius = radius;
    }


    /**
     * Gets the maximum XYZ position for the boundary
     * @return Location of that position
     */
    public Location getMax() {
        return new Location(
                this.location.getWorld(),
                this.location.getBlockX() + this.radius,
                this.location.getBlockY() + this.radius,
                this.location.getBlockZ() + this.radius
        );
    }

    /**
     * Gets the minimum XYZ position for the boundary
     * @return Location of that position
     */
    public Location getMin() {
        return new Location(
                this.location.getWorld(),
                this.location.getBlockX() - this.radius,
                this.location.getBlockY() - this.radius,
                this.location.getBlockZ() - this.radius
        );
    }

    /**
     * Checks if the provided point location is inside our boundary
     * @param point to check if is inside the boundary
     * @return boolean if inside the boundary
     */
    public boolean isInside(Location point) {
        if (point == null || point.getWorld() == null) return false;
        if (!point.getWorld().equals(this.location.getWorld())) return false;

        Location min = getMin();
        Location max = getMax();

        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();

        return
                x >= min.getX() && x <= max.getX()
                && y >= min.getY() && y <= max.getY()
                && z >= min.getZ() && z <= max.getZ();
    }

    /**
     * Gets a random block inside the boundary
     * @return Location of a random block in the boundary
     */
    public Location getRandomBlockInBoundary() {
        if (this.location.getWorld() == null) return null;

        Location min = getMin();
        Location max = getMax();

        double x = min.getX() + Math.random() * (max.getX() - min.getX());
        double y = min.getY() + Math.random() * (max.getY() - min.getY());
        double z = min.getZ() + Math.random() * (max.getZ() - min.getZ());

        return new Location(this.location.getWorld(), x, y, z);
    }

}


