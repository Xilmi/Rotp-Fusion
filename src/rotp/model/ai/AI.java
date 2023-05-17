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
package rotp.model.ai;

import static rotp.model.game.IGameOptions.BASE;
import static rotp.model.game.IGameOptions.FUN;
import static rotp.model.game.IGameOptions.FUSION;
import static rotp.model.game.IGameOptions.HYBRID;
import static rotp.model.game.IGameOptions.MODNAR;
import static rotp.model.game.IGameOptions.PERSONALITY;
import static rotp.model.game.IGameOptions.RANDOM;
import static rotp.model.game.IGameOptions.RANDOM_ADVANCED;
import static rotp.model.game.IGameOptions.RANDOM_BASIC;
import static rotp.model.game.IGameOptions.RANDOM_NO_RELATIONBAR;
import static rotp.model.game.IGameOptions.ROOKIE;
import static rotp.model.game.IGameOptions.XILMI;
import static rotp.model.game.IGameOptions.advancedAIset;
import static rotp.model.game.IGameOptions.allAIset;
import static rotp.model.game.IGameOptions.baseAIset;
import static rotp.model.game.IGameOptions.noRelationBarAIset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rotp.model.ai.interfaces.Diplomat;
import rotp.model.ai.interfaces.FleetCommander;
import rotp.model.ai.interfaces.General;
import rotp.model.ai.interfaces.Governor;
import rotp.model.ai.interfaces.Scientist;
import rotp.model.ai.interfaces.ShipCaptain;
import rotp.model.ai.interfaces.ShipDesigner;
import rotp.model.ai.interfaces.SpyMaster;
import rotp.model.ai.interfaces.Treasurer;
import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;
import rotp.model.empires.SystemView;
import rotp.model.galaxy.IMappedObject;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.GameSession;
import rotp.model.planet.Planet;
import rotp.model.ships.ShipDesign;
import rotp.ui.UserPreferences;
import rotp.ui.notifications.BombardSystemNotification;
import rotp.ui.notifications.ColonizeSystemNotification;
import rotp.util.Base;

public class AI implements Base {

    private final Empire empire;

    private final Diplomat diplomat;
    private final General general;
    private final FleetCommander fleetCommander;
    private final Governor governor;
    private final Scientist scientist;
    private final ShipCaptain captain;
    private final ShipDesigner shipDesigner;
    private final SpyMaster spyMaster;
    private final Treasurer treasurer;


