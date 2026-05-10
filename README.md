# Instamine
Deepslate and endstone have always been a major annoyance, same tools, same enchants, just slower for no good reason. Instamine fixes that by adjusting their hardness values to match stone. This also implies that with the ***Efficiency V & Haste II*** combo they instamine just like stone.

## Config
Defaults:
- All deepslate variants & deepslate ores
- Endstone & endstone bricks

The list of affected blocks can be configured inside the mod menu. It is also accessible in <code>config/instamine.json</code> after its generated.

## Server
Server-side setup should work for all players regardless of their client. It is however recommended to have it on the client too, especially if you want the mining animation to be consistent.

## Dependencies
- Minecraft 26.1.x
- Fabric loader
- Fabric API
- Cloth Config
- Mod Menu

## Setup
- Download original release:
    1. The latest release can be downloaded from the official <a href="https://modrinth.com/mod/instamine">Modrinth</a> page.

    2. Place the <code>.jar</code> in your <code>mods</code> directory as always, install the dependencies seperately and you're good to go.

- Build from source (requires Java 25 and Gradle 9.4.0.):
    1. ```bash
       .\gradlew.bat build
       ```

    2. The <code>.jar</code> will be in <code>build/libs/</code>



## License
MIT License
