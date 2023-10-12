package rotp.model.combat;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.image.BufferedImage;

// Combat Shield as ellipsoid
// Z target must be lower than Z source
// Delta Z should be tune for the best effects...
public final class CombatShield {
	// BR: a lot of "final" thingh, as it may (or may not) improve the performances!?

	private static final float topShield		= 15;
	private static final float alphaImpactMin	= 0.5f;
	private static final float alphaImpactMax	= 0.9f;
	private static final float alphaSpreadMin	= 0.1f;
	private static final float alphaSpreadMax	= 0.3f;
	private static final float alphaCloseMin	= 0.5f;
	private static final float alphaCloseMax	= 0.1f;
	private static final float alphaEndDiv		= 2.0f;
	private static final float impactRbase		= 0.05f;
	private static final float impactRmin		= 0.05f;
	private static final float impactRmax		= 0.20f;
	private static final float spreadDrEnd		= 0.05f;
	private static final float spreadDrMin		= 0.2f;
	private static final float spreadingRmax	= 0.95f - spreadDrEnd;
	private static final float edgeRay			= 1.0f - spreadDrEnd;
	private static final float edgeRay_			= edgeRay - spreadDrEnd;

	private static final float impactFadeFactor = 1.2f;
	private static final float impactAlphaRatio = 0.7f;
	private static final float spreadFadeFactor = 1.5f;

	private final Tint noColor	= new Tint();
	private final int shieldLevel;
	private float damage, beamForce;

	private static final double	PI		= Math.PI;
	private static final double	HALF_PI	= PI/2;
	private static final int FF		= 255;
	public	static final int ABOVE	= 0;
	public	static final int BELLOW	= 1;

	private static final int IMPACTING	= 0;
	private static final int SPREADING	= 1;
	private static final int FADING		= 2;
	private static final int RING_NUM	= 3;

	// Rendering
	private final GradientRing[] gradientRing = new GradientRing[RING_NUM];
	private final int shieldColumns, shieldRows, paintLength;
	private final int perimeter, halfPerimeter, quarterPerimeter, fullDistance;
	private final Tint[][] paintArray;
	private double noiseFactor = 0.;

	// Locations, size, weapons
	private final Pos targetCenter	= new Pos();
//	private final Pos targetCenter	= new Pos();
//	private final Pos shieldCenter	= new Pos();
	private final Pos shipSource	= new Pos();
	private final Pos shipImpact	= new Pos();
	private final Pos impactAdj		= new Pos();
	private int beamDiameter;
	private Color shieldColor, beamColor;
	private final int windUpFramesNum, holdFramesNum, landUpFramesNum, fadingFramesNum;
	private final int framesNum, beamFramesNum;
	private int attacksPerRound;
	private BufferedImage targetImage;

	// Results
	private final Pos shieldImpact = new Pos();
//	private final Geodesic geodesic = new Geodesic();
	private final Geodesic geodesic;
	private double insideRatio;
	private final BufferedImage[][] shieldArray = new BufferedImage[2][];
	private final int shieldOffsetX, shieldOffsetY, centerOffsetX, centerOffsetY;

	// Other variables
	private boolean neverInitialized = true;

	// Debug
	boolean showTiming = false;
	boolean debug = false;
	static int maxWidthPct = 0;
	static int maxHeightPct = 0;
	
	// = = = = = Parameters management = = = = =
	//
	private BufferedImage[][] shieldArray() {
		if (neverInitialized)
			initShieldArray();
		return shieldArray;
	}
	public double insideRatio() {
		if (neverInitialized)
			initShieldArray();
		return insideRatio;
	}

