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
	private static final int NUM_BY_ICON = 10;

	private Image landscapeImg;
	private boolean landscapeOnly = false;
	private boolean exited = false;
	private int sysId;
	private int iconWidth, barSep, blockSep, rowSep, colSep, maxColumns;
	private int factoryWidth, factoryHeight, factoryNum, factoryCols, factoryRows, factoryBars;
	private int speciesWidth, speciesHeight, speciesNum, speciesRows;
	private int missileWidth, missileHeight, missileNum, missileCols, missileRows;
	private BufferedImage factoryImg, speciesImg, missileImg;
	private String ambienceKey;

	public ColonyViewUI()		{ init(); }
	private void init()			{
		setBackground(Color.black);
		addMouseListener(this);
	}
	public void init(int sysId)	{
		this.sysId		= sysId;
		StarSystem sys	= galaxy().system(sysId);
		Colony colony	= sys.colony();
		exited			= false;
		Race race		= sys.empire().race();
		ambienceKey		= race.ambienceKey;
		float pop		= colony.population();
		float bases		= colony.defense().bases();
		float factories	= colony.industry().factories();
		factoryRows		= colony.industry().effectiveRobotControls();
		maxColumns		= 10;
		iconWidth		= s80;
		blockSep		= s20;
		barSep			= s10;
		rowSep			= -scaled(1*factoryRows);
		initDisplayVar(pop, bases, factories);
		if (speciesRows>2 || (factoryBars > 1 && factoryRows>4)) {
			maxColumns	= 15;
			iconWidth	= s56;
			blockSep	= s10;
			initDisplayVar(pop, bases, factories);
		}
		if (factoryBars > 1) {
			blockSep = blockSep *3/4;
		}
		colSep = 0;

		initFactoryImage();
		initMissileImage();
		initSpeciesImage(race);
		createImage(false);
	}
	private void initDisplayVar(float pop, float bases, float factories)	{
		speciesNum	= (int) Math.ceil(pop/NUM_BY_ICON);
		speciesRows	= (int) Math.ceil((double)speciesNum/maxColumns);
		missileNum	= (int) Math.ceil(bases/NUM_BY_ICON);
		missileCols	= (int) Math.ceil((double)missileNum/maxColumns);
		if (missileNum == 0)
			missileRows	= 0;
		else
			missileRows	= (int) Math.ceil((double)missileNum/missileCols);
		factoryNum	= (int) Math.ceil(factories/NUM_BY_ICON);
		factoryCols	= (int) Math.ceil((double)factoryNum/factoryRows);
		factoryBars	= (int) Math.ceil((double)factoryCols/maxColumns);
	}
	private void initSpeciesImage(Race race)	{
		speciesWidth  = iconWidth;
		speciesHeight = speciesWidth;
		int spH = speciesWidth*8/10;
		int spW = spH;
		int[] iHue = new int[] {180};
		BufferedImage mugshot = race.diplomatQuiet();
		int mW	= mugshot.getWidth(null);
		int mH	= mugshot.getHeight(null);
		int mX1	= 0;
		int mX2	= mX1 + mH;
		int mW2	= mX2 - mX1;
		int spW2 = spW*mW2/mW;

		BufferedImage[] mugHue = setHue(mugshot, iHue, 96);
		speciesImg    = newBufferedImage(speciesWidth, speciesHeight);
		Graphics2D g  = (Graphics2D) speciesImg.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		int x = 0;
		int y = 0;
		g.drawImage(mugshot, x, y, x+spW2, y+spH, mX1, 0, mX2, mH, null);
		x = speciesWidth-spW2;
		y = (speciesHeight-spH)/3;
		g.drawImage(mugshot, x, y, x+spW2, y+spH, mX2, 0, mX1, mH, null);
		g.drawImage(mugHue[0], x, y, x+spW2, y+spH, mX2, 0, mX1, mH, null);
		x = x/2;
		y = speciesHeight-spH;

		mugshot = race.scientistQuiet();
		mW	= mugshot.getWidth(null);
		mH	= mugshot.getHeight(null);
		mX1	= 0;
		mX2	= mX1 + mH;
		mW2	= mX2 - mX1;
		spW2 = spW*mW2/mW;
		g.drawImage(mugshot, x, y, x+spW2, y+spH, mX1, 0, mX2, mH, null);

		g.dispose();
	}
