package me.imillusion.drawmything.utils;

import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.canvas.Point;
import org.bukkit.Location;

public final class PointConverter {

    private PointConverter() {
        //Avoid initializing in utility class
    }

    /**
     * Converts a 2D point into a 3D location based on the top left and bottom right 3D locations
     *
     * @param point       - The point to convert
     * @param topLeft     - The top left 3D location
     * @param bottomRight - the bottom right 3D location
     * @return - The converted 3D location
     */
    public static Location adaptLocation(Point point, Location topLeft, Location bottomRight) {
        boolean southNorth = topLeft.getBlockZ() == bottomRight.getBlockZ();

        int adaptedY = bottomRight.getBlockY() + point.getY();

        int base = southNorth ? topLeft.getBlockX() : topLeft.getBlockZ();
        int compare = southNorth ? bottomRight.getBlockX() : bottomRight.getBlockZ();

        int adaptedX = base + (base < compare ? point.getX() : -point.getX());

        Location loc = topLeft.clone();

        if (southNorth)
            loc.setX(adaptedX);
        else
            loc.setZ(adaptedX);

        loc.setY(adaptedY);
        return loc;
    }

    public static Location adaptLocation(Point point, Canvas canvas) {
        return adaptLocation(point, canvas.getArena().getMap().getTopLeft(), canvas.getArena().getMap().getBottomRight());
    }

    public static Point adaptPoint(Location location, Canvas canvas) {
        Location topLeft = canvas.getArena().getMap().getTopLeft();
        Location bottomRight = canvas.getArena().getMap().getBottomRight();

        if (!locationBelongs(location, topLeft, bottomRight))
            return null;

        boolean southNorth = topLeft.getBlockZ() == bottomRight.getBlockZ();

        int x = southNorth ? location.getBlockX() - topLeft.getBlockX() : location.getBlockZ() - topLeft.getBlockZ();
        int y = location.getBlockY() - bottomRight.getBlockY();

        for (Point point : canvas.getPoints())
            if (point.getX() == x && point.getY() == y)
                return point;

        return null;
    }

    /**
     * Checks if a 2D point belongs within 2x 3D locations
     *
     * @param point       - The 2D point to check
     * @param topLeft     - The top left 3D location
     * @param bottomRight - the bottom right 3D location
     * @return TRUE if it belongs, FALSE otherwise
     */
    public static boolean pointBelongs(Point point, Location topLeft, Location bottomRight) {
        boolean southNorth = topLeft.getBlockZ() == bottomRight.getBlockZ();

        int adaptedY = bottomRight.getBlockY() + point.getY();

        if (adaptedY > topLeft.getBlockY() || adaptedY < bottomRight.getBlockY())
            return false;

        int top = southNorth ? topLeft.getBlockX() : topLeft.getBlockZ();
        int bottom = southNorth ? bottomRight.getBlockX() : bottomRight.getBlockZ();

        return point.getX() + Math.min(top, bottom) <= Math.max(top, bottom);
    }

    public static boolean locationBelongs(Location location, Canvas canvas) {
        return locationBelongs(location, canvas.getArena().getMap().getTopLeft(), canvas.getArena().getMap().getBottomRight());
    }

    public static boolean locationBelongs(Location location, Location topLeft, Location bottomRight) {

        return !(location.getBlockY() > topLeft.getBlockY() || location.getBlockY() < bottomRight.getBlockY() || //compare y
                location.getBlockX() < topLeft.getBlockX() || location.getBlockX() > bottomRight.getBlockX() || //compare x
                location.getBlockZ() < topLeft.getBlockZ() || location.getBlockZ() > bottomRight.getBlockZ()); //compare z
    }

    public static Location getTopLeftExtreme(Location pointOne, Location pointTwo) {
        boolean southNorth = pointOne.getBlockZ() == pointTwo.getBlockZ();

        return new Location(pointOne.getWorld(),
                southNorth ? Math.min(pointOne.getBlockX(), pointTwo.getX()) : pointOne.getBlockX(),
                Math.max(pointOne.getBlockY(), pointTwo.getBlockY()),
                southNorth ? pointOne.getBlockZ() : Math.min(pointOne.getBlockZ(), pointTwo.getBlockZ())
        );
    }

    public static Location getBottomRightExtreme(Location pointOne, Location pointTwo) {
        return getTopLeftExtreme(pointOne, pointTwo).equals(pointOne) ? pointTwo : pointOne;
    }
}
