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

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import rotp.model.game.IGameOptions;

// modnar: custom map shape, Spiral Arms
public class GalaxyBitmapShape extends GalaxyShape {
	public static final List<String> options1;
	public static final List<String> options2;
	private static final long serialVersionUID = 1L;
	private static final int GX = 127;
	private static final int GY = 127;
	private static final float GS = 127f/6f;
	private static final String DEFAULT_OPTION_1 = "*";
	
	static {
		// BR: reordered to add straight and very loose
		options1 = new ArrayList<>();
		options1.add(DEFAULT_OPTION_1); // Straight
		options2 = new ArrayList<>();
		options2.add("SETUP_BITMAP_GREY_NORMAL");
		options2.add("SETUP_BITMAP_GREY_INVERSE");
		options2.add("SETUP_BITMAP_COLOR");
		options2.add("SETUP_BITMAP_MULTIPLE");
	}
	private float adjustDensity = 1.0f;
    private float aspectRatio   = 1.0f;
    private float shapeFactor   = sqrt(aspectRatio);
    private float densityFactor = 1.0f;
    private float[][] greyMap;
    private float[][] redMap;
    private float[][] greenMap;
    private float[][] blueMap;
    private float[][] greyMapCS;
    private float[][] redMapCS;
    private float[][] greenMapCS;
    private float[][] blueMapCS;
	private float[] greyYCS;
	private float[] redYCS;
	private float[] greenYCS;
	private float[] blueYCS;

	private int xBM, yBM, gEB;
	private float offset, xMult, yMult, volume;
	private boolean isInverted = false;
	private boolean isColor    = false;
	private boolean isMultiple = false;