	// = = = = = Constructors, Setters and Initializers = = = = =
	//
	public CombatShield(int windUpFrames, int holdFrames, int landUpFrames, int fadingFrames,
			int xCtr, int yCtr, int z, Color color, int border,
			int shieldLevel, BufferedImage image, int boxWidth, int boxHeight) {
		windUpFramesNum = windUpFrames;
		holdFramesNum   = holdFrames;
		landUpFramesNum = landUpFrames;
		fadingFramesNum = fadingFrames;
		beamFramesNum	= 1 + landUpFramesNum;
		framesNum		= beamFramesNum + holdFramesNum + fadingFramesNum;
		shieldColor		= color;

		targetCenter.set(xCtr, yCtr, z);
//		targetCenter.set(xTopLeft+boxWidth/2, yTopLeft+boxHeight/2, z);
//		shieldCenter.set(0, 0, 0); // Alway 0: the references point
		this.shieldLevel = shieldLevel; // = design().maxShield
		targetImage = image;
		long timeStart = System.currentTimeMillis();
		
		ShipAnalysis sA = new ShipAnalysis(targetImage, boxWidth, boxHeight, border);
		
		shieldColumns = (int) (2*sA.b+1);
		shieldRows	 = (int) (2*sA.a+1);
		perimeter	 = (int) sA.ellipsePerimeter();
		halfPerimeter	 = perimeter/2;
		quarterPerimeter = perimeter/4;
		fullDistance	 = (int) (halfPerimeter * 1.2);
		paintLength		 = (int) (perimeter * 1.1); // with a little reserve!

		paintArray = new Tint[framesNum][];
		shieldArray[ABOVE]  = new BufferedImage[framesNum];
		shieldArray[BELLOW] = new BufferedImage[framesNum];
		for (int frame=0; frame< framesNum; frame++) {
			shieldArray[ABOVE][frame]	= new BufferedImage(shieldColumns, shieldRows, TYPE_INT_ARGB);
			shieldArray[BELLOW][frame]	= new BufferedImage(shieldColumns, shieldRows, TYPE_INT_ARGB);
			paintArray[frame]			= new Tint[paintLength];
		}
		shieldOffsetX = sA.shieldOffsetX();
		shieldOffsetY = sA.shieldOffsetY();
		centerOffsetX = sA.centerOffsetX();
		centerOffsetY = sA.centerOffsetY();
//		shieldCenter.set(x+shieldOffsetX, y+shieldOffsetY, z);
//		shieldCenter.set(x-shieldOffsetX, y-shieldOffsetY, z);
		
		geodesic = new Geodesic(sA.a, sA.b);
//		System.out.println("Given semi-axis: a=" + sA.a + " b=" + sA.b);
		
		if (showTiming)
			System.out.println("shipAnalyzis() Time = " + (System.currentTimeMillis()-timeStart));
	}
	public void setNoise(int pct) { noiseFactor = 0.01 * pct; }
	public int[] setImpact(int dx, int dy, int dz) {
		impactAdj.set(dx, dy, dz);
		shipImpact.set(targetCenter.x+dx+centerOffsetX, targetCenter.y+dy+centerOffsetY, dz);
		return new int[] { (int)shipImpact.x, (int)shipImpact.y};
	}
	public void setSource(int x, int y, int z, Color color, int beamSize) {
		shipSource.set(x, y, z);
		beamDiameter = beamSize;
		beamColor	 = color;
	}
	public void setWeapons(int attacksPerRound, float damage, float beamForce) {
		this.attacksPerRound = attacksPerRound;
		this.beamForce = beamForce;
		this.damage = damage;
	}

