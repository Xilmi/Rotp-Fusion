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
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import rotp.model.game.IGameOptions;

// modnar: custom map shape, Maze
public class GalaxyMazeShape extends GalaxyShape {
    public static final List<String> options1;
    //public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_MAZE_0");
        options1.add("SETUP_MAZE_1");
        options1.add("SETUP_MAZE_2");
        //options2 = new ArrayList<>();
        //options2.add("SETUP_NOT_AVAILABLE");
    }
    
	Shape block;
	Area totalArea, blockArea;
	
    public GalaxyMazeShape(IGameOptions options) {
        opts = options;
    }
    @Override
    public float maxScaleAdj()               { return 0.95f; }
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
		int adjust_seed = 1;
		
		// modnar: choose different mazes (different random initial conditions) with option1
		switch(option1) {
            case 0: {
                adjust_seed = 1;
                break;
            }
            case 1: {
                adjust_seed = 2;
                break;
            }
            case 2: {
                adjust_seed = 3;
                break;
            }
        }
		
		// determine maze size with numberStarSystems
		int width = (int) (4*Math.ceil(1.5*Math.log(opts.numberStarSystems())-5));
		int height = (int) (3*Math.ceil(1.5*Math.log(opts.numberStarSystems())-5));
		float deltaW = (float) gW/width;
		float deltaH = (float) gH/height;
		boolean WALL = false;
		boolean PASSAGE = !WALL;
		boolean[][] map = new boolean[width][height];
		
		block = new Rectangle2D.Float();
		blockArea = new Area(block);
		totalArea = blockArea;
		
		LinkedList<int[]> frontiers = new LinkedList<>();
        Random randnum = new Random();
		// keep same random number seed
		// modified by numberStarSystems, UI_option, and selectedNumberOpponents
		randnum.setSeed(opts.numberStarSystems()*adjust_seed + opts.selectedNumberOpponents());
        int x = randnum.nextInt(width);
        int y = randnum.nextInt(height);
        frontiers.add(new int[]{x,y,x,y});
		
		// maze generation with Prim's algorithm
        while ( !frontiers.isEmpty() ){
            int[] f = frontiers.remove( randnum.nextInt( frontiers.size() ) );
            x = f[2];
            y = f[3];
            if ( map[x][y] == WALL )
            {
                map[f[0]][f[1]] = map[x][y] = PASSAGE;
				
				// maze passage to be filled with stars
				block = new Rectangle2D.Float(gE+f[0]*deltaW,gE+f[1]*deltaH,deltaW,deltaH);
				blockArea = new Area(block);
				totalArea.add(blockArea);
				block = new Rectangle2D.Float(gE+x*deltaW,gE+y*deltaH,deltaW,deltaH);
				blockArea = new Area(block);
				totalArea.add(blockArea);
				
				// maze walls are meaningless for our purposes
                if ( x >= 2 && map[x-2][y] == WALL )
                    frontiers.add( new int[]{x-1,y,x-2,y} );
                if ( y >= 2 && map[x][y-2] == WALL )
                    frontiers.add( new int[]{x,y-1,x,y-2} );
                if ( x < width-2 && map[x+2][y] == WALL )
                    frontiers.add( new int[]{x+1,y,x+2,y} );
                if ( y < height-2 && map[x][y+2] == WALL )
                    frontiers.add( new int[]{x,y+1,x,y+2} );
            }
        }
        
        // reset w/h vars since aspect ratio may have changed
        initWidthHeight();
    }
	
    @Override
    protected int galaxyWidthLY() { 
        return (int) (Math.sqrt(0.75*4.0/3.0*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
        return (int) (Math.sqrt(0.75*3.0/4.0*opts.numberStarSystems()*adjustedSizeFactor()));
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
