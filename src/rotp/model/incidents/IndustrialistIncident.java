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
package rotp.model.incidents;

import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;

public class IndustrialistIncident extends DiplomaticIncident {
    private static final long serialVersionUID = 1L;
    final int empYou;
    final int empMe;
    float severityGoal;
    EmpireView iev;
    public static IndustrialistIncident create(EmpireView ev) {
        return new IndustrialistIncident(ev);
    }
    @Override
    public boolean triggeredByAction()   { return false; }
    private IndustrialistIncident(EmpireView ev) {
        iev = ev;
        empYou = iev.empId();
        empMe = iev.ownerId();
        severity = 0;
    }
    @Override
    public void update()
    {
        dateOccurred = galaxy().currentYear();
        severityGoal = 0;
        
        if (iev.embassy().unity() || !iev.owner().inEconomicRange(iev.empId())) {
            return;
        }
        if(iev.owner().generalAI().absolution() < 1 || !iev.owner().leader().isIndustrialist())
        {
            severity = 0;
            return;
        }
       
        float avgScore = 0;
        float currentScore = 0;
        float empiresChecked = 0;
        float max = 0;
        float min = Float.MAX_VALUE;
        
        for(Empire emp : iev.owner().contactedEmpires())
        {
            if(!iev.owner().inEconomicRange(emp.id))
                continue;
            float score = emp.totalPlanetaryProduction() / emp.totalEmpirePopulation();

            if(iev.is(emp))
                currentScore = score;
            if(score >= max)
                max = score;
            if(score <= min)
                min = score;
            avgScore += score;
            empiresChecked++;
        }  
        
        if(empiresChecked > 0)
            avgScore /= empiresChecked;
        if(currentScore < avgScore)
            severityGoal = -50 * (currentScore - avgScore) / (min - avgScore);
        if(currentScore > avgScore)
            severityGoal = 50 * (currentScore - avgScore) / (max - avgScore);
        //System.out.println(galaxy().currentTurn()+" "+ev.owner().name()+" evaluates "+ev.empire().name()+" industrial-score: "+currentScore+" min: "+min+" max: "+max+" avg: "+avgScore+" severity: "+severity);
        severity = (severityGoal - severity) / 10 + severity;
    }
    @Override
    public float currentSeverity()
    {
        return severity;
    }
    @Override
    public boolean isForgotten()
    {
        return false;
    }
    @Override
    public String title()            { return text("INC_INDUSTRIALIST_TITLE"); }
    @Override
    public String description()      { 
        if(severity < 0)
            return decode(text("INC_INDUSTRIALIST_DESC1"));
        else
            return decode(text("INC_INDUSTRIALIST_DESC2"));
    }
    @Override
    public String key() {
        return "Industrialist";
    }
    @Override
    public String decode(String s) {
        String s1 = super.decode(s);
        s1 = galaxy().empire(empMe).replaceTokens(s1, "my");
        s1 = galaxy().empire(empYou).replaceTokens(s1, "your");
        return s1;
    }
}