	private void initPaints() {
		float shieldForce = shieldLevel / topShield;
		float beamPowerFactor = 0;
		float absorptionRatio = 0;	   
		if (beamForce > 0) {
			beamPowerFactor = (float) Math.log10(beamForce);
			absorptionRatio = damage>0? 1 - (damage/beamForce):1;
		}
		// Transparencies: Transparent = 0; Opaque = 1
		float impactSpreadFactor = bounds(0, (float)Math.sqrt(beamPowerFactor * absorptionRatio), 1);
		float alphaImpact		 = alphaImpactMin + absorptionRatio * (alphaImpactMax-alphaImpactMin);
		float shieldAlphaSpread	 = alphaSpreadMin + impactSpreadFactor * (alphaSpreadMax-alphaSpreadMin);
		float shielAlphaEnd		 = Math.min(1f, shieldAlphaSpread*2);
		// Impact color based on BeamWeapon
		Tint impactColorStart	= new Tint(beamColor, alphaImpact);
		Tint impactColorEdge	= new Tint(beamColor, alphaImpact*impactAlphaRatio);
		// Spread color based on Target color
		Tint shieldSpreadColor	= new Tint(shieldColor, shieldAlphaSpread);
		Tint shieldSpreadColor2	= new Tint(shieldColor, shielAlphaEnd);

		// Rays
		int impactR0Start	= 0;
		int impactR0End		= 0;
		float impactR1Start	= beamDiameter/2; // Plain color Impact
		float impactR1End	= fullDistance * bounds(impactRmin, impactRbase*beamPowerFactor, impactRmax);
		float impactR2Start	= impactR1Start * 1.5f;
		float shieldR0Start	= impactR1Start * 1.f;
		
		float cover = 1f;
		float shieldR1End	= impactR1End + fullDistance * spreadDrMin;
		float deltaR1		= shieldR1End - impactR1End;
		float impactR2End	= impactR1End + deltaR1*cover;
		float shieldR0End	= shieldR1End - deltaR1*cover;

		float shieldFactor	= bounds(0, (float)Math.sqrt(shieldForce * absorptionRatio), 1);
		shieldFactor = 0;
		float shieldR2End	= shieldR1End + shieldFactor * (fullDistance*spreadingRmax-shieldR1End);
		float shieldR1Start	= shieldR1End * impactR1Start/impactR1End;
		float shieldR2Start	= shieldR2End * impactR1Start/impactR1End;

		float shieldR3End	= shieldR2End + deltaR1*cover;
		float shieldR3Start	= shieldR3End * impactR1Start/impactR1End;
		
		shieldR3End	  = fullDistance;
		//shieldR3Start = fullDistance;

		// On beam impact
		Tint[]	colors	  = new Tint[]	{impactColorStart, impactColorStart, noColor,      noColor};
		float[]	startRays = new float[]	{impactR0Start,    impactR1Start,   impactR2Start, fullDistance};
		float[]	stopRays  = new float[]	{impactR0End,      impactR1End,     impactR2End,   fullDistance};
		gradientRing[IMPACTING] = new GradientRing(colors, startRays, stopRays, beamFramesNum);	
		
		// Spreading
		colors    = new Tint[] {noColor, noColor,       shieldSpreadColor, shieldSpreadColor, noColor,       noColor};
		startRays = new float[]{0,       shieldR0Start, shieldR1Start,      shieldR2Start,      shieldR3Start, fullDistance};
		stopRays  = new float[]{0,       shieldR0End/2, shieldR1End,        shieldR2End,        shieldR3End,   fullDistance};
		gradientRing[SPREADING] = new GradientRing(colors, startRays, stopRays, beamFramesNum*4/2);
		
		// Fading
		Tint fullColor	= new Tint(FF,FF,FF,FF);
		float startFade = fullDistance / fadingFramesNum;
		colors    = new Tint[] {noColor, noColor,      fullColor,    fullColor};
		startRays = new float[]{0,       0,            startFade,    fullDistance};
		stopRays  = new float[]{0,       fullDistance, fullDistance, fullDistance};
		gradientRing[FADING] = new GradientRing(colors, startRays, stopRays, fadingFramesNum);
	}
	private void initPaintArray() {
		for (int frame=0; frame<framesNum; frame++) {
			Tint[] paint = gradientRing[IMPACTING].getGradientPaint(paintLength);
			gradientRing[IMPACTING].evolve();
			for (int idx=1; idx<2; idx++) {
				Tint[] tintArr = gradientRing[idx].getGradientPaint(paintLength);
				gradientRing[idx].evolve();
				for (int dist=0; dist<paintLength; dist++)
					paint[dist] = paint[dist].addColor(tintArr[dist]);
			}
			paintArray[frame] = paint;
		}

		// Shield fading
		for (int frame=beamFramesNum+holdFramesNum; frame<framesNum; frame++) {
			Tint[] paint   = paintArray[frame];
			Tint[] tintArr = gradientRing[FADING].getGradientPaint(paintLength);
			gradientRing[FADING].evolve();
			for (int dist=0; dist<paintLength; dist++) {
				paint[dist] = paint[dist].setAlphaMultiplier(tintArr[dist]);
			}
		}
	}
	private void initShieldArray() {
		long timeStart = System.currentTimeMillis();
		long timeMid = timeStart;
		neverInitialized = false;
		
		initPaints();
		initPaintArray();
		if (showTiming)
			System.out.println("init Rings and PAints Array Time = " + (System.currentTimeMillis()-timeMid));
		timeMid = System.currentTimeMillis();

//		locateShieldImpact();
		insideRatio = findShieldImpact(shipSource, impactAdj, shieldImpact);
		geodesic.setImpact(shieldImpact);
//		System.out.println("insideRatio: " + insideRatio() +  "  Impact: " + shieldImpact);	
		
		geodesic.drawShieldArray(noiseFactor);
		
		if (showTiming)
			System.out.println("initShieldArray() Time = " + (System.currentTimeMillis()-timeStart));
	}

	// = = = = = getters = = = = =
	//
	//BufferedImage getNextImage()		{ return shieldList().remove(0); }
	public BufferedImage[][] getShieldArray()	{ return  shieldArray(); }
	public int shieldOffsetX()	{ return shieldOffsetX; }
	public int shieldOffsetY()	{ return shieldOffsetY; }
	public int centerOffsetX()	{ return centerOffsetX; }
	public int centerOffsetY()	{ return centerOffsetY; }
	public int getA()			{ return (int) geodesic.a; }
	public int getB()			{ return (int) geodesic.b; }
	public int shieldTopLeftX()	{ return (int) (targetCenter.x+centerOffsetX-shieldColumns/2); }
	public int shieldTopLeftY()	{ return (int) (targetCenter.y+centerOffsetY-shieldRows/2); }
	public int[] shieldImpact()	{
		return new int[] {	(int) (targetCenter.x+centerOffsetX+geodesic.xI),
							(int) (targetCenter.y+centerOffsetY+geodesic.yI)};
	}

