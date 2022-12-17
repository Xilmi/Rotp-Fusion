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

import java.awt.Point;
import java.awt.image.BufferedImage;
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
    private float[][] greyPD; // Star Map
    private float[][] redPD;  // Opponent Map
    private float[][] greenPD; // User Map
    private float[][] bluePD;  // Orion Map
    private float[][] starCD;
    private float[][] userCD;
    private float[][] orionCD;
    private float[][] nebulaeCD;
    private float[][][] alienCD;

	private int xBM, yBM, gEB;
	private int alienSize = 1;
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
		greyPD     = new float[yBM][xBM];
		float cx   = (-1.0f + xBM)/2.0f;
		float cy   = (-1.0f + yBM)/2.0f;
		float s2   = -2*sigma*sigma;

		for (int y=0; y<yBM; y++) {
			float dy2 = sqr(y-cy);
			for (int x=0; x<xBM; x++) {
				float r2 = dy2 + sqr(x-cx);
				greyPD[y][x] = (float) Math.exp(r2/s2);
			}
		}
	}
	private void invertPD(float[][] pD) {
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				pD[y][x] = 1-pD[y][x];
		normalizeToOne(pD);
	}
	private void normalizeToOne(float[][] pD) {
		float max = 0f;
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				max = max(max, pD[y][x]);
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				pD[y][x] /= max;
	}
	private float cumulativeDensity(float[][] pD, float[][] cD) {
		float volume = 0f;
		for (int y=0; y<yBM; y++) {
			for (int x=0; x<xBM; x++) {
				volume  += pD[y][x];
				cD[y][x] = volume;
			}
		}
		return volume;		
	}
	private void normalizeCDToOne(float[][] cD, float volume) {
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				cD[y][x] /= volume;
	}
	private void openFileAndNormalize(String path) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			genGaussian(GX, GY, GS);
			return;
		}
		yBM = image.getHeight();
		xBM = image.getWidth();
		if (yBM == 0 || xBM == 0) {
			genGaussian(GX, GY, GS);
			return;
		}
		greyPD = new float[yBM][xBM];
		if (isColor) {
			redPD   = new float[yBM][xBM];
			greenPD = new float[yBM][xBM];
			bluePD  = new float[yBM][xBM];
		}
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
		        int pixel   = image.getRGB(x, y);
		        float red   = (pixel >> 16) & 0xff;
		        float green = (pixel >> 8) & 0xff;
		        float blue  = (pixel) & 0xff;
		        float grey  = max(blue, max(red, green));
		        
		        greyPD[y][x] = grey/255;
				if (isColor) {
					redPD  [y][x] = red  /255;
					greenPD[y][x] = green/255;
					bluePD [y][x] = blue /255;
				}
		    }
		}
		normalizeToOne(greyPD);
		if (isColor) {
			normalizeToOne(redPD);
			normalizeToOne(greenPD);
			normalizeToOne(bluePD);
		}
	}
	private void setRandom(float[][] cD, Point.Float pt) {
		float src  = (float) rand.randX();
		float srcX = (float) rand.randX();
		float srcY = (float) rand.randX();
		
		int iX = xBM-1;
		int iY;
		for (iY=0; iY<yBM; iY++) {
			if(src < cD[iY][iX])
				break;
		}
		if (iY == yBM) // src = 1.0
			iY--;
		for (iX=0; iX<xBM; iX++) {
			if(src < cD[iY][iX])
				break;
		} 
		float x = (srcX + iX) / xBM;
		float y = (srcY + iY) / yBM;
		
        pt.x = offset + x * xMult;
        pt.y = offset + y * yMult;
//        System.out.println("pt.x = " + pt.x + "    pt.y = " + pt.y);
//        System.out.println("pt.x = " + pt.x + "    pt.y = " + pt.y);
	}
	private boolean initMultiple() { // TODDO BR: isMultiple
	    float[][] greyCD = new float[yBM][xBM];
	    
	    float vol = cumulativeDensity(greyPD, greyCD);
	    if (vol == 0)
	    	return false; // Not a valid Multiple
	    
	    // Locate black lines
	    List<Integer> blakLines = new ArrayList<>();
	    blakLines.add(-1); // No black lines required at start
	    int x = xBM-1;
	    float lastSum = 0f;
	    for (int y=0; y<yBM; y++) {
	    	float cs = greyCD[y][x];
	    	if (cs == lastSum)
	    		blakLines.add(y); // Found a new black line
	    	lastSum = cs;
	    }
	    blakLines.add(yBM);  // No black lines required at end

	    // Locate blocks
	    List<Integer> startList = new ArrayList<>();
	    List<Integer> stopList  = new ArrayList<>();
	    Integer lastBL = -1;
	    for (Integer bl : blakLines) {
	    	int diff = bl - lastBL;
	    	if (diff > 1) { // end of block
	    		startList.add(lastBL+1);
	    		stopList.add(bl);
	    	}
	    	lastBL = bl;
	    }
	    int blockCount = startList.size();

	    // StarMap
	    int start = startList.get(0);
	    yBM = stopList.get(0) - start;
	    
		starCD = new float[yBM][xBM];
		float[][] map = new float[yBM][xBM];
		for (int i=0; i<yBM; i++)
			map[i] = greenPD[i+start];
		volume = cumulativeDensity(map, starCD);
		if (volume == 0)
	    	return false; // Not a valid Multiple

		normalizeCDToOne(starCD, volume);
		alienCD    = new float[1][yBM][xBM];
		alienCD[0] = starCD;
		userCD     = starCD;
		orionCD    = starCD;

		// Nebulae Map
		nebulaeCD = new float[yBM][xBM];
		for (int i=0; i<yBM; i++)
			map[i] = redPD[i+start];
		float mapVol = cumulativeDensity(map, nebulaeCD);
		if (mapVol == 0) { // Empty ==> star map
			nebulaeCD = starCD;
		} else
			normalizeCDToOne(nebulaeCD, mapVol);
		
		// Orion Map
		int mapId = 1;
		if (blockCount == mapId)
	    	return true; // Incomplete, but valid Multiple
		orionCD = getSubMap(startList.get(mapId));
		
		// User Map
		mapId = 2;
		if (blockCount == mapId)
	    	return true; // Incomplete, but valid Multiple
		userCD = getSubMap(startList.get(mapId));

		// alien Map
		mapId = 3;
		if (blockCount == mapId)
	    	return true; // Incomplete, but valid Multiple
		alienSize = blockCount-mapId;
		alienCD = new float[alienSize][yBM][xBM];
		for (int i=0; i<alienSize; i++)
			alienCD[i] = getSubMap(startList.get(mapId+i));

		return true;
	}
	private float[][] alienCD(int id) {
		int idx = Math.floorMod(id, alienSize);
		return alienCD[idx];
	}
	private float[][] alienCD() {
		return alienCD[0];
	}
	private float[][] getSubMap(int start) {
		float[][] mapCD = new float[yBM][xBM];
		float[][] mapPD = new float[yBM][xBM];
		for (int i=0; i<yBM; i++)
			mapPD[i] = redPD[i+start];
		float mapVol = cumulativeDensity(mapPD, mapCD);
		if (mapVol == 0) // Empty ==> star map
			mapCD = starCD;
		else
			normalizeCDToOne(mapCD, mapVol);
		return mapCD;
	}
	@Override protected void singleInit(boolean full) {
		super.singleInit(full);
		// System.out.println("========== GalaxyBitmapShape.singleInit()");

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
	            isColor    = true;
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
			openFileAndNormalize(option1);

		if (isMultiple) {
			if (initMultiple()) { // TODDO BR: isMultiple
				aspectRatio   = (float) yBM / xBM;
		        shapeFactor   = sqrt(max(aspectRatio, 1/aspectRatio));
		        float volumeFactor = 1/volume *xBM*yBM;
		        densityFactor = (float) Math.pow(volumeFactor, 1.0/3.0);
		        adjustDensity = sqrt(shapeFactor * densityFactor);
				return;
			}
			else // Failed ==> default galaxy
				genGaussian(GX, GY, GS);
		}

		if (isInverted) // only for grey Maps
			invertPD(greyPD);

		// Normalize and validate bitmap
		starCD = new float[yBM][xBM];
		volume = cumulativeDensity(greyPD, starCD);
		if (volume == 0) { // Empty ==> default shape
			genGaussian(GX, GY, GS);
			volume = cumulativeDensity(greyPD, starCD);
		}
		normalizeCDToOne(starCD, volume);
		alienCD    = new float[1][yBM][xBM];
		alienCD[0] = starCD;
		userCD     = starCD;
		orionCD    = starCD;
		nebulaeCD  = starCD;

		if (isColor) {
			alienCD[0] = new float[yBM][xBM];
			float redVol = cumulativeDensity(redPD, alienCD());
			if (redVol == 0) { // Empty ==> star map
				redPD      = greyPD;
				alienCD[0] = starCD;
			} else
				normalizeCDToOne(alienCD(), redVol);

			userCD = new float[yBM][xBM];
			float greenVol = cumulativeDensity(greenPD, userCD);
			if (greenVol == 0) { // Empty ==> star map
				greenPD = greyPD;
				userCD  = starCD;
			} else
				normalizeCDToOne(userCD, greenVol);

			orionCD = new float[yBM][xBM];
			float blueVol = cumulativeDensity(bluePD, orionCD);
			if (blueVol == 0) { // Empty ==> star map
				bluePD  = greyPD;
				orionCD = starCD;
			} else
				normalizeCDToOne(orionCD, blueVol);
		}

		aspectRatio = (float) yBM / xBM;
        shapeFactor = sqrt(max(aspectRatio, 1/aspectRatio));
        float volumeFactor = 1/volume *xBM*yBM;
        densityFactor = (float) Math.pow(volumeFactor, 1.0/3.0);
        adjustDensity = sqrt(shapeFactor * densityFactor);
//		System.out.println("aspectRatio = " + aspectRatio);
//		System.out.println("shapeFactor = " + shapeFactor);
//		System.out.println("volumeFactor = " + volumeFactor);
//		System.out.println("densityFactor = " + densityFactor);
//		System.out.println("-- adjustDensity = " + adjustDensity);
//		System.out.println();
	}
	@Override public void clean() {
		greyPD  = null;
		redPD   = null;
		greenPD = null;
		bluePD  = null;
		starCD  = nebulaeCD;
	}
	@Override public List<String> options1()  { return options1; }
	@Override public List<String> options2()  { return options2; }
	@Override public String defaultOption1()  { return options1.get(0); }
	@Override public String defaultOption2()  { return options2.get(0); }
	@Override public void init(int n) {
		super.init(n);
		// System.out.println("========== GalaxyBitmapShape.init()");
		// reset w/h vars since aspect ratio may have changed
		initWidthHeight();
		offset = gEB;
		xMult  = width - 2*gEB;
		yMult  = height - 2*gEB;
//		System.out.println("gEB = " + gEB);
//		System.out.println("width = " + width + "  height = " + height);
//		System.out.println("xMult = " + xMult + "  yMult = " + yMult);
//		System.out.println("adjustedSizeFactor() = " + adjustedSizeFactor());
//		System.out.println();
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
	@Override public void setRandom(Point.Float pt) { setRandom(starCD, pt); }
	// modnar: add possibility for specific placement of homeworld/orion locations
	@Override public void setSpecific(Point.Float pt) { // TODO BR: test isColor
//		if (!isColor) {
//			setRandom(starCD, pt);
//			return;
//		}
		if (indexWorld == 0) { // orion
			setRandom(orionCD, pt);
			return;
        }
		if (empSystems.size() == 0) { // Player homeworld
    		setRandom(userCD, pt);
    		return;
    	}
		// Aliens homeworlds
   		setRandom(alienCD(empSystems.size()-1), pt);
	}
	@Override public boolean valid(float x, float y)  { return true; }
	@Override protected float sizeFactor(String size) { return settingsFactor(0.8f); }
}
