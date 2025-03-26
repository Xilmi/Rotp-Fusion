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

2025.03.26 (Xilmi)
- Bugfixes in retreat-logic
  - Fixed a bug where the AI counted the damage of their own missiles towards the damage they would receive when it came to whether they should retreat from incoming missiles. This caused them to flee from fights they were winning.
  - Range- and speed-advantages no longer arbitrarily impact the combat-outcome calculations of the retreat-logic. This caused faster ships to sometimes not retreat when they should in order to prevent from being killed.
- Value colonizable planets in wars.
  - AI fleets without colony-ships would previously completely ignore all systems without colonies on them.
  - This caused several problems during wars:
    - Uninvolved parties could take over a lot of planets bombed out during a war.
    - Great staging-points for causing further havoc deep in the enemies' empire were given up because they weren't colonized yet.
  - The AI now values uncolonized systems with 1/10th the score of colonized ones for fleets without colony-ship. This will make them stay at such systems and wait for a colony-ship instead of travelling very long distances to another front. The AI will also leave some ships behind on these systems to cover up a colonization while advancing further.
- Avoid premature peace-treaties
  - The Xilmi-AIs will no longer automatically make peace once it's ships can no longer reach colonies of their enemies.
  - Instead the AI will check whether they are currently hovering colonizable systems with their fleets and only consider war when there are none.


[### Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges)


[## To-Do list](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/TodoList)