    public AI (Empire e, int aiType) {
        empire = e;
        
        switch (aiType) {
            case RANDOM:
                aiType = allAIset().random();
                break;
            case RANDOM_BASIC:
                aiType = baseAIset().random();
                break;
            case RANDOM_ADVANCED:
                aiType = advancedAIset().random();
                break;
            case RANDOM_NO_RELATIONBAR:
                aiType = noRelationBarAIset().random();
                break;
            default:
                break;
        }
        if(empire.selectedAI < 0)
            empire.selectedAI = aiType;
        switch(aiType) {
            case BASE: // BR: Tentative
                general =        new rotp.model.ai.base.AIGeneral(empire);
                captain =        new rotp.model.ai.base.AIShipCaptain(empire);
                governor =       new rotp.model.ai.base.AIGovernor(empire);
                scientist =      new rotp.model.ai.base.AIScientist(empire);
                diplomat =       new rotp.model.ai.base.AIDiplomat(empire);
                shipDesigner =   new rotp.model.ai.base.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.base.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.base.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.base.AITreasurer(empire);
                break;
            case MODNAR: // BR: Tentative
                general =        new rotp.model.ai.modnar.AIGeneral(empire);
                captain =        new rotp.model.ai.modnar.AIShipCaptain(empire);
                governor =       new rotp.model.ai.modnar.AIGovernor(empire);
                scientist =      new rotp.model.ai.modnar.AIScientist(empire);
                diplomat =       new rotp.model.ai.modnar.AIDiplomat(empire);
                shipDesigner =   new rotp.model.ai.modnar.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.modnar.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.modnar.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.modnar.AITreasurer(empire);
                break;
            case ROOKIE: // BR: Tentative
                general =        new rotp.model.ai.rookie.AIGeneral(empire);
                captain =        new rotp.model.ai.rookie.AIShipCaptain(empire);
                governor =       new rotp.model.ai.rookie.AIGovernor(empire);
                scientist =      new rotp.model.ai.rookie.AIScientist(empire);
                diplomat =       new rotp.model.ai.rookie.AIDiplomat(empire);
                shipDesigner =   new rotp.model.ai.rookie.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.rookie.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.rookie.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.rookie.AITreasurer(empire);
                break;
            case XILMI:
                general =        new rotp.model.ai.xilmi.AIGeneral(empire);
                captain =        new rotp.model.ai.xilmi.AIShipCaptain(empire);
                governor =       new rotp.model.ai.xilmi.AIGovernor(empire);
                scientist =      new rotp.model.ai.xilmi.AIScientist(empire);
                diplomat =       new rotp.model.ai.xilmi.AIDiplomat(empire);
                shipDesigner =   new rotp.model.ai.xilmi.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.xilmi.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.xilmi.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.xilmi.AITreasurer(empire);
                break;
            case HYBRID:
                general =        new rotp.model.ai.xilmi.AIGeneral(empire);
                captain =        new rotp.model.ai.xilmi.AIShipCaptain(empire);
                governor =       new rotp.model.ai.xilmi.AIGovernor(empire);
                scientist =      new rotp.model.ai.xilmi.AIScientist(empire);
                diplomat =       new rotp.model.ai.rookie.AIDiplomat(empire);
                shipDesigner =   new rotp.model.ai.xilmi.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.xilmi.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.rookie.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.xilmi.AITreasurer(empire);
                break;
            case FUN:
                general =        new rotp.model.ai.xilmi.AIGeneral(empire);
                captain =        new rotp.model.ai.xilmi.AIShipCaptain(empire);
                governor =       new rotp.model.ai.xilmi.AIGovernor(empire);
                scientist =      new rotp.model.ai.xilmi.AIScientist(empire);
                diplomat =       new rotp.model.ai.fun.AIDiplomat(empire);
                shipDesigner =   new rotp.model.ai.xilmi.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.xilmi.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.xilmi.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.xilmi.AITreasurer(empire);
                break;
            case PERSONALITY:
                general =        new rotp.model.ai.xilmi.AIGeneral(empire);
                captain =        new rotp.model.ai.xilmi.AIShipCaptain(empire);
                governor =       new rotp.model.ai.xilmi.AIGovernor(empire);
                scientist =      new rotp.model.ai.xilmi.AIScientist(empire);
                diplomat =       new rotp.model.ai.fusion.AIDiplomat(empire, 1);
                shipDesigner =   new rotp.model.ai.xilmi.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.xilmi.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.xilmi.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.xilmi.AITreasurer(empire);
                break;
            case FUSION:
            default:
                general =        new rotp.model.ai.xilmi.AIGeneral(empire);
                captain =        new rotp.model.ai.xilmi.AIShipCaptain(empire);
                governor =       new rotp.model.ai.xilmi.AIGovernor(empire);
                scientist =      new rotp.model.ai.xilmi.AIScientist(empire);
                diplomat =       new rotp.model.ai.fusion.AIDiplomat(empire);
                shipDesigner =   new rotp.model.ai.xilmi.AIShipDesigner(empire);
                fleetCommander = new rotp.model.ai.xilmi.AIFleetCommander(empire);
                spyMaster =      new rotp.model.ai.xilmi.AISpyMaster(empire);
                treasurer =      new rotp.model.ai.xilmi.AITreasurer(empire);
                break;
        }
    }

    // MISC INTERFACE
    public boolean isAI()     {  return empire.isAI(); }
    public boolean isPlayer() {  return empire.isPlayer(); }
    
    // direct
    public ShipCaptain shipCaptain()                   { return captain; }
    public General general()                           { return general; }
    public Diplomat diplomat()                         { return diplomat; }
    public FleetCommander fleetCommander()             { return fleetCommander; }
    public Governor governor()                         { return governor; }
    public Treasurer treasurer()                       { return treasurer; }
    public Scientist scientist()                       { return scientist; }
    public ShipDesigner shipDesigner()                 { return shipDesigner; }
    public SpyMaster spyMaster()                       { return spyMaster; }

