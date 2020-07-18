# DrawMyThing

Welcome to the DrawMyThing repository.

SETUP INSTRUCTIONS:

- Add DrawMyThing and ProtocolLib to the server 
- Restart the server
- Edit maps.yml to your liking
- Restart the server

Optional hooks:
- PlaceholderAPI

![score](https://www.code-inspector.com/project/11018/score/svg)
![status](https://www.code-inspector.com/project/11018/status/svg)

API:
 
Getting the plugin:

```java
Plugin plugin = Bukkit.getPluginManager().getPlugin("DrawMyThing");

if(plugin == null)
  return;
  
DrawPlugin drawPlugin = (DrawPlugin) plugin;
```

Getting a DrawPlayer:
```java
DrawPlayer drawPlayer = drawPlugin.getPlayerManager().get(player);
DrawPlayer drawPlayer = drawPlugin.getPlayerManager().get(uuid);
```
```java
UUID uuid = drawPlayer.getUuid();
TeamsScoreboard scoreboard = drawPlayer.getScoreboard(); //Used to write on the scoreboard
ScoreboardTemplate template = drawPlayer.getCurrentTemplate(); //Used to render a template from the config
Game game = drawPlayer.getCurrentGame(); //This can be null if the game already ended
boolean isHiding = drawPlayer.isHiding(); //true if the player is hiding other players

int points = drawPlayer.getPoints(); //Get the current points earned
DyeColor color = drawPlayer.getSelectedColor(); //The current color used for drawer

boolean isDrawing = drawPlayer.isDrawing();
Player player = drawPlayer.getPlayer();


drawPlayer.setScoreboard(scoreboard); //can cause bugs
drawPlayer.setGame(game); //can cause bugs
drawPlayer.setHiding(boolean); //don't forget to set back the items
drawPlayer.setPoints(points);
drawPlayer.setColor(color);
```

Drawing on the canvas:
```java
Canvas canvas = drawPlayer.getGame().getArena().getCanvas();

Point point = PointConverter.adaptPoint(location, canvas); //will return null if the location is outside the canvas
canvas.drawPizels(color, point);
```

Rendering the canvas to a player:
```java
canvas.renderCanvas(player);
```

Useful methods:
```java
Point point = PointConverter.adaptPoint(location, canvas);
Location location = PointConverter.adaptLocation(point, canvas);

boolean locationbelongs = PointConverter.locationBelongs(location, canvas);
```

Registering a painting tool:
- Make a class that implements PaintingTool
- Implement all methods
```java
drawPlugin.getToolManager().registerTool(new MyPaintingTool());
```

