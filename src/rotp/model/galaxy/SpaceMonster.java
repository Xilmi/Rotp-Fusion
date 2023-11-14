/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.galaxy;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import rotp.model.combat.CombatStack;
import rotp.model.empires.Empire;
import rotp.model.incidents.DiplomaticIncident;
import rotp.model.incidents.KillMonsterIncident;
import rotp.ui.BasePanel;
import rotp.ui.main.GalaxyMapPanel;

public abstract class SpaceMonster extends ShipFleet implements NamedObject {
	private static final long serialVersionUID = 1L;
	protected final String nameKey;
	protected int lastAttackerId;
	private final List<Integer> path = new ArrayList<>();
	protected float travelSpeed = max(0.4f, 1f / (1.5f * max(1, 100.0f/galaxy().maxNumStarSystems())));
	protected Float monsterLevel;
	private transient List<CombatStack> combatStacks = new ArrayList<>();
	private transient BufferedImage shipImage;
	
	public SpaceMonster(String name, int empId, Float speed, Float level)	{
		super(empId, 0, 0);
		nameKey		= name;
		if (level == null)
			monsterLevel	= 1f;
		else
			monsterLevel	= level;
		if (speed == null)
			travelSpeed = max(0.4f, 1f / (1.5f * max(1, 100.0f/galaxy().maxNumStarSystems())));
		else
			travelSpeed = speed;
	}

	public Empire lastAttacker()			{ return galaxy().empire(lastAttackerId); }
	public void lastAttacker(Empire c)		{ lastAttackerId = c.id; }
	public void visitSystem(int sysId)		{ path.add(sysId); }
	public List<Integer> vistedSystems()	{ return path; }
	public int vistedSystemsCount()			{ return path.size(); }
	public List<CombatStack> combatStacks()	{
		if (combatStacks == null)
			combatStacks = new ArrayList<>();
		return combatStacks; 
	}
	public Image image()		{ return image(nameKey); }
	public void initCombat()	{ }
	public void addCombatStack(CombatStack c)	{ combatStacks.add(c); }
	public float monsterLevel()					{ return monsterLevel; }
	@Override public String name()				{ return text(nameKey);  }
	@Override public IMappedObject source()		{ return this; }
	@Override public void draw(GalaxyMapPanel map, Graphics2D g2)	{
		if (!displayed())
			return;

		int imgSize = 3;
		if (map.scaleX() > GalaxyMapPanel.MAX_FLEET_LARGE_SCALE) 
			imgSize--;
		if (map.scaleX() > GalaxyMapPanel.MAX_FLEET_SMALL_SCALE)
			imgSize--;
		// are we zoomed out too far to show a fleet of this size?
		if (imgSize < 1)
			return;

		BufferedImage img = getImage();
		int w = img.getWidth();
		int h = img.getHeight();
		int x = mapX(map);
		int y = mapY(map);
		
		if (!hasDestination() || (destX() >= x()))
			g2.drawImage(img, x-w/4, y-h/4, w, h, map);
		else
			g2.drawImage(img, x+w/2, y-h/4, -w, h, map);

		int pad = BasePanel.s8;
		selectBox().setBounds(x-pad,y-pad,w+pad+pad,h+pad+pad);
		int s5 = BasePanel.s5;
		int s10 = BasePanel.s10;
		int cnr = BasePanel.s10;
		if (map.parent().isClicked(this))
			drawSelection(g2, map, x-s5, y-s5, w+s10, h+s10, cnr);
		else if (map.parent().isHovering(this))
			drawHovering(g2, map, x-s5, y-s5, w+s10, h+s10, cnr);
	}
	@Override public boolean canSendTo(int sysId)			{ return false; }
	@Override public float travelSpeed()					{ return travelSpeed; }
	@Override public boolean visibleTo(int empId)			{ return true; } // TODO BR: improve monster visibility analysis
	@Override public int empId()							{ return -2; }
	@Override public boolean inTransit()					{ return true; }
	@Override public boolean deployed()						{ return false; }
	@Override public int maxMapScale()						{ return GalaxyMapPanel.MAX_FLEET_HUGE_SCALE; }
	@Override public boolean isPotentiallyArmed(Empire e)	{ return true; }
	@Override public boolean decideWhetherDisplayed(GalaxyMapPanel map) {
		if (!map.displays(this))
			return false;
		if (map.scaleX() > maxMapScale())
			return false;
		return true;
	}
	public abstract Image shipImage();
	public boolean alive()	{ 
		boolean alive = false;
		for (CombatStack st: combatStacks) {
			if (!st.destroyed())
				return true;
		}
		return alive;
	}
	public void plunder()	{ notifyGalaxy(); }
	
	protected void setEmpireSystem(int sysId, StarSystem s) {
		sysId(sysId);
		setXY(s);
	}
	protected DiplomaticIncident killIncident(Empire emp) { return KillMonsterIncident.create(emp.id, lastAttackerId, nameKey); }
	
	private void notifyGalaxy() {
		Empire slayerEmp = lastAttacker();
		for (Empire emp: galaxy().empires()) {
			if ((emp.id != lastAttackerId) && emp.knowsOf(slayerEmp)) {
				DiplomaticIncident inc = killIncident(emp);
				emp.diplomatAI().noticeIncident(inc, slayerEmp);
			}
		}
	}
	private BufferedImage getImage() {
		if (shipImage == null) {
			Image baseShipImg = shipImage();
			int imgW	= baseShipImg.getWidth(null);
			int imgH	= baseShipImg.getHeight(null);
			int destW	= BasePanel.s30;
			int destH	= BasePanel.s20;
			shipImage = newBufferedImage(destW, destH);
			Graphics2D g = (Graphics2D) shipImage.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawImage(baseShipImg, 0, 0, destW, destH, 0, 0, imgW, imgH, null);
			g.dispose();
		}
		return shipImage;
	}
	public void degradePlanet(StarSystem sys) {}
}
