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
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import rotp.model.game.IGameOptions;

// modnar: custom map shape, Spiral Arms
public class GalaxySpiralArmsShape extends GalaxyShape {
    public static final List<String> options1;
    //public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_SPIRALARMS_0");
        options1.add("SETUP_SPIRALARMS_1");
        options1.add("SETUP_SPIRALARMS_2");
        //options2 = new ArrayList<>();
        //options2.add("SETUP_NOT_AVAILABLE");
    }
    
    public GalaxySpiralArmsShape(IGameOptions options) {
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
        
        // reset w/h vars since aspect ratio may have changed
        initWidthHeight();
    }
    @Override
    public float maxScaleAdj()               { return 1.1f; }
    @Override
    protected int galaxyWidthLY() { 
        return (int) (Math.sqrt(2.0*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(2.0*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    public void setRandom(Point.Float pt) {
		
        int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        //int option2 = max(0, options2.indexOf(opts.selectedGalaxyShapeOption2()));
        
		float gW = (float) galaxyWidthLY();
		float gH = (float) galaxyHeightLY();
		
		float numSwirls = (float) 2.0f;
		float armRadius = (float) max(1.5f, 0.03f*gW);
		
		// choose spiral swirl size and max spiral arm width with options1
		// // maybe (?) scale up the spiral swirl size with size of map (?)
		// // Math.round(Math.sqrt(Math.sqrt(Math.sqrt(opts.numberStarSystems()))));
		// scale max spiral arm width (2*armRadius) with map size
		switch(option1) {
            case 0: {
                // normal swirls, medium swirl arm width
                numSwirls = (float) 2.0f;
                armRadius = (float) max(1.5f, 0.03f*gW);
                break;
            }
            case 1: {
                // loose swirls, large swirl arm width
                numSwirls = (float) 1.0f;
                armRadius = (float) max(1.5f, 0.05f*gW);
                break;
            }
            case 2: {
                // tight swirls, small swirl arm width
                numSwirls = (float) 3.0f;
                armRadius = (float) max(1.5f, 0.02f*gW);
                break;
            }
        }
		
		// scale up the number of spirals with size of map
		int numSpirals = (int) Math.floor(Math.sqrt(Math.sqrt(opts.numberStarSystems())));
		int numSteps = (int) 50*numSpirals;
		
		int armSelect = ThreadLocalRandom.current().nextInt(numSpirals);
		int stepSelect = ThreadLocalRandom.current().nextInt(numSteps);
		
		float xArm = (float) (0.5f*gW + galaxyEdgeBuffer() + 0.45f*gW*stepSelect*Math.cos(numSwirls*stepSelect*Math.PI/numSteps + armSelect*2*Math.PI/numSpirals)/numSteps);
		float yArm = (float) (0.5f*gW + galaxyEdgeBuffer() + 0.45f*gW*stepSelect*Math.sin(numSwirls*stepSelect*Math.PI/numSteps + armSelect*2*Math.PI/numSpirals)/numSteps);
		
		double phi = random() * 2 * Math.PI;
		double radiusSelect = Math.sqrt(random()) * armRadius * (numSteps - stepSelect)/numSteps;
		
		pt.x = (float) (radiusSelect * Math.cos(phi) + xArm);
        pt.y = (float) (radiusSelect * Math.sin(phi) + yArm);
		
    }
    @Override
    public void setSpecific(Point.Float pt) { // modnar: add possibility for specific placement of homeworld/orion locations
        setRandom(pt);
    }
    @Override
    public boolean valid(float x, float y) {
        return true;
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
