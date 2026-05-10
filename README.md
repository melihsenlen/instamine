<img src="src\main\resources\assets\instamine.png"></img>

# Instamine
Deepslate and endstone have always been a major annoyance, same tools, same enchants, just slower for no good reason. Instamine fixes that by adjusting their hardness values to match the hardness value of regular stone. This also implies that with the ***Efficiency V & Haste II*** combo, you can instamine these blocks just like regular stone.

## Config
Defaults and their previous hardness values:
- Deepslate (3.0)
- Endstone (3.0)
- Cobblestone (2.0)

Stone is originally (1.5) hardness, so that's what you will get out of the box with this mod. In addition, you can also configure this target hardness value as of version 1.2.1.

The list of affected blocks can be configured with the ModMenu mod. Config file will be located in <code>config/instamine.json</code> after its generation.

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