[Official website](https://www.remnantsoftheprecursors.org) <br/>

New Java requirement: minimum JRE-17, recommended JRE-23.

[Installation instructions](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/installation.md)


<b><ins>Very last changes:</ins></b>

2025.04.12 (Xilmi)
- Using stasis-field now only plays the animation once instead of 4 times.
- AI smarter about Blackhole-generator and Stasis-field-usage
  - When a defending fleet has black-hole-generators or stasis-field-generators they will now no longer try to concede the first-hit to cloaked attackers. That is because black-hole-generator success-chance isn't reduced by cloaking-device and stasis-field has 100% chance to succeed.
  - AI-ships equipped with stasis-field-generator will now select targets as if they could kill them in order to make it more likely to freeze the most dangerous stack.
- Update AIGeneral.java
  - When choosing whom to attack next the AI now puts less emphasis on a power-advantage compared to distance and potential gains.
  - Previously AIs would all dog-pile on whoever is weakest, even if this meant a lot of inefficienct flying back and forth. Now they are more likely to take on someone who they can quickly make huge territorial gains against.

2025.04.12 (BR)
- Fixed fleets retreating after the player was threatened.
  - They were not allowed to change destination.
  - They can now be redirected to another of the player's colonies.


#### [Features Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/FeaturesChanges.md)

#### [Reverse  Chronological Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges.md)
