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

public interface DynamicOptions {

    public Boolean	setBoolean(String id, Boolean value);
    public Boolean	getBoolean(String id);
    public Boolean	getBoolean(String id, Boolean defaultValue);
    public Float	setFloat  (String id, Float value);
    public Float	getFloat  (String id);
    public Float	getFloat  (String id, Float defaultValue);
    public Integer	setInteger(String id, Integer value);
    public Integer	getInteger(String id);
    public Integer	getInteger(String id, Integer defaultValue);
    public Object	setObject (String id, Serializable value);
    public Object	getObject (String id);
    public Object	getObject (String id, Serializable defaultValue);
    public String	setString (String id, String value);
    public String	getString (String id);
    public String	getString (String id, String defaultValue);
}
