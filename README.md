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

2025.04.12 (Frank Zago)
- French translation fixes and improvement.

2025.04.12 (BR)
- Fixed fleets retreating after the player was threatened.
  - They were not allowed to change destination.
  - They can now be redirected to another of the player's colonies.

2025.04.12 (Xilmi)
- Using stasis-field now only plays the animation once instead of 4 times.
- AI smarter about Blackhole-generator and Stasis-field-usage
  - When a defending fleet has black-hole-generators or stasis-field-generators they will now no longer try to concede the first-hit to cloaked attackers. That is because black-hole-generator success-chance isn't reduced by cloaking-device and stasis-field has 100% chance to succeed.
  - AI-ships equipped with stasis-field-generator will now select targets as if they could kill them in order to make it more likely to freeze the most dangerous stack.
- Update AIGeneral.java
  - When choosing whom to attack next the AI now puts less emphasis on a power-advantage compared to distance and potential gains.
  - Previously AIs would all dog-pile on whoever is weakest, even if this meant a lot of inefficienct flying back and forth. Now they are more likely to take on someone who they can quickly make huge territorial gains against.


### [Features Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/FeaturesChanges.md)

### [Reverse Chronological Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges.md)


## [To-Do list](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/TodoList.md)

