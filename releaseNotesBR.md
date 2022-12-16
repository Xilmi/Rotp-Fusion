Added a new galaxy type: Bitmap.

This will allow you to draw your galaxy shape, the level of gray giving the probability of having a star.
  - Click on the first option and select your bitmap.
  - The second option will allow you to invert the grey level.
  - A color bitmap gives you the option to select:
    - your preferred location (green level).
    - Orion's location (blue level).
    - the aliens' location (red level).
    - The remaining stars will be distributed according to the grey level, which is defined by the maximum color intensity.
    - If a color is missing, it will be replaced by the grey level.
    - In the worst case, a smoothed circular galaxy will be generated.
Included: Two bitmap files as example.

Other changes:
  - The "Global" vs "Local" options loading and saving will be clearly indicated on the buttons. (Toggle with "Shift")
  - The "Last" option was only last when Launching ROTP, and then was updated when navigating from menu to menu. It won't be updated anymore.
  - The "Last" option will remain unchanged till the next session. (It was updated when navigating from menu to menu)
  - Live.options will be the one following the current GUI state, and will be set to "Last" when Launching ROTP.
  - The "Restore" Option allows you to reload the Live.options.
