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

public class CoexistIncident extends DiplomaticIncident {
    private static final long serialVersionUID = 1L;
    final int empYou;
    final int empMe;
    float severityGoal;
    EmpireView iev;
    public static CoexistIncident create(EmpireView ev) {
        return new CoexistIncident(ev);
    }
    @Override
    public boolean triggeredByAction()   { return false; }
    private CoexistIncident(EmpireView ev) {
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
        if(iev.trade().active())
            severityGoal += 15;
        if(iev.embassy().treaty().isPact())
            severityGoal += 20;
        else if(iev.embassy().treaty().isAlliance())
            severityGoal += 35;
        for(DiplomaticIncident inc : iev.embassy().allIncidents())
        {
            if(inc.currentSeverity() < 0)
            {
                severityGoal += inc.currentSeverity();
            }
        }
        severityGoal = max(0, severityGoal);
        if(severity < severityGoal)
            severity++;
        severity = min(severity, severityGoal);
        //System.out.println(galaxy().currentTurn()+" "+iev.owner().name()+" shifts CoExist-Incident towards "+iev.empire().name()+" to: "+severityGoal+" current: "+severity);
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
    public String title()            { return text("INC_COEXIST_TITLE"); }
    @Override
    public String description()      { 
        if(severity < 16)
            return decode(text("INC_COEXIST_DESC1"));
        else if(severity < 36)
            return decode(text("INC_COEXIST_DESC2"));
        else if(severity < 50)
            return decode(text("INC_COEXIST_DESC3"));
        else
            return decode(text("INC_COEXIST_DESC4"));
    }
    @Override
    public String key() {
        return "Coexist";
    }
    @Override
    public String decode(String s) {
        String s1 = super.decode(s);
        s1 = galaxy().empire(empMe).replaceTokens(s1, "my");
        s1 = galaxy().empire(empYou).replaceTokens(s1, "your");
        return s1;
    }
}
