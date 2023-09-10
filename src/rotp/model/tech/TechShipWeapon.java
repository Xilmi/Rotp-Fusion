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
package rotp.model.tech;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import rotp.model.combat.CombatStack;
import rotp.model.empires.Empire;
import rotp.model.ships.ShipComponent;
import rotp.model.ships.ShipWeaponBeam;
import rotp.ui.BasePanel;
import rotp.ui.combat.ShipBattleUI;

public final class TechShipWeapon extends Tech {
	private static int WIND_UP_FRAMES = 3;
	private static int HOLD_FRAMES = 0;
	
    private int damageLow = 0;
    private int damageHigh = 0;
    public int range = 1;

    public boolean heavyAllowed = false;
    private int heavyDamageLow = 0;
    private int heavyDamageHigh = 0;
    public int heavyRange = 2;

    public int attacksPerRound = 1;
    public int computer = 0;
    public float enemyShieldMod = 1;
    public boolean streaming = false;
    private String soundEffect = "ShipLaser";

    // graphic effects
    public int weaponSpread = 1;
    public int windUpFrames = WIND_UP_FRAMES;
    public int holdFrames = HOLD_FRAMES;

    private int beamStroke, dashStroke;
    private transient Color beamColor;
    private transient Color beamColor2;
    private transient Color cycleColor;
    private transient Color cycleColor2;
    private Stroke weaponStroke;

    public int damageLow()       { return (int) (session().damageBonus() * damageLow); }
    public int damageHigh()      { return (int) (session().damageBonus() * damageHigh); }
    public int heavyDamageLow()  { return (int) (session().damageBonus() * heavyDamageLow); }
    public int heavyDamageHigh() { return (int) (session().damageBonus() * heavyDamageHigh); }
    
    public float comparableDamageValue() {
        return 7.0f* level * 0.5f*(damageLow() + damageHigh()) * attacksPerRound / enemyShieldMod / (size + power);
    }  
    protected String soundEffect()		{ return "ShipLaser"; }
    protected String soundEffectMulti()	{ return "ShipMultiLaser"; }
    protected String newSoundEffect()	{ return soundEffect; } // BR:

