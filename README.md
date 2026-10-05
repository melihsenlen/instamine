<img src="src/main/resources/assets/instamine/icon.png" alt="Instamine icon" width="128">

# Instamine

Mining deepslate has always felt inconsistent. Same tools, same enchants, slower mining speed for no meaningful gameplay reason.

Instamine fixes that by reducing the hardness of **selected blocks** to match the hardness of regular stone. This means with **Efficiency V & Haste II**, affected blocks become practically **instamineable**, just like stone.

## Features

- Makes **deepslate** and **end stone** instamineable out of the box
- Fully customizable block list through the **Mod Menu** config screen
- Configurable target hardness for those who have other things in mind
- Dedicated toggles for ores and logs, plus a master on/off switch
- Works server-side without requiring clients to install the mod

## Dependencies

> [!IMPORTANT]
> Any updates on this mod will only follow the latest release of the game.

| Dependency | Version | Required |
|---|---|:---:|
| Minecraft | 26.3 | Yes |
| [Fabric Loader](https://fabricmc.net/use/installer/) | 0.15.5 + | Yes |
| [Cloth Config API](https://github.com/shedaniel/cloth-config) | 26.3.158 + | Yes |
| [Mod Menu](https://github.com/TerraformersMC/ModMenu) | 21.0.0-beta.1 + | No |

## Installation

### Method #1: Download release

1. Download the latest version from [Modrinth](https://modrinth.com/mod/instamine) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/instamine).
2. Make sure you have all the required [dependencies](#dependencies).
3. Place all the `.jar` files in your `mods` file in your instance.

### Method #2: Build from source

Requirements:

- Java 25
- Gradle 9.4.0

The wrapper is already included, so you just have to build it.

```bash
# Windows
.\gradlew.bat build

# macOS / Linux
./gradlew build
```

The built `.jar` should be located under `build/libs` with the corresponding version tag.

## How It Works

Instamine doesn't edit the blocks themselves. It hooks into the game's mining progress calculation. For a selected block, the formula is the same as vanilla's, except it divides by the configured hardness instead of the block's own.

Tool speed, enchantments and effects like Efficiency and Haste still apply as usual. The right tool is still needed for drops, and mining with the wrong tool is still slower. Only mining speed changes. Blast resistance and drops are untouched.

## Configuration

The mod configuration includes the block list, the ore and log toggles, the hardness value and the master switch. All of it can be edited through Mod Menu, or directly in `config/instamine.json` after its generation.

### Default changes

| Block | Vanilla hardness | Instamine hardness |
|---|---:|---:|
| Deepslate | 3.0 | 1.5 |
| End stone | 3.0 | 1.5 |
| Cobblestone | 2.0 | 1.5 |
| Cobbled deepslate | 3.5 | 1.5 |
| Ores | 3.0 - 4.5 | 1.5 |
| Logs & wood | 2.0 | 2.0 (by default) |

`1.5` is the vanilla hardness of stone, and yes, cobblestone is not instamineable in vanilla. It's also the default target, and every selected block uses the same value.

### Ores & logs

Rather than typing blocks separately into the block list, ores and logs each get their own dedicated toggle in the config screen.

| Toggle | Spans | Default |
|---|---|---:|
| Ores | All 18 ore blocks: stone, deepslate and Nether variants | On |
| Logs | All 48 vanilla log/stem, wood/hyphae, and stripped variants | Off |

Everything else, individual blocks like `deepslate` or `end stone`, still goes in the block list.

### Config file

`config/instamine.json` is created on first launch with these defaults:

```json
{
  "blocks": [
    "deepslate",
    "end stone",
    "cobblestone",
    "cobbled deepslate"
  ],
  "hardness": 1.5,
  "enabled": true,
  "ores": true,
  "logs": false
}
```

> [!NOTE]
> - Block names are forgiving. Case, spaces, underscores and a `minecraft:` prefix are ignored, so `End Stone`, `end_stone` and `minecraft:end_stone` all mean the same block. Entries that don't match any block are skipped, with a warning in the log.
> - `hardness` has to be above 0, otherwise the default of `1.5` is used. The config screen accepts values starting from 0.1.
> - Changes saved through Mod Menu apply right away. Edits to the file are read when the game or server starts.
> - Dedicated servers don't have Mod Menu, so edit the file there.

## Multiplayer

> [!IMPORTANT]
> - Dedicated servers need Instamine and Cloth Config installed.
> - Having the mod client-side only won't let you instamine on servers that don't have the mod installed.

Instamine works server-side for all players, technically even without the mod installed on their client. Installing it on the client too is recommended however, mainly so that the mining animation stays visually consistent. 

## License

[MIT license](LICENSE)
