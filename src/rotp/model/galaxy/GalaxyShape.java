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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rotp.mod.br.AddOns.GalaxyOptions;
import rotp.mod.br.profiles.Profiles;
import rotp.model.game.IGameOptions;
import rotp.util.Base;
import rotp.ui.UserPreferences; // modnar: add option to start game with additional colonies

// BR: Added symmetric galaxies functionalities
// moved modnar companion worlds here with some more random positioning
// added some comments to help my understanding of this class
public abstract class GalaxyShape implements Base, Serializable {
	private static final long serialVersionUID = 1L;
	static final int GALAXY_EDGE_BUFFER = 12;
	static final double twoPI = Math.PI * 2.0; // BR:
	static float orionBuffer = 10;
	static float empireBuffer = 8;	
	float[] x;
	float[] y;
	CompanionWorld[] companionWorlds; // BR:
	int numCompanions;
	ShapeRegion[][] regions;
	int regionScale = 16;
	int width = 0;
	int height = 0;
	int maxStars = 0;
	int num = 0;
	int homeStars = 0;
	int genAttempt = 0;
	boolean usingRegions = false;
	boolean fullyInit = false;
	List<EmpireSystem> empSystems = new ArrayList<>();
	public Point.Float orionXY;
	IGameOptions opts;
	
	// BR: added for symmetric galaxy
	public static float cx; // Width galaxy center
	public static float cy; // Height galaxy center
	float sysBuffer = 1.9f;
	int numEmpires;
	int numOpponents;
	Rand rand = new Rand(); // random number generator
	long tm0; // for timing computation
	// \BR

	public int width()		  { return width; }
	public int height()		 { return height; }
	public boolean fullyInit()  { return fullyInit; }
	// ========== abstract and overridable methods ==========
	protected abstract int galaxyWidthLY();
	protected abstract int galaxyHeightLY();
	public abstract void setRandom(Point.Float p);
	public abstract boolean valid(float x, float y);
	protected abstract float sizeFactor(String size);

	// modnar: add possibility for specific placement of homeworld/orion locations
	// indexWorld variable will be used by setSpecific in each Map Shape for locations
	public abstract void setSpecific(Point.Float p);
	public int indexWorld;

	public boolean isSymmetric()            { return false; }
	public boolean isCircularSymmetric()    { return false; }
	public boolean isRectangulatSymmetric() { return false; }
	public CtrPoint getPlayerSymmetricHomeWorld() {
		// This may have been be abstract... but symmetric isn't mandatory...
		// So either an empty method here or one in every non symmetric child class
		return null;
	}
	public CtrPoint getValidRandomSymmetric() {
		// This may have been be abstract... but symmetric isn't mandatory...
		// So either an empty method here or one in every non symmetric child class
		return null;
	}
	protected CtrPoint[] getOtherSymmetricSystems(CtrPoint src) {
		return null; // if relevant, this method should be overridden
	}
	// BR: ========== Getter Methods ==========
	//
	public int numberStarSystems()			{ return num; }
	// modnar: add option to start game with additional colonies
	// modnar: these colonies are in addition to number of stars chosen in galaxy
	public int totalStarSystems()			 { return num+homeStars+UserPreferences.companionWorlds()*(opts.selectedNumberOpponents()+1);}
	public List<EmpireSystem> empireSystems() { return empSystems; }
	public int empireSystemStars()			{ return homeStars; }
	public float adjustedSizeFactor()		{ return sizeFactor(opts.selectedGalaxySize()) + (genAttempt/3); }

	public List<String> options1()			{ return new ArrayList<>(); }
	public List<String> options2()			{ return new ArrayList<>(); }
	public int numOptions1()				  { return options1().size(); }
	public int numOptions2()				  { return options2().size(); }
	public String defaultOption1()			{ return ""; }
	public String defaultOption2()			{ return ""; }