	// = = = = = Private Methods = = = = =
	//
	// = = = = = Tools = = = = =
	//
	private double findShieldImpact(Pos src, Pos impactAdj, Pos unused) {
		// -> Center the ellipsoid on point = [0,0,0]
		// Surface equation: x*x/a/a + y*y/b/b + z*z/c/c = 1  
		// New target
		double xo = impactAdj.x;
		double yo = impactAdj.y;
		double zo = impactAdj.z;
//		System.out.println("xo =" + xo + "  yo =" + yo + "  zo =" + zo);
		// - beam path
		double dx = src.x - shipImpact.x;
		double dy = src.y - shipImpact.y;
		double dz = src.z - shipImpact.z; // Should be positive
//		System.out.println("dx =" + dx + "  dy =" + dy + "  dz =" + dz);
		// beam equation: [xo, yo, zo] + m * [dx, dy, dz] = [x, y, z] with m>0
		// Intersection point (z always positive)
		// (xo + m∙dx)^2 /a +  (yo + m∙dy)^2 /b +  (zo + m∙dz)^2 /c = 1
		//
		double dxN = dx/geodesic.bb;
		double dyN = dy/geodesic.aa;
		double dzN = dz/geodesic.aa;
		double k1 = 2*(xo*dxN + yo*dyN + zo*dzN);
		double k2 = 2*(dx*dxN + dy*dyN + dz*dzN);
		double k3 = xo*xo/geodesic.bb + (yo*yo+zo*zo)/geodesic.aa;
		double sr = Math.sqrt(k1*k1 - 2*(k3-1)*k2);
		double m1 = (-sr-k1)/k2;
		double m2 = (sr-k1)/k2;
		double insideRatio = m2>m1 ? m2 : m1;
		shieldImpact.x = impactAdj.x + insideRatio * dx;
		shieldImpact.y = impactAdj.y + insideRatio * dy;
		shieldImpact.z = impactAdj.z + insideRatio * dz;
//		System.out.println("shieldImpact =" + shieldImpact.toString());
		return insideRatio;
	}
//	private void locateShieldImpact() { insideRatio = findShieldImpact(shipSource, shipImpact, shieldImpact); }
	private float bounds(float low, float val, float hi) {
		return Math.min(Math.max(low, val), hi);
	}
	private int bounds(int low, int val, int hi) {
		return Math.min(Math.max(low, val), hi);
	}

	// = = = = = Nested Classes = = = = =
	//
	private final class ShipAnalysis { // TODO BR: shipAnalyzis(...)
		private final BufferedImage img;
		private final int imgW, imgH;
		private final double centerHeight;
		private final double[] dySqMax;

		private boolean evenSymmetry;
		private int ctrRow, ctrCol, colMin, colMax;
		private double a, b, aa, bb;
		private double shipRatio, boxRatio;
		private double leftSqMax, rightSqMax, distSqMax;
		private boolean tooHigh, tooLong, tooBig;
		
		private static final int alphaLim = 32;

