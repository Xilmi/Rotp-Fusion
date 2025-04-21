[Official website](https://www.remnantsoftheprecursors.org) <br/>

New Java requirement: minimum JRE-17, recommended JRE-23.

[Installation instructions](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/installation.md)


<b><ins>Very last changes:</ins></b>

2025.04.21 (BR)
- Fixed "Concurrent Modification Exception" in Ship combat Manager.
- Added distinct Messages for when rebels repel invasion, or are eliminated.
- Fixed retaking abandoned system with surrendering transports.
  - Surrendering transports size were set to -1, and were not always checked for surrendering.
    - While joining an abandoned colony, its size was negative.
    - While joining non surrendering transport, their size were reduced by one.
  - Surrendering transports size is now set to 0, and a surrendering flag is set.
  - Abandoned colony will only accept non empty transport.


#### [Features Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/FeaturesChanges.md)

#### [Reverse  Chronological Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges.md)
