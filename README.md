 Remnants of the Precursors

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

26-05-23 (BR)
- Fixed a bug that caused retreating ships to leave the victorious faction in certain cases.
  - This occurred when the retreating ships had never engaged in combat and none of the ships that had participated in the battle had retreated.
- Fixed weapon action text that overflows the boxes.
- New option to tell the combat automation to not target a colony that doesn't have missile bases.
  - Later, when selecting the automation, you can change the default value. You can force a “Yes” by pressing the “Shift” key, or force a ‘No’ by pressing the “Control” key.

26-05-21 (Xilmi)
- Fix in retreat logic
  - Ships that still can attack enemies with specials that deal no damage but not anything else will no longer want to stay in combat.
- Bio-bomber-fix
  - Bio-bombers shouldn't retreat if they can inflict damage to colonies with missile-bases.

26-05-19 (BR)
- Fixed prohibited combinations of special devices:
  - Specifically, ion beam projectors and neutron beam projectors.
  - In both User GUI and AI New Ship Template.

26-05-18 (BR)
- New Options: Max Range Tech Gap & Max Warp Tech Gap
  - It's always frustrating when no engine technologies or no fuel technologies are available for research... These options ensure that at least some of these technologies are included in the technology tree.
- Fixed English guide text not showing for this new option.


### [Features Historic](FeaturesChanges.md)

### [Reverse Chronological Historic](DetailedChanges.md)


## [To-Do list](TodoList.md)

[How To](doc/HowTo.md)
