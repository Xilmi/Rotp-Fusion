/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.sprites;

import java.awt.Color;
import java.awt.Graphics2D;

import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.game.MergedDynamicOptionsUI;
import rotp.ui.main.GalaxyMapPanel;

public class OptionsWidgetSprite extends MapControlSprite {
    public OptionsWidgetSprite(int xOff, int yOff, int w, int h) {
        xOffset = scaled(xOff);
        yOffset = scaled(yOff);
        width = scaled(w);
        height = scaled(h);
    }
    @Override
    public boolean acceptDoubleClicks()         { return false; }
    @Override
    public void click(GalaxyMapPanel map, int count, boolean rightClick, boolean click, boolean middleClick) {
    	if (!options().isGameOptionsAllowed()) {
    		MergedDynamicOptionsUI optionsUI = RotPUI.mergedDynamicOptionsUI();
			optionsUI.start("");
    	}
    }
    @Override
    public void draw(GalaxyMapPanel map, Graphics2D g2) {
    	if (options().isGameOptionsAllowed()) {
    		return;
    	}

        drawBackground(map,g2);

        int cnr = BasePanel.s12;        
        g2.setColor(Color.black);
        g2.fillRoundRect(startX, startY, width, height, cnr, cnr);

        int margin = width/6;
        int dx = width/3 - scaled(2);
        int xL = startX + margin;
        int xR = startX - margin  + width - dx;
        
        int dy = scaled(7);
        int h  = scaled(3);
        int y  = startY + height/2 - dy - h/2;        
        g2.setColor(Color.orange);

        g2.fillRect(xL, y, dx, h);
        g2.fillRect(xR, y, dx, h);
        y += dy;
        g2.fillRect(xL, y, dx, h);
        g2.fillRect(xR, y, dx, h);
        y += dy;
        g2.fillRect(xL, y, dx, h);
        g2.fillRect(xR, y, dx, h);

        drawBorder(map,g2);
    }
}
