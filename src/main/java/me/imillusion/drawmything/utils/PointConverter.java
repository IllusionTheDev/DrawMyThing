package me.imillusion.drawmything.utils;

import me.imillusion.drawmything.game.canvas.Point;
import org.bukkit.Location;

public class PointConverter {

    /**
     * Converts a 2D point into a 3D location based on the top left and bottom right 3D locations
     *
     * @param point       - The point to convert
     * @param topLeft     - The top left 3D location
     * @param bottomRight - the bottom right 3D location
     * @return - The converted 3D location
     */
    public static Location adaptLocation(Point point, Location topLeft, Location bottomRight)
    {
        boolean southNorth = topLeft.getBlockZ() == bottomRight.getBlockZ();

        int adaptedY = bottomRight.getBlockY() + point.getY();
        int adaptedX;

        int base = southNorth ? topLeft.getBlockX() : topLeft.getBlockZ();
        int compare = southNorth ? bottomRight.getBlockX() : bottomRight.getBlockZ();

        adaptedX = base + (base < compare ? point.getX() : -point.getX());

        return topLeft.clone()
                .add(southNorth ? adaptedX : 0,
                        topLeft.getBlockY() - bottomRight.getBlockY() + adaptedY,
                        !southNorth ? adaptedX : 0);
    }

    /**
     * Checks if a 2D point belongs within 2x 3D locations
     *
     * @param point       - The 2D point to check
     * @param topLeft     - The top left 3D location
     * @param bottomRight - the bottom right 3D location
     * @return TRUE if it belongs, FALSE otherwise
     */
    public boolean pointBelongs(Point point, Location topLeft, Location bottomRight)
    {
        boolean southNorth = topLeft.getBlockZ() == bottomRight.getBlockZ();

        int adaptedY = bottomRight.getBlockY() + point.getY();

        if (adaptedY > topLeft.getBlockY() || adaptedY < bottomRight.getBlockY())
            return false;

        if (southNorth) //if it is either north or south
        {
            if (topLeft.getBlockX() < bottomRight.getBlockX()) //if it's north
                return point.getX() + topLeft.getBlockX() < bottomRight.getBlockX();
            return point.getX() + bottomRight.getBlockX() < topLeft.getBlockX();
        }

        if (topLeft.getBlockZ() < bottomRight.getBlockZ())
            return point.getX() + topLeft.getBlockZ() < bottomRight.getBlockZ();
        return point.getX() + bottomRight.getBlockZ() < topLeft.getBlockZ();

    }

    public Location getTopLeftExtreme(Location pointOne, Location pointTwo)
    {
        boolean southNorth = pointOne.getBlockZ() == pointTwo.getBlockZ();

        return new Location(pointOne.getWorld(),
                southNorth ? Math.min(pointOne.getBlockX(), pointTwo.getX()) : 0,
                Math.max(pointOne.getBlockY(), pointTwo.getBlockY()),
                !southNorth ? Math.min(pointOne.getBlockZ(), pointTwo.getBlockZ()) : 0
        );
    }
}
