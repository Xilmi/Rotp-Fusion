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
import rotp.ui.UserPreferences;

// modnar: custom map shape, Spiral Arms
public class GalaxyBitmapShape extends GalaxyShape {
	public static final List<String> options1;
	public static final List<String> options2;
	private static final long serialVersionUID = 1L;
	private static final int GX = 127;
	private static final int GY = 127;
	private static final float GS = 127f/6f;
//	private static final String DEFAULT_OPTION_2 = "";
	private static enum Option1Enum {
		SETUP_BITMAP_GREY_NORMAL,
		SETUP_BITMAP_GREY_INVERSE,
		SETUP_BITMAP_COLOR,
		SETUP_BITMAP_ADVANCED
	}
	private static enum Option2Enum {
		SETUP_BITMAP_NORMAL,
		SETUP_BITMAP_SHARP1,
		SETUP_BITMAP_SHARP2,
		SETUP_BITMAP_SHARP3,
		SETUP_BITMAP_SHARP4,
	}
	
	static {
		options1 = new ArrayList<>();
		for (Option1Enum o : Option1Enum.values())
			options1.add(o.toString());
		options2 = new ArrayList<>();
		for (Option2Enum o : Option2Enum.values())
			options2.add(o.toString());
//		options2.add(DEFAULT_OPTION_2); // Straight
	}
	private float adjustDensity;
    private float aspectRatio;
    private float shapeFactor;
    private float densityFactor;
    private float[][] greyPD; // Star Map
    private float[][] redPD;  // Opponent Map
    private float[][] greenPD; // User Map
    private float[][] bluePD;  // Orion Map
    private float[][] starCD;
    private float[][] userCD;
    private float[][] orionCD;
    private float[][] nebulaeCD;
    private float[][][] alienCD;

	private int xBM, yBM;
	private int alienSize;
	private int sharpNb;
	private float offset, xMult, yMult, volume;
	private boolean isInverted, isColor, isMultiple;
	private boolean isSharp;