    public TechShipWeapon(String typeId, int lv, int seq, boolean b, TechCategory c) {
        id(typeId, seq);
        typeSeq = seq;
        level = lv;
        cat = c;
        free = b;
        init();
        // System.out.println(soundEffect);
        // playAudioClip(soundEffect);
        // sleep(1500);
    }
    @Override
    public boolean canBeMiniaturized()      { return true; }
    @Override
    public void init() {
        super.init();
        techType = Tech.SHIP_WEAPON;
        dashStroke = 0;

        switch(typeSeq) {
            case 0: // LASER
                damageLow = 1;
                damageHigh = 4;
                heavyAllowed = true;
                heavyDamageLow = 1;
                heavyDamageHigh = 7;
                cost = 8;
                size = 10;
                power = 25;
                beamColor = new Color(0x9f,0x33,0x35);
                beamStroke = 1;
                soundEffect = "ShipLaser";
                break;
            case 1: // GATLING LASER
                damageLow = 1;
                damageHigh = 4;
                attacksPerRound = 4;
                cost = 20;
                size = 20;
                power = 70;
                beamColor = new Color(0xbd,0x23,0x3a);
                beamColor2 = new Color(0x72,0x10,0x10);
                beamStroke = 1;
                soundEffect = "ShipMultiLaser";
                break;
            case 2: // NEUTRON PELLET GUN
                damageLow = 2;
                damageHigh = 5;
                enemyShieldMod = .5f;
                cost = 7.5f;
                size = 15;
                power = 25;
                beamColor = new Color(0xa8,0xb4,0x85);
                beamStroke = 1;
                dashStroke = 3;
                soundEffect = "ShipNeutronPelletGun";
                break;
            case 3: // ION CANNON
                damageLow = 3;
                damageHigh = 8;
                heavyAllowed = true;
                heavyDamageLow = 3;
                heavyDamageHigh = 15;
                cost = 10;
                size = 15;
                power = 35;
                beamColor = new Color(0xa4,0x7b,0x56);
                beamStroke = 1;
                soundEffect = "ShipIonCannon";
                break;
            case 4: // MASS DRIVER
                weaponSpread = 1;
                damageLow = 5;
                damageHigh = 8;
                enemyShieldMod = .5f;
                cost = 18;
                size = 55;
                power = 50;
                beamColor = new Color(0xac,0xac,0xac);
                beamStroke = 1;
                dashStroke = 3;
                soundEffect = "ShipMassDriver";
                break;
            case 5: // NEUTRON BLASTER
                damageLow = 3;
                damageHigh = 12;
                heavyAllowed = true;
                heavyDamageLow = 3;
                heavyDamageHigh = 24;
                cost = 15;
                size = 20;
                power = 60;
                beamColor = new Color(0x3c,0x03,0x78);
                beamColor2 = new Color(0xcd,0xb1,0xe8);
                beamStroke = 1;
                soundEffect = "ShipNeutronBlaster";
                break;
            case 6: // GRAVITON BEAM
                damageLow = 1;
                damageHigh = 15;
                streaming = true;
                weaponSpread = 4;
                holdFrames = 6;
                cost = 12;
                size = 30;
                power = 60;
                beamColor = new Color(0x28,0x00,0x7e);
                beamColor2 = new Color(0xf5,0xb7,0xf3);
                beamStroke = 1;
                soundEffect = "ShipGravitonBeam";
                break;
            case 7: // HARD BEAM
                weaponSpread = 1;
                damageLow = 8;
                damageHigh = 12;
                enemyShieldMod = .5f;
                cost = 25;
                size = 50;
                power = 100;
                beamColor = new Color(0xf0,0xb5,0x6e);
                beamColor2 = new Color(0xcb,0x81,0x29);
                beamStroke = 1;
                soundEffect = "ShipHardBeam";
                break;
            case 8: // FUSION BEAM
                damageLow = 4;
                damageHigh = 16;
                heavyAllowed = true;
                heavyDamageLow = 4;
                heavyDamageHigh = 30;
                cost = 13;
                size = 20;
                power = 75;
                beamColor = new Color(0x0c,0x56,0x0c);
                beamColor2 = new Color(0x82,0xc8,0x82);
                beamStroke = 1;
                soundEffect = "ShipFusionBeam";
                break;
            case 9: // MEGABOLT CANNON
                damageLow = 2;
                damageHigh = 20;
                weaponSpread = 4;
                holdFrames = 6;
                computer = 3;
                cost = 16;
                size = 30;
                power = 65;
                beamColor = new Color(0xe5,0xee,0xbe);
                cycleColor = new Color(0xce,0xe2,0x89);
                cycleColor2 = new Color(0xea,0xbb,0xea);
                beamStroke = 1;
                soundEffect = "ShipMegaBoltCannon";
                break;
            case 10: // PHASOR
                damageLow = 5;
                damageHigh = 20;
                heavyAllowed = true;
                heavyDamageLow = 5;
                heavyDamageHigh = 40;
                cost = 18;
                size = 20;
                power = 90;
                beamColor = new Color(0xb6,0x07,0x5a);
                beamColor2 = new Color(0xde,0x8d,0xb3);
                beamStroke = 1;
                soundEffect = "ShipPhasor";
                break;
            case 11: // AUTO-BLASTER
                damageLow = 4;
                damageHigh = 16;
                attacksPerRound = 3;
                cost = 24;
                size = 30;
                power = 90;
                beamColor = new Color(0x24,0xbe,0x93);
                beamColor2 = new Color(0x03,0x25,0x1d);
                beamStroke = 1;
                soundEffect = "ShipAutoBlaster";
                break;
            case 12: // TACHYON BEAM
                damageLow = 1;
                damageHigh = 25;
                weaponSpread = 4;
                holdFrames = 6;
                streaming = true;
                cost = 18;
                size = 30;
                power = 80;
                beamColor = new Color(0x36,0x06,0x00);
                beamColor2 = new Color(0xe1,0xa3,0x8d);
                beamStroke = 1;
                soundEffect = "ShipTachyonBeam";
                break;
            case 13: // GAUSS AUTO-CANNON
                damageLow = 7;
                damageHigh = 10;
                enemyShieldMod = .5f;
                attacksPerRound = 4;
                cost = 40;
                size = 105;
                power = 105;
                beamColor = new Color(0xac,0xac,0xac);
                beamStroke = 3;
                dashStroke = 1;
                soundEffect = "ShipGaussAutoCannon";
                break;
            case 14: // PARTICLE BEAM
                damageLow = 10;
                damageHigh = 20;
                enemyShieldMod = .5f;
                cost = 26;
                size = 90;
                power = 75;
                beamColor = new Color(0x3b,0x39,0x48);
                beamColor2 = new Color(0x95,0x94,0x9d);
                dashStroke = 1;
                beamStroke = 3;
                soundEffect = "ShipParticleBeam";
                break;
            case 15: // PLASMA CANNON
                damageLow = 6;
                damageHigh = 30;
                weaponSpread = 4;
                cost = 24;
                size = 30;
                power = 110;
                beamColor = new Color(0xfe,0x29,0x28);
                beamColor2 = new Color(0xce,0x1f,0x1e);
                beamStroke = 1;
                soundEffect = "ShipPlasmaCannon";
                break;
            case 16: // DEATH RAY
                range = 1;
                damageLow = 200;
                damageHigh = 1000;
                weaponSpread = 7;
                holdFrames = 6;
                restricted = true;
                cost = 120;
                size = 2000;
                power = 2000;
                beamColor = new Color(0x56,0x02,0xc2);
                beamColor2 = new Color(0xcb,0x33,0x5e);
                beamStroke = 1;
                soundEffect = "ShipDeathRay";
                break;
            case 17: // DISRUPTOR
                damageLow = 10;
                damageHigh = 40;
                range = 2;
                cost = 100;
                size = 70;
                power = 160;
                beamColor = new Color(0xa4,0x7b,0x56);
                beamColor2 = new Color(0x82,0xc8,0x82);
                beamStroke = 1;
                soundEffect = "ShipDisruptor";
                break;
            case 18: // PULSE PHASOR
                damageLow = 5;
                damageHigh = 20;
                attacksPerRound = 3;
                holdFrames = 4;
                cost = 42;
                size = 40;
                power = 120;
                beamColor = new Color(0xb6,0x07,0x5a);
                cycleColor = new Color(0xde,0x8d,0xb3);
                beamStroke = 1;
                soundEffect = "ShipPulsePhasor";
                break;
            case 19: // TRI-FOCUS PLASMA CANNON
                damageLow = 20;
                damageHigh = 50;
                weaponSpread = 2;
                cost = 55;
                size = 65;
                power = 180;
                beamColor = new Color(0xfe,0x29,0x28);
                beamColor2 = new Color(0xce,0x1f,0x1e);
                beamStroke = 1;
                soundEffect = "ShipTriFocusPlasmaCannon";
                break;
            case 20: // STELLAR CONVERTOR
                damageLow = 10;
                damageHigh = 35;
                attacksPerRound = 4;
                weaponSpread = 4;
                holdFrames = 4;
                range = 3;
                cost = 105;
                size = 200;
                power = 300;
                beamColor = new Color(0xff,0xff,0xb0);
                cycleColor = new Color(0xff,0xff,0xff);
                beamStroke = 1;
                soundEffect = "ShipStellarConvertor";
                break;
            case 21: // MAULER DEVICE
                damageLow = 20;
                damageHigh = 100;
                weaponSpread = 7;
                holdFrames = 4;
                cost = 120;
                size = 150;
                power = 300;
                beamColor = new Color(0x00,0xaf,0x7d);
                beamColor2 = new Color(0x4c,0xd0,0xab);
                beamStroke = 1;
                soundEffect = "ShipMauler";
                break;
            case 22: // AMOEBA STREAM
                damageLow = 250;
                damageHigh = 1000;
                range = 3;
                streaming = true;
                restricted = true;
                beamColor = Color.green;
                beamStroke = 5;
                soundEffect = "ShipAmoebaStream";
                break;
            case 23: // CRYSTAL RAY
                damageLow = 100;
                damageHigh = 300;
                range = 3;
                attacksPerRound = 4;
                restricted = true;
                beamColor = Color.white;
                beamStroke = 4;
                soundEffect = "ShipMultiLaser";
                break;
        }
    }
    @Override
    public float baseValue(Empire c) { return c.ai().scientist().baseValue(this); }
    @Override
    public float warModeFactor()        { return 2; }
    @Override
    public boolean providesShipComponent()  { return true; }
    @Override
    public float baseCost()   { return cost; }
    @Override
    public float baseSize()   { return size; }
    @Override
    public float basePower()   { return power; }
    @Override
    public boolean isObsolete(Empire c) {
        return (c.tech().topShipWeaponTech() != null) && (level < c.tech().topShipWeaponTech().level);
    }
    @Override
    public void provideBenefits(Empire c) {
        super.provideBenefits(c);

        ShipWeaponBeam sh = new ShipWeaponBeam(this, false);
        c.shipLab().addWeapon(sh);
        if (!isObsolete(c))
            c.tech().topShipWeaponTech(this);
        
        if (heavyAllowed) {
            ShipWeaponBeam sh2 = new ShipWeaponBeam(this, true);
            c.shipLab().addWeapon(sh2);
        }
        if (c.isPlayerControlled())
            galaxy().giveAdvice("MAIN_ADVISOR_SHIP_WEAPON");
    }
    @Override // Shielded
    public void drawIneffectiveAttack(CombatStack source, CombatStack target, int wpnNum, int count) {
        ShipBattleUI ui = source.mgr.ui;
        if (ui == null)
            return;

        int stW = ui.stackW();
        int stH = ui.stackH();
        int st0X = ui.stackX(source);
        int st0Y = ui.stackY(source);
        int st1X = ui.stackX(target);
        int st1Y = ui.stackY(target);

        int x0 = st0X > st1X ? st0X+(stW/3) :st0X+(stW*2/3);
        int y0 = st0Y+stH/2;
        int x1 = st1X+stW/2;
        int y1 = st1Y+stH/2;
        drawAttack(source, target, x0, y0, x1, y1, wpnNum, -1f, count, stW, stH, 0f);
    }
    @Override  // Miss
    public void drawUnsuccessfulAttack(CombatStack source, CombatStack target, int wpnNum, int count) {
        ShipBattleUI ui = source.mgr.ui;
        if (ui == null)
            return;

        int stW = ui.stackW();
        int stH = ui.stackH();
        int st0X = ui.stackX(source);
        int st0Y = ui.stackY(source);
        int st1X = ui.stackX(target);
        int st1Y = ui.stackY(target);

        int xRoll = roll(0,2);
        int xMiss = stW/4 + (xRoll *stW/4);
        int yMiss = xRoll == 1 ? stH/4+roll(0,1) *stH/2 : stH/4+(roll(0,2) *stH/4);

        int x0 = st0X > st1X ? st0X+(stW/3) :st0X+(stW*2/3);
        int y0 = st0Y+stH/2;
        int x1 = st1X+xMiss;
        int y1 = st1Y+yMiss;
        drawAttack(source, target, x0, y0, x1, y1, wpnNum, 0f, count, stW, stH, 0f);
    }
    @Override
    public void drawSuccessfulAttack(CombatStack source, CombatStack target, int wpnNum, float dmg, int count, float force) {
        ShipBattleUI ui = source.mgr.ui;
        if (ui == null)
            return;

        int stW = ui.stackW();
        int stH = ui.stackH();
        int st0X = ui.stackX(source);
        int st0Y = ui.stackY(source);
        int st1X = ui.stackX(target);
        int st1Y = ui.stackY(target);

        int x0 = st0X > st1X ? st0X+(stW/3) :st0X+(stW*2/3);
        int y0 = st0Y+stH/2;
        int x1 = st1X+stW/2;
        int y1 = st1Y+stH/2;
        drawAttack(source, target, x0, y0, x1, y1, wpnNum, dmg, count, stW, stH, force);
    }
    private void drawAttack(CombatStack source, CombatStack target, int x0, int y0, int x1, int y1,
    		int wpnNum, float dmg, int count, int boxW, int boxH, float force) {
        ShipBattleUI ui = source.mgr.ui;
        if (!source.mgr.showAnimations()) 
            return;

       

        // BR: Add Shield effect
        boolean showShield = options().newWeaponAnimation() && (dmg != 0);
        
        Dimension shieldSize = showShield? target.shieldSize(boxW, boxH) : new Dimension(boxW, boxH);
        int xS = x1 - shieldSize.width/2;
        int yS = y1 - shieldSize.height/2;
       
        ShipComponent wpn = source.weapon(wpnNum);
        int wpnCount = count / attacksPerRound / source.num;
        Graphics2D g = (Graphics2D) ui.getGraphics();
        Stroke prev = g.getStroke();
        
        int distFactor = 8*source.movePointsTo(target.x, target.y);

        g.setColor(beamColor);
        if(beamColor2 == null && cycleColor == null)
        	beamColor2 = multColor(beamColor, 0.75f);
        if(beamColor2 != null) {
            GradientPaint gp = new GradientPaint(x0,y0, beamColor,
            		x0+(x1-x0)/distFactor,y0+(y1-y0)/distFactor, beamColor2, true);
            g.setPaint(gp);
        }

        if ((dashStroke > 0) && (weaponStroke == null)) {
            int w = scaled(3);
            float dash = scaled(w*dashStroke);
            weaponStroke = new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{0, dash}, 0);
        }

