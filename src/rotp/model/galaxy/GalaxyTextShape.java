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
package rotp.model.galaxy;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import rotp.model.game.IGameOptions;

// modnar: custom map shape, Text
public class GalaxyTextShape extends GalaxyShape {
	private static final long serialVersionUID = 1L;
	public static final List<String> options1;
	public static final List<String> options2;
	static {
		options1 = new ArrayList<>();
		options1.add("ROTP"); // For the initial setting
		options2 = new ArrayList<>();
		options2.add("SETUP_1_LINE");
		options2.add("SETUP_2_LINE");
		options2.add("SETUP_3_LINE");
		options2.add("SETUP_MULTI_LINE_LEFT");
		options2.add("SETUP_MULTI_LINE_CENTER");
		options2.add("SETUP_MULTI_LINE_RIGHT");
	}

    private float aspectRatio;
    private float shapeFactor;
    private double textW;
    private double textH;
	private Area textShape;
    private Font font;
	private int  lineSpacing = 5;
	
	private Font font() {
    	if (font == null) {
			font = galaxyFont(96);
    	}
    	return font;
    }
	public GalaxyTextShape(IGameOptions options)		{ super(options); }
	private Area getArea(Graphics2D g2, String str)		{ return new Area(getShape(g2, str)); }
	private Shape getShape(Graphics2D g2, String str)	{
		Shape sh =  font().createGlyphVector(g2.getFontRenderContext(), str).getOutline();
		Rectangle bounds = sh.getBounds();
		int dx = -bounds.x;
		int dy = -bounds.y;
		if (option2 == 5)
			dx -= bounds.width;
		else if (option2 == 4)
			dx -= bounds.width/2;
		AffineTransform moveText = new AffineTransform();
		moveText.translate(dx, dy);
		sh = moveText.createTransformedShape(sh);
		return sh;
	}
	private void initTextArea (Graphics2D g2, String s)	{
		if (s.trim().isEmpty()) {
			s = "!!!Blank!!!";
			textShape = getArea(g2, s);
			return;
		}
		else if (!s.contains(System.lineSeparator())) {
			textShape = getArea(g2, s);
			return;
		}
		textShape = new Area();
		boolean isEmpty	= true;
		System.out.println();
		String[] txtArr	= s.split(System.lineSeparator());
		int moveY = 0;
		for (String txt : txtArr) {
			if (!txt.isEmpty()) {
				Shape sh = getShape(g2, txt);
				Rectangle bounds = sh.getBounds();
				int h = bounds.height;
				if (!isEmpty) {
					AffineTransform moveText = new AffineTransform();
					moveText.translate(0, moveY);
					sh = moveText.createTransformedShape(sh);
				}
				textShape.add(new Area(sh));
				moveY += h;
				isEmpty = false;
			}
			moveY += lineSpacing;
			isEmpty &= txt.isEmpty();
		}
		if (isEmpty) {
			s = "!!!Blank!!!";
			textShape = getArea(g2, s);
			return;
		}
	}
	@Override protected float   minEmpireFactor()		{ return 4f; }
	@Override protected boolean allowExtendedPreview()	{ return false; }
	@Override public void clean()				{ font = null; }
	@Override public List<String> options1()	{ return options1; }
	@Override public List<String> options2()	{ return options2; }
	@Override public String defaultOption1()	{ return options1.get(0); }
	@Override public String defaultOption2()	{ return options2.get(0); }
	@Override public void init(int n)	{
		super.init(n);
		lineSpacing = options().selectedShapeLineSpacing();

		BufferedImage img = new BufferedImage(16, 10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		initTextArea (g2, finalOption1);

		// modnar: choose number of times to repeat text string with option2
		switch(option2) {
			case 1: {
				// repeat twice, 2 lines, with some in-between spacing
				Rectangle bounds = textShape.getBounds();
				AffineTransform moveText = new AffineTransform();
				moveText.translate(0, bounds.height + lineSpacing);
				Shape sh = moveText.createTransformedShape(textShape);
				textShape.add(new Area(sh));
				break;
			}
			case 2: {
				// repeat thrice, 3 lines, with some in-between spacing
				Rectangle bounds = textShape.getBounds();
				AffineTransform moveText = new AffineTransform();
				moveText.translate(0, bounds.height + lineSpacing);
				Shape sh = moveText.createTransformedShape(textShape);
				textShape.add(new Area(sh));
				sh = moveText.createTransformedShape(textShape);
				textShape.add(new Area(sh));
				break;
			}
		}
		textW = textShape.getBounds().getWidth();
		textH = textShape.getBounds().getHeight();

		// set galaxy aspect ratio to the textShape aspect ratio
		// this accommodates very long or short text strings
		// for multi-line texts, use adjust_line
        aspectRatio = (float) (textW / textH);
        shapeFactor = sqrt(max(aspectRatio, 1/aspectRatio));

        // reset w/h vars since aspect ratio may have changed
        initWidthHeight();

		// rescale textShape to fit galaxy map, then move into map center
		AffineTransform scaleText = new AffineTransform();
		AffineTransform moveText = new AffineTransform();

		// rescale
		double zoom;
		if (shapeFactor > 1.0) // Use Zoom X
			zoom = galaxyWidthLY() / textW;
		else // Use Zoom Y
			zoom = galaxyHeightLY() / textH;
		zoom = Math.max(0.1	, zoom); // min result in too munch attempt!
		scaleText.scale(zoom, zoom);
		textShape = new Area(scaleText.createTransformedShape(textShape));

		// recenter with multiple lines
		double oldX = textShape.getBounds().getX();
		double oldY = textShape.getBounds().getY();
        double moveX = (galaxyWidthLY()-textShape.getBounds().getWidth())/2 - oldX + galaxyEdgeBuffer();
        double moveY = (galaxyHeightLY()-textShape.getBounds().getHeight())/2 - oldY + galaxyEdgeBuffer();
		moveText.translate(moveX, moveY);
		textShape = new Area(moveText.createTransformedShape(textShape));
	}

    @Override public float maxScaleAdj()		{ return 0.95f; }
    @Override protected int galaxyWidthLY()		{ 
        return (int) (Math.sqrt(1.4f*aspectRatio*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override protected int galaxyHeightLY()	{
        return (int) (Math.sqrt(1.4f*(1/aspectRatio)*opts.numberStarSystems()*adjustedSizeFactor()));
    }
	@Override public void setSpecific(Point.Float pt)	{ setRandom(pt); }
	@Override public boolean valid(float x, float y)	{ return textShape.contains(x, y); }
	@Override protected float sizeFactor(String size)	{ return settingsFactor(1.0f); }
}