/*	private void initSpeciesImage2(Race race)	{
		speciesWidth  = iconWidth;
		speciesHeight = speciesWidth;
		int spW = speciesWidth*4/8;
		int spH = spW * 41/38;
		int[] iHue = new int[] {0, 90, 180};
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
		x = speciesWidth-spW;
		y = spH/5;
		g.drawImage(mugshot, x, y, x+spW, y+spH, 0, 0, mW, mH, null);
		g.drawImage(mugHue[1], x, y, x+spW, y+spH, 0, 0, mW, mH, null);
		x = x/3;
		y = speciesHeight-spH;
		g.drawImage(mugshot, x, y, x+spW, y+spH, 0, 0, mW, mH, null);
		g.drawImage(mugHue[2], x, y, x+spW, y+spH, 0, 0, mW, mH, null);

		g.dispose();
	} */
	private BufferedImage rect(int w, int h, Color c, Double angleDeg) {
		BufferedImage img = new BufferedImage(w, h, TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(c);
		g.fillRect(0, 0, w, h);
		return rotate(img, angleDeg);
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
	private void initMissileImage()				{
		missileWidth  = iconWidth*3/4;
		missileHeight = missileWidth;
		int w0	= 512;
		int h0	= w0;
		BufferedImage image = new BufferedImage(w0, h0, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		int wheelRadius = 40;
		// Frame
		int frX = 0;
		int frGap = wheelRadius * 3/2;
		int frH = 44;
		int frLowY = h0 - frGap;
		int frTopY = frLowY - frH;
		int frW = w0;

		// Holder
		Color darkGreen = new Color(75, 83, 32);
		int holdH = 48;
		int holdL = 300;
		BufferedImage holder = rect(holdL, holdH, darkGreen, -45.0);
		int hW = holder.getWidth();
		int hH = holder.getHeight();
		int hX = 160;
		int hY = frLowY - hH;
		g.drawImage(holder, hX, hY, hX+hW, hY+hH, 0, 0, hW, hH, null);
		int hold2L = 220;
		int hold2H = 24;
		holder = rect(hold2L, hold2H, darkGreen, -70.0);
		hW = holder.getWidth();
		hH = holder.getHeight();
		hX = 290;
		hY = frLowY - hH;
		g.drawImage(holder, hX, hY, hX+hW, hY+hH, 0, 0, hW, hH, null);

		// Cabin
		int cabH = 120 + frH;
		int cabW = 120;
		int cabX = w0 - cabW;
		int cabY = frLowY - cabH;
		int arcW  = 40;
		int arcH  = arcW * 3;
		g.setColor(darkGreen);
		g.fillRoundRect(cabX, cabY, cabW, cabH, arcW, arcH);

		// frame
		g.setColor(darkGreen);
		g.fillRect(frX, frTopY, frW-arcW, frH);
		
		// Windows
		int winH = 48;
		int winW = cabW * 2/3;
		int winX = w0 - winW - 4;
		int winY = cabY + 32;
		//g.setColor(Color.GRAY);
		g.setColor(new Color(80, 80, 112));
		g.fillRect(winX, winY, winW, winH);
		
		// Wheels
		int wheelNum = 5;
		int wheelDia = 2 * wheelRadius;
		int wheelSep = w0/(wheelNum);
		int wheelX   = wheelSep/2 - wheelRadius;
		int wheelY   = h0 - wheelDia;
		int missing  = 6;
		g.setColor(Color.BLACK);
		for (int i=0; i<wheelNum; i++)
			if (i!=missing)
				g.fillRoundRect(wheelX+wheelSep*i, wheelY, wheelDia, wheelDia, wheelDia, wheelDia);
		int tireH  = wheelRadius/2;
		int rimDia = wheelDia - 2*tireH;
		int rimY   = wheelY + tireH;
		int rimX   = wheelX + tireH;
		g.setColor(Color.GRAY);
		for (int i=0; i<wheelNum; i++)
			if (i!=missing)
				g.fillRoundRect(rimX+wheelSep*i, rimY, rimDia, rimDia, rimDia, rimDia);
		int axeDia = 32;
		int dR     = (wheelDia-axeDia)/2;
		int axeY   = wheelY + dR;
		int axeX   = wheelX + dR;
		g.setColor(darkGreen);
		for (int i=0; i<wheelNum; i++)
			if (i!=missing)
				g.fillRoundRect(axeX+wheelSep*i, axeY, axeDia, axeDia, axeDia, axeDia);

		
		// Missiles
		int missileNum = 3;
		int missileLen = 360;
		BufferedImage rotMissile = rotate(missile(missileLen), -45.0);
		int rW = rotMissile.getWidth();
		int rH = rotMissile.getHeight();
		int missileDx = 45;
		int missileDy = missileDx;
		int missileX  = 0;
		int missileY  = 0;
		int mX = missileX;
		int mY = missileY;
		for (int i=0; i<missileNum; i++) {
			g.drawImage(rotMissile, mX, mY, mX+rW, mY+rH, 0, 0, rW, rH, null);
			mX += missileDx;
			mY += missileDy;
		}
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
	private BufferedImage dome(int radius)		{
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
	private void initFactoryImage()				{
		factoryWidth  = iconWidth * 2/4;
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
	private void createImage(boolean bgOnly)	{
		int w = getWidth();
		int h = getHeight();
		StarSystem sys = galaxy().system(sysId);
		Colony colony  = sys.colony();
		//Empire empire = sys.empire();
		Race race = sys.empire().race();
		landscapeImg = newBufferedImage(w,h);
		Graphics2D g = (Graphics2D) landscapeImg.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(colony.planet().type().atmosphereImage(), 0, 0, w, h, null);
		g.drawImage(colony.planet().type().randomCloudImage(), 0, 0, w, h, null);
		g.drawImage(colony.planet().landscapeImage(), 0, 0, w, h, null);

		// draw fortress
		BufferedImage fortImg = race.fortress(sys.colony().fortressNum());
		//BufferedImage fortImg = race.fortress(0);
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

		if (bgOnly) {
			g.dispose();
			return;
		}

		int marginX	= s10;
		int marginY	= marginX;
		int dx		= iconWidth + colSep;

		// draw pop
		int xSpecies = marginX+dx/8;
		int ySpecies = h - marginY - speciesRows*(speciesHeight + rowSep) + rowSep;
		int xa = xSpecies;
		int ya = ySpecies;
		for (int row=speciesRows-1; row>=0; row--) {
			if (row%2==0)
				xa = xSpecies;
			else
				xa = xSpecies + dx/2;
			int colLim = min (maxColumns, speciesNum - row*maxColumns);
			for (int col=0; col<colLim; col++) {
				g.drawImage(speciesImg, xa, ya, xa+speciesWidth, ya+speciesHeight, 0, 0, speciesWidth, speciesHeight, null);
				xa += dx;
			}
			ya += speciesHeight + rowSep;
		}
		
		// draw Factories
		int xFactory = marginX;
		int yFactory = ySpecies - factoryBars*(factoryRows*(factoryHeight+rowSep)-rowSep+blockSep);
		ya = yFactory;
		int factoryPerBar = maxColumns * factoryRows;
		for (int bar=factoryBars-1; bar>=0; bar--) {
			int factLim	= min(factoryPerBar, factoryNum - bar*factoryPerBar);
			int numCol	= min (maxColumns, factoryCols - bar*maxColumns);
			int rowTop	= min(factoryRows, factLim)-1;
			int yBar = ya;
			
			for (int col=0; (col<numCol) && (factLim>=0); col++) {
				int rowStart = min(factoryRows, factLim)-1;
				for (int row=rowStart; row>=0; row--) {
					ya = yBar + (rowTop-row) * (factoryHeight + rowSep);
					if (row%2==0)
						xa = xFactory + col*dx;
					else
						xa = xFactory + col*dx + dx/2;
					g.drawImage(factoryImg, xa, ya, xa+factoryWidth, ya+factoryHeight, 0, 0, factoryWidth, factoryHeight, null);
					factLim--;
					ya += factoryHeight + rowSep;
				}
			}
			ya += barSep - rowSep;
		}
		
		// draw Missiles
		int xMissile = marginX;
		int yMissile = yFactory - missileHeight-blockSep;
		ya = yMissile;
		xa = marginX;
		for (int row=missileRows-1; row>=0; row--) {
			if (row%2==0)
				xa = xMissile;
			else
				xa = xMissile + dx/2;
			int colLim = min (maxColumns, missileNum - row*maxColumns);
			for (int col=0; col<colLim; col++) {
				g.drawImage(missileImg, xa, ya, xa+missileWidth, ya+missileHeight, 0, 0, missileWidth, missileHeight, null);
				xa += dx;
			}
			ya += missileHeight + rowSep;
		}

//		// Draw test
//		int tstH  = s64;
//		int tstW  = tstH;
//		int yTest = yMissiles - tstH-s10;
//		ya = yTest;
//		int yb = yTest-tstH-s10;
//		x0 = marginX;
//		for (Race species : Race.races()) {
//			initSpeciesImage(species);
//			int dW = speciesImg.getWidth();
//			int dH = speciesImg.getHeight();
//			int dSize = max(dW,dH);
//			int w2 = tstW * dW/dSize;
//			int h2 = tstH * dH/dSize;
//			g.drawImage(speciesImg, x0, ya, x0+w2, ya+h2, 0, 0, dW, dH, null);
//
////			initSpeciesImage2(species);
////			dW = speciesImg.getWidth();
////			dH = speciesImg.getHeight();
////			dSize = max(dW,dH);
////			w2 = tstW * dW/dSize;
////			h2 = tstH * dH/dSize;
////			g.drawImage(speciesImg, x0, yb, x0+w2, yb+h2, 0, 0, dW, dH, null);
//			x0 += w2+s5;
//		}
		

		// draw subtitle last (so it overlays any ship)
		int y0 = s30;
		String subtitle = text(sys.planet().type().description(sys.empire()));
		g.setFont(narrowFont(24));
		int sw = g.getFontMetrics().stringWidth(subtitle);
		int x0 = (w-sw)/2;
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
	private void advanceScreen()				{
		exited = true;
		buttonClick();
		RotPUI.instance().selectMainPanel(false);
		landscapeImg = null;
		factoryImg = null;
		speciesImg = null;
		missileImg = null;
	}
	@Override public String ambienceSoundKey()			{ return ambienceKey; }
	@Override public void keyPressed(KeyEvent e)		{
		int k = e.getKeyCode();

		switch(k) {
			case KeyEvent.VK_Z:
				landscapeOnly = !landscapeOnly;
				createImage(landscapeOnly);
				repaint();
				break;
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_SPACE:
				advanceScreen();
		}
	}
	@Override public void paintComponent(Graphics g0)	{
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(landscapeImg, 0, 0, null);
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