	public float systemBuffer() {
		switch (opts.selectedStarDensityOption()) {
			case IGameOptions.STAR_DENSITY_LOWEST:  return 2.5f;
			case IGameOptions.STAR_DENSITY_LOWER:   return 2.3f;
			case IGameOptions.STAR_DENSITY_LOW:	 return 2.1f;
			case IGameOptions.STAR_DENSITY_HIGH:	return 1.7f;
			case IGameOptions.STAR_DENSITY_HIGHER:  return 1.5f;
			case IGameOptions.STAR_DENSITY_HIGHEST: return 1.3f;
		}
		return 1.9f;
	}
	protected Point.Float getCompanion(int empId, int compId) {
		return this.companionWorlds[empId].cW[compId].get();
	}
	// BR: ========== Very symmetry specific Methods ==========
	//
	protected CtrPoint[] getCircularSymmetricSystems(CtrPoint src) {
		return src.rotate(twoPI/numEmpires, numOpponents);
	}
	protected CtrPoint[] getRectangularSymmetricSystems(CtrPoint src) {
		return null; // To be implemented later
	}
	protected CtrPoint[] getSymmetricSystems(CtrPoint src) {
		if (isCircularSymmetric()) {
			return getCircularSymmetricSystems(src);
		}
		else if (isRectangulatSymmetric()) {
			return getRectangularSymmetricSystems(src);
		}
		else {
			return getOtherSymmetricSystems(src);
		}
	}
    protected double galaxyRay() {
    	return cx - galaxyEdgeBuffer();
    }
	public void generateSymmetric(boolean full) {
		genAttempt = 0;
		// add systems needed for empires
		while (empSystems.size() < numEmpires) { // Empires attempts loop
			if (full)
				fullInit();
			else
				quickInit();
			genAttempt++;
			empSystems.clear();
			homeStars = 0;
			num = 0;
			// ===== First Orion
			// Center of the galaxy is the only reasonable symmetric option
			orionXY = new Point.Float(cx, cy);
			addSystem(orionXY);
			indexWorld = 1;
			// ===== Then the home world systems
			CtrPoint ptCtr = getPlayerSymmetricHomeWorld();
			Point.Float pt = ptCtr.get();
			if (valid(pt) && !isTooNearExistingSystem(pt.x, pt.y, true)) {
				EmpireSystem sys = new EmpireSystem(this, pt);
				empSystems.add(sys);
				homeStars++; // the two nearby system will be set later
			} else {
				continue; // Fail... Retry					
			}
			// ----- now the opponents Homes
			// get the stars
			CtrPoint[] opp = getSymmetricSystems(ptCtr);
			//  no test needed, they are valid by symmetry.
			for ( CtrPoint p : opp) {
				EmpireSystem sys = new EmpireSystem(this, p.get());
				empSystems.add(sys);
				homeStars++; // the two nearby system will be set later
		   	}
			// ===== Then the nearby systems
			boolean valid = true;
			for (int i=1; i<3; i++) { // 2 nearby systems
				// get player nearby system
				EmpireSystem player = empSystems.get(0);
				if (player.addNearbySystems(this, null)) {
					homeStars++;
				} else {
					valid = false;
					break; // Fail... Retry
				}
				// ----- now the opponents
				// get the stars
				opp = getSymmetricSystems(new CtrPoint(player.x[i], player.y[i]));
				//  no test needed, they are valid by symmetry.
				for (int k=0; k<numOpponents; k++) {
			   		empSystems.get(k+1).addNearbySystems(this, opp[k].get());
			   		homeStars++;
			   	}
			}
			if (!valid) {
				continue; // something was wrong!
			}
		} // The empires are set

		// ===== add other systems to fill out galaxy
		int attempts = addUncolonizedSystemsSymmetric();
		companionWorlds = new CompanionWorld(empSystems.get(0), numCompanions).symmetric();
		long tm1 = System.currentTimeMillis();
		log("Galaxy generation: "+(tm1-tm0)+"ms  Regions: " + usingRegions+"  Attempts: ", str(attempts), "  stars:", str(num), "/", str(maxStars));
	}
	protected int addUncolonizedSystemsSymmetric() {
		int maxAttempts = maxStars * 10;
		// we've already generated 3 stars for every empire so reduce their
		// total from the count of remaining stars to create ("too many stars" bug)
		int nonEmpireStars = maxStars - (empSystems.size() *3);
		// Adjust for compatibility with symmetric galaxy 
		// Remove Orion, modulo number of Empires, then add Orion
		nonEmpireStars = 1 + Math.floorDiv(nonEmpireStars-1, numEmpires) * numEmpires;
		int attempts = 0;
		CtrPoint[] oppPt;
		while ((num < nonEmpireStars) && (attempts++ < maxAttempts)) {
			// Find a location
			CtrPoint pt = getValidRandomSymmetric();
			if (!isTooNearExistingSystem(pt.getX(), pt.getY() ,false)) {
				addSystem(pt.get());
				// Then fill the symmetry
				// get the stars
				oppPt = getSymmetricSystems(pt);
				//  no test needed, they are valid by symmetry.
			   	for (CtrPoint opp : oppPt) {
			   		addSystem(opp.get());
			   	}
			}
		}
		return attempts;
	}
	// \BR
	public boolean valid(Point.Float p) { return valid(p.x, p.y); }
	public float maxScaleAdj()			   { return 1.0f; }
	public void coords(int n, Point.Float pt) {
		int i = n;
		if (usingRegions) {
			for (int a=0;a<regionScale;a++) {
				for (int b=0;b<regionScale;b++) {
					if (i >= regions[a][b].num)
						i -= regions[a][b].num;
					else {
						pt.x = regions[a][b].x[i];
						pt.y = regions[a][b].y[i];
						//log("system: "+n+"  is a:"+a+" b:"+b+"  i:"+i);
						return;
					}
				}
			}
			throw new RuntimeException("Invalid x index requested: "+i);
		}
		else {
			pt.x = x[i];
			pt.y = y[i];
		}
	}
	// BR: ========== Initialization Methods ==========
	//
	public void singleInit() {
		// common symmetric and non symmetric initializer for generation
		numOpponents = opts.selectedNumberOpponents();
		numEmpires = numOpponents + 1;
		log("Galaxy shape: "+maxStars+ " stars"+ "  regionScale: "+regionScale+"   emps:"+numEmpires);
		tm0 = System.currentTimeMillis();
		empSystems.clear();

		// systemBuffer() is minimum distance between any 2 stars
		sysBuffer = systemBuffer();
		float minEmpireBuffer = 4*sysBuffer; // modnar: increase spacing between empires
		float maxMinEmpireBuffer = 15*sysBuffer;
		float minOrionBuffer = 5*sysBuffer; // modnar: increase spacing between empires and orion

		// BR: not optimized for symmetric
		if (Profiles.isMaximizeSpacingEnabled() && !isSymmetric()) {
			GalaxyOptions.initMaximizeSpacing(maxStars, numEmpires, sysBuffer);
			minEmpireBuffer	= GalaxyOptions.getMinEmpireBuffer();
			maxMinEmpireBuffer = GalaxyOptions.getMaxMinEmpireBuffer();
			minOrionBuffer	 = GalaxyOptions.getMinOrionBuffer();
		}
		// \BR:

		// the stars/empires ratio for the most "densely" populated galaxy is about 8:1
		// we want to set the minimum distance between empires to half that in ly, with a minimum
		// of 6 ly... this means that it will not increase until there is at least a 12:1
		// ratio. However, the minimum buffer will never exceed the "MAX_MIN", to ensure that
		// massive maps don't always GUARANTEE hundreds of light-years of space to expand uncontested
		empireBuffer = min(maxMinEmpireBuffer, max(minEmpireBuffer, (maxStars/(numEmpires*2))));
		// Orion buffer is 50% greater with minimum of 8 ly.
		orionBuffer = max(minOrionBuffer, empireBuffer*3/2);
	}
	public void fullInit() {
		fullyInit = true;
		init(opts.numberStarSystems());
	}
	public void quickInit() {
		fullyInit = false;
		init(min(5000,opts.numberStarSystems()));
	}
	public void init(int numStars) {
		numOpponents = opts.selectedNumberOpponents();
		numEmpires = numOpponents + 1;
		numCompanions = UserPreferences.companionWorldsSigned();
		num = 0;
		homeStars = 0;
		empSystems.clear();
		maxStars = numStars;
		initWidthHeight();
		float minSize = min(width, height);
		usingRegions = minSize > 100;
		if (usingRegions) {
			regionScale = min(64, (int) (minSize / 6.0));
			regions = new ShapeRegion[regionScale][regionScale];
			int regionStars = (int) (2.5*maxStars/regionScale);
			for (int i=0;i<regionScale;i++) {
				for (int j=0;j<regionScale;j++)
					regions[i][j] = new ShapeRegion(regionStars);
			}
		}
		else {
			x = new float[maxStars];
			y = new float[maxStars];
		}
	}
	public void initWidthHeight() {
		width = galaxyWidthLY() + (2 * galaxyEdgeBuffer());
		height = galaxyHeightLY() + (2 * galaxyEdgeBuffer());
		// BR: for symmetric galaxy
		cx = width  / 2.0f;
		cy = height / 2.0f;
	}
	public void fullGenerate() {
		generate(true);
	}
	public void quickGenerate() {
		generate(false);
	}
	public void generate(boolean full) {
		singleInit();
		if (isSymmetric()) {
			generateSymmetric(full);
			return;
		}
		genAttempt = 0;
		// add systems needed for empires
		while (empSystems.size() < numEmpires) {
			if (full)
				fullInit();
			else
				quickInit();
			genAttempt++;
			empSystems.clear();
			homeStars = 0;
			num = 0;
			orionXY = addOrion();
			indexWorld = 1; // modnar: after specific orion placement, set indexWorld=1 for homeworlds
			for (int i=0;i<numEmpires;i++) {
				EmpireSystem sys = new EmpireSystem(this);
				if (sys.valid) {
					empSystems.add(sys);
					homeStars += sys.numSystems();
				}
			}
		}

		// add other systems to fill out galaxy
		int attempts = addUncolonizedSystems();
		addCompanionsWorld();
		long tm1 = System.currentTimeMillis();
		log("Galaxy generation: "+(tm1-tm0)+"ms  Regions: " + usingRegions+"  Attempts: ", str(attempts), "  stars:", str(num), "/", str(maxStars));
	}
	protected int galaxyEdgeBuffer() {
		switch(opts.selectedGalaxySize()) {
			case IGameOptions.SIZE_TINY:	  return 1;
			case IGameOptions.SIZE_SMALL:	 return 1;
			case IGameOptions.SIZE_SMALL2:	return 1;
			case IGameOptions.SIZE_MEDIUM:	return 2;
			case IGameOptions.SIZE_MEDIUM2:   return 2;
			case IGameOptions.SIZE_LARGE:	 return 2;
			case IGameOptions.SIZE_LARGE2:	return 2;
			case IGameOptions.SIZE_HUGE:	  return 3;
			case IGameOptions.SIZE_HUGE2:	 return 3;
			case IGameOptions.SIZE_MASSIVE:   return 3;
			case IGameOptions.SIZE_MASSIVE2:  return 3;
			case IGameOptions.SIZE_MASSIVE3:  return 3;
			case IGameOptions.SIZE_MASSIVE4:  return 4;
			case IGameOptions.SIZE_MASSIVE5:  return 4;
			case IGameOptions.SIZE_INSANE:	return 5;
			case IGameOptions.SIZE_LUDICROUS: return 8;
		}
		return GALAXY_EDGE_BUFFER;
	}
	protected void addColonizedSystems() {

	}
	protected Point.Float addOrion() {
		Point.Float pt = new Point.Float();
		indexWorld = 0; // modnar: explicitly set indexWorld=0 for orion
		findSpecificValidLocation(pt); // modnar: specific placement for orion location
		//findAnyValidLocation(pt);
		addSystem(pt);
		return pt;
	}
	private void addCompanionsWorld() {
		companionWorlds = new CompanionWorld[numEmpires];
		for (int i=0; i<numEmpires; i++) {
			companionWorlds[i] = new CompanionWorld(empSystems.get(i), numCompanions);
		}
	}
	protected int addUncolonizedSystems() {
		int maxAttempts = maxStars * 10;

		// we've already generated 3 stars for every empire so reduce their
		// total from the count of remaining stars to create ("too many stars" bug)
		int nonEmpireStars = maxStars - (empSystems.size() *3);
		int attempts = 0;
		Point.Float pt = new Point.Float();
		while ((num < nonEmpireStars) && (attempts++ < maxAttempts)) {
			findAnyValidLocation(pt);
			if (!isTooNearExistingSystem(pt.x,pt.y,false))
				addSystem(pt);
		}
		return attempts;
	}
	public Point.Float findAnyValidLocation(Point.Float p) {
		setRandom(p);
		while (!valid(p))
			setRandom(p);

		return p;
	}
	// modnar: add specific placement of orion/homeworld locations
	public Point.Float findSpecificValidLocation(Point.Float p) {
		setSpecific(p);
		indexWorld++; // modnar: increment indexWorld for subsequent homeworld locations
		while (!valid(p)) {
			setSpecific(p);
			// modnar: incrementing indexWorld here to prevent accidental infinite loop of bad locations,
			// but need setSpecific to have some form of repeating modulo cut-off
			indexWorld++;
		}
		return p;
	}
	protected void addSystem(Point.Float pt) { // BR: changed to protected
		addSystem(pt.x, pt.y);
	}
	private void addSystem(float x0, float y0) {
		if (usingRegions) {
			int xRgn = (int) (regionScale*x0/width);
			int yRgn = (int) (regionScale*y0/height);
			regions[xRgn][yRgn].addSystem(x0,y0);
			num++;
		}
		else {
			x[num] = x0;
			y[num] = y0;
			num++;
		}
	}
	protected boolean isTooNearExistingSystem(float x0, float y0, boolean isHomeworld) {
		if (isHomeworld) {
			if (distance(x0,y0,orionXY.x,orionXY.y) <= orionBuffer)
				return true;
			for (EmpireSystem emp: empSystems) {
				if (distance(x0,y0,emp.colonyX(),emp.colonyY()) <= empireBuffer)
					return true;
			}
		}
		// float buffer = systemBuffer(); // BR: made global
		// not too close to other systems in galaxy
		if (usingRegions) {
			if (isTooNearSystemsInNeighboringRegions(x0,y0))
				return true;
		}
		else {
			if (isTooNearSystemsInEntireGalaxy(x0,y0, sysBuffer)) // BR: global
				return true;
		}
		// not too close to other systems in any empire system
		for (EmpireSystem emp: empSystems) {
			for (int i=0;i<emp.num;i++) {
				if (distance(x0,y0,emp.x(i),emp.y(i)) <= sysBuffer) // BR: global
					return true;
			}
		}
		return false;
	}
	private boolean isTooNearSystemsInNeighboringRegions(float x0, float y0) {
		int xRgn = (int)(x0*regionScale/width);
		int yRgn = (int)(y0*regionScale/height);
		int yMin = max(0,yRgn-1);
		int yMax = min(regionScale-1,yRgn+1);
		int xMin = max(0,xRgn-1);
		int xMax = min(regionScale-1,xRgn+1);

		for (int x1=xMin;x1<=xMax;x1++) {
			for (int y1=yMin;y1<=yMax;y1++) {
				if (regions[x1][y1].isTooNearSystems(x0,y0))
					return true;
			}
		}
		return false;
	}
	private boolean isTooNearSystemsInEntireGalaxy(float x0, float y0, float buffer) {
		for (int i=0;i<num;i++) {
			if (distance(x0,y0,x[i],y[i]) <= buffer)
				return true;
		}
		return false;
	}
	// ========================================================================
	// Nested Classes
	//
	private class ShapeRegion implements Serializable {
		int num = 0;
		float[] x;
		float[] y;
		public ShapeRegion(int maxStars) {
			x = new float[maxStars];
			y = new float[maxStars];
		}
		public boolean isTooNearSystems(float x0, float y0) {
			float buffer = systemBuffer();
			for (int i=0;i<num;i++) {
				if (distance(x0,y0,x[i],y[i]) <= buffer)
					return true;
			}
			return false;
		}
		private void addSystem(float x0, float y0) {
			x[num] = x0;
			y[num] = y0;
			num++;
		}
	}
	public final class EmpireSystem implements Serializable {
		float[] x = new float[3];
		float[] y = new float[3];
		int num = 0;
		boolean valid = false;