        if (weaponStroke != null)
            g.setStroke(weaponStroke);
        else if (wpn.heavy())
            g.setStroke(BasePanel.baseStroke(beamStroke*2+1));
        else
            g.setStroke(BasePanel.baseStroke(beamStroke*2));

        if (options().newWeaponSound())
        	 playAudioClip(newSoundEffect()); // BR:
        else if (attacksPerRound > 1)
            playAudioClip(soundEffectMulti());
        else
            playAudioClip(soundEffect());
       
        if (showShield) {
            windUpFrames = WIND_UP_FRAMES;
            holdFrames = HOLD_FRAMES;        	
        } else {
            windUpFrames = source.mgr.autoComplete ? 1 : WIND_UP_FRAMES;
            holdFrames = source.mgr.autoComplete ? 0 : HOLD_FRAMES;        	
        }
        BufferedImage[][] shieldImg = new BufferedImage[attacksPerRound][windUpFrames];

        int sourceSize = 3;
        if (source.design() != null)
        	sourceSize = source.design().size();
        // Full Beam trajectory generation
        ArrayList<Line2D.Double> lines = new ArrayList<>();
        for(int i = 0; i < attacksPerRound; ++i) {
            int xAdj = scaled(roll(-4,4)*2);
            int yAdj = scaled(roll(-4,4)*2);
            if (showShield) {
            	shieldImg[i] = target.shieldImg(windUpFrames, attacksPerRound,
            			shieldSize, x0, y0, x1, y1, xAdj, yAdj, beamColor, force, dmg);
            }
            if (weaponSpread > 1) {
                int xMod = (source.y == target.y) ? 0 : 1;
                int yMod = (source.x == target.x) ? 0 : 1;
                if ((source.x < target.x) && (source.y < target.y))
                    xMod = -1;
                else if ((source.x > target.x) && (source.y > target.y))
                    xMod = -1;
                for (int n = -1 * weaponSpread; n <= weaponSpread; n++) {
                    if (!source.mgr.showAnimations()) 
                        break;
                    int adj = scaled(n);
                    lines.addAll(addMultiLines(sourceSize, wpnCount, x0, y0, x1+xAdj+(xMod*adj), y1+yAdj+(yMod*adj)));
                }
            } else {
                lines.addAll(addMultiLines(sourceSize, wpnCount, x0, y0, x1+xAdj, y1+yAdj));
            }
        }
        // Beams Progression generation
        SortedMap<Integer, ArrayList<Line2D.Double>> partLines = new TreeMap<>();
        long sleepTime = 50; // Original = 50;
        for(int i = 0; i < windUpFrames; ++i) {
            ArrayList<Line2D.Double> pl = new ArrayList<>();
            for(Line2D.Double line : lines) {
                double newX1 = line.getX1() + (line.getX2() - line.getX1()) * i / windUpFrames;
                double newY1 = line.getY1() + (line.getY2() - line.getY1()) * i / windUpFrames;
                double newX2 = line.getX1() + (line.getX2() - line.getX1()) * (i + 1) / windUpFrames;
                double newY2 = line.getY1() + (line.getY2() - line.getY1()) * (i + 1) / windUpFrames;
                pl.add(new Line2D.Double(newX1, newY1, newX2, newY2));
            }
            partLines.put(i, pl);
            paintLines(pl, g);
            sleep(sleepTime);
            //ui.paintAllImmediately();
        }
        // Show Continuous
        for(int i = 0; i < holdFrames; i++) {
            if(beamColor2 != null) {
                GradientPaint gp = new GradientPaint(x0+i*scaled(10),y0+i*scaled(10), beamColor,
                		x0+i*scaled(10)+(x1-x0)/distFactor,y0+i*scaled(10)+(y1-y0)/distFactor, beamColor2, true);
                g.setPaint(gp);
            }
            if(cycleColor != null) {
                if(i%2 == 0) {
                    g.setColor(cycleColor);
                } else if (cycleColor2 != null && i%3 == 0) {
                    g.setColor(cycleColor2);
                } else {
                    g.setColor(beamColor);
                }
            }
            paintLines(partLines, g);
            sleep(sleepTime);
        }
        // Show end of beam
        for(int i = 0; i < windUpFrames; ++i) {
            ui.paintCellsImmediately(source.x, target.x, source.y, target.y);
            partLines.get(i).clear();
            paintLines(partLines, g);
            if (showShield) {
    	    	//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    	    	g.setComposite(AlphaComposite.SrcOver);
            	for(int k = 0; k < attacksPerRound; k++) {
            		g.drawImage(shieldImg[k][i], xS, yS, null);
            	}
    	    	g.setComposite(AlphaComposite.Src);
            }
            sleep(sleepTime);
        }
        
