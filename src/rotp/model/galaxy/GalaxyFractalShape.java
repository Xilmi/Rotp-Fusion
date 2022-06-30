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

// modnar: custom map shape, Fractal
public class GalaxyFractalShape extends GalaxyShape {
    public static final List<String> options1;
    public static final List<String> options2;
    private static final long serialVersionUID = 1L;
    static {
        options1 = new ArrayList<>();
        options1.add("SETUP_FRACTAL_0");
        options1.add("SETUP_FRACTAL_1");
        options2 = new ArrayList<>();
        options2.add("SETUP_GALAXY_MAP_OPTION_A");
        options2.add("SETUP_GALAXY_MAP_OPTION_B");
        options2.add("SETUP_GALAXY_MAP_OPTION_C");
    }
	float adjust_density = 2.0f; // modnar: adjust stellar density
    
    public GalaxyFractalShape(IGameOptions options) {
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
		return (int) (Math.sqrt(adjust_density*opts.numberStarSystems()*adjustedSizeFactor()));
    }
    @Override
    protected int galaxyHeightLY() { 
		return (int) (Math.sqrt(adjust_density*opts.numberStarSystems()*adjustedSizeFactor()));
    }
	
	// returns the midpoint of point1 and point2
	private static Point.Float midPoint(Point.Float point1, Point.Float point2) {
        return new Point.Float((point1.x + point2.x) / 2.0f, (point1.y + point2.y) / 2.0f);
    }
	
	// returns the point between point1 and point2, two-third of the way to point2
	private static Point.Float twothirdPoint(Point.Float point1, Point.Float point2) {
        return new Point.Float(point1.x/3.0f + 2.0f*point2.x/3.0f, point1.y/3.0f + 2.0f*point2.y/3.0f);
    }
	
