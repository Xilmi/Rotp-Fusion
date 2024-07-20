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
package rotp.ui.planets;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import rotp.model.colony.Colony;
import rotp.model.empires.Race;
import rotp.model.galaxy.StarSystem;
import rotp.ui.BasePanel;
import rotp.ui.RotPUI;

public class ColonyViewUI extends BasePanel implements MouseListener {
	private static final long serialVersionUID = 1L;

	private Image landscapeImg;
	private boolean exited = false;
	private int sysId;
	private int iconWidth = s48;
	private int factoryWidth, factoryHeight;
	private int speciesWidth, speciesHeight;
	private int missileWidth, missileHeight;
	private BufferedImage factoryImg, speciesImg, missileImg;
	private String ambienceKey;

	public ColonyViewUI()	{ init(); }
	private void init()		{
		setBackground(Color.black);
		addMouseListener(this);
	}
	public void init(int sysId)	{
		this.sysId		= sysId;
		exited			= false;
		StarSystem sys	= galaxy().system(sysId);
		Race race		= sys.empire().race();
		ambienceKey		= race.ambienceKey;
		iconWidth		= s32;
		initFactoryImage();
		initMissileImage();
		initSpeciesImage(race);
		initLandscapeImage(sys.colony());
	}
	private void initSpeciesImage(Race race)	{
		speciesWidth  = iconWidth;
		speciesHeight = speciesWidth;
		int spW = speciesWidth*4/8;
		int spH = spW * 41/38;
		int[] iHue = new int[] {0, 90, 180};
//		BufferedImage scientist  = race.advisorScout();
//		int sW = scientist.getWidth(null);
//		int sH = scientist.getHeight(null);
//		BufferedImage leader  = race.councilLeader();
//		int lW = leader.getWidth(null);
//		int lH = leader.getHeight(null);
		BufferedImage mugshot = race.diploMugshotQuiet();
		int mW = mugshot.getWidth(null);
		int mH = mugshot.getHeight(null);

		BufferedImage[] mugHue = setHue(mugshot, iHue, 128);
		speciesImg    = newBufferedImage(speciesWidth, speciesHeight);
		Graphics2D g  = (Graphics2D) speciesImg.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		int x = 0;
		int y = 0;
		//y = spH/5;
		g.drawImage(mugshot, x, y, x+spW, y+spH, mW, 0, 0, mH, null);
//		g.drawImage(mugHue[0], x, y, x+spW, y+spH, 0, 0, mW, mH, null);
		x = speciesWidth-spW;
		y = spH/5;
		g.drawImage(mugshot, x, y, x+spW, y+spH, 0, 0, mW, mH, null);
		g.drawImage(mugHue[1], x, y, x+spW, y+spH, 0, 0, mW, mH, null);
		x = x/3;
		y = speciesHeight-spH;
		g.drawImage(mugshot, x, y, x+spW, y+spH, 0, 0, mW, mH, null);
		g.drawImage(mugHue[2], x, y, x+spW, y+spH, 0, 0, mW, mH, null);
//		x = 0;
//		spW = speciesWidth;
//		g.drawImage(leader, x, y, x+spW, y+spH, 0, 0, lW, lH, null);
//		g.drawImage(scientist, x, y, x+spW, y+spH, 0, 0, sW, sH, null);

		g.dispose();
	}
	private BufferedImage rotate(BufferedImage bimg, Double angleDeg) {
		double angle = Math.toRadians(angleDeg);
	    double sin = Math.abs(Math.sin(angle));
	    double cos = Math.abs(Math.cos(angle));
	    int w = bimg.getWidth();
	    int h = bimg.getHeight();
	    int neww = (int) Math.floor(w*cos + h*sin);
	    int newh = (int) Math.floor(h*cos + w*sin);
	    BufferedImage rotated = new BufferedImage(neww, newh, TYPE_INT_ARGB);
	    Graphics2D graphic = rotated.createGraphics();
	    graphic.translate((neww-w)/2, (newh-h)/2);
	    graphic.rotate(angle, w/2, h/2);
	    graphic.drawRenderedImage(bimg, null);
	    graphic.dispose();
	    return rotated;
	}
	private BufferedImage missile(int length)	{
		Image missile = image("BASE_MISSILE");
		int wi = missile.getWidth(null);
		int hi = missile.getHeight(null);
		int h = hi * length / wi;
		BufferedImage image = new BufferedImage(length, h, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(missile, 0, 0, length, h, 0, 0, wi, hi, null);
		g.dispose();
		return image;
	}
	private void initMissileImage()	{
		missileWidth  = iconWidth;
		missileHeight = missileWidth;
		int w0	= 512;
		int h0	= w0;
		int cX	= w0/2-100;
		int missileLen = 450;
		int missileSep = 160;
		BufferedImage missile = missile(missileLen);
		BufferedImage image = new BufferedImage(w0, h0, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// Rear Missile
		BufferedImage rotMissile = rotate(missile, -60.0);
		int rW = rotMissile.getWidth();
		int rH = rotMissile.getHeight();
		int misX = w0 - rW - missileSep;
		int misY = 0;
		g.drawImage(rotMissile, misX, misY, misX+rW, misY+rH, 0, 0, rW, rH, null);

		// Pole
		int poleW = 100;
		int poleH = 200;
		int poleX = cX-poleW/2;
		int poleY = h0-poleH;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(poleX, poleY, poleW, poleH);
		// Base
		int baseW = 300;
		int baseH = 32;
		int baseX = cX-baseW/2;
		int baseY = h0-baseH;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(baseX, baseY, baseW, baseH);
		// Top
		int topW = baseW;
		int topH = 32;
		int topX = cX-topW/2;
		int topY = poleY;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(topX, topY, topW, topH);

		// Front Missile
		misX += missileSep;
		g.drawImage(rotMissile, misX, misY, misX+rW, misY+rH, 0, 0, rW, rH, null);

		g.dispose();

		missileImg = newBufferedImage(missileWidth, missileHeight);
		g  = (Graphics2D) missileImg.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(image, 0, 0, missileWidth, missileHeight, 0, 0, w0, h0, null);
		g.dispose();
	}
	private BufferedImage[] setHue(BufferedImage src, int[] iHue, int alpha)	{
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int n = iHue.length;
		BufferedImage img[] = new BufferedImage[n];
		float hue[] = new float[n];
		for (int i=0; i<n; i++) {
			hue[i] = iHue[i]/360.0f;
			img[i] = new BufferedImage(w, h, TYPE_INT_ARGB);
		}
		for(int y=0; y<h; y++) {
			for(int x=0; x<w; x++) {
				int rgb = src.getRGB(x,y);
				//int a = rgb | 0x00ffffff;
				int a = min(alpha, (rgb >> 24) & 0xff);
				a = (a<<24) | 0x00ffffff;
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb) & 0xff;
				float hsv[] = new float[3];
				Color.RGBtoHSB(r, g, b, hsv);
				for (int i=0; i<n; i++) {
					int argb = Color.getHSBColor(hue[i], hsv[1], hsv[2]).getRGB() & a;
					img[i].setRGB(x,y,argb);
				}
			}
		}
		return img;
	}
//	private void initFactoryImage()				{
//		wFact = s12;
//		hFact = s12;
//		Image img  = image("FACTORY_ICON");
//		factoryImg = newBufferedImage(wFact, hFact);
//		Graphics2D g = (Graphics2D) factoryImg.getGraphics();
//		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//		g.drawImage(img, 0, 0, wFact, hFact, 0, 0, img.getWidth(null), img.getHeight(null), null);
//		g.dispose();
//	}
	private BufferedImage dome(int radius)	{
		int w = 2*radius;
		BufferedImage img = new BufferedImage(w, w, TYPE_INT_ARGB);
		Point2D center	= new Point2D.Float(radius, radius);
		Color cDark		= new Color(200, 120, 0);
		Color cCenter	= new Color(255, 150, 60);
		Color[] colors	= {cCenter, cCenter, cDark, cDark};
		float[] dist	= {0.0f, 0.1f, 0.95f, 1.0f};
		Graphics2D g	= (Graphics2D) img.getGraphics();
		RadialGradientPaint grad = new RadialGradientPaint(center, radius, dist, colors);
		g.setPaint(grad);
		g.fillRoundRect(0, 0, w, w, w, w);
		g.dispose();
		return img;
	}
	private BufferedImage chimney(int w, int h)	{
		BufferedImage img = new BufferedImage(w, h, TYPE_INT_ARGB);
		Graphics2D g	= (Graphics2D) img.getGraphics();
		GradientPaint grad = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, h, Color.DARK_GRAY);
		g.setPaint(grad);
		g.fillRect(0, 0, w, h);
		g.dispose();
		return img;
	}
	private BufferedImage walls(int w, int h)	{
		BufferedImage img = new BufferedImage(w, h, TYPE_INT_ARGB);
		Graphics2D g	= (Graphics2D) img.getGraphics();
		GradientPaint grad = new GradientPaint(0, 0, Color.GRAY, 0, h, Color.DARK_GRAY);
		g.setPaint(grad);
		g.fillRect(0, 0, w, h);
		g.dispose();
		return img;
	}
	private BufferedImage porch(int w, int h)	{
		BufferedImage img = new BufferedImage(w, h, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, w, h);
		GradientPaint grad = new GradientPaint(0, 0, Color.GRAY, w/2, 0, Color.DARK_GRAY, true);
		g.setPaint(grad);
		int top = h/5	;
		g.fillRect(0, top, w, h-top);
		g.dispose();
		return img;
	}
	private BufferedImage windows(int w, int h)	{
		BufferedImage img = new BufferedImage(w, h, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(new Color(48, 48, 112));
		g.fillRect(0, 0, w, h);
		g.dispose();
		return img;
	}
	private void initFactoryImage()			{
		factoryWidth  = iconWidth * 3/4;
		factoryHeight = factoryWidth * 3/4;
		int w0	= 512;
		int h0	= w0;
		int domeR	= 192;
		int domeCx	= w0/2;
		int domeCy	= h0-180;
		int wallH	= 220;
		int wallY	= h0-wallH;
		int chemX	= 48;
		int chemW	= 56;
		int chemXb	= chemX + 90;
		int chemXc	= w0 - chemX - chemW;
		int chemXd	= w0 - chemXb - chemW;
		int porchH	= wallH-64;
		int porchW	= 190;
		int porchX	= (w0-porchW)/2;
		int porchY	= h0 - porchH;
		int winW	= 40;
		int winH	= 64;
		int winX	= 24;
		int winY	= wallY + 40;
		int winYb	= winY + 96;
		int winXb	= winX + 64;
		int winXc	= w0 - winX - winW;
		int winXd	= w0 - winXb - winW;
		BufferedImage image = new BufferedImage(w0, h0, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		// Chimneys
		BufferedImage img = chimney(chemW, h0);
		g.drawImage(img, chemX,  0, chemX +chemW, h0, 0, 0, chemW, h0, null);
		g.drawImage(img, chemXb, 0, chemXb+chemW, h0, 0, 0, chemW, h0, null);
		g.drawImage(img, chemXc, 0, chemXc+chemW, h0, 0, 0, chemW, h0, null);
		g.drawImage(img, chemXd, 0, chemXd+chemW, h0, 0, 0, chemW, h0, null);
		// Dome
		img = dome(domeR);
		g.drawImage(img, domeCx-domeR, domeCy-domeR, domeCx+domeR, domeCy+domeR, 0, 0, 2*domeR, 2*domeR, null);
		// Walls
		img = walls(w0, wallH);
		g.drawImage(img, 0, wallY, w0, h0, 0, 0, w0, wallH, null);
		// Porch
		img = porch(porchW, porchH);
		g.drawImage(img, porchX, porchY, porchX+porchW, h0, 0, 0, porchW, porchH, null);
		// Windows
		img = windows(winW, winH);
		g.drawImage(img, winX,  winY,  winX+winW,  winY+winH,  0, 0, winW, winH, null);
		g.drawImage(img, winXb, winY,  winXb+winW, winY+winH,  0, 0, winW, winH, null);
		g.drawImage(img, winXc, winY,  winXc+winW, winY+winH,  0, 0, winW, winH, null);
		g.drawImage(img, winXd, winY,  winXd+winW, winY+winH,  0, 0, winW, winH, null);
		g.drawImage(img, winXb, winYb, winXb+winW, winYb+winH, 0, 0, winW, winH, null);
		g.drawImage(img, winXd, winYb, winXd+winW, winYb+winH, 0, 0, winW, winH, null);
		

		g.dispose();

//		// blur a little bit
//		int blur	= 9;
//		int blurSq	= blur*blur;
//		float flt	= 1.0f/blurSq;
//		float[] matrix = new float[blurSq];
//		for (int i = 0; i < blurSq; i++)
//			matrix[i] = flt;
//		ConvolveOp op = new ConvolveOp( new Kernel(blur, blur, matrix), ConvolveOp.EDGE_NO_OP, null );
//		BufferedImage img2 = op.filter(image, new BufferedImage(w0, h0, TYPE_INT_ARGB));
//		img2 = op.filter(img2, new BufferedImage(w0, h0, TYPE_INT_ARGB));

		factoryImg = new BufferedImage(factoryWidth, factoryHeight, TYPE_INT_ARGB);
		g = (Graphics2D) factoryImg.getGraphics();
		g.setComposite(AlphaComposite.SrcOver);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(image, 0, 0, factoryWidth, factoryHeight, 0, 0, w0, h0, null);
		g.dispose();
	}
	private void initLandscapeImage(Colony c)	{
		int w = getWidth();
		int h = getHeight();
		StarSystem sys = galaxy().system(sysId);
		//Empire empire = sys.empire();
		Race race = sys.empire().race();
		landscapeImg = newBufferedImage(w,h);
		Graphics2D g = (Graphics2D) landscapeImg.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		// modnar: use (slightly) better upsampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(c.planet().type().atmosphereImage(), 0, 0, w, h, null);
		g.drawImage(c.planet().type().randomCloudImage(), 0, 0, w, h, null);
		g.drawImage(c.planet().landscapeImage(), 0, 0, w, h, null);

		// draw fortress
		//BufferedImage fortImg = colony.empire().race().fortress(colony.fortressNum());
		BufferedImage fortImg = race.fortress(0);
		int fortW = scaled(fortImg.getWidth());
		int fortH = scaled(fortImg.getHeight());
		int fortX = w-fortW;
		int fortY = h-scaled(180)-fortH;
		g.drawImage(fortImg, fortX, fortY, fortX+fortW, fortY+fortH, 0, 0, fortImg.getWidth(), fortImg.getHeight(), null);

		// for hostile planets, draw shield
		if (race.isHostile(sys.planet().type())) {
			BufferedImage shieldImg = race.shield();
			g.drawImage(shieldImg, fortX, fortY, fortX+fortW, fortY+fortH, 0, 0, shieldImg.getWidth(), shieldImg.getHeight(), null);
		}

		g.dispose();
	}
	private void advanceScreen()				{
		exited = true;
		buttonClick();
		RotPUI.instance().selectMainPanel(false);
	}
	private void paintScene(Image img)			{
		Graphics2D g = (Graphics2D) img.getGraphics();
		setFontHints(g);
		int w = getWidth();
		int h = getHeight();
		int marginX	= s10;
		int marginY	= speciesHeight + s10;
		int sepHa	= factoryHeight * 4/5;
		int sepHb	= s10;
		int wBar	= w - 2*marginX-scaled(250);
		int dxMax	= iconWidth * 3/2;
		//Color detailLineC = Color.white;
		StarSystem sys	= galaxy().system(sysId);
		Colony col		= sys.colony();
		float pop		= col.population();
		float plants	= col.industry().factories();
		float bases		= col.defense().bases();
		int popNum		= (int) Math.ceil(pop/10);
		int plantNum	= (int) Math.ceil(plants/10);
		int baseNum		= (int) Math.ceil(bases/10);
		int rControl	= col.industry().effectiveRobotControls();
		int factColumn	= plantNum/rControl;
		if (factColumn*rControl < plantNum)
			factColumn++;

		int dxPop	= wBar / popNum;
		int dxFact	= wBar / factColumn;
		int dx		= min(dxPop, dxFact, dxMax);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(landscapeImg, 0, 0, w, h, null);

		// draw pop
		int x0 = marginX+dx/8;
		int yPop = h - marginY;
		int ya = yPop;
		for (int i=0; i<popNum; i++) {
			g.drawImage(speciesImg, x0, ya, x0+speciesWidth, ya+speciesHeight, 0, 0, speciesWidth, speciesHeight, null);
			x0 += dx;
		}
		
		// draw Factories
		int yFact = yPop - sepHb - (rControl)*sepHa;
		ya = yFact;
		int num = plantNum;
		boolean isShifted = false;
		for (int lev=0; lev<rControl && num>0; lev++) {
			if (isShifted)
				x0 = marginX + dx/2;
			else
				x0 = marginX;
			factColumn = num / (rControl-lev);
			if (factColumn*(rControl-lev) < num)
				factColumn++;
			for (int i=0; i<factColumn && num>0; i++) {
				g.drawImage(factoryImg, x0, ya, x0+factoryWidth, ya+factoryHeight, 0, 0, factoryWidth, factoryHeight, null);
				x0 += dx;
				num--;
			}
			isShifted = !isShifted;
			ya += sepHa;
		}

		// draw Missiles
		int yMissiles = yFact - missileHeight-s10;
		ya = yMissiles;
		x0 = marginX;
		for (int i=0; i<baseNum; i++) {
			g.drawImage(missileImg, x0, ya, x0+missileWidth, ya+missileHeight, 0, 0, missileWidth, missileHeight, null);
			x0 += dx;
		}

		// draw subtitle last (so it overlays any ship)
		int y0 = s30;
		String subtitle = text(sys.planet().type().description(sys.empire()));
		g.setFont(narrowFont(24));
		int sw = g.getFontMetrics().stringWidth(subtitle);
		x0 = (w-sw)/2;
		drawBorderedString(g, subtitle, 1, x0, y0, Color.black, Color.yellow);

		// draw title last (so it overlays any ship)
		y0 += s40;
		String title = sys.name();
		g.setFont(narrowFont(40));
		sw = g.getFontMetrics().stringWidth(title);
		x0 = (w-sw)/2;
		drawBorderedString(g, title, 1, x0, y0, Color.black, Color.yellow);
		
		g.dispose();
	}
	@Override public String ambienceSoundKey()		{ return ambienceKey; }
	@Override public void keyPressed(KeyEvent e)	{
		int k = e.getKeyCode();

		switch(k) {
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_SPACE:
				advanceScreen();
		}
	}
	@Override public void paintComponent(Graphics g)	{
		super.paintComponent(g);
		paintScene(screenBuffer());
		g.drawImage(screenBuffer(), 0, 0, null);
	}
	@Override public void animate()	{
		if (exited)
			return;
		repaint();
	}
	@Override public void mouseClicked(MouseEvent e)	{ }
	@Override public void mouseEntered(MouseEvent e)	{ }
	@Override public void mouseExited(MouseEvent e)		{ }
	@Override public void mousePressed(MouseEvent e)	{ }
	@Override public void mouseReleased(MouseEvent e)	{
		if ((e.getButton() > 3) || e.getClickCount() > 1)
			return;
		advanceScreen();
	}
}
