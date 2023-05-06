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

package rotp.ui.util;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JRadioButton;

import static rotp.ui.BasePanel.*;
import rotp.ui.RotPUI;
import rotp.ui.main.SystemPanel;

public class SimpleCheckBox implements Icon {

    int dim = 10;
    Color bgColor = Color.white;

    public SimpleCheckBox (int dimension, Color bgColor){
        this.dim	 = dimension;
        this.bgColor = bgColor;
    }

    protected int getDimension() {
        return dim;
    }

    @Override public void paintIcon(Component component, Graphics g0, int x, int y) {
        ButtonModel buttonModel = ((AbstractButton) component).getModel();
        Graphics2D g = (Graphics2D) g0;
        int y_offset = (int) (0.5 * (component.getSize().getHeight() - getDimension()));
        int x_offset = 2;
        int corner	 = 0;
        if (component instanceof JRadioButton)
        	corner = dim;
        
        if (buttonModel.isRollover()) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.DARK_GRAY);
        }
        g.fillRoundRect(x_offset, y_offset, dim, dim, corner, corner);
        if (buttonModel.isPressed()) {
            g.setColor(Color.GRAY);
        } else {
            g.setColor(bgColor);
        }
        g.fillRoundRect(1 + x_offset, y_offset + 1, dim - 2, dim - 2, corner, corner);
        
        if (buttonModel.isSelected()) {
        	Stroke prev = g.getStroke();
            g.setStroke(new BasicStroke(s3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(SystemPanel.whiteText);
            g.drawLine(x_offset-s1, dim+y_offset-s8, x_offset+s4, dim+y_offset-s4);
            g.drawLine(x_offset+s4, dim+y_offset-s4, x_offset+dim, dim+y_offset-s16);
            g.setStroke(prev);
        }
    }

    @Override public int getIconWidth() {
        return getDimension();
    }

    @Override public int getIconHeight() {
        return getDimension();
    }
}