		public EmpireSystem(GalaxyShape sp) {
			// empire is valid if it can create a valid home system
			// and two valid nearby stars
			valid = addNewHomeSystem(sp);
			valid = valid && addNearbySystem(sp, colonyX(), colonyY(), 3.0f);
			valid = valid && addNearbySystem(sp, colonyX(), colonyY(), 3.0f);
		}
		// BR: for symmetric galaxy
		public EmpireSystem(GalaxyShape sp, Point.Float pt) {
			// create first a valid home system (pt is already validated)
			// then from a second call two valid nearby stars
			addSystem(pt.x,pt.y);
		 	valid = true;
		}
		// BR: for symmetric galaxy
		public boolean addNearbySystems(GalaxyShape sp, Point.Float pt) {
			// if pt = null then search for a nearby system
			// else the system is already validated... add it
			boolean valid = false;
			if (pt == null) { // player world, search for one
				valid = addNearbySystem(sp, colonyX(), colonyY(), 3.0f);
				return valid;
			}
			addSystem(pt.x,pt.y);
			valid = true;
			return valid;
		}
 
		public int numSystems()   { return num; }
		public float x(int i)	{ return x[i]; }
		public float y(int i)	{ return y[i]; }
		public float colonyX()   { return x[0]; }
		public float colonyY()   { return y[0]; }

