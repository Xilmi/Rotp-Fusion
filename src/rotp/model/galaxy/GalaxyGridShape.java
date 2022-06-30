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
import java.util.Collections;
import java.util.Random;
import java.util.List;
import rotp.model.game.IGameOptions;

// modnar: custom map shape, Grid
public class GalaxyGridShape extends GalaxyShape {
    public static final List<String> options1;
    //public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_GRID_0");
        options1.add("SETUP_GRID_1");
        options1.add("SETUP_GRID_2");
        //options2 = new ArrayList<>();
        //options2.add("SETUP_NOT_AVAILABLE");
    }
	
    public GalaxyGridShape(IGameOptions options) {
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
        return (int) (Math.sqrt(1.5*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(1.5*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    public void setRandom(Point.Float pt) {
		
        int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        //int option2 = max(0, options2.indexOf(opts.selectedGalaxyShapeOption2()));
        
		// choose number of grids, clusters, and cluster radii with option1
		// scale up the number of grid lines with size of map
		// scale up number of clusters with size of map
		// scale cluster radius with ~nGrid
		float nGrid = (float) Math.floor(Math.sqrt(Math.sqrt(opts.numberStarSystems())) - 1);
		float nClusters = 0.0f;
		float clusterR = 0.1f;
		
		switch(option1) {
            case 0: {
                // rough grid, some clusters at intersections
                nGrid = (float) Math.floor(Math.sqrt(Math.sqrt(opts.numberStarSystems())) - 1);
                nClusters = (float) min((nGrid+1)*(nGrid+1)-1, (float) Math.floor(Math.sqrt(opts.numberStarSystems())/1.5));
                clusterR = (float) (nGrid + 5.0f) / 2.0f;
                break;
            }
            case 1: {
                // rough grid, clusters at all intersections
                nGrid = (float) Math.floor(Math.sqrt(Math.sqrt(opts.numberStarSystems())) - 1);
                nClusters = (float) (nGrid+1)*(nGrid+1);
                clusterR = (float) (nGrid + 5.0f) / 2.0f;
                break;
            }
            case 2: {
                // fine grid, no clusters
                nGrid = (float) Math.floor(1.8f*Math.sqrt(Math.sqrt(opts.numberStarSystems())) - 1);
                nClusters = 0.0f;
                clusterR = 0.1f;
                break;
            }
        }
		
		float gW = (float) galaxyWidthLY() - 2.0f*clusterR - 2.0f*galaxyEdgeBuffer();
		float gH = (float) galaxyHeightLY() - 2.0f*clusterR - 2.0f*galaxyEdgeBuffer();
		
		// scale the resolution of the grid with map dimensions and number of grids
		int numSteps = (int) (10*(gW+gH)*(nGrid+1));
		int horizontalSteps = (int) (10*gW*(nGrid+1));
		
		// randomly assign clusters at intersections
		// but use map size and number of opponents as seed to ensure same sequence
		// use shuffle list to ensure unique draws
		ArrayList<Integer> clusterList = new ArrayList<Integer>();
		for(int i = 0; i < (nGrid+1)*(nGrid+1); i++){
			clusterList.add(i);
		}
		Collections.shuffle(clusterList, new Random(opts.numberStarSystems()+opts.selectedNumberOpponents()));
		
		
		// switch between populating the grid vs cluster
		switch (ThreadLocalRandom.current().nextInt(2)) {
            case 0:
				
				int stepSelect = (int) Math.floor(random()*numSteps);
				
				// horizontal grids
				if (stepSelect < horizontalSteps) { 
				
					int gridRow = (int) Math.floor(stepSelect/(10*gW));
					
					pt.x = (float) (clusterR + galaxyEdgeBuffer() + gW*(stepSelect-gridRow*(10*gW))/(10*gW));
					pt.y = (float) (clusterR + galaxyEdgeBuffer() + gH*(gridRow/nGrid));
					
					break;
				}
				
				// vertical grids
				else {
				
					int gridColumn = (int) Math.floor((stepSelect - horizontalSteps)/(10*gH));
					
					pt.x = (float) (clusterR + galaxyEdgeBuffer() + gW*(gridColumn/nGrid));
					pt.y = (float) (clusterR + galaxyEdgeBuffer() + gH*(stepSelect-horizontalSteps-gridColumn*(10*gH))/(10*gH));
					
					break;
				}
            case 1:
				
				int clusterSelect = (int) Math.floor(random()*nClusters);
				
				int clusterPos = clusterList.get(clusterSelect);
                int clusterX = (int) (clusterPos % (nGrid+1));
				int clusterY = (int) Math.floor(clusterPos / (nGrid+1));
				
				float xCluster = (float) ((clusterX/nGrid)*gW + clusterR + galaxyEdgeBuffer());
				float yCluster = (float) ((clusterY/nGrid)*gH + clusterR + galaxyEdgeBuffer());
				
				double phiCluster = random() * 2 * Math.PI;
				double radiusSelect = Math.sqrt(random()) * clusterR;
				
				pt.x = (float) (radiusSelect * Math.cos(phiCluster) + xCluster);
				pt.y = (float) (radiusSelect * Math.sin(phiCluster) + yCluster);
				
                break;
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
