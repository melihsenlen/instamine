<img src="src\main\resources\assets\instamine.png"></img>
# Instamine
Mining deepslate has always felt inconsistent. Same tools, same enchants, slower mining speed for no meaningful gameplay reason.

Instamine fixes that by reducing the hardness of selected blocks to match the hardness of regular stone. This means with **Efficiency V & Haste II**, affected blocks become practically instamineable, just like stone.

## Features
- Makes **deepslate**, **endstone**, and **cobblestone** instamineable out of the box
- Fully customizable block list through the **Mod Menu** mod or the config file
- Configurable target hardness for those who have other things in mind
- Works server-side without requiring clients to install the mod

## Dependencies
- <a href="https://modrinth.com/mod/cloth-config">Cloth Config API</a>
- <a href="https://modrinth.com/mod/modmenu">Mod Menu</a>

## Configuration

#### Default Changes
| Block | Vanilla Hardness | Target Hardness |
|---|---:|---:|
| Deepslate | 3.0 | 1.5 |
| Endstone | 3.0 | 1.5 |
| Cobblestone | 2.0 | 1.5 |

`1.5` is the vanilla hardness of stone, and yes cobblestone is originally not instaminable.

The mod configuration including the affected blocks list and the target hardness can be easily edited through Mod Menu or directly through `config/instamine.json` after its generation.

## Servers
Instamine works server-side for all players, even without the mod installed on their client.

Installing it on both server and client is recommended so the mining animation stays visually consistent.

## Installation
#### Download Release
1. Download the latest version from <a href="https://modrinth.com/mod/instamine">Modrinth</a> or <a href="https://www.curseforge.com/minecraft/mc-mods/instamine">CurseForge</a>
2. Make sure you have all the required <a href="#dependencies">Dependencies</a>
3. Place all the `.jar` files in your `mods` directory

#### Build From Source
Requirements:
- Java 25
- Gradle 9.4.0

```bash
.\gradlew.bat build
```

Built `.jar` file should be located in `build/libs`

## License
MIT License
