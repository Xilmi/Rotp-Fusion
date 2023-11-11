package rotp.model.galaxy;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import rotp.model.empires.Empire;
import rotp.ui.BasePanel;
import rotp.ui.main.GalaxyMapPanel;

//public abstract class ShipMonster extends FleetBase {
public abstract class ShipMonster extends ShipFleet {
	private float travelSpeed = max(0.4f, 1f / (1.5f * max(1, 100.0f/galaxy().maxNumStarSystems())));
	private transient BufferedImage shipImage;

	public ShipMonster(int emp) {
		super(emp, 0, 0);
	}
	protected void initEmpireSystem(int sysId, StarSystem s) {
		sysId(sysId);
		setXY(s);
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
	@Override public IMappedObject source()							{ return this; }
	@Override public void draw(GalaxyMapPanel map, Graphics2D g2)	{
		// TODO BR:  Auto-generated method stub
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
			g2.drawImage(img, x, y, w, h, map);
		else
			g2.drawImage(img, x+w, y, -w, h, map);

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
//	@Override public float travelSpeed()					{ return max(0.4f, 1f / (1.5f * max(1, 100.0f/galaxy().maxNumStarSystems()))); }
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
}
