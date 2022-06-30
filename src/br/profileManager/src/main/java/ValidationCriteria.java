
/*
 * Licensed under the GNU General License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.profileManager.src.main.java;

/**
 * All boolean conditions for parameters validations
 * with default values already set
 * used as "struct"
 * so setters and getters are not mandatory
 */
public class ValidationCriteria {

	private boolean isNullAllowed   = false;
	private boolean isBlankAllowed  = true;
	private boolean isRandomAllowed = true;
	private boolean userViewEquals  = true ;
	private boolean codeViewEquals  = true ;
	private boolean categoryEquals  = false;
	private boolean userViewIsCaseSensitive  = false;
	private boolean codeViewIsCaseSensitive  = false;
	private boolean categoryIsCaseSensitive  = false;
	
	// ==================================================
    // getters
    //
	boolean isNullAllowed()  { 
		return isNullAllowed; 
	}
	
	boolean isBlankAllowed() { 
		return isBlankAllowed; 
	}
	boolean isRandomAllowed() { 
		return isRandomAllowed; 
	}
	
	/**
	 * @return <b>true</b>: user View equals 
	 * <b>false</b>: user View isContained
	 */
	boolean userViewEquals() { 
		return userViewEquals; 
	}
	
	/**
	 * @return <b>true</b>: code View equals 
	 * <b>false</b>: code View isContained
	 */
	boolean codeViewEquals() { 
		return codeViewEquals; 
	}
	
	/**
	 * @return <b>true</b>: category equals 
	 * <b>false</b>: category Contains
	 */
	boolean categoryEquals() { 
		return categoryEquals; 
	}
	
	boolean userViewIsCaseSensitive() { 
		return userViewIsCaseSensitive; 
	}
	
	boolean categoryIsCaseSensitive() { 
		return categoryIsCaseSensitive; 
	}
	
	boolean codeViewIsCaseSensitive() { 
		return codeViewIsCaseSensitive; 
	}
	
	// ==================================================
    // Setters for chaining purpose
    //

	/**
	 * @param allowed the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria isNullAllowed(boolean allowed) {
		isNullAllowed = allowed;
		return this; 
	}
	
	/**
	 * @param allowed the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria isBlankAllowed(boolean allowed) { 
		isBlankAllowed = allowed;
		return this; 
	}
	
	/**
	 * @param allowed the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria isRandomAllowed(boolean allowed) { 
		isRandomAllowed = allowed;
		return this; 
	}
	
	/**
	 * <b>true</b>: user View equals 
	 * <b>false</b>: user View isContained
	 * @param equals the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria userViewEquals(boolean equals) { 
		userViewEquals = equals;
		return this; 
	}
	
	/**
	 * <b>true</b>: code View equals 
	 * <b>false</b>: code View isContained
	 * @param equals the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria codeViewEquals(boolean equals) { 
		codeViewEquals = equals;
		return this; 
	}
	
	/**
	 * <b>true</b>: category equals 
	 * <b>false</b>: category Contains
	 * @param equals the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria categoryEquals(boolean equals) { 
		categoryEquals = equals;
		return this; 
	}
	
	/**
	 * @param caseSensitive the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria userViewIsCaseSensitive(boolean caseSensitive) { 
		userViewIsCaseSensitive = caseSensitive;
		return this; 
	}
	
	/**
	 * @param caseSensitive the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria categoryIsCaseSensitive(boolean caseSensitive) { 
		categoryIsCaseSensitive = caseSensitive;
		return this; 
	}
	
	/**
	 * @param caseSensitive the new value
	 * @return this for chaining purpose
	 */
	public ValidationCriteria codeViewIsCaseSensitive(boolean caseSensitive) { 
		codeViewIsCaseSensitive = caseSensitive;
		return this; 
	}
}
