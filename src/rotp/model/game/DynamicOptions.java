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

public interface DynamicOptions {

    public Boolean setBooleanOptions(String id, Boolean value);
    public Boolean getBooleanOptions(String id);
    public Boolean getBooleanOptions(String id, Boolean defaultValue);
    public Float   setFloatOptions  (String id, Float value);
    public Float   getFloatOptions  (String id);
    public Float   getFloatOptions  (String id, Float defaultValue);
    public Integer setIntegerOptions(String id, Integer value);
    public Integer getIntegerOptions(String id);
    public Integer getIntegerOptions(String id, Integer defaultValue);
    public Object  setObjectOptions (String id, Object value);
    public Object  getObjectOptions (String id);
    public Object  getObjectOptions (String id, Object defaultValue);
    public String  setStringOptions (String id, String value);
    public String  getStringOptions (String id);
    public String  getStringOptions (String id, String defaultValue);
}
