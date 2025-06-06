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
package rotp.model.empires;

public class TreatyPact extends DiplomaticTreaty {
    private static final long serialVersionUID = 1L;
    public TreatyPact(Empire e1, Empire e2) {
        super(e1,e2,"RACES_PACT");
    }
    public TreatyPact(int e1, int e2) { super(e1, e2, "RACES_PACT"); }
    @Override
    public boolean isPact()                   { return true; }
    @Override
    public int listOrder()                      { return 4; }
}