		private void minimize(double margin, boolean show) {
			double maxAdjust	= Math.sqrt(distSqMax); // Min Product
			a = a * maxAdjust * margin;
			b = b * maxAdjust * margin;
			aa = a*a;
			bb = b*b;
			if (show)
				System.out.println("minimize");
			testSize(show);
		}
		private ShipAnalysis(BufferedImage shipImg, int boxWidth, int boxHeight, int border) {
			img		= shipImg;
			imgW	= img.getWidth();
			imgH	= img.getHeight();
			dySqMax	= new double[imgW];

			// Locate vertical position (relative to ship image)
			centerHeight = getVerticalCenter();
			// Find border square distances
			squareDistance(boxWidth, boxHeight);
			// Get first overview
			a(boxHeight/2.0);
			b(boxWidth/2.0);
			ctrCol = (colMax + colMin)/2;
			
			boolean show = false;
			
			// Set the ship ratio (increase size)
			double ratioAdjust = shipRatio/boxRatio;
			if (ratioAdjust>1) // extend b
				b (a * shipRatio);
			else
				a (b / shipRatio);
			testSize(show);

			// Centering and resize first pass
			center(show);
			minimize(1.02, show);
			// Centering and resize second pass
			center(show);
			minimize(1.0, show);
			
			// Add Borders
			a(a+2*border);
			b(b+2*border);
			
			// validate size
			tooHigh	= (int)(2*a) > boxHeight;
			tooLong	= (int)(2*b) > boxWidth;
			tooBig	= tooHigh || tooLong;
			
//			System.out.println("b=" + (int)b + "  a=" + (int)a);
			maxWidthPct = Math.max(maxWidthPct, (int)((int)(2*b)*100.0/boxWidth));
			maxHeightPct = Math.max(maxHeightPct, (int)((int)(2*a)*100.0/boxHeight));
//			System.out.println("maxWidthPct=" + maxWidthPct + "%  maxHeightPct=" + maxHeightPct + "%");
			
			if (tooBig)
				System.err.println("Shield needs to be bigger than box size: H:" +
						(int)((int)(2*a)*100.0/boxHeight) + "%  W:" +
						(int)((int)(2*b)*100.0/boxWidth) + "%");
		}
		private void a(double val) {
			a  = val;
			aa = a*a;
		}
		private void b(double val) {
			b  = val;
			bb = b*b;
		}
		private void testSize(boolean show) {
			leftSqMax  = 0;
			rightSqMax = 0;
			for (int x=colMin; x<ctrCol; x++)
				leftSqMax = Math.max(leftSqMax, dySqMax[x]/aa + (x-ctrCol)*(x-ctrCol)/bb);
			for (int x=ctrCol; x<colMax; x++)
				rightSqMax = Math.max(rightSqMax, dySqMax[x]/aa + (x-ctrCol)*(x-ctrCol)/bb);
			distSqMax = Math.max(rightSqMax, leftSqMax);
			if (show) {
				System.out.println("leftSqMax = " + leftSqMax);
				System.out.println("rightSqMax = " + rightSqMax);
				System.out.println("distSqMax = " + distSqMax);			
			}
		}
		private void center(boolean show) {
			if (show)
				System.out.println("Center");
			int corr = (int) Math.abs(b * (leftSqMax-rightSqMax)/2);
			while (corr > 0) {
//				System.out.println("corr = " + corr);
				if (rightSqMax > leftSqMax)
					ctrCol+=corr;
				else
					ctrCol-=corr;
				ctrCol = Math.max(0,Math.min(ctrCol, imgW));
				testSize(show);
//				System.out.println("Centered semi-axis: a=" + (int)a + " b=" + (int)b + " off=" + (b-ctrCol));
				corr = (int) Math.abs(b * (leftSqMax-rightSqMax)/2);
			}
		}
		private void mapRatio(boolean show) {
			double maxAdjust	= Math.sqrt(distSqMax); // Min Product
			double ratioAdjust	= shipRatio/boxRatio;
			if (ratioAdjust>1) { // extend b
				a = a * maxAdjust;
				b = a * shipRatio;
			} else {
				b = b * maxAdjust;
				a = b / shipRatio;
			}
			aa = a*a;
			bb = b*b;
			if (show)
				System.out.println("mapRatio");
			testSize(show);
		}
		private void squareDistance(int boxWidth, int boxHeight) {
			colMin = -1;
			colMax = 0;
			double dySq = 0;
			for (int col=0; col<imgW; col++) {
				dySqMax[col] = 0;
				for(int row=0; row<=ctrRow; row++) {
					int color = img.getRGB(col, row);
					int alpha = color >> 32 & FF;
					if (alpha > alphaLim) {
						double sqMax = Math.pow(row-centerHeight, 2);
						if (sqMax > dySq)
							dySq = sqMax;
						dySqMax[col] = sqMax;
						if (colMin == -1)
							colMin = col;
						colMax = col;
						break;
					}
				}
			}
			int shipWidth	 = colMax-colMin+1;
			int shipHeight	 = (int) Math.round(2 * Math.sqrt(dySq));
//			System.out.println("boxWidth: " + boxWidth);
//			System.out.println("boxHeight: " + boxHeight);
//			System.out.println("shipWidth: " + shipWidth);
//			System.out.println("shipHeight: " + shipHeight);
			shipRatio = shipWidth / (double)shipHeight;
			boxRatio  = boxWidth / (double)boxHeight;			
		}
		private double getVerticalCenter() {
			int ctrCol = imgW/2;
			ctrRow = 0;
			int rowMin = -1;
			for(int row=0; row<imgH; row++) {
				int color = img.getRGB(ctrCol, row);
				int alpha = color >> 32 & FF;
//				System.out.println("alpha = " + alpha);
				if (alpha > alphaLim) {
					ctrRow = row;
					if (rowMin == -1)
						rowMin = row;
				}
			}
			if (ctrRow==0) { // if the ship is too off-centered
				ctrRow = imgH/2;
				rowMin = ctrRow;
			}
			int vSize = ctrRow-rowMin+1;
//			System.out.println("vSize = " + vSize);
//			System.out.println("ctrRow = " + ctrRow);
//			System.out.println("rowMin = " + rowMin);
			evenSymmetry = vSize%2==0;
//			System.out.println("evenSymmetry = " + evenSymmetry);
			double centerHeight = ctrRow - (vSize-1)/2.0;
			ctrRow -= vSize/2;
//			System.out.println("centerHeight = " + centerHeight);
//			System.out.println("ctrRow = " + ctrRow);
			return centerHeight;
		}
		private double ellipsePerimeter() {
			// Approximative Ramanujan's formulas
			return Math.round(PI*(3*(a + b) - Math.sqrt((3*a+b)*(a+3*b))));
		}
		private int shieldOffsetX() { return (int)(ctrCol-b); }
		private int shieldOffsetY() { return (int)(ctrRow-a); }
		private int centerOffsetX() { return (int)(ctrCol-imgW/2); }
		private int centerOffsetY() { return (int)(ctrRow-imgH/2); }
	}
	private final class Tint {
		//private final int[] tint;
		private int trans = FF; // Transparency = 1 - alpha
		private int red   = 0;
		private int green = 0;
		private int blue  = 0;
		
