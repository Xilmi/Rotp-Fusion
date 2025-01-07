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
package rotp.ui.planets;

import static rotp.model.colony.Colony.ECOLOGY;
import static rotp.model.colony.Colony.RESEARCH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.SwingUtilities;

import rotp.model.colony.Colony;
import rotp.model.galaxy.StarSystem;
import rotp.ui.BasePanel;
import rotp.ui.SystemViewer;
import rotp.ui.main.MainUI;
import rotp.ui.main.SystemPanel;

class MultiColonySpendingPane extends BasePanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private static final Color buttonTextHasOrder	= new Color(0, 0, 255);
    private static final Color buttonTextHasOrderD	= new Color(65, 65, 192);
    private static final Color buttonTextUrged		= new Color(255, 0, 255);
    private static final Color buttonTextUrgedD		= new Color(142, 65, 192);
    private static final Color buttonTextMixed		= new Color(128, 0, 255);
    private static final Color buttonTextMixedD		= new Color(65, 32, 96);

    private LinearGradientPaint greenBackC;
    private Color textC, backC;
    private Rectangle spending0Box = new Rectangle();
    private Rectangle spending25Box = new Rectangle();
    private Rectangle spending50Box = new Rectangle();
    private Rectangle spending75Box = new Rectangle();
    private Rectangle spendingMaxBox = new Rectangle();
    private Rectangle[] catBox = new Rectangle[5];
    private Rectangle hoverBox;
    private int selectedCat = 0;

    private final SystemViewer parent;
    MultiColonySpendingPane(SystemViewer p, Color c0, Color text, Color hi, Color lo) {
        parent = p;
        textC = text;
        backC = c0;
        init();
    }
    @Override
    public String textureName()            { return parent.subPanelTextureName(); }
    private void init() {
        for (int i=0;i<catBox.length;i++)
            catBox[i] = new Rectangle();

        setOpaque(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(backC);
    }
    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        super.paintComponent(g);

        int w = getWidth();
        
        if (greenBackC == null)
            initGradients();

        g.setFont(narrowFont(20));
        String titleText = text("MAIN_COLONY_ALLOCATE_SPENDING");
        int y0 = s22;
        drawShadowedString(g, titleText, 2, s5, y0, MainUI.shadeBorderC(), textC);

        int n = catBox.length;
        int gap = s5;
        int w0 = (w-s20-((n-1)*gap))/n;

        int x0 = s10;
        int h0 = s20;
        y0 += s8;
        
        boolean showPendingOrders = options().showPendingOrders();
        for (int i=0;i<n;i++) {
        	drawSpendingButton(g, i, showPendingOrders, x0, y0, w0, h0);
            x0 = x0+w0+gap;
        }

        String catName = text(Colony.categoryName(selectedCat));
        int buttonH = s30;
        y0 += s30;
        drawGreenButton(g,text("PLANETS_SPENDING_0",catName), spending0Box, hoverBox, y0);
        y0 += buttonH;
        drawGreenButton(g,text("PLANETS_SPENDING_25",catName), spending25Box, hoverBox, y0);
        y0 += buttonH;
        drawGreenButton(g,text("PLANETS_SPENDING_50",catName), spending50Box, hoverBox, y0);
        y0 += buttonH;
        drawGreenButton(g,text("PLANETS_SPENDING_75",catName), spending75Box, hoverBox, y0);
        y0 += buttonH;
        drawGreenButton(g,text("PLANETS_SPENDING_MAX",catName), spendingMaxBox, hoverBox, y0);
        y0 += buttonH;

        y0 += s15;
        g.setColor(SystemPanel.blackText);            
        String desc = text("FLEETS_ADJUST_SPENDING_DESC2");
        if (!player().ignoresPlanetEnvironment())
            desc = desc + " " + text("FLEETS_ADJUST_SPENDING_DESC3");
        g.setFont(narrowFont(14));
        List<String> descLines = wrappedLines(g, desc, w-s20);
        for (String line: descLines) {
            drawString(g,line, s10, y0);
            y0 += s16;
        }
    }
	private Boolean isUrged(List<StarSystem> systems, int cat)	{
		boolean yes = false;
		boolean no  = false;
		for (StarSystem sys: systems) {
			if (sys.colony().isUrged(cat))
				yes = true;
			else
				no = true;
			if (yes && no)
				return null;
		}
		return yes;
	}
	private Boolean hasOrder(List<StarSystem> systems, int cat)	{
		boolean yes = false;
		boolean no  = false;
		for (StarSystem sys: systems) {
			if (sys.colony().hasOrder(cat))
				yes = true;
			else
				no = true;
			if (yes && no)
				return null;
		}
		return yes;
	}
    private void drawSpendingButton(Graphics2D g, int cat, boolean showPendingOrders, int x, int y, int w, int h) {
		String label = text(Colony.categoryName(cat));
		Rectangle actionBox = catBox[cat];
		boolean selected = cat == selectedCat;
		Boolean hasOrder = false;
		Boolean isUrged  = false;
		boolean isMixed  = false;
		List<StarSystem> systems = parent.systemsToDisplay();
		if (showPendingOrders) {
			hasOrder = hasOrder(systems, cat);
			isUrged  = isUrged(systems, cat);
			isMixed  = (hasOrder == null) || (isUrged == null) || (isUrged && hasOrder);
			if (isMixed) {
				hasOrder = false;
				isUrged  = false;
			}
		}

		actionBox.setBounds(x, y, w, h);
		g.setColor(SystemPanel.buttonShadowC);
		g.fillRoundRect(x+s1, y+s3, w, h, s8, s8);
		g.fillRoundRect(x+s2, y+s4, w, h, s8, s8);

		g.setPaint(greenBackC);
		g.fillRoundRect(x, y, w, h, s5, s5);

		boolean hovering = actionBox == hoverBox;
		Color c0, c1;
		int shadow = 1;
		if (hovering) {
			c0 = Color.yellow;
			c1 = c0;
		}

		else if (selected) {
			c1 = Color.white;
			if (isMixed)
				c0 = buttonTextMixed;
			else if (isUrged)
				c0 = buttonTextUrged;
			else if (hasOrder)
				c0 = buttonTextHasOrder;
			else {
				c0 = Color.white;
				c1 = SystemPanel.textShadowC;
				shadow = 3;
			}
		}
		else {
			c1 = SystemPanel.grayText;
			if (isMixed)
				c0 = buttonTextMixedD;
			else if (isUrged)
				c0 = buttonTextUrgedD;
			else if (hasOrder)
				c0 = buttonTextHasOrderD;
			else {
				c0 = SystemPanel.grayText;
				c1 = SystemPanel.textShadowC;
				shadow = 3;
			}
		}

		g.setFont(narrowFont(16));
		int sw = g.getFontMetrics().stringWidth(label);
		int x0 = x+((w-sw)/2);
		drawShadowedString(g, label, shadow, x0, y+h-s5, c1, c0);

		g.setColor(c0);
		Stroke prev2 = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(x+s1, y, w-s2, h, s5, s5);
		g.setStroke(prev2);
	}
    private void drawGreenButton(Graphics2D g, String label, Rectangle actionBox, Shape hoverBox, int y) {
        int buttonH = s24;
        int x1 = s10;
        int w1 = getWidth()-s20;
        if (actionBox != null)
            actionBox.setBounds(x1,y,w1,buttonH);
        g.setColor(SystemPanel.buttonShadowC);
        g.fillRoundRect(x1+s1,y+s3,w1,buttonH,s8,s8);
        g.fillRoundRect(x1+s2,y+s4,w1,buttonH,s8,s8);

        g.setPaint(greenBackC);
        g.fillRoundRect(x1,y,w1,buttonH,s5,s5);

        boolean hovering = (actionBox != null) && (actionBox == hoverBox);
        Color c0 = (actionBox == null) ? SystemPanel.grayText : hovering ? Color.yellow : SystemPanel.whiteText;

        g.setFont(narrowFont(16));
        int sw = g.getFontMetrics().stringWidth(label);
        int x0 = x1+((w1-sw)/2);
        drawShadowedString(g, label, 3, x0, y+buttonH-s7, SystemPanel.textShadowC, c0);

        g.setColor(c0);
        Stroke prev2 = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(x1+s1,y,w1-s2,buttonH,s5,s5);
        g.setStroke(prev2);
    }
    private void initGradients() {
        int w = getWidth();
        int leftM = s2;
        int rightM = w-s2;
        Point2D start = new Point2D.Float(leftM, 0);
        Point2D end = new Point2D.Float(rightM, 0);
        float[] dist = {0.0f, 0.5f, 1.0f};

        Color greenEdgeC = new Color(44,59,30);
        Color greenMidC = new Color(71,93,48);
        Color[] greenColors = {greenEdgeC, greenMidC, greenEdgeC };

        greenBackC = new LinearGradientPaint(start, end, dist, greenColors);
    }
    void selectCat(int i) {
        if (i != selectedCat) {
            selectedCat = i;
            repaint();
        }
    }
	private void toggleCatOrders(int cat) {
		selectedCat = cat;
		List<StarSystem> systems = parent.systemsToDisplay();
		Boolean hasOrder = hasOrder(systems, cat);
		Boolean isUrged  = isUrged(systems, cat);
		boolean isMixed  = (hasOrder == null) || (isUrged == null) || (isUrged && hasOrder);
		if (isMixed)
			for (StarSystem sys: systems) {
				Colony colony = sys.colony();
				colony.removeOrder(cat);
				colony.governIfNeeded();
			}
		else
			for (StarSystem sys: systems) {
				Colony colony = sys.colony();
				colony.toggleOrder(cat);
				colony.governIfNeeded();
			}
		repaint();
	}
    void toggleGovernor() {
        List<StarSystem> systems = parent.systemsToDisplay();
        for (StarSystem sys: systems) {
            Colony c = sys.colony();
            if (c != null) {
                c.setGovernor(!c.isGovernor());
                if (c.isGovernor()) {
                    c.govern();
                }
            }
        }
        parent.repaintAll();
    }
    public void setGovernor(boolean gov) {
        List<StarSystem> systems = parent.systemsToDisplay();
        for (StarSystem sys: systems) {
            Colony c = sys.colony();
            if (c != null) {
                c.setGovernor(gov);
                if (c.isGovernor()) {
                    c.govern();
                }
            }
        }
        parent.repaintAll();
    }
    void increaseBase(InputEvent e) {
        List<StarSystem> systems = parent.systemsToDisplay();
        for (StarSystem sys: systems) {
            Colony c = sys.colony();
            if (c != null) {
            	c.defense().incrMaxBases(1, false, false);
            	c.governIfNeeded();
            	//c.defense().incrMaxBases(1, e.isShiftDown(), e.isControlDown());
            }
        }
        parent.repaintAll();
    }
    void decreaseBase(InputEvent e) {
        List<StarSystem> systems = parent.systemsToDisplay();
        for (StarSystem sys: systems) {
            Colony c = sys.colony();
            if (c != null) {
            	c.defense().incrMaxBases(-1, false, false);
            	c.governIfNeeded();
            	//c.defense().incrMaxBases(-1, e.isShiftDown(), e.isControlDown());
            }
        }
        parent.repaintAll();
    }
    public void setSpendingLevel(float pct) {
        List<StarSystem> systems = parent.systemsToDisplay();
        for (StarSystem sys: systems) {
            Colony c = sys.colony();
            if (c != null) {
                c.forcePct(selectedCat, pct);
                c.ensureMinimumCleanup();
            }
        }
        parent.repaintAll();
    }
    void smoothSmartMax() {
        List<StarSystem> systems = parent.systemsToDisplay();
        for (StarSystem sys: systems) {
            Colony colony = sys.colony();
            if (colony != null) {
            	if(!colony.locked(selectedCat))
                    colony.smoothMaxSlider(selectedCat);
                if(selectedCat != ECOLOGY)
                	colony.checkEcoAtClean();
                if(selectedCat != RESEARCH && !colony.locked(RESEARCH))
                	colony.allocation(RESEARCH, 0);
                colony.redistributeReducedEcoSpending();
            }
        }
        parent.repaintAll();
    }

    void setLock(int cat, boolean lock) {
        List<StarSystem> systems = parent.systemsToDisplay();
        for (StarSystem sys: systems) {
            Colony c = sys.colony();
            c.locked(cat, lock);
        }
        parent.repaintAll();
    }
	@Override public void mouseClicked(MouseEvent arg0) { parent.enterCurrentPane(this); }
	@Override public void mouseEntered(MouseEvent arg0) { parent.enterCurrentPane(this); }
	@Override public void mouseExited(MouseEvent arg0) {
		parent.exitCurrentPane(this);
        if (hoverBox != null) {
            hoverBox = null;
            repaint();
        }
    }
	@Override public void mousePressed(MouseEvent ev) { parent.enterCurrentPane(this); }
	@Override public void mouseReleased(MouseEvent e) {
		parent.enterCurrentPane(this);
        if (e.getButton() > 3)
            return;

		for (int i=0;i<catBox.length;i++) {
			if (hoverBox == catBox[i]) {
				if (SwingUtilities.isRightMouseButton(e))
					toggleCatOrders(i);
				else
					selectCat(i);
				return;
			}
		}
        if (hoverBox == spending0Box) 
            setSpendingLevel(0);
        else if (hoverBox == spending25Box) 
            setSpendingLevel(0.25f);
        else if (hoverBox == spending50Box) 
            setSpendingLevel(0.5f);
        else if (hoverBox == spending75Box) 
            setSpendingLevel(0.75f);
        else if (hoverBox == spendingMaxBox) 
            setSpendingLevel(1);
    }
	@Override public void mouseDragged(MouseEvent arg0) { parent.enterCurrentPane(this); }
	@Override public void mouseMoved(MouseEvent e) {
		parent.enterCurrentPane(this);
        int x = e.getX();
        int y = e.getY();

        Rectangle newHover = null;
        if (spending0Box.contains(x,y))
            newHover = spending0Box;
        else if (spending25Box.contains(x,y))
            newHover = spending25Box;
        else if (spending50Box.contains(x,y))
            newHover = spending50Box;
        else if (spending75Box.contains(x,y))
            newHover = spending75Box;
        else if (spendingMaxBox.contains(x,y))
            newHover = spendingMaxBox;
        else {
            for (int i=0;i<catBox.length;i++) {
                if (catBox[i].contains(x,y)) {
                    newHover = catBox[i];
                    break;
                }
            }
        }

        if (newHover != hoverBox) {
            hoverBox = newHover;
            repaint();
        }
    }
}