		public boolean inNebula(Nebula neb) {
			for (int i=0;i<num;i++) {
				if (neb.contains(x[i], y[i]))
					return true;
			}
			return false;
		}

		private boolean addNewHomeSystem(GalaxyShape sp) {
			int attempts = 0;
			Point.Float pt = new Point.Float();
			while (attempts++ < 100) {
				findSpecificValidLocation(pt); // modnar: add specific placement of homeworld locations
				//findAnyValidLocation(pt);
				if (!sp.isTooNearExistingSystem(pt.x,pt.y,true)) {
					addSystem(pt.x,pt.y);
					return true;
				}
			}
			return false;
		}
		private boolean addNearbySystem(GalaxyShape sh, float x0, float y0, float maxDistance) {
			float x1 = x0-maxDistance;
			float x2 = x0+maxDistance;
			float y1 = y0-maxDistance;
			float y2 = y0+maxDistance;
			int attempts = 0;
			Point.Float pt = new Point.Float();
			float buffer = systemBuffer();
			while (attempts < 100) {
				attempts++;
				pt.x = random(x1, x2);
				pt.y = random(y1, y2);
				if (sh.valid(pt)) {
					boolean tooCloseToAny = isTooNearExistingSystem(sh,pt.x,pt.y, buffer);
					boolean tooFarFromRef = distance(x0, y0, pt.x,pt.y) >= maxDistance;
					if (!tooCloseToAny && !tooFarFromRef) {
						addSystem(pt.x,pt.y);
						return true;
					}
				}
			}
			return false;
		}
		private boolean isTooNearExistingSystem(GalaxyShape sh, float x0, float y0, float buffer) {
			for (int i=0;i<num;i++) {
				if (distance(x0,y0,x[i],y[i]) <= buffer)
					return true;
			}
			return sh.isTooNearExistingSystem(x0,y0,false);
		}
		private void addSystem(float x0, float y0) {
			x[num] = x0;
			y[num] = y0;
			num++;
		}
	}

