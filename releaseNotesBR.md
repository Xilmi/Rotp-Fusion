[Official website](https://www.remnantsoftheprecursors.org) <br/>

New Java requirement: minimum JRE-17, recommended JRE-23.

[Installation instructions](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/installation.md)


<b><ins>Very last changes:</ins></b>

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


#### [Features Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/FeaturesChanges.md)

#### [Reverse  Chronological Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges.md)
