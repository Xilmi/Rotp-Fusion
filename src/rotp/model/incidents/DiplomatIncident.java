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

public class DiplomatIncident extends DiplomaticIncident {
    private static final long serialVersionUID = 1L;
    final int empYou;
    final int empMe;
    float severityGoal;
    EmpireView iev;
    public static DiplomatIncident create(EmpireView ev) {
        return new DiplomatIncident(ev);
    }
    @Override
    public boolean triggeredByAction()   { return false; }
    private DiplomatIncident(EmpireView ev) {
        iev = ev;
        empYou = iev.empire().id;
        empMe = iev.ownerId();
        severity = 0;
    }
    @Override
    public void update()
    {
        dateOccurred = galaxy().currentYear();
        severityGoal = 0;
        
        if (iev.embassy().unity() || !iev.owner().inEconomicRange(iev.empId())) {
            severity = 0;
            return;
        }
        if(iev.owner().generalAI().absolution() < 1 || !iev.owner().leader().isDiplomat())
        {
            severity = 0;
            return;
        }
       
        float avgDiploScore = 0;
        float currentDiploScore = 0;
        float empiresChecked = 0;
        float maxPopularity = -Float.MAX_VALUE;
        float minPopularity = Float.MAX_VALUE;
        
        for(Empire emp : iev.owner().contactedEmpires())
        {
            if(!iev.owner().inEconomicRange(emp.id))
                continue;
            float score = 0;
            for(Empire contacts : emp.contactedEmpires())
            {
                if(contacts.alliedWith(emp.id))
                    score += 3;
                else if(contacts.pactWith(emp.id))
                    score += 2;
                else if(contacts.atWarWith(emp.id))
                    score -= 2;
                else
                    score += 1;
            }
            if(emp == iev.empire())
                currentDiploScore = score;
            if(score >= maxPopularity)
                maxPopularity = score;
            if(score <= minPopularity)
                minPopularity = score;
            avgDiploScore += score;
            empiresChecked++;
        }  
        
        if(empiresChecked > 0)
            avgDiploScore /= empiresChecked;
        if(currentDiploScore < avgDiploScore)
            severityGoal = -50 * (currentDiploScore - avgDiploScore) / (minPopularity - avgDiploScore);
        if(currentDiploScore > avgDiploScore)
            severityGoal = 50 * (currentDiploScore - avgDiploScore) / (maxPopularity - avgDiploScore);
        severity = (severityGoal - severity) / 10 + severity;
        //System.out.println(galaxy().currentTurn()+" "+iev.owner().name()+" evaluates "+iev.empire().name()+" diplo-score: "+currentDiploScore+" min: "+minPopularity+" max: "+maxPopularity+" avg: "+avgDiploScore+" severityGoal: "+severityGoal+" severity: "+severity);
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
    public String title()            { return text("INC_DIPLOMAT_TITLE"); }
    @Override
    public String description()      { 
        if(severity < 0)
            return decode(text("INC_DIPLOMAT_DESC1"));
        else
            return decode(text("INC_DIPLOMAT_DESC2"));
    }
    @Override
    public String key() {
        return "Diplomat";
    }
    @Override
    public String decode(String s) {
        String s1 = super.decode(s);
        s1 = galaxy().empire(empMe).replaceTokens(s1, "my");
        s1 = galaxy().empire(empYou).replaceTokens(s1, "your");
        return s1;
    }
}