		private Tint()				{ }
		private Tint(Tint source)	{ set(source); }
		private Tint(int... source)	{ set(source); }
		private Tint(Color source)	{ set(source); }
		private Tint(int source)	{ set(source); }
		private Tint(Color source, double alpha) {
			set(source);
			saturate((int)(alpha * FF));
		}

		@Override protected Tint clone() {return new Tint(this); }
		@Override public String toString() { 
			return "A=" + (FF-trans) +
					" R=" + red +
					" G=" + green +
					" B=" + blue +
					" T=" + trans;
		}
		// Getters
		private int argb(double transparencyFactor)	{ // most intensively used
			if (trans==FF)
				return 0;
			return ((FF-(int)(trans*transparencyFactor) << 24) 
					| (red << 16) | (green << 8) | blue);
		}
		// Setters
		private Tint set(Tint color) {
			trans = color.trans;
			red = color.red;
			green = color.green;
			blue = color.blue;
			return this;
		}
		private Tint set(int... color) {
			trans = FF-color[0];
			red	  = color[1];
			green = color[2];
			blue  = color[3];
			return this;
		}
		private Tint set(int color)	{
			trans = FF-((color >> 24) & FF);
			red   = (color >> 16) & FF; 
			green = (color >> 8) & FF;
			blue  = color & 0xff;
			return this;
		}		
		private Tint set(Color source) {
			set(source.getRGB());
			return this;
		}
		// Operations
		private Tint addColor(Tint color) {
			if (trans == FF)
				return set(color);
			if (color.trans == FF)
				return this;
			int ct = color.trans;
			trans = trans * ct / FF;
			int ca = FF-color.trans;
			red   = (red   * ct + color.red   * ca) / FF;
			green = (green * ct + color.green * ca) / FF;
			blue  = (blue  * ct + color.blue  * ca) / FF;
			return this;
		}
		private Tint saturate() { // new int[]
			int max = Math.max(red, Math.max(green, blue));
			if (max == 0) {
				red = FF;
				green = FF;
				blue = FF;
			}
			else {
				float factor = 255f/max;
				red *= factor;
				green *= factor;
				blue *= factor;
			}
			return this;
		}
		private Tint saturate(int alpha) {
			trans = FF-alpha;
			return saturate();
		}
		private Tint setAlphaMultiplier(double mult) {
			trans = (int) (FF-mult*(FF-trans));
			return this;
		}
		private Tint setAlphaMultiplier(Tint mult) {
			trans = FF-(FF-mult.trans)*(FF-trans)/FF;
			return this;
		}
		private Tint minus(Tint sub) {
			Tint diff = new Tint();
			diff.trans = trans - sub.trans;
			diff.red   = red   - sub.red;
			diff.green = green - sub.green;
			diff.blue  = blue  - sub.blue;
			return diff;
		}
		private Tint extend(Tint diff, int mult, float div) {
			Tint mix = new Tint();
			mix.trans = bounds(0, trans + (int) (diff.trans * mult / div), FF);
			mix.red   = bounds(0, red   + (int) (diff.red   * mult / div), FF);
			mix.green = bounds(0, green + (int) (diff.green * mult / div), FF);
			mix.blue  = bounds(0, blue  + (int) (diff.blue  * mult / div), FF);
			return mix.saturate();
		}
	}
	private final class Geodesic {
		// Lambert's formula for long lines
		// Ellipsoid parameters
		private final double[] beta;
		private final double[] sqSinPcosQx2;
		private final double[] sqCosPsinQx2;
		private final double a, b;		// a = The circle radius, b the third semi-axis
		private final double f;		// flattening
		private final double half_f;
		private final double aa, bb; 
		private final double aabb;
		private final double iaa, ibb;
		private final double b_a, aa_bb;	// b/a