        ui.paintAllImmediately();

        String missLabel = dmg < 0 ? text("SHIP_COMBAT_DEFLECTED") : text("SHIP_COMBAT_MISS");
        target.drawAttackResult(g,x1,y1,x0, dmg,missLabel);   
        g.setStroke(prev);
        ui.paintAllImmediately();
    }
    private ArrayList<Line2D.Double> addMultiLines(int size, int weapons, int sx, int sy, int tx, int ty) {
        int offSet = scaled((size + 1) * 5);
        ArrayList<Line2D.Double> lines = new ArrayList<>();
        if(weapons < 3)
            lines.add(new Line2D.Double(sx, sy, tx, ty));
        else if(weapons < 6) {
            lines.add(new Line2D.Double(sx, sy + offSet, tx, ty));
            lines.add(new Line2D.Double(sx, sy - offSet, tx, ty));
        } else {
            lines.add(new Line2D.Double(sx, sy, tx, ty));
            lines.add(new Line2D.Double(sx, sy + offSet, tx, ty));
            lines.add(new Line2D.Double(sx, sy - offSet, tx, ty));
        }
        return lines;
    }
    private void paintLines(SortedMap<Integer, ArrayList<Line2D.Double>> lines, Graphics2D g) {
        for(ArrayList<Line2D.Double> lineList : lines.values()) {
            for(Line2D.Double line : lineList) {
                g.draw(line);
            }
        }
    }
    private void paintLines(ArrayList<Line2D.Double> lines, Graphics2D g) {
        for(Line2D.Double line : lines) {
            g.draw(line);
        }
    }
}
