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

import static rotp.ui.UserPreferences.maximizeSpacing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import rotp.model.game.IGameOptions;

// modnar: custom map shape, Spiral Arms
public class GalaxyBitmapShape extends GalaxyShape {
	public static final List<String> options1;
	// public static final List<String> options2;
	private static final long serialVersionUID = 1L;
	static {
		// BR: reordered to add straight and very loose
		options1 = new ArrayList<>();
		options1.add("SETUP_SPIRALARMS_0"); // Straight
		//options2 = new ArrayList<>();
		//options2.add("SETUP_NOT_AVAILABLE");
	}
	private float adjust_density = 1.0f;

	public GalaxyBitmapShape(IGameOptions options) {
		opts = options;
	}
	@Override
	public List<String> options1()  { return options1; }
	// @Override
	// public List<String> options2()  { return options2; }
	@Override
	public String defaultOption1()  { return options1.get(3); }
	// @Override
	// public String defaultOption2()  { return options2.get(0); }
	@Override
	public void init(int n) {
		super.init(n);
		// reset w/h vars since aspect ratio may have changed
		initWidthHeight();
		int option1 = max(0, options1.indexOf(opts.selectedGalaxyShapeOption1()));
		float gW = (float) galaxyWidthLY();

		switch(option1) {
			case 0: { // straight, no swirls, very large swirl arm width
				break;
			}
		}
	}
	@Override
	public float maxScaleAdj()	{ return 1.1f; }
	@Override // BR: added adjust_density for the void in symmetric galaxies
	protected int galaxyWidthLY() { 
		return (int) (Math.sqrt(2.0*opts.numberStarSystems()*adjust_density*adjustedSizeFactor()));
	}
	@Override // BR: added adjust_density for the void in symmetric galaxies
	protected int galaxyHeightLY() { 
		return (int) (Math.sqrt(2.0*opts.numberStarSystems()*adjust_density*adjustedSizeFactor()));
	}
	@Override
	public void setRandom(Point.Float pt) {
			   
		float gW = (float) galaxyWidthLY();
		
		// scale up the number of spirals with size of map
		
		
		
		
		pt.x = (float) (0);
		pt.y = (float) (0);
		
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
	protected float sizeFactor(String size) { return settingsFactor(1.0f); }
}
