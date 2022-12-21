
## Short description:

- Added a new galaxy type: Bitmap, which allows to load an image.
  - Real shots of galaxies give nice results.
  - You can draw your own galaxies, in shades of gray or colors.
  - With colored galaxies, you can define regions where Orion, Player and Aliens stars systemes shoul be.
  - An advanced color mode allows you to define the regions of each Aliens, as well as Nebula regions!
  - Some examples can be found in BitmapGalaxyExamples.zip"
- Galaxy Preview:
   - Added option to color Galaxy preview and make them circular;
      - Blue for Orion.
      - Green for the Player.
      - Red for aliens. (Including the two nearby stars)
     - You can choose the size of these colored dots. (In Mod Global Options)
   - Stars are now represented by circles.
- Fixed a very old bug that incorrectly placed Orion  on the top left of large maps!
  - (Everything was in place to put this bug on the map!)
- Fixed weird crashes of "TextGalaxy" and "BitmapGalaxy", due to array overflow!
  - Made these arrays dynamic
  - Reduced their size security reserve by a factor 10.
- The "Global" vs "Local" options loading and saving will be clearly marked on the buttons. (Toggled with "Shift")
- The "Last" option will remain unchanged till the next session. (It was updated when navigating from menu to menu)
- Live.options will be the one that tracks the current state of the GUI, and will be set to "Last" when Launching ROTP.
- The "Restore" Option allows you to reload the Live.options.

## Detailed description:

Bitmap type galaxies:
- Preferably use a small bitmap image to define the galaxy.
  - VGA size is more than enough... Galaxies are still randomly generated!
- Option 1: "Grey/Sum":
  - Color bitmaps will be converted to grayscale by adding the three colors together. (Standard ways)
- Option 1: "Grey/Sum":
  - Color bitmaps will be converted to grayscale with the maximum of the three colors. (Sometimes gives better results)
- Option 1: "Grey Inverted":
  - Because some nice pictures have a white background.
- Option 2: Sharp, Sharper, Sharpest, Razor like.
  - This allows the tuning of real galaxy photography, try it!
- Option 1: "Color":
  - The colors of the images are interpreted:
    - Blue for Orion.
    - Green for the Player.
    - Red for the Aliens.
    - The remaining stars and nebulae are distributed over these three regions.
    - If one color is missing: it will be replaced by global grayscale.
- Option 1: "Advanced":
  - It uses multiple sub-bitmaps grouped vertically in a single Bitmap file.
  - Start with Black as background (sub-bitmap separator)
  - Fist Sub-Bitmap: Definition of lone star systems:
    - Add Blue rectangle to define the limits of the sub-bitmap.
    - Add Green inside the first rectangle to define the density of lone stars.
      - Can be mixed with blue, or replace it.
      - The intensity of green defines the probability of having a star:
        - 255 = Max
        - 0 = no stars.
  - Other Sub-Bitmaps:
  	- Copy the Lone star sub-bitmap, as often as necessary, keeping a few black lines as separators.
  	- Blue is still needed to fill the gap and avoid black lines in the middle of the sub-bitmap.
  	- The Green color is then ignored, but you may will to keep it as repair point.
  	- The intensity of Red will be used to defines the next probabilities.
  	- You can use White instead of Red, as White contains Red, and other colors are ignored.
  	- All invalid Sub-Bitmaps will be reblaced by the first one. 
  - Second Sub-Bitmap: Preferred location of nebulae:
    - No Red = same as lone star systems.
    - For "No Nebulaes"use the already existing option in the good old "Advanced Options" panel.
 - Third Sub-Bitmap: Orion's favourite location:
 - Fourth Sub-Bitmap: Preferred location of Player:
 - Following Sub-Bitmap: Preferred location of aliens:
   - Up to 49 Alien's sub-bitmap can be defined.
   - If there are more Aliens than sub-bitmap, the galaxy factory will loop on the available Alien sub-bitmap.
- Option 1: "Advanced/Mask" and "Advanced/Mask2":
  - This allows you to define the color gradiens only once, on the firts sub-bitmap, and uses uniform colors to define the regions on the following maps. 
  - The two maps are then multiplyed to define a resulting map.
  - /Mask2 adds a sharpening effect to keeps the Empire inside the other stars.
  
... You have examples, just try it!

And do not hesitate to share your Galaxies!