	public GalaxyBitmapShape(IGameOptions options) {
		opts = options;
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
	private void sharpenPD(float[][] pD) {
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				pD[y][x] *= pD[y][x];
	}
	private void invertPD(float[][] pD) {
		for (int y=0; y<yBM; y++)
			for (int x=0; x<xBM; x++)
				pD[y][x] = 1-pD[y][x];
		normalizeToOne(pD);
	}
	private void normalizeToOne(float[][] pD) {
		float max = Float.MIN_VALUE; // to avoid division by 0
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
			// e.printStackTrace();
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
		float source = (float) rand.randD5();
		int iX = xBM-1;
		int iY;
		for (iY=0; iY<yBM; iY++) {
			if(source < cD[iY][iX])
				break;
		}
		if (iY == yBM) // source = 1.0
			iY--;
		for (iX=0; iX<xBM; iX++) {
			if(source < cD[iY][iX])
				break;
		} 
		if (iX == xBM) // source = 1.0
			iX--;
		float x = iX + (float) rand.randD5();
		float y = iY + (float) rand.randD5();
		
        pt.x = offset + x * xMult;
        pt.y = offset + y * yMult;
//        System.out.println("pt.x = " + pt.x + "    pt.y = " + pt.y);
//        System.out.println("pt.x = " + pt.x + "    pt.y = " + pt.y);
	}
	private boolean initMultiple() {
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
		float[][] mapPD = new float[yBM][xBM];
		for (int i=0; i<yBM; i++)
			mapPD[i] = greenPD[i+start];
		normalizeToOne(mapPD);
		processOption2(mapPD);
		volume = cumulativeDensity(mapPD, starCD);
		if (volume == 0)
	    	return false; // Not a valid Multiple
		normalizeCDToOne(starCD, volume);
		alienCD    = new float[1][yBM][xBM];
		alienCD[0] = starCD;
		userCD     = starCD;
		orionCD    = starCD;
		nebulaeCD  = starCD;

		// Nebulae Map
		int mapId = 1;
		if (blockCount == mapId)
	    	return true; // Incomplete, but valid Multiple
		nebulaeCD = getSubMap(startList.get(mapId));
		
		// Orion Map
		mapId += 1;
		if (blockCount == mapId)
	    	return true; // Incomplete, but valid Multiple
		orionCD = getSubMap(startList.get(mapId));
		
		// User Map
		mapId += 1;
		if (blockCount == mapId)
	    	return true; // Incomplete, but valid Multiple
		userCD = getSubMap(startList.get(mapId));

		// alien Map
		mapId += 1;
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
		// System.out.println("alienCD(int id): alienSize= " + alienSize + "  id= " + id + " idx= " + idx);
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
		normalizeToOne(mapPD);
		processOption2(mapPD);
		float mapVol = cumulativeDensity(mapPD, mapCD);
		if (mapVol == 0) // Empty ==> star map
			mapCD = starCD;
		else
			normalizeCDToOne(mapCD, mapVol);
		return mapCD;
	}
	private void processOption2(float[][] pD) {
		if (isInverted)
			invertPD(pD);
		if (isSharp)
			for (int i=0; i<sharpNb; i++)
				sharpenPD(pD);
	}
	@Override protected void singleInit(boolean full) {
		super.singleInit(full);
		// System.out.println("========== GalaxyBitmapShape.singleInit()");
		alienSize = 1;
		String option1 = opts.selectedGalaxyShapeOption1();
		Option1Enum opt1 = Option1Enum.values()[0];
		for (Option1Enum o1 : Option1Enum.values())
			if(option1.endsWith(o1.toString())) {
				opt1 = o1;
				break;
			}
		switch (opt1) {
	        case SETUP_BITMAP_GREY_INVERSE:
	        	isInverted = true;
	            isColor    = false;
	            isMultiple = false;
	        	break;
	        case SETUP_BITMAP_COLOR:
	            isInverted = false;
	            isColor    = true;
	            isMultiple = false;
	        	break;
	        case SETUP_BITMAP_ADVANCED:
	            isInverted = false;
	            isColor    = true;
	            isMultiple = true;
	        	break;
	        case SETUP_BITMAP_GREY_NORMAL:
	    	default:
	            isInverted = false;
	            isColor    = false;
	            isMultiple = false;
	    }
		String option2 = opts.selectedGalaxyShapeOption2();
		Option2Enum opt2 = Option2Enum.values()[0];
		for (Option2Enum o2 : Option2Enum.values())
			if(option2.endsWith(o2.toString())) {
				opt2 = o2;
				break;
			}
		switch (opt2) {
	        case SETUP_BITMAP_SHARP1:
	           	isSharp = true;
	           	sharpNb = 1;
	        	break;
	        case SETUP_BITMAP_SHARP2:
	           	isSharp = true;
	           	sharpNb = 2;
	        	break;
	        case SETUP_BITMAP_SHARP3:
	           	isSharp = true;
	           	sharpNb = 3;
	        	break;
	        case SETUP_BITMAP_SHARP4:
	           	isSharp = true;
	           	sharpNb = 4;
	        	break;
	        case SETUP_BITMAP_NORMAL:
	    	default:
	        	isSharp = false;
	           	sharpNb = 0;
	    }

        // Get bitmap (Normalized to One)
		String option3 = UserPreferences.shapeOption3.get();
		if(option3==null || option3.equals(""))
			genGaussian(GX, GY, GS);
		else
			openFileAndNormalize(option3);

		if (isMultiple) {
			if (initMultiple()) { // TODDO BR: isMultiple
				postSingleInit();
				return;
			}
			else // Failed ==> default galaxy
				genGaussian(GX, GY, GS);
		}

		processOption2(greyPD);
		if (isColor) {
			processOption2(redPD);
			processOption2(greenPD);
			processOption2(bluePD);
		}

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

		postSingleInit();
	}
	private void postSingleInit() {
		aspectRatio = (float) xBM / yBM;
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
//		System.out.println("========== GalaxyBitmapShape.init()");
		// reset w/h vars since aspect ratio may have changed
		initWidthHeight();
		offset = galaxyEdgeBuffer();
		xMult  = (float) width/xBM;
		yMult  = (float) height/yBM;
		
//		System.out.println("xMult = " + xMult + "    yMult = " + yMult);
//		System.out.println("gEB = " + gEB);
//		System.out.println("xBM = " + xBM + "  yBM = " + yBM);
//		System.out.println("width = " + width + "  height = " + height);
//		System.out.println("aspectRatio = " + aspectRatio);
//		System.out.println("xMult = " + xMult + "  yMult = " + yMult);
//		System.out.println("adjustedSizeFactor() = " + adjustedSizeFactor());
//		System.out.println();
	}
	@Override public float maxScaleAdj()	{ return 1.1f; }
	// BR: added adjust_density for the void in symmetric galaxies
	@Override protected int galaxyWidthLY() { 
		return (int) (Math.sqrt(opts.numberStarSystems()*adjustDensity*adjustedSizeFactor()*aspectRatio));
	}
	// BR: added adjust_density for the void in symmetric galaxies
	@Override protected int galaxyHeightLY() { 
		return (int) (Math.sqrt(opts.numberStarSystems()*adjustDensity*adjustedSizeFactor()/aspectRatio));
	}
	@Override public void setRandom(Point.Float pt) { setRandom(starCD, pt); }
	// modnar: add possibility for specific placement of homeworld/orion locations
	@Override public void setSpecific(Point.Float pt) {
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
	@Override public boolean valid(float x, float y) {
		if (x<0)          return false;
		if (y<0)          return false;
		if (x>fullWidth)  return false;
		if (y>fullHeight) return false;
		return true;
	}
	@Override protected float sizeFactor(String size) { return settingsFactor(0.8f); }
}