    @Override
    public void setRandom(Point.Float pt) {
		
        int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
        int option2 = max(0, options2.indexOf(opts.selectedGalaxyShapeOption2()));
        
        // choose fractal type with options1
		switch(option1) {
            case 0: {
                adjust_density = 3.0f;
                // reset w/h vars since aspect ratio may have changed
                initWidthHeight();
                // choose fractal generation with option2
                switch(option2) {
                    case 0: {
                        // Sierpinski Triangle
                        // set Sierpinski dimensions
                        float triangleWidth = (float) galaxyWidthLY() - 8*galaxyEdgeBuffer();
                        float triangleHeight = (float) Math.ceil(triangleWidth * Math.sqrt(3.0f/4.0f));
                        
                        // outer Sierpinski triangle vertex points
                        Point.Float p1 = new Point.Float(0.0f, triangleHeight+0.05f*galaxyHeightLY());
                        Point.Float p2 = new Point.Float(triangleWidth/2.0f, 0.0f+0.05f*galaxyHeightLY());
                        Point.Float p3 = new Point.Float(triangleWidth, triangleHeight+0.05f*galaxyHeightLY());
                        
                        // initial start point for chaos game, take middle point with some variation
                        Point.Float pnew = new Point.Float(triangleWidth/2.0f+(random()-0.5f)*2.0f, triangleHeight/2.0f+(random()-0.5f)*2.0f);
                        
                        // scale number of iterations with stars
                        int n = (int) Math.ceil(random() * 1.5 * maxStars);
                        int i = 0;
                        
                        // iterate through chaos game for Sierpinski randomly
                        while (i < n)
                        {
                            switch (ThreadLocalRandom.current().nextInt(3)) {
                                case 0:
                                    pnew = midPoint(pnew, p1);
                                    break;
                                case 1:
                                    pnew = midPoint(pnew, p2);
                                    break;
                                case 2:
                                    pnew = midPoint(pnew, p3);
                                    break;
                            }
                            i++;
                        }
                        
                        pt.x = (float) pnew.x + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*1.0f;
                        pt.y = (float) pnew.y + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*1.0f;
                        break;
                    }
                    
                    case 1: {
                        // Sierpinski Carpet
                        // (?) perhaps too "full"? maybe use Vicsek fractal (?)
                        // set Chaos game boundary dimensions
                        float boxWidth = (float) galaxyWidthLY() - 8*galaxyEdgeBuffer();
                        float boxHeight = (float) galaxyHeightLY() - 8*galaxyEdgeBuffer();
                        
                        // box vertex points
                        Point.Float p1 = new Point.Float(0.0f, 0.0f);
                        Point.Float p2 = new Point.Float(boxWidth, 0.0f);
                        Point.Float p3 = new Point.Float(boxWidth, boxHeight);
                        Point.Float p4 = new Point.Float(0.0f, boxHeight);
                        Point.Float p5 = new Point.Float(0.5f*boxWidth, 0.0f);
                        Point.Float p6 = new Point.Float(0.0f, 0.5f*boxHeight);
                        Point.Float p7 = new Point.Float(boxWidth, 0.5f*boxHeight);
                        Point.Float p8 = new Point.Float(0.5f*boxWidth, boxHeight);
                        
                        // initial start point for chaos game, take near middle point with some variation
                        Point.Float pnew = new Point.Float(boxWidth/3.0f+(random()-0.5f)*1.0f, boxHeight/3.0f+(random()-0.5f)*1.0f);
                        
                        // scale number of iterations with stars
                        int n = (int) Math.ceil(random() * 1.5 * maxStars);
                        int i = 0;
                        // selection verticies
                        int newVertex = 0;
                        
                        // sierpinski carpet chaos game
                        while (i < n)
                        {
                            newVertex = ThreadLocalRandom.current().nextInt(8);
                            if (newVertex == 0)
                            {
                            pnew = twothirdPoint(pnew, p1);
                            }
                            else if (newVertex == 1)
                            {
                            pnew = twothirdPoint(pnew, p2);
                            }
                            else if (newVertex == 2)
                            {
                            pnew = twothirdPoint(pnew, p3);
                            }
                            else if (newVertex == 3)
                            {
                            pnew = twothirdPoint(pnew, p4);
                            }
                            else if (newVertex == 4)
                            {
                            pnew = twothirdPoint(pnew, p5);
                            }
                            else if (newVertex == 5)
                            {
                            pnew = twothirdPoint(pnew, p6);
                            }
                            else if (newVertex == 6)
                            {
                            pnew = twothirdPoint(pnew, p7);
                            }
                            else if (newVertex == 7)
                            {
                            pnew = twothirdPoint(pnew, p8);
                            }
                            i++;
                        }
                        
                        pt.x = (float) pnew.x + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*0.2f;
                        pt.y = (float) pnew.y + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*0.2f;
                        break;
                    }
                    
                    case 2: {
                        // Barnsley Fern
                        // scale number of iterations with stars
                        int n = (int) Math.ceil(maxStars + random() * 1.5 * maxStars);
                        int i = 0;
                        Point.Float pnew = new Point.Float(0.5f, 0.0f);

                        // Barnsley fern, repeated choose one of four update rules at random
                        while (i < n)
                        {
                            Point.Float ptemp = new Point.Float();
                            float r = random();

                            // stem
                            if (r <= 0.10f)  { // original probability = 0.01f, increase to get more stem connectivity
                                ptemp.x = 0.50f;
                                ptemp.y = 0.16f * pnew.y;
                            }

                            // largest left-hand leaflet
                            else if (r <= 0.15f) { // original probability = 0.08f
                                ptemp.x =  0.20f * pnew.x - 0.26f * pnew.y + 0.400f;
                                ptemp.y =  0.23f * pnew.x + 0.22f * pnew.y - 0.045f;
                            }

                            // largest right-hand leaflet
                            else if (r <= 0.20f) { // original probability = 0.15f
                                ptemp.x = -0.15f * pnew.x + 0.28f * pnew.y + 0.575f;
                                ptemp.y =  0.26f * pnew.x + 0.24f * pnew.y - 0.086f;
                            }

                            // successively smaller leaflets
                            else {
                                ptemp.x =  0.85f * pnew.x + 0.04f * pnew.y + 0.075f;
                                ptemp.y = -0.04f * pnew.x + 0.85f * pnew.y + 0.180f;
                            }
                            pnew = ptemp;
                            i++;
                        }
                        
                        pt.x = (float) ((pnew.x-0.55f)*1.9f+0.55f)*(galaxyWidthLY()-8.0f*galaxyEdgeBuffer()) + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*0.5f;
                        pt.y = (float) (pnew.y+0.02f)*0.95f*(galaxyHeightLY()-8.0f*galaxyEdgeBuffer()) + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*0.5f;
                        break;
                    }
                }
                break;
            }
            case 1: {
                adjust_density = 2.0f;
                // reset w/h vars since aspect ratio may have changed
                initWidthHeight();
                // set Chaos game boundary dimensions
                float boxWidth = (float) galaxyWidthLY() - 8*galaxyEdgeBuffer();
                float boxHeight = (float) galaxyHeightLY() - 8*galaxyEdgeBuffer();
                
                // box vertex points
                Point.Float p1 = new Point.Float(0.0f, 0.0f);
                Point.Float p2 = new Point.Float(boxWidth, 0.0f);
                Point.Float p3 = new Point.Float(boxWidth, boxHeight);
                Point.Float p4 = new Point.Float(0.0f, boxHeight);
                
                // initial start point for chaos game, take near middle point with some variation
                Point.Float pnew = new Point.Float(boxWidth/2.0f+(random()-0.5f)*1.0f, boxHeight/2.0f+(random()-0.5f)*1.0f);
                
                // scale number of iterations with stars
                int n = (int) Math.ceil(random() * 1.5 * maxStars);
                int i = 0;
                // selection verticies
                int newVertex = 0;
                int oldVertex = 0; int oldVertex2 = 1;
                
                // iterate through chaos game, with different rules
                // choose with option2
                switch(option2) {
                    case 0: {
                        // currently chosen vertex cannot neighbor the previously chosen vertex if the two previously chosen vertices are the same
                        while (i < n)
                        {
                            newVertex = ThreadLocalRandom.current().nextInt(4);
                            if (newVertex == 0 && !(oldVertex==3 && oldVertex2==3) && !(oldVertex==1 && oldVertex2==1))
                            {
                            pnew = midPoint(pnew, p1);
                            oldVertex2 = oldVertex;
                            oldVertex = 0;
                            }
                            else if (newVertex == 1 && !(oldVertex==0 && oldVertex2==0) && !(oldVertex==2 && oldVertex2==2))
                            {
                            pnew = midPoint(pnew, p2);
                            oldVertex2 = oldVertex;
                            oldVertex = 1;
                            }
                            else if (newVertex == 2 && !(oldVertex==1 && oldVertex2==1) && !(oldVertex==3 && oldVertex2==3))
                            {
                            pnew = midPoint(pnew, p3);
                            oldVertex2 = oldVertex;
                            oldVertex = 2;
                            }
                            else if (newVertex == 3 && !(oldVertex==2 && oldVertex2==2) && !(oldVertex==0 && oldVertex2==0))
                            {
                            pnew = midPoint(pnew, p4);
                            oldVertex2 = oldVertex;
                            oldVertex = 3;
                            }
                            i++;
                        }
                        break;
                    }
                    case 1: {
                        // current vertex cannot be the same as the previously chosen vertex
                        while (i < n)
                        {
                            newVertex = ThreadLocalRandom.current().nextInt(4);
                            if (newVertex == 0 && oldVertex != 0)
                            {
                            pnew = midPoint(pnew, p1);
                            oldVertex = 0;
                            }
                            else if (newVertex == 1 && oldVertex != 1)
                            {
                            pnew = midPoint(pnew, p2);
                            oldVertex = 1;
                            }
                            else if (newVertex == 2 && oldVertex != 2)
                            {
                            pnew = midPoint(pnew, p3);
                            oldVertex = 2;
                            }
                            else if (newVertex == 3 && oldVertex != 3)
                            {
                            pnew = midPoint(pnew, p4);
                            oldVertex = 3;
                            }
                            i++;
                        }
                        break;
                    }
                    case 2: {
                        // current vertex cannot be one place away (anti-clockwise) from the previously chosen vertex
                        while (i < n)
                        {
                            newVertex = ThreadLocalRandom.current().nextInt(4);
                            if (newVertex == 0 && oldVertex != 1)
                            {
                            pnew = midPoint(pnew, p1);
                            oldVertex = 0;
                            }
                            else if (newVertex == 1 && oldVertex != 2)
                            {
                            pnew = midPoint(pnew, p2);
                            oldVertex = 1;
                            }
                            else if (newVertex == 2 && oldVertex != 3)
                            {
                            pnew = midPoint(pnew, p3);
                            oldVertex = 2;
                            }
                            else if (newVertex == 3 && oldVertex != 0)
                            {
                            pnew = midPoint(pnew, p4);
                            oldVertex = 3;
                            }
                            i++;
                        }
                        break;
                    }
                }
                
                pt.x = (float) pnew.x + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*1.0f;
                pt.y = (float) pnew.y + 4.0f*galaxyEdgeBuffer() + (random()-0.5f)*1.0f;
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
