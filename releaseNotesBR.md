
## Short description:

- Added a new galaxy type: Bitmap.
  - Grey level gives the probability of having a star system; White = most probable; Black = no star.
  - Inverted grey level gives the probability of having a star system; Black = most probable; White = no star.
  - Color Bitmap allows to specify the probability for Orion, Player and Aliens.
  - Advanced color Bitmap allows to specify the probability for Star systems, Nebulae, Orion, Player and each Aliens.
- Added colors to Galaxy preview; Blue = Orion; Green = Player; Red = Aliens; (Including the two near by stars)
- The "Global" vs "Local" options loading and saving will be clearly indicated on the buttons. (Toggled with "Shift")
- The "Last" option was only last when Launching ROTP, and then was updated when navigating from menu to menu. It won't be updated anymore.
- The "Last" option will remain unchanged till the next session. (It was updated when navigating from menu to menu)
- Live.options will be the one following the current GUI state, and will be set to "Last" when Launching ROTP.
- The "Restore" Option allows you to reload the Live.options.

## Detailed description:

Added a new galaxy type: Bitmap.
- Uses small uncompressed bitmap to define the galaxy shape with variable star density.
- A few examples can be found in "BitmapExamples.zip"
- Grey maps, likes all other maps haves the same density of probability for lones start, orion, player, aliens, and nebulae.
- Color maps uses the Blue for Orion, Green for Player, Red for Alien, Grey for nebulae and lones stars.
  - Grey is defined by the max value of each colors.
  - If one color is missing: it will be replaced by Grey.
-  Advanced color Bitmap:
  - It uses multiple sub-bitmaps grouped vertically in one Bitmap file.
  - Start with Black as background (sub bitmap separator)
  - Fist Sub-Bitmap: Lone star systems definition:
    - Add Blue rectangle to define the sub-bitmap limits.
    - Add Green inside the first rectangle to define the lone star density.
      - Can be mixed with the Blue, or replace it.
      - Green intensity defines the probability to have a star: 255 = Max, 0 = no stars.
  - Other Sub-Bitmaps:
  	- Copy the Lone star sub-bitmap, as often as needed, keeping some black lines as separators.
  	- The Blue is still needed to fill the gap and avoid black lines in the middle of the sub-bitmap.
  	- The Green is ignored, but you may will to keep it as repair point.
  	- The Red intensity will be used to defines the next probabilities.
  	- You may use White instead of Red, as White contains Red, and other colors are ignored.
  - Second Sub-Bitmap: Nebulae's prefered location:
    - No Red = same as Lone star systems.
    - Uses the already existing option to removes nebulae.
 - Third Sub-Bitmap: Orion's prefered location:
 - Fourth Sub-Bitmap: Player's prefered location:
 - Following Sub-Bitmap: Aliens's prefered location:
   - Up to 49 Alien's sub-bitmap may be defined.
   - If there is more Aliens than sub-bitmap, the galaxy factory will loop thru the available Alien's sub-bitmap.

Do not hesitate to share your Galaxies!
 




