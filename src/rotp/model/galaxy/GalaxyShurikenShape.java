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

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import rotp.model.game.IGameOptions;

// modnar: custom map shape, Shuriken
public class GalaxyShurikenShape extends GalaxyShape {
    public static final List<String> options1;
    //public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_SHURIKEN_0");
        options1.add("SETUP_SHURIKEN_1");
        options1.add("SETUP_SHURIKEN_2");
        //options2 = new ArrayList<>();
        //options2.add("SETUP_NOT_AVAILABLE");
    }
    
    Path2D flake;
	Shape flakeROT;
	Area totalArea, flakeArea;
	int numPoints = 16;
	
    public GalaxyShurikenShape(IGameOptions options) {
        opts = options;
    }
    @Override
    public List<String> options1()  { return options1; }
    //@Override
    //public List<String> options2()  { return options2; }
    @Override
    public String defaultOption1()  { return options1.get(0); }
    //@Override
    //public String defaultOption2()  { return options2.get(0); }
    @Override
    public void init(int n) {
        super.init(n);
        
        int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        //int option2 = max(0, options2.indexOf(opts.selectedGalaxyShapeOption2()));
		
		float gE = (float) galaxyEdgeBuffer();
		float gW = (float) galaxyWidthLY();
		float gH = (float) galaxyHeightLY();
		
		// modnar: choose different number of points for the flake polygon with option1
		switch(option1) {
            case 0: {
                numPoints = 9;
                break;
            }
            case 1: {
                numPoints = 11;
                break;
            }
            case 2: {
                numPoints = 13;
                break;
            }
        }
		
		Random randnum = new Random();
		// keep same random number seed
		// modified by numberStarSystems, UI_option, and selectedNumberOpponents
		randnum.setSeed(opts.numberStarSystems()*numPoints + opts.selectedNumberOpponents());
		
		flake = new Path2D.Float();
		// initial flake polygon start, ensure polygon extent
		flake.moveTo(gE + 0.5f*gW, gE + 0.65f*gH);
		flake.lineTo(gE + 0.4f*gW, gE + 0.02f*gH);
		
		// points to define triangular slice within which to draw flake polygon
		Point.Float p1 = new Point.Float(gE + 0.5f*gW, gE + 0.65f*gH);
		Point.Float p2 = new Point.Float(gE + 0.35f*gW, gE + 0.02f*gH);
		Point.Float p3 = new Point.Float(gE + 0.65f*gW, gE + 0.02f*gH);
		
		// create flake polygon shape, with random points/path
		for (int i = 0; i < numPoints; i++)
        {
			// uniform random point within triangle slice
			float rand1 = randnum.nextFloat();
			float rand2 = randnum.nextFloat();
			float px = p1.x*(1-rand1) + p2.x*((float) Math.sqrt(rand1)*(1-rand2)) + p3.x*((float) Math.sqrt(rand1)*rand2);
			float py = p1.y*(1-rand1) + p2.y*((float) Math.sqrt(rand1)*(1-rand2)) + p3.y*((float) Math.sqrt(rand1)*rand2);
			flake.lineTo(px, py);
        }
        flake.closePath();
		
		flakeArea = new Area(flake);
		totalArea = flakeArea;
		
		// rotate flakes and combine together
		for (int i = 1; i < 6; i++)
        {
		AffineTransform rotate = AffineTransform.getRotateInstance(i*Math.PI/3, gE + 0.5f*gW, gE + 0.5f*gH);
		flakeROT = rotate.createTransformedShape(flake);
		
		flakeArea = new Area(flakeROT);
		totalArea.add(flakeArea);
		}
        
        // reset w/h vars since aspect ratio may have changed
        initWidthHeight();
    }
    @Override
    public float maxScaleAdj()               { return 1.1f; }
    @Override
    protected int galaxyWidthLY() { 
        return (int) (Math.sqrt(1.2*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(1.2*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    public void setRandom(Point.Float pt) {
        pt.x = randomLocation(width, galaxyEdgeBuffer());
        pt.y = randomLocation(height, galaxyEdgeBuffer());
    }
    @Override
    public void setSpecific(Point.Float pt) { // modnar: add possibility for specific placement of homeworld/orion locations
        setRandom(pt);
    }
    @Override
    public boolean valid(float x, float y) {
        return totalArea.contains(x, y);
    }
    float randomLocation(float max, float buff) {
        return buff + (random() * (max-buff-buff));
    }
    @Override
    protected float sizeFactor(String size) {
        float adj = 1.0f;
        switch (opts.selectedStarDensityOption()) {
            case IGameOptions.STAR_DENSITY_LOWEST:  adj = 1.3f; break;
            case IGameOptions.STAR_DENSITY_LOWER:   adj = 1.2f; break;
            case IGameOptions.STAR_DENSITY_LOW:     adj = 1.1f; break;
            case IGameOptions.STAR_DENSITY_HIGH:    adj = 0.9f; break;
            case IGameOptions.STAR_DENSITY_HIGHER:  adj = 0.8f; break;
            case IGameOptions.STAR_DENSITY_HIGHEST: adj = 0.7f; break;
        }
        switch (opts.selectedGalaxySize()) {
            case IGameOptions.SIZE_TINY:      return adj*10; 
            case IGameOptions.SIZE_SMALL:     return adj*12; 
            case IGameOptions.SIZE_SMALL2:    return adj*13;
            case IGameOptions.SIZE_MEDIUM:    return adj*13; 
            case IGameOptions.SIZE_MEDIUM2:   return adj*14; 
            case IGameOptions.SIZE_LARGE:     return adj*16; 
            case IGameOptions.SIZE_LARGE2:    return adj*18; 
            case IGameOptions.SIZE_HUGE:      return adj*20; 
            case IGameOptions.SIZE_HUGE2:     return adj*22; 
            case IGameOptions.SIZE_MASSIVE:   return adj*24; 
            case IGameOptions.SIZE_MASSIVE2:  return adj*26; 
            case IGameOptions.SIZE_MASSIVE3:  return adj*28; 
            case IGameOptions.SIZE_MASSIVE4:  return adj*30; 
            case IGameOptions.SIZE_MASSIVE5:  return adj*32; 
            case IGameOptions.SIZE_INSANE:    return adj*36; 
            case IGameOptions.SIZE_LUDICROUS: return adj*40; 
            default:             return adj*19; 
        }
    }
}
