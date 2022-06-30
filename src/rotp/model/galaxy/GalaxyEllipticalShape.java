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
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rotp.model.game.IGameOptions;

public class GalaxyEllipticalShape extends GalaxyShape {
    public static final List<String> options1;
    public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_ELLIPSE_0");
        options1.add("SETUP_ELLIPSE_1");
        options1.add("SETUP_ELLIPSE_2");
        options1.add("SETUP_ELLIPSE_3");
        options1.add("SETUP_ELLIPSE_4");
        options2 = new ArrayList<>();
        options2.add("SETUP_VOID_0");
        options2.add("SETUP_VOID_1");
        options2.add("SETUP_VOID_2");
        options2.add("SETUP_VOID_3");
        options2.add("SETUP_VOID_4");
    }

    Shape ellipse, hole, orionSpot;
    Area totalArea, circleArea, holeArea, orionArea;
    float adjust_density = 1.0f; // modnar: adjust stellar density
    float ellipseRatio = 2.0f;
    float voidSize = 0.0f;
	
    public GalaxyEllipticalShape(IGameOptions options) {
        opts = options;
    }
    @Override
    public List<String> options1()  { return options1; }
    @Override
    public List<String> options2()  { return options2; }
    @Override
    public String defaultOption1()  { return options1.get(2); }
    @Override
    public String defaultOption2()  { return options2.get(0); }
    @Override
    public float maxScaleAdj()               { return 0.8f; }
    @Override
    public void init(int n) {
        super.init(n);

        int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        int option2 = max(0, options2.indexOf(opts.selectedGalaxyShapeOption2()));
        
        switch(option1) {
            case 0: ellipseRatio = 1.0f; break;
            case 1: ellipseRatio = 1.5f; break;
            case 2: ellipseRatio = 2.0f; break;
            case 3: ellipseRatio = 3.0f; break;
            case 4: ellipseRatio = 5.0f; break;
            default: ellipseRatio = 2.0f; break;
        }
        
        switch(option2) {
            case 0: voidSize = 0.0f; break;
            case 1: voidSize = 0.2f; break;
            case 2: voidSize = 0.4f; break;
            case 3: voidSize = 0.6f; break;
            case 4: voidSize = 0.8f; break;
            default: voidSize = 0.0f; break;
        }
        
        // modnar: account for void size
        adjust_density = 1.0f / (1.0f - voidSize*voidSize);
        
        // reset w/h vars since aspect ratio may have changed
        initWidthHeight();
        
        float gE = (float) galaxyEdgeBuffer();
        float gW = (float) galaxyWidthLY();
        float gH = (float) galaxyHeightLY();
        
        ellipse = new Ellipse2D.Float(gE,gE,gW,gH);
        circleArea = new Area(ellipse);
        totalArea = circleArea; // modnar: use totalArea for valid(x,y)
        
        hole = null;
        if (voidSize > 0) {
            float vW = voidSize*gW;
            float vH = voidSize*gH;
            float vX = gE+((gW-vW)/2);
            float vY = gE+((gH-vH)/2);
            hole = new Ellipse2D.Float(vX, vY,vW,vH);
            holeArea = new Area(hole);
            totalArea.subtract(holeArea);
        }
        
        // modnar: add central orion location for circular, void-4, non-small maps
        float rOrion = 1.0f;
        orionSpot = new Ellipse2D.Float(gE+0.5f*gW-rOrion,gE+0.5f*gH-rOrion,2.0f*rOrion,2.0f*rOrion);
        orionArea = new Area(orionSpot);
        if ((option1 == 0)&&(opts.numberStarSystems()>90)&&(voidSize > 0.7f)) {
            totalArea.add(orionArea);
        }
        
    }
    @Override
    protected int galaxyWidthLY() { 
        return (int) (Math.sqrt(adjust_density*ellipseRatio*maxStars*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(adjust_density*(1/ellipseRatio)*maxStars*adjustedSizeFactor()));
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
        
        int opt1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        
        // modnar: setSpecific only for circular, void-4, non-small maps
        if ((opt1 == 0)&&(opts.numberStarSystems()>90)&&(voidSize > 0.7f)) {
            if (indexWorld == 0) { // orion
                pt.x = galaxyEdgeBuffer()+0.5f*galaxyWidthLY();
                pt.y = galaxyEdgeBuffer()+0.5f*galaxyHeightLY();
            }
            else { // empire homeworlds
                int numStarts = opts.selectedNumberOpponents()+1;
                float rStart = 0.45f*galaxyHeightLY();
                float xStart = rStart * (float)Math.cos(indexWorld*2*Math.PI/numStarts);
                float yStart = rStart * (float)Math.sin(indexWorld*2*Math.PI/numStarts);
                pt.x = galaxyEdgeBuffer()+0.5f*galaxyWidthLY()+xStart;
                pt.y = galaxyEdgeBuffer()+0.5f*galaxyHeightLY()+yStart;
            }
        }
        else {
            setRandom(pt);
        }
    }
    @Override
    public boolean valid(float x, float y) {
        /*
        if (hole == null)
            return ellipse.contains(x, y);
        else
            return ellipse.contains(x, y) && !hole.contains(x, y);
        */
        return totalArea.contains(x, y); // modnar: use totalArea for valid(x,y)
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
            case IGameOptions.SIZE_TINY:      return adj*8; 
            case IGameOptions.SIZE_SMALL:     return adj*10; 
            case IGameOptions.SIZE_SMALL2:    return adj*12;
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
            case IGameOptions.SIZE_LUDICROUS: return adj*49; 
            default:             return adj*19; 
        }
    }
}