		// Impact parameters
		private double xI, yI, zI;	// the impact point
		private double xI_bb, yI_aa, zI_aa;	// the impact point
		private double betaI;	// reduced latitude
		
//		private Geodesic() {}
		private Geodesic(double a, double b) {
			this.a	= a;
			this.b	= b;
			f		= (a-b)/(double)a;
			half_f	= f/2;
			b_a		= b/(double)a;
			aa_bb	= 1/(b_a*b_a);
			aa	= a*a;
			bb	= b*b;
			aabb = (long)aa*bb;
			iaa = 1/(double)aa;
			ibb = 1/(double)bb;
			sqSinPcosQx2 = new double[shieldColumns];
			sqCosPsinQx2 = new double[shieldColumns];
			// Fill the spheric correction
			beta = new double[shieldColumns];
			for (int i=0; i<shieldColumns; i++) {
				beta[i] = reducedLatitude(i-b);
			}
		}
		@Override public String toString() { 
			return "x=" + xI + " y=" + yI + " z=" + zI;
		}

		private void setImpact(Pos I) {
			xI = I.x;
			yI = I.y;
			zI = I.z;
			xI_bb = xI*ibb;
			yI_aa = yI*iaa;
			zI_aa = zI*iaa;
			betaI = beta[(int) Math.round(xI+b)];
			for (int i=0; i<shieldColumns; i++) {
				double betaP = reducedLatitude(i-b);
				double cosP = Math.cos((betaI + betaP)/2);
				double cos2P = cosP*cosP;
				double sin2P = 1-cos2P;
				double cosQ = Math.cos((betaI - betaP)/2);
				double cos2Q = cosQ*cosQ;
				double sin2Q = 1-cos2Q;
				sqSinPcosQx2[i] = 2*sin2P*cos2Q;
				sqCosPsinQx2[i] = 2*cos2P*sin2Q;
			}
		}
		private boolean insideEllipse(double x, double y) {
			return (x*x*aa + y*y*bb) <= aabb;
		}
		private double reducedLatitude(double x) { // z in earth geodesic, but x for us!
			// tan(β)=(1-f)tan(Φ) with f = (a-b)/a = 1-b/a
			// tan(β)= b/a * tan(Φ)
			// Known: x = b*sin(Φ) ==> sin(Φ) = x/b;
			// tan(β)= b/a * tan(asin(x/b))
			// Known: tan(asin(α)) = α/sqrt(1-a^2)
			// tan(β)=b/a * x/b / sqrt(1-x^2/b^2)
			// tan(β)=x/a / sqrt(1-x^2/b^2)
			// tan(β)=x*b/a / sqrt(b^2-x^2)
			
			double delta = bb - x*x;
			if (delta==0)
				if (x>0)
					return HALF_PI;
				else
					return -HALF_PI;
			else
				return Math.atan(b_a*x/Math.sqrt(delta));
		}