    // uncategorized
    public List<StarSystem> bestSystemsForInvasion(EmpireView v) {
        // invoked when going to war
        List<StarSystem> systems = empire.systemsInShipRange(v.empire());
        Collections.sort(systems,StarSystem.INVASION_PRIORITY);
        return systems;
    }
    private int popNeeded(int sysId, float pct) {
        if (empire.sv.missing(sysId))
            return 0;
        StarSystem sys = empire.sv.view(sysId).system();
        Colony col = sys.colony();
        if (col == null)
            return 0;
       
        return col.calcPopNeeded(pct);
    }
    private ColonyTransporter createColony(StarSystem sys, int minTransports) {
        int sysId = id(sys);
        float targetPct = empire.governorAI().targetPopPct(sysId);
        int popNeeded = popNeeded(sysId, targetPct);        
        int maxPopToGive = (int) empire.sv.maxPopToGive(sysId, targetPct);
        if ((popNeeded < minTransports) && (maxPopToGive < minTransports))
            return null;

        return new ColonyTransporter(sys.colony(), popNeeded, maxPopToGive, minTransports);
    }
    public void sendTransports() {
        long tm0 = System.currentTimeMillis();
        int minTransportSize = empire.generalAI().minTransportSize();
        List<ColonyTransporter> needy = new ArrayList<>();
        List<ColonyTransporter> givey = new ArrayList<>();
        for (StarSystem sys: empire.allColonizedSystems()) {
            ColonyTransporter col = createColony(sys, minTransportSize);
            if (col != null) {
                if ((col.popNeeded >= minTransportSize) && (col.popNeeded >= col.maxPopToGive))
                    needy.add(col);
                else if ((col.maxPopToGive >= minTransportSize) && (col.maxPopToGive > col.popNeeded))
                {
                    if(empire.isPlayerControlled() && session().getGovernorOptions().isTransportRichDisabled() && (sys.planet().productionAdj() > 1 || sys.planet().researchAdj() > 1))
                        continue;
                    if(empire.isAI() || sys.colony().isGovernor() || GameSession.instance().getGovernorOptions().isAutotransportUngoverned())
                        givey.add(col);
                }
            }
        }

        if (needy.isEmpty() || givey.isEmpty()) {
            log("sendTransports (NONE): "+empire.raceName()+"   "+(System.currentTimeMillis()-tm0)+"ms");
            return;
        }

        Collections.sort(needy,TRANSPORT_PRIORITY);

        for(ColonyTransporter needer : needy)
        {
            TARGET_COLONY = needer;
            Collections.sort(givey,DISTANCE_TO_TARGET);
            boolean allGiversBusy = true;
            for(ColonyTransporter giver : givey)
            {
                if(giver.colony.transport().size() > 0)
                    continue;
                allGiversBusy = false;
                float allowableTurns = (float) (1 + Math.min(7, Math.floor(22 / empire.tech().topSpeed())));
                float travelTime = giver.colony.transport().travelTime(needer.colony.starSystem());
                if ((giver.maxPopToGive >= minTransportSize) && (giver.transportPriority < needer.transportPriority)
                        && travelTime <= allowableTurns) {
                    float needed = needer.popNeeded - ((int) (Math.ceil(giver.transportTimeTo(needer))) * needer.growth);
                    int trPop = (int) min(needed, giver.maxPopToGive);
                    if (trPop >= minTransportSize) {
                        giver.sendTransportsTo(needer, trPop);
                    }
                }
            }
            if(allGiversBusy)
                break;
        }
        long tm1 = System.currentTimeMillis();
        log("sendTransports: "+empire.raceName()+"   "+(tm1-tm0)+"ms");
    }
    public void checkColonize(StarSystem sys, ShipFleet fl) {
        if (fl.retreating())
            return;
        if (sys.orbitingShipsInConflict())
            return;

        if (sys.colony() != null)
            return;
        if (!empire.canColonize(sys.planet().type()))
            return;

        ShipDesign bestDesign = shipDesigner().bestDesignToColonize(fl, sys);
        // if no useable colony design, exit
        if (bestDesign == null)
            return;

        // AT THIS POINT, the fleet can definitely colonize the planet
        // confirm if player controlled & if colonize prompt is disabled
        if (empire.isAIControlled() || UserPreferences.autoColonize())
            fl.colonizeSystem(sys, bestDesign);
        else
            ColonizeSystemNotification.create(sys.id, fl, bestDesign);
    }
    //Xilmi: return value of 1 means yes 2 means yes, but target-bombing
    public int promptForBombardment(StarSystem sys, ShipFleet fl) {
        // if player, prompt for decision to bomb instead of deciding here
        if (empire.isPlayerControlled()) {
            if (UserPreferences.autoBombardNever())
                return 0;
            boolean autoBomb = false;
            // user preference auto-bombard set to always?
            if (UserPreferences.autoBombardYes())
                autoBomb = true;
            // auto-bombard set to whenever at war?
            boolean atWar = empire.atWarWith(sys.empId());
            if (UserPreferences.autoBombardWar() && atWar) 
                autoBomb = true;
            // auto-bombard set to whenever at war and not invading?
            int transports = empire.transportsInTransit(sys);
            if (UserPreferences.autoBombardInvading() && atWar && (transports == 0))
                autoBomb = true;
            int bombTarget = 0;
            if(UserPreferences.targetBombardAllowedForPlayer() && empire.transportsInTransit(sys) > 0)
                bombTarget = UserPreferences.bombingTarget.get();
            BombardSystemNotification.create(id(sys), fl, autoBomb, bombTarget);
            return 0;
        }
        
        // ail: asking our general for permission
        if(!empire.generalAI().allowedToBomb(sys))
            return 0;
        
        // estimate bombardment damage and resulting population loss
        float damage = fl.expectedBombardDamage(false);
        float popLoss = damage / 200;
        float sysPop = empire.sv.population(id(sys));

        // if colony will NOT be destroyed, then bombs away!
        if (popLoss < (sysPop * .9))
            return 1;

        // determine number of troops in transit
        int transports = empire.transportsInTransit(sys);

        // if none in transit, then bombs away!
        if (transports < 1)
            return 1;

        // else don't bomb
        //Xilmi: Not a nice way, but a way to tell Xilmi-AIs apart from base-AIs:
        if(empire.generalAI().absolution() != 0)
        {
            if(rotp.ui.UserPreferences.targetBombardAllowedForAI() == true)
            {
                return 2;
            }
        }
        return 0;
    }
    @SuppressWarnings("unused")
	private float targetPopPct(SystemView sv) {
        if (sv.borderSystem()) return .75f;
        
        Planet pl = sv.system().planet();

        if (pl.isResourceRich()) return .75f;
        if (pl.isResourceUltraRich()) return .75f;
        if (pl.isArtifact()) return .75f;
        if (pl.isOrionArtifact()) return .75f;
        if (pl.currentSize() <= 20) return .75f;

        if (sv.supportSystem()) return .5f;
        if (pl.currentSize() <= 40) return .5f;

        return .25f;
    }
    class ColonyTransporter implements IMappedObject {
        Colony colony;
        float x, y;
        float transportPriority;
        float growth;
        int popNeeded;
        int maxPopToGive;
        public ColonyTransporter(Colony c, int needs, int gives, int min) {
            colony = c;
            StarSystem sys = c.starSystem();
            x = sys.x();
            y = sys.y();
            popNeeded = needs;
            maxPopToGive = gives;

            // calc these values only for needy colonies
            if ((popNeeded >= min) && (popNeeded >= maxPopToGive)) {
                transportPriority = c.empire().fleetCommanderAI().transportPriority(sys);
                growth = c.normalPopGrowth();
            }
        }
        @Override
        public float x() { return x; }
        @Override
        public float y() { return y; }
        public float transportTimeTo(ColonyTransporter dest) {
            return colony.starSystem().transportTimeTo(dest.colony.starSystem());
        }
        public void sendTransportsTo(ColonyTransporter dest, int trPop) {
            colony.scheduleTransportsToSystem(dest.colony.starSystem(), trPop);
            maxPopToGive = 0;
            dest.popNeeded -= trPop;
        }
    }
    public static Comparator<ColonyTransporter> TRANSPORT_PRIORITY = (ColonyTransporter col1, ColonyTransporter col2) -> Base.compare(col1.transportPriority,col2.transportPriority);
    public static ColonyTransporter TARGET_COLONY;
    public static Comparator<ColonyTransporter> DISTANCE_TO_TARGET = new Comparator<ColonyTransporter>() {
        @Override
        public int compare(ColonyTransporter sys1, ColonyTransporter sys2) {
            float pr1 = sys1.distanceTo(TARGET_COLONY);
            float pr2 = sys2.distanceTo(TARGET_COLONY);
            return Base.compare(pr1, pr2);
        }
    };
}
