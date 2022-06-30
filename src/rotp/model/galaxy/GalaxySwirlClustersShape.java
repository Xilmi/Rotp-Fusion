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

// modnar: custom map shape, Swirl Clusters
public class GalaxySwirlClustersShape extends GalaxyShape {
    public static final List<String> options1;
    //public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_SWIRLCLUSTERS_0");
        options1.add("SETUP_SWIRLCLUSTERS_1");
        options1.add("SETUP_SWIRLCLUSTERS_2");
        //options2 = new ArrayList<>();
        //options2.add("SETUP_NOT_AVAILABLE");
    }
    
    public GalaxySwirlClustersShape(IGameOptions options) {
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
        return (int) (Math.sqrt(1.8*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(1.8*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    public void setRandom(Point.Float pt) {
		
        int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        //int option2 = max(0, options2.indexOf(opts.selectedGalaxyShapeOption2()));
        
		float gW = (float) galaxyWidthLY();
		float gH = (float) galaxyHeightLY();
		
		// modnar: choose swirl size, number of clusters, and cluster radii with options1
		// scale up the swirl size with size of map
		// scale up number of clusters with size of map
		// scale cluster radius with ~numSwirls
		float numSwirls = (float) Math.sqrt(Math.sqrt(opts.numberStarSystems())) - 1;
		int numClusters = (int) Math.floor(Math.sqrt(opts.numberStarSystems())*Math.log(opts.numberStarSystems())/10);
		float clusterR = (float) (numSwirls + 4.0f) / 1.5f;
		float swirlWidth = 0.05f;
		float clusterDelta = 0.0f; // distance to displace cluster
		
		switch(option1) {
            case 0: {
                // clusters distributed along spiral
                numSwirls = (float) Math.sqrt(Math.sqrt(opts.numberStarSystems())) - 1;
                numClusters = (int) Math.floor(Math.sqrt(opts.numberStarSystems())*Math.log(opts.numberStarSystems())/10);
                clusterR = (float) (numSwirls + 4.0f) / 1.5f;
                clusterDelta = 0.0f;
                break;
            }
            case 1: {
                // clusters spanning across tighter spirals
                numSwirls = (float) Math.sqrt(Math.sqrt(opts.numberStarSystems()))*1.5f - 1;
                numClusters = (int) Math.floor(Math.sqrt(opts.numberStarSystems())*Math.log(opts.numberStarSystems())/10);
                clusterR = (float) (numSwirls + 4.0f) / 1.5f;
                clusterDelta = clusterR;
                break;
            }
            case 2: {
                // very tight spiral only, no visible clusters
                numSwirls = (float) (Math.sqrt(opts.numberStarSystems()))/3.0f;
                numClusters = 5;
                clusterR = 0.1f;
                clusterDelta = 0.0f;
                break;
            }
        }
		
		int numSteps = (int) (200*numSwirls*numSwirls);
		// drop a cluster "every" clusterSteps
		// not quite since distance along swirl is not uniform with steps
		int clusterSteps = (int) Math.floor(2*numSteps / (numClusters-1));
		int stepSelect = ThreadLocalRandom.current().nextInt(2*numSteps)+1;
		// select cluster position non-uniformally
		int clusterRandom = ThreadLocalRandom.current().nextInt(numClusters);
		int clusterSelect = (int) Math.floor(Math.sqrt(clusterRandom)*Math.sqrt(numClusters-1)*clusterSteps);
		
		// switch between populating the swirl vs cluster
		switch (ThreadLocalRandom.current().nextInt(2)) {
            case 0:
                float xSwirl = (float) (0.5f*gW + galaxyEdgeBuffer() + 0.225f*gW*stepSelect*Math.cos(numSwirls*stepSelect*Math.PI/numSteps)/numSteps);
				float ySwirl = (float) (0.5f*gW + galaxyEdgeBuffer() + 0.225f*gW*stepSelect*Math.sin(numSwirls*stepSelect*Math.PI/numSteps)/numSteps);
				
				double phiSwirl = random() * 2 * Math.PI;
				double radiusSwirl = Math.sqrt(random()) * swirlWidth;
				
				pt.x = (float) (radiusSwirl * Math.cos(phiSwirl) + xSwirl);
				pt.y = (float) (radiusSwirl * Math.sin(phiSwirl) + ySwirl);
		
                break;
            case 1:
                float xDelta = (float) (0.225f*gW*clusterSelect*Math.cos(numSwirls*clusterSelect*Math.PI/numSteps)/numSteps);
				float yDelta = (float) (0.225f*gW*clusterSelect*Math.sin(numSwirls*clusterSelect*Math.PI/numSteps)/numSteps);
				
				float xCluster = (float) (0.5f*gW + galaxyEdgeBuffer() + xDelta);
				float yCluster = (float) (0.5f*gW + galaxyEdgeBuffer() + yDelta);
				
				float dCluster = (float) Math.sqrt(xDelta*xDelta + yDelta*yDelta);
				
				// move cluster a distance clusterDelta closer to center, if they are sufficently far away
				if ((dCluster > clusterR) && (clusterDelta > 0.1f)) {
					
					xCluster = (float) ((clusterDelta/dCluster)*(0.5f*gW + galaxyEdgeBuffer()) + (1.0f - (clusterDelta/dCluster))*(0.5f*gW + galaxyEdgeBuffer() + xDelta));
					yCluster = (float) ((clusterDelta/dCluster)*(0.5f*gW + galaxyEdgeBuffer()) + (1.0f - (clusterDelta/dCluster))*(0.5f*gW + galaxyEdgeBuffer() + yDelta));
				}
				
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