		private void drawShieldArray(double noiseFactor) { // TODO BR: Here
			long timeStart = System.currentTimeMillis();

			double y = -a;
			for (int row=0; row<shieldRows; row++) {
				double x = -b;
				for (int column=0; column<shieldColumns; column++) {
					if (geodesic.insideEllipse(x, y)) {
						// Color on the shield
						// x = b*cos(θ) = b*sin(Φ)
						// y = a*cos(λ)*sin(θ) = a*cos(λ)*cos(Φ)
						// z = a*sin(λ)*sin(θ) = a*sin(λ)*cos(Φ)
						// set point
						double z = Math.sqrt(aa - y*y - x*x*aa_bb);
						// Transparency factor: Close to the border, transparency = 0
						// ==> z=0 ==> transparency = 0; alpha = 1
//						double minTransparency = 0.0;
//						double transparencyFactor =  minTransparency + (1-minTransparency) * z/a;
//						double transparencyFactor =  z/a;
//						double transparencyFactor =  Math.sqrt(z/a);
						double transparencyFactor =  Math.pow(z/a, 0.25);
						// Spherical central angle Δσ
						// Δσ = arcCos(sin(θ1)sin(θ2) + cos(θ1)cos(θ2)cos(λ1-λ2))
						// sin(θ1)sin(θ2) = (x1*x2)/b^2
						// cos(θ1)cos(θ2)cos(λ1-λ2) = cos(θ1)cos(θ2)cos(λ1)cos(λ2) + cos(θ1)cos(θ2)sin(λ1)sin(λ2)
						// cos(θ1)cos(θ2)cos(λ1-λ2) = (y1*y2+z1*z2)/a^2
						// Δσ = arcCos(x1*x2/b^2 + (y1*y2+z1*z2)/a^2)
						double xPN = x*xI_bb + y*yI_aa;
						double zzN = z*zI_aa;
						// Above the ship
						double cosΔσ = xPN + zzN;
						double Δσ = Math.acos(cosΔσ); 
						double distR = 1+noiseFactor*(Math.random()-0.5);
						int aboveDist = (int) (distR*a*(Δσ-half_f*( 2*Δσ + Math.sin(Δσ) *
								(sqCosPsinQx2[column]/(1-cosΔσ) - sqSinPcosQx2[column]/(1+cosΔσ)) )));
						// Bellow the ship
						cosΔσ = xPN -zzN;
						Δσ = Math.acos(cosΔσ); 
						int bellowDist = (int) (distR*a*(Δσ-half_f*( 2*Δσ + Math.sin(Δσ) *
								(sqCosPsinQx2[column]/(1-cosΔσ) - sqSinPcosQx2[column]/(1+cosΔσ)) )));


						for (int frame=0; frame<framesNum; frame++) {
							int aboveColor  = paintArray[frame][aboveDist].argb(transparencyFactor);
							if (aboveColor!=0)
								shieldArray[ABOVE][frame].setRGB(column, row, aboveColor);
							int bellowColor = paintArray[frame][bellowDist].argb(transparencyFactor);
							if (bellowColor!=0)
								shieldArray[BELLOW][frame].setRGB(column, row, bellowColor);
						}
					}
					x++;
				}
				y++;
			}
			if (showTiming)
				System.out.println("- Full drawShieldArray() Time = " + (System.currentTimeMillis()-timeStart));		
		}
	}
	private final class Pos {
		private double x, y, z;
		
		private Pos() {}
		private Pos(int x, int y, int z) { set(x, y, z); }
		private Pos(int[] src) { set(src); }
		private Pos set(int[] src) {
			x = src[0];
			y = src[1];
			z = src[2];
			return this;
		}
		private Pos set(double X, double Y, double Z) {
			x = X;
			y = Y;
			z = Z;
			return this;
		}
		@Override public String toString() { return "x=" + x + " y=" + y + " z=" + z; }
	}
	private final class GradientRing {
		private final Tint[]  colors;
		private final Tint[]  deltaColor;	// Evolution along distances
		private final float[] rays;
		private final float[] raysEvol;		// Evolution from a frame to the next one
		
		private GradientRing(Tint[] colors, float[] startRays, float[] stopRays, int numEvol) {
			this.rays	= startRays.clone();
			this.colors	= new Tint[colors.length];
			for (int idx=0; idx<colors.length; idx++) 
				this.colors[idx] = new Tint(colors[idx]);

			deltaColor	= new Tint[colors.length-1];
			for (int idx=0; idx<deltaColor.length; idx++) 
				deltaColor[idx] = colors[idx+1].minus(colors[idx]);

			raysEvol	= new float[startRays.length];
			for (int idx=0; idx<raysEvol.length; idx++) 
				raysEvol[idx] = (stopRays[idx]-startRays[idx]) / numEvol;
		}
		private void evolve() {
			for (int idx=0; idx<rays.length; idx++)
				rays[idx] += raysEvol[idx];
		}
		private Tint[] getGradientPaint(int length) {
			Tint[] paint = new Tint[length];
			int rayStart=0, dist=0;
			for (int idx=0; idx<deltaColor.length; idx++) { // loop thru rings
				Tint colorStart	= new Tint(colors[idx]);
				Tint colorDelta	= deltaColor[idx];
				rayStart		= (int) (0.5 + rays[idx]);
				int	 rayStop	= Math.min(length, (int) (0.5 + rays[idx+1]));
				int	 rayDelta	= rayStop - rayStart;
				for (dist=0; dist<rayDelta; dist++) { // loop thru distances
					int distance = rayStart+dist;
					double opacityFactor = spreadingFactor(distance);
					paint[rayStart+dist] = colorStart.extend(colorDelta, dist+1, rayDelta+1)
													 .setAlphaMultiplier(opacityFactor);
				}
			}
			for (int pos=rayStart+dist; pos<length; pos++) {
				if (paint[pos] == null)
					paint[pos] = new Tint();
			}
			return paint;
		}
		private double spreadingFactor(int distance) {
			int dist = distance;
			if (dist>halfPerimeter)
				dist = perimeter-dist;
			if (dist<beamDiameter)
				return 1;
			// transparency increase as the beam spread away
			// then decrease after 1/4 turn
			// Opacity factor
			// ~ beamRay/(distance)
			return Math.pow(beamDiameter/(double)dist, 0.25);
		}
	}
}
