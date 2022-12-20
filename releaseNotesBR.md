
## Short description:

- Added a new galaxy type: Bitmap, which allows you to load an image.
  - Real galaxies pictures give nice results.
  - You can draw your own galaxies, in grey intensities or in colors.
  - With the colored galaxies, you can define the regions where Orion, Player, And Aliens stars systemes shoul be.
  - An advanced color mode allows you to define the regions of Aliens individually, as well as for the nebulae's!
  - A few examples can be found in BitmapGalaxyExamples.zip"
- Added an option to color to Galaxy preview;
  - Blue for Orion.
  - Green for the Player.
  - Red for the Aliens. (Including the two near by stars)
  - You can choose the size of these colored dots.
- Fixed a very old bug placing Orion wrongly on the to left of the big maps!
  - (Everything was in place to put this bug on the map!)
- Fixed some odd "TextGalaxy" and "BitmapGalaxy" crashing due to array's overflow!
  - Made these arrays dynamic
  - Reduced their size security reserve by a factor 10.
- The "Global" vs "Local" options loading and saving will be clearly indicated on the buttons. (Toggled with "Shift")
- The "Last" option was only last when Launching ROTP, and then was updated when navigating from menu to menu. It won't be updated anymore.
- The "Last" option will remain unchanged till the next session. (It was updated when navigating from menu to menu)
- Live.options will be the one following the current GUI state, and will be set to "Last" when Launching ROTP.
- The "Restore" Option allows you to reload the Live.options.

## Detailed description:

Bitmap type galaxies:
- Uses preferably small and bitmap to define the galaxy.
  - VGA sized is more than enough... They are still randmly generated!
- Option 1: "Grey/Sum":
  - Color bitmaps will be converted in grey leved by summing the three colors. (The standard ways)
- Option 1: "Grey/Sum":
  - Color bitmaps will be converted in grey leved with the max of the three colors. (Sometimes gives better results)
- Option 1: "Grey Inverted":
  - Because some nice pictures have white background.
- Option 2: Sharp, Sharper, Sharpest, Razor like.
  - This allows the tuning of real galaxy photography, Try it!
- Option 1: "Color":
  - The colors of the pictures are interpreted:
    - Blue for Orion.
    - Green for the Player.
    - Red for the Aliens.
    - The remaining stars and nebulae are spread over thes three regions.
    - If one color is missing: it will be replaced by global grey intensity.
- Option 1: "Advanced":
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
  	- The Green color is then ignored, but you may will to keep it as repair point.
  	- The Red intensity will be used to defines the next probabilities.
  	- You may use White instead of Red, as White contains Red, and other colors are ignored.
  	- every invalid Sub-Bitmaps will be reblaced by the first one. 
  - Second Sub-Bitmap: Nebulae's prefered location:
    - No Red = same as Lone star systems.
    - For "No Nebulaes"use the already existing option in the good old "Advanced Options" panel.
 - Third Sub-Bitmap: Orion's prefered location:
 - Fourth Sub-Bitmap: Player's prefered location:
 - Following Sub-Bitmap: Aliens's prefered location:
   - Up to 49 Alien's sub-bitmap may be defined.
   - If there is more Aliens than sub-bitmap, the galaxy factory will loop thru the available Alien's sub-bitmap.

- Option 1: "Advanced/Mask" and "Advanced/Mask2":
  - This allows you to define the colors' gradiens only once, on the firts sub-bitmap, and uses uniform patches to define the regions on the following maps. 
  - The two maps are then multiplyed to define a resulting map.
  - /Mask2 add add a sharping effect to keeps the Empire inside the "free" stars.
  
... You have examples, just try it!

And do not hesitate to share your Galaxies!
