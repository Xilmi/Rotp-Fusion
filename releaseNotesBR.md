Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!

<b><ins>Updates since last releases:</ins></b>
- New option: Dark Galaxy Mode.
  - Only Star systems in scout range or in scanner range are shown.
  - Option to:
	- Spies also gives approximate location and info of contacted empires.
	- Spies are unable to give info on out of range Star systems.
	- If your empire shrink, out of range scouted planet are hidden.
  - Only the final replay will show the full galaxy!
- Shields animations tuning:
  - Added access to former 2D shields animations, as 3D glitches with mac OS.
  - Added a new 3D shield animations that should be compatible with mac OS.
  - Added the fire under the ship animation.
    - Was shown on the demo but not in combat.
  - Replaced the meaningless Animations delay by Animation Fps.
    - The real delay was dependent on the computing time.
    - The animation duration will depends on the fps and the number of frames... 

<b><ins>Bug Fixes:</ins></b>
- Fixed sound echo default values.
- Fixed ship building turns estimation when ship reserve is used.
- Fixed guide appearance on compact option panels.
- Fixed some animations blocked by the temporisation of Result panel.
- Fixed Comet Event not resetting its timer! (Leading to negative delays!)


<b><ins>New 3D Shields effects:</ins></b>
- Ship images are analyzed to determine an enveloping ellipse, the basis for the shield ellipsoid.
- The center of the cell was the target... but the ships are not always centered in the cell ==> The ships are now the real target.
- When the player is the target, sounds start when the beam hits the ship.
  - Echo effects are available... (Being hit should sound different)
- Testing tools are still available to allow you to adjust the effects to your liking.
 - Settings -> Display Preferences -> Start Demo
 - Toggle Keys
   - "R" Toggle Random Animations
   - "P" Toggle Player is Target (Add Echo)
   - "Q" Toggle Quiet (mute)
  - Sequences Keys
    - "C"/"Shift-C"/"Ctrl-C": Shield color Next/Previous/Loop
    - "W"/"Shift-W"/"Ctrl-W": Weapon Selection Next/Previous/Loop
    - "M"/"Shift-M": Ship Model (shape) Next/Previous
    - "H"/"Shift-H": Ship Hull (size) Next/Previous
    - "T"/"Shift-T": Ship Type (species) Next/Previous
    - "<-"/"->": Loop thru all ships Next/Previous
    - "Z"/"Shift-Z": Zoom (window size) Next/Previous
  - Locations Keys
    - Num Pad: Weapon Location ("5" = Loop)

<b><ins>Other Updates:</ins></b>
- Last Xilmi's AI missile update.
- Fixed disconnected "Deterministic Artifact"-option.
- Fixed some display options not always being saved.
- Fixed and re-enabled governor's animations.
- Added new options to allow/disallow Technologies Research:
  - Atmospheric Terraforming,
  - Cloning,
  - Advanced Soil Enrichment.