	public GalaxyBitmapShape(IGameOptions options) {
		opts = options;
		gEB = galaxyEdgeBuffer();
	}
	private float sqr(float x) { return x*x;}
	private void genGaussian(int sx, int sy, float sigma) {
		// Bad file, Generate a working grey Map
		xBM = sx;
		yBM = sy;
        isColor    = false;
        isMultiple = false;
		greyMap    = new float[yBM][xBM];
		float cx   = (-1.0f + xBM)/2.0f;
		float cy   = (-1.0f + yBM)/2.0f;
		float s2   = -2*sigma*sigma;

		for (int y=0; y<yBM; y++) {
			float dy2 = sqr(y-cy);
			for (int x=0; x<xBM; x++) {
				float r2 = dy2 + sqr(x-cx);
				greyMap[y][x] = (float) Math.exp(r2/s2);
			}
		}
	}
	private void invertBitmap(float[][] bitmap) {
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				bitmap[y][x] = 1-bitmap[y][x];
		normalizeToOne(bitmap);
	}
	private void normalizeToOne(float[][] bitmap) {
		float max = 0f;
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				max = max(max, bitmap[y][x]);
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				bitmap[y][x] /= max;
	}
	private float cumulativeSum(float[][] bitmap, float[][] mapCumSum, float[] yCumSum) {
		float volume = 0f;
		for (int y=0; y<yBM; y++) {
			for (int x=0; x<xBM; x++) {
				volume  += bitmap[y][x];
				mapCumSum[y][x] = volume;
			}
			yCumSum[y]  = volume;
		}
		return volume;		
	}
	private void normalizeCumSumToOne(float[][] mapCumSum, float[] yCumSum, float volume) {
		for (int y=0; y<yBM; y++) {
			for (int x=0; x<xBM; x++) {
				mapCumSum[y][x] /= volume;
			}
			yCumSum[y] /= volume;
		}
	}
	private void openFile(String path) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			genGaussian(GX, GY, GS);
			return;
		}
		yBM = image.getHeight();
		xBM = image.getWidth();
		greyMap  = new float[yBM][xBM];
		if (isColor) {
			redMap   = new float[yBM][xBM];
			greenMap = new float[yBM][xBM];
			blueMap  = new float[yBM][xBM];
		}
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
		        int pixel   = image.getRGB(x, y);
		        float red   = (pixel >> 16) & 0xff;
		        float green = (pixel >> 8) & 0xff;
		        float blue  = (pixel) & 0xff;
		        float grey  = max(blue, max(red, green));
		        
		        greyMap[y][x] = grey/255;
				if (isColor) {
					redMap  [y][x] = red  /255;
					greenMap[y][x] = green/255;
					blueMap [y][x] = blue /255;
				}
		    }
		}
		normalizeToOne(greyMap);
		if (isColor) {
			normalizeToOne(redMap);
			normalizeToOne(greenMap);
			normalizeToOne(blueMap);
		}
		
		System.out.println("width = " + image.getWidth());
		System.out.println("height = " + image.getHeight());
		System.out.println();
	}
	@Override protected void singleInit(boolean full) {
		super.singleInit(full);
		System.out.println("========== GalaxyBitmapShape.singleInit()");

        switch (opts.selectedGalaxyShapeOption2()) {
	        case "SETUP_BITMAP_GREY_INVERSE":
	        	isInverted = true;
	            isColor    = false;
	            isMultiple = false;
	        	// invertBitmap(greyMap);
	        	break;
	        case "SETUP_BITMAP_COLOR": 
	            isInverted = false;
	            isColor    = true;
	            isMultiple = false;
	        	break;
	        case "SETUP_BITMAP_MULTIPLE":
	            isInverted = false;
	            isColor    = false;
	            isMultiple = true;
	        	break;
	        case "SETUP_BITMAP_GREY_NORMAL": 
        	default:
	            isInverted = false;
	            isColor    = false;
	            isMultiple = false;
        }

        // Get bitmap (Normalized yo One)
		String option1 = opts.selectedGalaxyShapeOption1();
		if(option1==null || option1.equals(DEFAULT_OPTION_1))
			genGaussian(GX, GY, GS);
		else
			openFile(option1); // TODO BR: Load Bitmap

		if (isInverted) // only for grey Maps
			invertBitmap(greyMap);

		if (isMultiple) {
			// TODDO BR: isMultiple
		}
		// Normalize and validate bitmap
		greyMapCS = new float[yBM][xBM];
		greyYCS  = new float[yBM];
		volume = cumulativeSum(greyMap, greyMapCS, greyYCS);
		if (volume == 0) { // Empty ==> default shape
			genGaussian(GX, GY, GS);
			volume = cumulativeSum(greyMap, greyMapCS, greyYCS);
		}
		normalizeCumSumToOne(greyMapCS, greyYCS, volume);
		if (isColor) {
			float redVol = cumulativeSum(redMap, redMapCS, redYCS);
			if (redVol == 0) { // Empty ==> default shape
				redMap = greyMap;
				redMapCS  = greyMapCS;
				redYCS   = greyYCS;
			} else
				normalizeCumSumToOne(redMapCS, redYCS, redVol);

			float greenVol = cumulativeSum(greenMap, greenMapCS, greenYCS);
			if (greenVol == 0) { // Empty ==> default shape
				greenMap = greyMap;
				greenMapCS  = greyMapCS;
				greenYCS   = greyYCS;
			} else
				normalizeCumSumToOne(greenMapCS, greenYCS, greenVol);

			float blueVol = cumulativeSum(blueMap, blueMapCS, blueYCS);
			if (blueVol == 0) { // Empty ==> default shape
				blueMap = greyMap;
				blueMapCS  = greyMapCS;
				blueYCS   = greyYCS;
			} else
				normalizeCumSumToOne(blueMapCS, blueYCS, blueVol);
		}

		aspectRatio   = (float) yBM / xBM;
        shapeFactor   = sqrt(max(aspectRatio, 1/aspectRatio));
        float volumeFactor = 1/volume *xBM*yBM;
        densityFactor = (float) Math.pow(volumeFactor, 1.0/3.0);
        adjustDensity = sqrt(shapeFactor * densityFactor);
		System.out.println("aspectRatio = " + aspectRatio);
		System.out.println("shapeFactor = " + shapeFactor);
		System.out.println("volumeFactor = " + volumeFactor);
		System.out.println("densityFactor = " + densityFactor);
		System.out.println("-- adjustDensity = " + adjustDensity);
		System.out.println();
	}
	@Override public void clean() {}
	@Override public List<String> options1()  { return options1; }
	@Override public List<String> options2()  { return options2; }
	@Override public String defaultOption1()  { return options1.get(0); }
	@Override public String defaultOption2()  { return options2.get(0); }
	@Override public void init(int n) {
		super.init(n);
		System.out.println("========== GalaxyBitmapShape.init()");
		// reset w/h vars since aspect ratio may have changed
		initWidthHeight();
		offset = gEB;
		xMult  = width - 2*gEB;
		yMult  = height - 2*gEB;
		System.out.println("gEB = " + gEB);
		System.out.println("width = " + width + "  height = " + height);
		System.out.println("xMult = " + xMult + "  yMult = " + yMult);
		System.out.println("adjustedSizeFactor() = " + adjustedSizeFactor());
		System.out.println();
	}
	@Override public float maxScaleAdj()	{ return 1.1f; }
	// BR: added adjust_density for the void in symmetric galaxies
	@Override protected int galaxyWidthLY() { 
		return (int) (Math.sqrt(opts.numberStarSystems()*adjustDensity*adjustedSizeFactor()));
	}
	// BR: added adjust_density for the void in symmetric galaxies
	@Override protected int galaxyHeightLY() { 
		return (int) (Math.sqrt(opts.numberStarSystems()*adjustDensity*adjustedSizeFactor()));
	}
	@Override public void setRandom(Point.Float pt) {
		float src  = (float) rand.rand();
		float srcX = (float) rand.randX();
		float srcY = (float) rand.randY();
		
		int iX, iY;
		for (iY=0; iY<yBM; iY++) {
			if(src < greyYCS[iY])
				break;
		}
		if (iY == yBM) // src = 1.0
			iY--;
		for (iX=0; iX<xBM; iX++) {
			if(src < greyMapCS[iY][iX])
				break;
		} 
		float x = (srcX + iX) / xBM;
		float y = (srcY + iY) / yBM;
		
        pt.x = offset + x * xMult;
        pt.y = offset + y * yMult;
	}
	// modnar: add possibility for specific placement of homeworld/orion locations
	@Override public void setSpecific(Point.Float pt) { // TODO BR: test isColor
		setRandom(pt);
	}
	@Override public boolean valid(float x, float y)  { return true; }
	@Override protected float sizeFactor(String size) { return settingsFactor(0.8f); }
}
