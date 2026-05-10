# Instamine
Deepslate takes twice as long to mine as stone for a quite obvious reason, however this mod simply does not care. The hardness value of deepslate and endstone variants have been adjusted to match those of stone, which directly implies the ultimate purpose of this mod; With **Efficiency V** & **Haste II**, these blocks will instamine just like how regular stone does.

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
1. Download original release:
    - The latest release can be downloaded from the official <a href="https://modrinth.com/mod/instamine">Modrinth</a> page.

    - Place the <code>.jar</code> in your <code>mods</code> directory as always, install the dependencies seperately and you're good to go.

2. Build from source (requires Java 25 and Gradle 9.4.0.):
    - ```bash
      .\gradlew.bat build
      ```

    - The <code>.jar</code> will be in <code>build/libs/</code>



## License
MIT License