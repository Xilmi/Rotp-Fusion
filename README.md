# Remnants of the Precursors

Remnants of the Precursors is a Java-based modernization of the original Master of Orion game from 1993. <br/>

### Fusion version
### Mixt of of Xilmi Fusion with Modnar new races
### With BrokenRegistry Options Manager. <br/>
... and some more features

Summary of the differences of Fusion-Mod to the base-game:
        [https://www.reddit.com/r/rotp/comments/x2ia8x/differences_between_fusionmod_and_vanillarotp/](https://www.reddit.com/r/rotp/comments/x2ia8x/differences_between_fusionmod_and_vanillarotp/) <br/>

Description of the different AI-options in Fusion-Mod:
        [https://www.reddit.com/r/rotp/comments/xhsjdr/some_more_details_about_the_different_aioptions/](https://www.reddit.com/r/rotp/comments/xhsjdr/some_more_details_about_the_different_aioptions/) <br/>

The decription of the additions/changes by Modnar can be found there: <br/>
	[https://github.com/modnar-hajile/rotp/releases](https://github.com/modnar-hajile/rotp/releases) <br/>


### To build and run locally:

On Debian / Ubuntu:

```
sudo apt install vorbis-tools
sudo apt install webp
mvn clean package -Dmaven.javadoc.skip=true
java -jar target/rotp-<timestamp>-mini.jar
```

On Fedora:

```
sudo dnf install libwebp-tools vorbis-tools
mvn clean package -Dmaven.javadoc.skip=true
java -jar target/rotp-<timestamp>-mini.jar
```

# Other Links
[Official website](https://www.remnantsoftheprecursors.org/) <br/>
[Community subreddit](https://www.reddit.com/r/rotp/) <br/>
[Download build](https://rayfowler.itch.io/remnants-of-the-precursors)


## What's New

2025.04.19 (BR)
- Galaxy Shape options don't share the same variable anymore.
  - So don't need to be a String from a list.
  - They are now owned by the shape class itself, and memorized independently.
  - Galaxy Shape option will be individually memorized.
- Random Galaxy are no more managed by UI nor Galaxy Factory.
  - It's a regular galaxy classe that calls the other shape.
- Options panel can now refresh the param list.
- many class final
- Reworked sequence of IGameOption instances creation...
  - Grouped their call to RulesSetManager instead of RotPUI.
- Moved initialisation of the independant class from RotPUI in Rotp.
  - This to make the full initialisation sequence easier to debug.
- Partial initialisation of RotPUI is now done in Rotp.
  - This to get an instance of RotPUI before it's final initialilsation.
  - This to have access to the already initialised panel.
- Debug's options that are saved in remant.cfg are now static (they where default).
  - So they can be read before an option set is created.
- Fixes:
  - Removed some useless Galaxy generation in setup panels.
  - Fixed calls to the wrong options.
  - Removed old unused methods that would crash the game if called.
  - Removed unused redundant methods that were confusing.


### [Features Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/FeaturesChanges.md)

### [Reverse Chronological Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges.md)


## [To-Do list](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/TodoList.md)

