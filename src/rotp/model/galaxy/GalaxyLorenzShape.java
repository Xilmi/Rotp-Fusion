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
import java.util.ArrayList;
import java.util.List;
import rotp.model.game.IGameOptions;

// modnar: custom map shape, Lorenz
public class GalaxyLorenzShape extends GalaxyShape {
    public static final List<String> options1;
    public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_LORENZ_0");
        options1.add("SETUP_LORENZ_1");
        options2 = new ArrayList<>();
        options2.add("SETUP_VIEW_0");
        options2.add("SETUP_VIEW_1");
        options2.add("SETUP_VIEW_2");
    }
    
	private double dt1=0.005; // integration time interval;
	private double sigma=10.0, rho=28.0, beta=8.0/3.0; // Lorenz-1 coefficients values
    private double dt2=0.02; // integration time interval;
	private double a=1.5, b=0.5, c=5.0; // Lorenz-2 coefficients values
    public GalaxyLorenzShape(IGameOptions options) {
        opts = options;
    }
    @Override
    public List<String> options1()  { return options1; }
    @Override
    public List<String> options2()  { return options2; }
    @Override
    public String defaultOption1()  { return options1.get(0); }
    @Override
    public String defaultOption2()  { return options2.get(0); }
    @Override
    public void init(int n) {
        super.init(n);
        
        // reset w/h vars since aspect ratio may have changed
        initWidthHeight();
    }
    @Override
    public float maxScaleAdj()               { return 0.95f; }
    @Override
    protected int galaxyWidthLY() { 
        return (int) (Math.sqrt(2.0*4.0/3.0*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(2.0*3.0/4.0*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    public void setRandom(Point.Float pt) {
		
        int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        int option2 = max(0, options2.indexOf(opts.selectedGalaxyShapeOption2()));
        
        // choose lorenz function with option1
        switch(option1) {
            case 0: {
                // iterate over the Lorenz-1 attractor function a random
                // number of steps to get a random point on the function.
                double x = 10.0; double y = 10.0; double z = 10.0; //starting point for Lorenz
                int maxsteps = (int) Math.max(1000, Math.ceil(1.5 * maxStars)); // scale number of iterations with stars
                int n = (int) Math.ceil(random() * maxsteps);
                for (int i = 0; i < n; i++) {
                    double x1 = x + dt1 * sigma*(y-x); 
                    double y1 = y + dt1 * (rho*x - y - x*z); 
                    double z1 = z + dt1 * (x*y - beta*z); 
                    x = x1;
                    y = y1;
                    z = z1;
                }
                
                float xf = (float) x;
                float yf = (float) y;
                float zf = (float) z;
                // choose lorenz view-point with option2
                switch(option2) {
                    case 0: {
                        pt.x = galaxyEdgeBuffer() + ((xf+22.0f)/44.0f)*galaxyWidthLY() + (random()-0.5f)*1.0f;
                        pt.y = galaxyEdgeBuffer() + ((yf+30.0f)/60.0f)*galaxyHeightLY() + (random()-0.5f)*1.0f;
                        break;
                    }
                    case 1: {
                        pt.x = galaxyEdgeBuffer() + ((xf+22.0f)/44.0f)*galaxyWidthLY() + (random()-0.5f)*1.0f;
                        pt.y = galaxyEdgeBuffer() + ((zf+0.0f)/55.0f)*galaxyHeightLY() + (random()-0.5f)*1.0f;
                        break;
                    }
                    case 2: {
                        pt.x = galaxyEdgeBuffer() + ((yf+30.0f)/60.0f)*galaxyWidthLY() + (random()-0.5f)*1.0f;
                        pt.y = galaxyEdgeBuffer() + ((zf+0.0f)/55.0f)*galaxyHeightLY() + (random()-0.5f)*1.0f;
                        break;
                    }
                }
                break;
            }
            case 1: {
                // iterate over the Lorenz-2 attractor function a random
                // number of steps to get a random point on the function.
                double x = 0.5; double y = 1.0; double z = 0.5; //starting point for Lorenz-2
                int maxsteps = Math.max(2000, 1 * maxStars); // scale number of iterations with stars
                int n = (int) Math.ceil(random() * maxsteps);
                for (int i = 0; i < n; i++) {
                    double x1 = x + dt2 * y; 
                    double y1 = y + dt2 * (-a*x - b*y + y*z); 
                    double z1 = z + dt2 * (-c*x*y - x*x + y*y); 
                    x = x1;
                    y = y1;
                    z = z1;
                }
                
                float xf = (float) x;
                float yf = (float) y;
                float zf = (float) z;
                
                // choose Lorenz-2 view-point with option2
                switch(option2) {
                    case 0: {
                        pt.x = galaxyEdgeBuffer() + ((xf+2.1f)/4.2f)*galaxyWidthLY() + (random()-0.5f)*2.0f;
                        pt.y = galaxyEdgeBuffer() + ((yf+2.9f)/5.8f)*galaxyHeightLY() + (random()-0.5f)*2.0f;
                        break;
                    }
                    case 1: {
                        pt.x = galaxyEdgeBuffer() + ((xf-yf+2.5f)/5.0f)*galaxyWidthLY() + (random()-0.5f)*2.0f;
                        pt.y = galaxyEdgeBuffer() + ((zf+5.1f)/7.5f)*galaxyHeightLY() + (random()-0.5f)*2.0f;
                        break;
                    }
                    case 2: {
                        pt.x = galaxyEdgeBuffer() + ((yf+xf+3.8f)/8.0f)*galaxyWidthLY() + (random()-0.5f)*2.0f;
                        pt.y = galaxyEdgeBuffer() + ((zf+5.1f)/7.5f)*galaxyHeightLY() + (random()-0.5f)*2.0f;
                        break;
                    }
                }
                break;
            }
        }
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
            case IGameOptions.SIZE_SMALL:     return adj*15; 
            case IGameOptions.SIZE_SMALL2:    return adj*17;
            case IGameOptions.SIZE_MEDIUM:    return adj*19; 
            case IGameOptions.SIZE_MEDIUM2:   return adj*20; 
            case IGameOptions.SIZE_LARGE:     return adj*21; 
            case IGameOptions.SIZE_LARGE2:    return adj*22; 
            case IGameOptions.SIZE_HUGE:      return adj*23; 
            case IGameOptions.SIZE_HUGE2:     return adj*24; 
            case IGameOptions.SIZE_MASSIVE:   return adj*25; 
            case IGameOptions.SIZE_MASSIVE2:  return adj*26; 
            case IGameOptions.SIZE_MASSIVE3:  return adj*27; 
            case IGameOptions.SIZE_MASSIVE4:  return adj*28; 
            case IGameOptions.SIZE_MASSIVE5:  return adj*29; 
            case IGameOptions.SIZE_INSANE:    return adj*32; 
            case IGameOptions.SIZE_LUDICROUS: return adj*36; 
            default:             return adj*19; 
        }
    }

}
