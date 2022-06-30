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
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rotp.model.game.IGameOptions;

// modnar: custom map shape, Bullseye
public class GalaxyBullseyeShape extends GalaxyShape {
    public static final List<String> options1;
    //public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_BULLSEYE_0");
        options1.add("SETUP_BULLSEYE_1");
        options1.add("SETUP_BULLSEYE_2");
        //options2 = new ArrayList<>();
        //options2.add("SETUP_NOT_AVAILABLE");
    }
    
    Shape circle, square, arc;
	Area totalArea, circleArea, squareArea, arcArea;
    float adjust_density = 2.0f;
	
    public GalaxyBullseyeShape(IGameOptions options) {
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
		
		// modnar: different bullseye/target configurations with options1
        switch(option1) {
            case 0: { // standard dart board, exclusiveOr
                adjust_density = 2.0f;
                // reset w/h vars since aspect ratio may have changed
                initWidthHeight();
                
                float gE = (float) galaxyEdgeBuffer();
                float gW = (float) galaxyWidthLY();
                float gH = (float) galaxyHeightLY();
                
                // number of arc sections in the dart board
                int nArcs = (int) Math.min(20, Math.ceil(Math.sqrt(opts.numberStarSystems())/4.0f));
                
                // double ring
                circle = new Ellipse2D.Float(gE,gE,gH,gH);
                circleArea = new Area(circle);
                totalArea = circleArea;
                circle = new Ellipse2D.Float(gE+0.0235f*gH, gE+0.0235f*gH, 0.953f*gH, 0.953f*gH);
                circleArea = new Area(circle);
                totalArea.subtract(circleArea);
                
                // treble ring
                circle = new Ellipse2D.Float(gE+0.1625f*gH, gE+0.1625f*gH, 0.675f*gH, 0.675f*gH);
                circleArea = new Area(circle);
                totalArea.add(circleArea);
                circle = new Ellipse2D.Float(gE+0.1855f*gH, gE+0.1855f*gH, 0.629f*gH, 0.629f*gH);
                circleArea = new Area(circle);
                totalArea.subtract(circleArea);
                
                // arc segments/sections
                for ( int i = 0; i < nArcs; i++ ){
                    float arcStart = (float) (i*360.0f/nArcs + 90.0f/nArcs);
                    float arcExtent = (float) (180.0f/nArcs);
                    arc = new Arc2D.Float(gE, gE, gH, gH, arcStart, arcExtent, Arc2D.PIE);
                    arcArea = new Area(arc);
                    totalArea.exclusiveOr(arcArea);
                }
                
                // central bullseye
                circle = new Ellipse2D.Float(gE+0.45325f*gH, gE+0.45325f*gH, 0.0935f*gH, 0.0935f*gH);
                circleArea = new Area(circle);
                totalArea.add(circleArea);
                circle = new Ellipse2D.Float(gE+0.4813f*gH, gE+0.4813f*gH, 0.0374f*gH, 0.0374f*gH);
                circleArea = new Area(circle);
                totalArea.subtract(circleArea);
                break;
            }
            case 1: { // concentric ring target
                adjust_density = 1.4f;
                // reset w/h vars since aspect ratio may have changed
                initWidthHeight();
                
                float gE = (float) galaxyEdgeBuffer();
                float gW = (float) galaxyWidthLY();
                float gH = (float) galaxyHeightLY();
                
                // number of rings/halos
                int nRings = (int) Math.min(200, Math.floor(Math.sqrt(opts.numberStarSystems())/2.5f));
                
                // width of each ring/halo, in ly
                float rWidth = 4.0f;
                
                // create each circular ring/halo
                circle = new Ellipse2D.Float(gE,gE,gH,gH);
                circleArea = new Area(circle);
                totalArea = circleArea;
                circle = new Ellipse2D.Float(gE+rWidth/2, gE+rWidth/2, gH-rWidth, gH-rWidth);
                circleArea = new Area(circle);
                totalArea.subtract(circleArea);
                
                for ( int i = 1; i < nRings; i++ ){
                    float ringD = (float) (1.0f-1.0f*i/nRings);
                    float ringPos = (float) 0.5f*(1.0f-ringD);
                    
                    circle = new Ellipse2D.Float(gE+ringPos*gH, gE+ringPos*gH, ringD*gH, ringD*gH);
                    circleArea = new Area(circle);
                    totalArea.add(circleArea);
                    circle = new Ellipse2D.Float(gE+ringPos*gH+rWidth/2, gE+ringPos*gH+rWidth/2, ringD*gH-rWidth, ringD*gH-rWidth);
                    circleArea = new Area(circle);
                    totalArea.subtract(circleArea);
                }
                
                // central bullseye
                circle = new Ellipse2D.Float(gE+0.5f*gH*(1.0f-0.5f/nRings), gE+0.5f*gH*(1.0f-0.5f/nRings), 0.5f*gH/nRings, 0.5f*gH/nRings);
                circleArea = new Area(circle);
                totalArea.add(circleArea);
                break;
            }
            case 2: { // concentric square target
                adjust_density = 1.2f;
                // reset w/h vars since aspect ratio may have changed
                initWidthHeight();
                
                float gE = (float) galaxyEdgeBuffer();
                float gW = (float) galaxyWidthLY();
                float gH = (float) galaxyHeightLY();
                
                // number of rings/halos
                int nRings = (int) Math.min(200, Math.floor(Math.sqrt(opts.numberStarSystems())/3));
                
                // width of each ring/halo, in ly
                float rWidth = 3.0f;
                
                // create each square ring/halo
                square = new Rectangle2D.Float(gE,gE,gH,gH);
                squareArea = new Area(square);
                totalArea = squareArea;
                square = new Rectangle2D.Float(gE+rWidth/2, gE+rWidth/2, gH-rWidth, gH-rWidth);
                squareArea = new Area(square);
                totalArea.subtract(squareArea);
                
                for ( int i = 1; i < nRings; i++ ){
                    float ringD = (float) (1.0f-1.0f*i/nRings);
                    float ringPos = (float) 0.5f*(1.0f-ringD);
                    
                    square = new Rectangle2D.Float(gE+ringPos*gH, gE+ringPos*gH, ringD*gH, ringD*gH);
                    squareArea = new Area(square);
                    totalArea.add(squareArea);
                    square = new Rectangle2D.Float(gE+ringPos*gH+rWidth/2, gE+ringPos*gH+rWidth/2, ringD*gH-rWidth, ringD*gH-rWidth);
                    squareArea = new Area(square);
                    totalArea.subtract(squareArea);
                }
                
                // central bullseye
                square = new Rectangle2D.Float(gE+0.5f*gH*(1.0f-0.5f/nRings), gE+0.5f*gH*(1.0f-0.5f/nRings), 0.5f*gH/nRings, 0.5f*gH/nRings);
                squareArea = new Area(square);
                totalArea.add(squareArea);
                break;
            }
        }
    }
    @Override
    public float maxScaleAdj()               { return 1.0f; }
    @Override
    protected int galaxyWidthLY() { 
        return (int) (Math.sqrt(adjust_density*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(adjust_density*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    public void setRandom(Point.Float pt) {
        // modnar: use quasi-random low-discrepancy additive recurrence sequence instead of random()
        // based on generalised golden ratio values, in 2D this is the plastic number
        // http://extremelearning.com.au/unreasonable-effectiveness-of-quasirandom-sequences/
        // currently not better than random(), but could in principle allow better separated star systems
        
        double c1 = 0.7548776662466927600495; // inverse of plastic number
        double c2 = 0.5698402909980532659114; // square inverse of plastic number
        
        Random rand = new Random();
        int rand_int = rand.nextInt(20*opts.numberStarSystems());
        
        pt.x = galaxyEdgeBuffer() + (width - 2*galaxyEdgeBuffer()) * (float)( (0.5 + c1*rand_int)%1 );
        pt.y = galaxyEdgeBuffer() + (height - 2*galaxyEdgeBuffer()) * (float)( (0.5 + c2*rand_int)%1 );
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
