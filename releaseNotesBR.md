Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!

Last updates:
- New buttons names: "Static" -> "pregame" and "Dynamic" -> "in-game",
  - Thanks to William482 for the name proposal.
- All Governor off will now also disable: 
  - Transport from ungoverned colony.
  - Auto-Spy.
  - Auto-Infiltrate.
- Simplified options' management.
  - Setup options are now only loaded once at startup...
    - No more silently updated when going through the main menu.
    - The player decide when setup options needs to be reloaded.
  - Loaded games will always load their own options. (No mix with some current setup options)
    - The player decide if an when to change some options.
  - Removed "global" load and save from sub-menus.
    - Only Race setup panel and Galaxy setup panel will allow Global reload.
    - Sub panels are available in-game, and it wouldn't be a good idea to reload all parameters there!
- Player initial race will be randomized (as it was on vanilla Rotp)
  - If the race was a custom race, id wont'be randomized.
  - If you always want to play with the same race: Standard races can be selected in custom race menu.
  - "R" in Race setup panel will randomize your race.


Last Fixes:
- New Governor Panel don't crash any more on Apple OS.


Last Anouuncement:

- New Governor Panel:
  - Changed to Rotp Colors.
  - Changed to Rotp Fonts.
  - Added race image. (Click on it to stop / restart the animation)
  - Option to choose between the old and new design.
  - Option to adjust the size. (New design only)
  - Option to adjust the brightness. (New design only)
  - Options are now saved to the game file.
  - New compact panel available from setup Menu. (To configure User's preferences)
  - Position is Memorized.
  - In Fullscreen mode, if the OS allows "Always on top": The Governor panel will be set "Always on top".
    - If the Os does'nt allow it, You can still move the panel to a secondary monitor:
      - Whith the second Governor compact panel...
      - Or when in windowed mode.
  - Governor Spying "Spare Xenophobes": The spying will now be stopped only when framed.
    - Spying will start again once the timer is expired.
- Guide improvement: 
  - Short list are now diplayed with all their options.
  - Some new contents.
  - Better integration in Custom Race Menu.
- Ships Design Scrap Refund:
  - New option to change the refund percentage.
  - New option to limit the ships location to get the refund.
- Renamed "recentStart.rotp" to "!!! To Replay Last Turn !!!".
  - This should clarify its purpose and keep it on the top of the list.
- Restored vanilla Planet Quality options.
  - Modnar options are still available.
  - Added Hell and Heaven quality.
- ECO bar adjustment:
  - Right Click on the text on the right = Enough to terraform, no growing.
  - Middle Click on the text on the right = Enough to Clean.
- New option: Terraform End; The auto terraformation will stop either:
  - When fully populated.
  - When fully terraformed. (Don't bother populating)
  - When fully cleaned. (Don't bother terraforming)
- Different Text colors for Gaia and Fertile planets.
- New option to select the screen on which you will play.
- Set AI:Hybrid as default AI.
- AI:Base and AI:Modnar are now optional.

New Fixes:
- PopUp selection list: "Cancel" will work again!
- Fixed Governor not updating options when reloading a file.
- Fixed Bombing auto firing the turn following a "Fire all weapon".
- Fixed base setting auto colonize and auto bombard not being saved.
- Governor panel: Fixed ugly html text on Mac.
- If no flags, only show one black flag.
- Fixed Missiles not reaching planet.
  - It was a bug that has probably always been there! The distance to unlock the fire button was calculated according to ship movement rules (Diagonal movement = 1). But missiles follow another rule! (Diagonal movement = sqrt(2)). When ships are on the side of the screen, their distance to the planet is 6, but for missiles, this distance is ~7.2. Since the merculite missile range is 6 (+0.7 attack range)... You were allowed to fire, but the missiles run out of fuel before reaching the target and are destroyed!
  - The fix only unlocks the button when the missiles are really in range.