	/**
	 *  integrated the modnar companions world here
	 *  to avoid to spread symmetry management everywhere
	 *  Added some diversity in their angular placement
	 */
	public class CompanionWorld { // BR:

		final double minRandom = twoPI / 6.0;
		CtrPoint[] cW;
		
		// ========== constructors ==========
		//
		CompanionWorld(int numComp) {
			cW = new CtrPoint[abs(numComp)];			
		}
		CompanionWorld(EmpireSystem empire, int numComp) {
			this(numComp);
			CtrPoint home = new CtrPoint(empire.colonyX(), empire.colonyY());
			if (numComp == 0) { return; }
			if (numComp > 0) {
				double ctr   = twoPI / numComp;
				double width = twoPI / numComp - minRandom;
				double orientation = rand.rand(twoPI); // Global orientation
				for (int i=0; i<numComp; i++) {
					cW[i] = home.shift(unit(orientation + rand.sym(i * ctr, width)));
				}
			} else { // old way
				numComp = -numComp;
				double ctr = twoPI / 4;
				double orientation = twoPI / 8 ; // Global orientation
				for (int i=0; i<numComp; i++) {
					cW[i] = home.shift(unit(orientation - i * ctr));
				}			
			}
		}
	   	// ========== Getters ==========
	   	//
		CompanionWorld[] symmetric() {
			CompanionWorld[] cw = new CompanionWorld[numEmpires];
			double angle = twoPI / numEmpires;
			cw[0] = this;
			for (int i=1; i<numEmpires; i++) {
				cw[i] = rotate(i * angle);
			}
			return cw;
		}
		private CompanionWorld rotate(double angle) {
			CompanionWorld cw = new CompanionWorld(cW.length);
			for (int i=0; i<cW.length; i++) {
				cw.cW[i] = cW[i].rotate(angle);
			}
			return cw;
		}
		private CtrPoint unit(double angle) {
			return new CtrPoint(1.0, 0.0).rotate(angle);
		}
	}
	/**
	 * Quite similar to Point.Float for center referenced point
	 * Not an extended Point class to prevent confusion
	 */
	public class CtrPoint implements Cloneable { // BR:

