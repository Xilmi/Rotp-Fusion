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

25-04-28 (BR)
- Fixed galaxies with a single option field not following the value selected for that option.
- Custom Species can now have their own intro:
  - Create a folder "CustomSpecies"
  - add a file named 'speciesname'.intro.txt
  - The fist line will also be used as title
  - Inspiration can be taken from the intro files in the directory: rotp/lang/en/races

25-04-27 (BR)
- Added "Symbiosis" to the "Warmongering" level list. This allows you to form an alliance on first contact. (Quite bland... Will be improved)
- Moved two hard-coded texts to the label files to make them available for translation.
- Fixed the number of alien factories being hidden under the sent population.
- In colony multi-selection, "G" no longer toggles governors individually; they are now all synchronized to the displayed value.
- Fixed the issue where the opponent's AI guide was not displaying relevant information.
- Fixed Random generator throwing error when asked for a number between 0 and 0; will return 0 instead.
- Fixed a comodification error in ship combat.


### [Features Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/FeaturesChanges.md)

### [Reverse Chronological Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges.md)


## [To-Do list](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/TodoList.md)
