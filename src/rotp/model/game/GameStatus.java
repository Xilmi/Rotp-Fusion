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
package rotp.model.game;

import java.io.Serializable;
import rotp.util.Base;

public class GameStatus implements Base, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int lostNewRepublicAsAllied  = 1;
    private static final int lostRebellionAsFollower  = 2;
    private static final int wonDiplomaticMajor       = 3;
    private static final int wonDiplomaticFeared      = 4;
    private static final int wonNewRepublicAsAllied   = 5;
    private static final int wonNewRepublicAsChampion = 6;
    private static final int wonRebellionAsLeader     = 7;
    private static final int wonRebellionAsFollower   = 8;
    private static final int wonRebellionAllianceAsLeader   = 9;
    private static final int wonRebellionAllianceAsFollower = 10;
    
    enum Status { NO_GAME, IN_PROGRESS, LOSS_OVERTHROWN, LOSS_MILITARY, LOSS_DIPLOMATIC, 
        LOSS_NEW_REPUBLIC, LOSS_REBELLION, WIN_DIPLOMATIC, WIN_MILITARY, WIN_MILITARY_ALLIANCE, 
        WIN_NEW_REPUBLIC, WIN_REBELLION, WIN_REBELLION_ALLIANCE, WIN_COUNCIL_ALLIANCE, LOSS_NO_COLONIES; }
    private Status status = Status.NO_GAME;
    // BR: changing Enu will break backward compatibility!
    private int subStatus = 0;

    public String key() { return status.toString(); }
    public boolean inProgress()       {
    	if (status != Status.NO_GAME && status != Status.IN_PROGRESS && options().debugAutoRun())
    		return GameSession.instance().galaxy().numActiveEmpires() > 1;
    	return status == Status.IN_PROGRESS;
    }
    public boolean lost() {
    	return lostOverthrown() || lostMilitary() || lostDiplomatic()
    		|| lostNewRepublic() || lostRebellion() || lostNoColonies(); 
    }
    public boolean won() {
    	return wonCouncilAlliance() || wonMilitary() || wonMilitaryAlliance() 
            || wonNewRepublic() || wonRebellion() || wonRebellionAlliance() || wonDiplomatic();
    }
    public boolean endAutoRun() {
    	return won() || lostOverthrown() || lostDiplomatic()
    			|| lostNewRepublic() || lostRebellion(); 
    }
    public boolean lostOverthrown()       { return status == Status.LOSS_OVERTHROWN; }
    public boolean lostMilitary()         { return status == Status.LOSS_MILITARY; }
    public boolean lostDiplomatic()       { return status == Status.LOSS_DIPLOMATIC; }
    public boolean lostNewRepublic()      { return status == Status.LOSS_NEW_REPUBLIC; }
    public boolean lostRebellion()        { return status == Status.LOSS_REBELLION; }
    public boolean lostNoColonies()       { return status == Status.LOSS_NO_COLONIES; }
    public boolean wonDiplomatic()        { return status == Status.WIN_DIPLOMATIC; }
    public boolean wonMilitary()          { return status == Status.WIN_MILITARY; }
    public boolean wonMilitaryAlliance()  { return status == Status.WIN_MILITARY_ALLIANCE; }
    public boolean wonNewRepublic()       { return status == Status.WIN_NEW_REPUBLIC; }
    public boolean wonRebellion()         { return status == Status.WIN_REBELLION; }
    public boolean wonRebellionAlliance() { return status == Status.WIN_REBELLION_ALLIANCE; }
    public boolean wonCouncilAlliance()   { return status == Status.WIN_COUNCIL_ALLIANCE; }
    
    // BR: Not available in every languages... English only
    public boolean lostNewRepublicAsAllied()        { return subStatus == lostNewRepublicAsAllied; }
    public boolean lostRebellionAsFollower()        { return subStatus == lostRebellionAsFollower; }
    public boolean wonDiplomaticMajor()             { return subStatus == wonDiplomaticMajor; }
    public boolean wonDiplomaticFeared()            { return subStatus == wonDiplomaticFeared; }
    public boolean wonNewRepublicAsAllied()         { return subStatus == wonNewRepublicAsAllied; }
    public boolean wonNewRepublicAsChampion()       { return subStatus == wonNewRepublicAsChampion; }
    public boolean wonRebellionAsLeader()           { return subStatus == wonRebellionAsLeader; }
    public boolean wonRebellionAsFollower()         { return subStatus == wonRebellionAsFollower; }
    public boolean wonRebellionAllianceAsLeader()   { return subStatus == wonRebellionAllianceAsLeader; }
    public boolean wonRebellionAllianceAsFollower() { return subStatus == wonRebellionAllianceAsFollower; }

    public void startGame()               { status = Status.IN_PROGRESS; }
    // Never happen!
    public void loseOverthrown()          { status = Status.LOSS_OVERTHROWN; }
    // All colony lost...
    public void loseMilitary()            { status = Status.LOSS_MILITARY; }
    // All colony lost as ally
    public void loseNewRepublicAsAllied() {
    	status = Status.LOSS_MILITARY;
    	subStatus = lostNewRepublicAsAllied;
    }
    // Accept council decision
    public void loseDiplomatic()          { status = Status.LOSS_DIPLOMATIC; }
    // player was leader of alliance and still lost!
    public void loseNewRepublic()         { status = Status.LOSS_NEW_REPUBLIC; }
    // player was a rebel leader against alliance and lost
    public void loseRebellion()           { status = Status.LOSS_REBELLION; }
    // player was following rebels against alliance and lost
    public void loseRebellionAsFollower() {
    	status = Status.LOSS_REBELLION;
    	subStatus = lostRebellionAsFollower;
    }
    // no one killed us... abandonment suicide
    public void loseNoColonies()          { status = Status.LOSS_NO_COLONIES; }
    // if player won the vote and no rebels, game over; player weight is over 50% but less than 2/3
    public void winDiplomaticMajor()      {
    	status = Status.WIN_DIPLOMATIC;
    	subStatus = wonDiplomaticMajor;
    }
    // if player won the vote and no rebels, game over; player weight is over 2/3
    public void winDiplomaticFeared()     {
    	status = Status.WIN_DIPLOMATIC;
    	subStatus = wonDiplomaticFeared;
    }
    // if player won the vote and no rebels, game over; player weight is less than 50%
    public void winDiplomatic()           { status = Status.WIN_DIPLOMATIC; }
    // if only one empire is left then player must have won
    public void winMilitary()             { status = Status.WIN_MILITARY; }
    // multiple empires, all allied with player.. that's a win
    public void winMilitaryAlliance()     { status = Status.WIN_MILITARY_ALLIANCE; }
    // rebellion has been defeated; Player = Leader
    public void winNewRepublic()          { status = Status.WIN_NEW_REPUBLIC; }
    // rebellion has been defeated; Player = Allies
    public void winNewRepublicAsAllied()  {
    	status = Status.WIN_NEW_REPUBLIC;
    	subStatus = wonNewRepublicAsAllied;
    }
    // rebellion has been defeated; Player = Allies; leader was defeated
    public void winNewRepublicAsChampion()  {
    	status = Status.WIN_NEW_REPUBLIC;
    	subStatus = wonNewRepublicAsChampion;
    }
    // New Republic has been defeated;  Player = Leader
    public void winRebellion()            {
    	status = Status.WIN_REBELLION;
    	subStatus = wonRebellionAsLeader;
    }
    // rebellion win;
    public void winRebellionAlliance()    {
    	status = Status.WIN_REBELLION_ALLIANCE;
    	subStatus = wonRebellionAllianceAsLeader;
    }
    public void winRebellionAsFollower()  {
    	status = Status.WIN_REBELLION;
    	subStatus = wonRebellionAsFollower;
    }
    public void winRebellionAllianceAsFollower() {
    	status = Status.WIN_REBELLION_ALLIANCE;
    	subStatus = wonRebellionAllianceAsFollower;
    }
    public void winCouncilAlliance()      { status = Status.WIN_COUNCIL_ALLIANCE; }

    
    
    
}