		// double as all Math operation are performed in double!
		// And this allow to differentiate centered points (double)
		// vs non centered points (float)
		double x = 0f;
		double y = 0f;

		// ========== constructors ==========
		//
	   	CtrPoint() {}
	   	/**
	   	 * @param x referenced to center; y=0
	   	 */
	   	CtrPoint(double x) { this.x = x; }
	   	/**
	   	 * @param xc referenced to center
	   	 * @param yc referenced to center
	   	 */
	   	CtrPoint(double xc, double yc) { x = xc; y = yc; }
	   	/**
	   	 * @param xe referenced to the edge
	   	 * @param ye referenced to the edge
	   	 */
	   	CtrPoint(float xe, float ye) { x = xe - cx; y = ye - cy; }
	   	/**
	   	 * @param pt referenced to the edge
	   	 */
	   	CtrPoint(Point.Float pt) { x = pt.x - cx; y = pt.y - cy; }
	   	/**
	   	 * @param pt referenced to center
	   	 */
	   	CtrPoint(CtrPoint pt) { x = pt.x; y = pt.y; }

	   	// ========== Getters ==========
	   	//
	   	Point.Float get()   { return new Point.Float((float)x + cx, (float)y + cy); }   	
	   	float getX()        { return (float) x + cx; }   	
	   	float getY()        { return (float) y + cy; }   	
		double ray()        { return Math.sqrt(x*x + y*y); }
		double angle()      { return Math.atan2(y, x); } // yes y and x in this order!!!
		CtrPoint mirrorX()  { return new CtrPoint(-x, y); }
		CtrPoint mirrorY()  { return new CtrPoint(x, -y); }
		CtrPoint mirrorXY() { return new CtrPoint(-x, -y); }
		CtrPoint rotate(double angle) {
			return new CtrPoint(Math.cos(angle) * x + Math.sin(angle) * y,
								Math.cos(angle) * y - Math.sin(angle) * x);
		}
		CtrPoint shift(CtrPoint shift) {
			return new CtrPoint(x + shift.x, y + shift.y);
		}
		CtrPoint shift(double dx, double dy) {
			return new CtrPoint(x + dx, y + dy);
		}
		@Override protected CtrPoint clone() { return new CtrPoint(this); }
	   	// ========== Other Methods ==========
	   	//
		protected CtrPoint[] rotate(double angle, int n) {
			CtrPoint[] result = new CtrPoint[n];
			CtrPoint pt = this;
			for (int i=0; i<n; i++) {
				pt = pt.rotate(angle);
				result[i] = pt;
			}
			return result;
		}

