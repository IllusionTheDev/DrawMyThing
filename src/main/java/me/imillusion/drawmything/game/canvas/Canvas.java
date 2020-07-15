package me.imillusion.drawmything.game.canvas;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import lombok.Getter;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.arena.ArenaMap;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Canvas {

    private Location topLeft;
    private Location bottomRight;
    @Getter
    private Arena arena;

    private List<Point> points = new ArrayList<>();
    private Map<Point, List<Location>> sortedPoints = new HashMap<>();

    public Canvas(ArenaMap map, Arena arena)
    {
        this.topLeft = map.getTopLeft();
        this.bottomRight = map.getBottomRight();

        this.arena = arena;

        for (int x = topLeft.getBlockX(); x <= bottomRight.getBlockX(); x++)
            for (int y = bottomRight.getBlockY(); y <= topLeft.getBlockY(); y++)
                points.add(new Point(x - topLeft.getBlockX(), y - bottomRight.getBlockY(), DyeColor.WHITE));
    }

    /**
     * Clears the canvas by painting it white
     */
    public void clear()
    {
        fill(DyeColor.WHITE);
    }

    /**
     * Draws a pixel in the canvas
     *
     * @param point - The canvas point that gets colored
     * @param color - The color to draw with
     */
    public void drawPizel(Point point, DyeColor color)
    {
        Location loc = adaptLocation(point);

        if (!belongs(loc))
            return;

        Chunk chunk = loc.getChunk();

        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);

        ChunkCoordIntPair chunkcoords = new ChunkCoordIntPair(chunk.getX(), chunk.getZ());
        MultiBlockChangeInfo[] change = new MultiBlockChangeInfo[1];

        change[0] = new MultiBlockChangeInfo(loc, WrappedBlockData.createData(Material.WOOL, color.getWoolData()));

        packet.getChunkCoordIntPairs().write(0, chunkcoords);
        packet.getMultiBlockChangeInfoArrays().write(0, change);

        sendPacket(packet);

        point.setColor(color);

    }

    /**
     * Draws multiple pixels on the canvas with only 1 packet per chunk
     *
     * @param color  - The color to draw with
     * @param points - The points where to draw
     */
    public void drawPixels(DyeColor color, Point... points)
    {
        Map<Point, List<Location>> sorted = sortPoints(points);

        sorted.forEach((chunkpoint, list) -> {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);

            ChunkCoordIntPair chunkcoords = new ChunkCoordIntPair(chunkpoint.getX(), chunkpoint.getY());
            MultiBlockChangeInfo[] change = new MultiBlockChangeInfo[list.size()];

            for (int i = 0; i < list.size(); i++)
                change[i] = new MultiBlockChangeInfo(list.get(i), WrappedBlockData.createData(Material.WOOL, color.getWoolData()));

            packet.getChunkCoordIntPairs().write(0, chunkcoords);
            packet.getMultiBlockChangeInfoArrays().write(0, change);

            sendPacket(packet);
        });

        for (Point point : points)
            point.setColor(color);
    }

    /**
     * Fills the canvas with a specific color
     *
     * @param color - The color to fill the canvas with
     */
    public void fill(DyeColor color)
    {
        Map<Point, List<Location>> sorted = sortColors();

        sorted.forEach((chunk, list) -> {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);

            ChunkCoordIntPair chunkcoords = new ChunkCoordIntPair(chunk.getX(), chunk.getY());
            MultiBlockChangeInfo[] change = new MultiBlockChangeInfo[list.size()];

            for (int i = 0; i < list.size(); i++)
                change[i] = new MultiBlockChangeInfo(list.get(i), WrappedBlockData.createData(Material.WOOL, color.getWoolData()));

            packet.getChunkCoordIntPairs().write(0, chunkcoords);
            packet.getMultiBlockChangeInfoArrays().write(0, change);

            sendPacket(packet);
        });

        points.forEach(point -> point.setColor(color));
    }

    /**
     * Sorts points per chunk
     *
     * @param points - The points to sort
     * @return - The sorted points per chunk
     */
    private Map<Point, List<Location>> sortPoints(Point... points)
    {
        Map<Point, List<Location>> chunks = new HashMap<>();

        for (Point point : points) {
            Location loc = adaptLocation(point);
            int chunkx = loc.getChunk().getX();
            int chunkz = loc.getChunk().getZ();
            Point chunkpoint = new Point(chunkx, chunkz);

            chunks.putIfAbsent(chunkpoint, new ArrayList<>());
            List<Location> list = chunks.get(chunkpoint);
            list.add(loc);
            chunks.replace(chunkpoint, list);
        }

        return chunks;

    }

    /**
     * Sorts all canvas points per chunk
     *
     * @return - The sorted points per chunk
     */
    private Map<Point, List<Location>> sortColors()
    {
        if (!sortedPoints.isEmpty())
            return sortedPoints;

        return sortedPoints = sortPoints(points.toArray(new Point[]{}));
    }

    /**
     * Converts a Location to a Point
     *
     * @param location - The location to convert
     * @return - The converted Point
     */
    public Point adaptPoint(Location location)
    {
        int adaptedX = location.getBlockX() - topLeft.getBlockX();
        int adaptedY = location.getBlockY() - bottomRight.getBlockY();

        if (!belongs(location))
            return null;

        for (Point point : points)
            if (point.getX() == adaptedX && point.getY() == adaptedY)
                return point;

        return null;
    }

    /**
     * Converts a Point to a Location
     *
     * @param point - The point to convert from
     * @return - The converted Location
     */
    public Location adaptLocation(Point point)
    {
        int adaptedX = point.getX() + topLeft.getBlockX();
        int adaptedY = point.getY() + bottomRight.getBlockY();

        return new Location(topLeft.getWorld(), adaptedX, adaptedY, topLeft.getZ());
    }

    /**
     * @param origin - The origin point
     * @param x      - The X modifier
     * @param y      - The Y modifier
     * @return The new point
     */
    public Point getRelative(Point origin, int x, int y)
    {
        int newX = origin.getX() + x;
        int newY = origin.getY() + y;

        if (newX < 0 || newY < 0 || newX > getMaxPointX() || newY > getMaxPointY())
            return null;

        for (Point point : points)
            if (point.getX() == newX && point.getY() == newY)
                return point;

        return null;
    }

    /**
     * Checks if a Location belongs to the canvas
     *
     * @param location - The location
     * @return - true if the location belongs, false otherwise
     */
    public boolean belongs(Location location)
    {
        return !(location.getBlockZ() != topLeft.getBlockZ() || //if the z doesn't match
                location.getBlockY() > topLeft.getBlockY() || //if it is above the top
                location.getBlockY() < bottomRight.getBlockY() || //if it is below the bottom
                location.getBlockX() > bottomRight.getBlockX() || //if it is outside through the right
                location.getBlockX() < topLeft.getBlockX()); //if it is outside through the left
    }

    /**
     * Renders the full canvas to a new player
     *
     * @param player - The player to render to
     */
    public void renderCanvas(Player player)
    {
        sortColors().forEach((chunk, list) -> {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);

            ChunkCoordIntPair chunkcoords = new ChunkCoordIntPair(chunk.getX(), chunk.getY());
            MultiBlockChangeInfo[] change = new MultiBlockChangeInfo[list.size()];

            for (int i = 0; i < list.size(); i++)
                change[i] = new MultiBlockChangeInfo(list.get(i),
                        WrappedBlockData.createData(Material.WOOL,
                                adaptPoint(list.get(i)).getColor().getWoolData()));

            packet.getChunkCoordIntPairs().write(0, chunkcoords);
            packet.getMultiBlockChangeInfoArrays().write(0, change);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sends a packet to every player in the arena
     *
     * @param packet - The packet to send
     */
    private void sendPacket(PacketContainer packet)
    {
        arena.getPlayers().forEach(p -> {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private int getMaxPointX()
    {
        return bottomRight.getBlockX() - topLeft.getBlockX();
    }

    private int getMaxPointY()
    {
        return topLeft.getBlockY() - bottomRight.getBlockY();
    }
}