	   	// ========== Point.Float Methods ==========
	   	//
		float ray(float x, float y)   { return distance(cx, cy, x, y); }
		float ray(Point.Float pt)     { return ray(pt.x, pt.y); }
		float angle(float x, float y) { return (float) Math.atan2(y- cy, x - cx); }
		float angle(Point.Float pt)   { return angle(pt.y, pt.x); }
		Point.Float mirrorX(Point.Float pt)  { return new Point.Float(width - pt.x, pt.y); }
		Point.Float mirrorY(Point.Float pt)  { return new Point.Float(pt.x, height - pt.y); }
		Point.Float mirrorXY(Point.Float pt) { return new Point.Float(width - pt.x, height - pt.y); }
	}
	public static class Rand { // BR: based on Modnar chosen randomization
		// For 1 dimensional generation:
		private static final double c0 = 0.6180339887498948482046; // inverse of golden ratio
		// For 2 dimensional generation:
		private static final double cX = 0.7548776662466927600495; // inverse of plastic number
		private static final double cY = 0.5698402909980532659114; // square inverse of plastic number
		private double last  = Math.random();
		private double lastX = Math.random();
		private double lastY = Math.random();
		// Base Setters
		/**
		 * Give a new seed and return a random value
		 * @param seed the new seed value
		 * @return  0 <= random value < 1
		 */
		public double seed (double seed) { return last  = (last  + c0)%1; }
		/**
		 * Give a new seed and return a random value
		 * @param seed the new seed value
		 * @return  0 <= random value < 1
		 */
		public double seedX(double seed) { return lastX = (seed  + cX)%1; }
		/**
		 * Give a new seed and return a random value
		 * @param seed the new seed value
		 * @return  0 <= random value < 1
		 */
		public double seedY(double seed) { return lastY = (seed  + cY)%1; }
		// Base Getters
		/**
		 * @return  0 <= random value < 1
		 */
		public double rand()  { return last  = (last  + c0)%1; }
		/**
		 * @return  0 <= random value < 1
		 */
		public double randX() { return lastX = (lastX + cX)%1; }
		/**
		 * @return  0 <= random value < 1
		 */
		public double randY() { return lastY = (lastY + cY)%1; }
		// Getters with multiplier
		/**
		 * @return  0 <= random value < max
		 */
		public double rand (double max) { return max * rand();  }
		/**
		 * @return  0 <= random value < max
		 */
		public double randX(double max) { return max * randX(); }
		/**
		 * @return  0 <= random value < max
		 */
		public double randY(double max) { return max * randY(); }
		// Getters with Limits
		/**
		 * @return  min(lim1, lim2) <= random value < max(lim1, lim2)
		 */
		public double rand(double lim1, double lim2) {
			return rand(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
		}
		/**
		 * @return  min(lim1, lim2) <= random value < max(lim1, lim2)
		 */
		public double randX(double lim1, double lim2) {
			return randX(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
		}
		/**
		 * @return  min(lim1, lim2) <= random value < max(lim1, lim2)
		 */
		public double randY(double lim1, double lim2) {
			return randY(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
		}
		// Base Symmetric Getters
		/**
		 * @return  -1 <= random value < 1
		 */
		public double sym()  { return rand() * 2.0 - 1.0; }
		/**
		 * @return  -1 <= random value < 1
		 */
		public double symX() { return lastX = (lastX + cX)%1; }
		/**
		 * @return  -1 <= random value < 1
		 */
		public double symY() { return lastY = (lastY + cY)%1; }
		// Symmetric Getters with multiplier
		/**
		 * @return  -max1 <= random value < max
		 */
		public double sym (double max) { return max * sym();  }
		/**
		 * @return  -max1 <= random value < max
		 */
		public double symX(double max) { return max * symX(); }
		/**
		 * @return  -max1 <= random value < max
		 */
		public double symY(double max) { return max * symY(); }
		// Symmetric Getters with Limits
		/**
		 * @return  ctr-width/2 <= random value < ctr+width/2
		 */
		public double sym(double ctr, double width) {
			return (rand() - 0.5) * width + ctr;
		}
		/**
		 * @return  ctr-width/2 <= random value < ctr+width/2
		 */
		public double symX(double ctr, double width) {
			return (randX() - 0.5) * width + ctr;
		}
		/**
		 * @return  ctr-width/2 <= random value < ctr+width/2
		 */
		public double symY(double ctr, double width) {
			return (randY() - 0.5) * width + ctr;
		}
	}
}
