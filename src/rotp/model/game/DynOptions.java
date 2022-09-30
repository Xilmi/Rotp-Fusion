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

import static rotp.util.ObjectCloner.deepCopy;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import rotp.util.ObjectCloner;

public class DynOptions implements DynamicOptions, Serializable {
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String, Boolean>	booleanOptions	= new LinkedHashMap<>();
	private LinkedHashMap<String, Float>	floatOptions	= new LinkedHashMap<>();
	private LinkedHashMap<String, Integer>	integerOptions	= new LinkedHashMap<>();
    private LinkedHashMap<String, Object>	objectOptions	= new LinkedHashMap<>();
    private LinkedHashMap<String, String>	stringOptions	= new LinkedHashMap<>();

    // -------------------- Overriders --------------------
    //
	@Override public Boolean setBooleanOptions(String id, Boolean value) {
		return booleanOptions.put(id, value);
	}
	@Override public Boolean getBooleanOptions(String id) {
		return booleanOptions.get(id);
	}
	@Override public Boolean getBooleanOptions(String id, Boolean defaultValue) {
		return booleanOptions.getOrDefault(id, defaultValue);
	}
	@Override public Float setFloatOptions(String id, Float value) {
		return floatOptions.put(id, value);
	}
	@Override public Float getFloatOptions(String id) {
		return floatOptions.get(id);
	}
	@Override public Float getFloatOptions(String id, Float defaultValue) {
		return floatOptions.getOrDefault(id, defaultValue);
	}
	@Override public Integer setIntegerOptions(String id, Integer value) {
		return integerOptions.put(id, value);
	}
	@Override public Integer getIntegerOptions(String id) {
		return integerOptions.get(id);
	}
	@Override public Integer getIntegerOptions(String id, Integer defaultValue) {
		return integerOptions.getOrDefault(id, defaultValue);
	}
	@Override public Object setObjectOptions(String id, Object value) {
		return objectOptions.put(id, deepCopy(value));
	}
	@Override public Object getObjectOptions(String id) {
		return objectOptions.get(id);
	}
	@Override public Object getObjectOptions(String id, Object defaultValue) {
		return objectOptions.getOrDefault(id, defaultValue);
	}
	@Override public String setStringOptions(String id, String value) {
		return stringOptions.put(id, value);
	}
	@Override public String getStringOptions(String id) {
		return stringOptions.get(id);
	}
	@Override public String getStringOptions(String id, String defaultValue) {
		return stringOptions.getOrDefault(id, defaultValue);
	}
	// -------------------- File Management --------------------
	//
	public void save(String path, String fileName) {
		saveOptions(this, path, fileName);
	}
	public void load(String path, String fileName) {
		DynOptions opts	= loadOptions(path, fileName);
		booleanOptions	= opts.booleanOptions;
		floatOptions	= opts.floatOptions;
		integerOptions	= opts.integerOptions;
	    objectOptions	= opts.objectOptions;
	    stringOptions	= opts.stringOptions;
	}
	public DynOptions copy() {
		return (DynOptions) ObjectCloner.deepCopy(this);
	}
	public static DynOptions copy(DynOptions options) {
		return (DynOptions) ObjectCloner.deepCopy(options);
	}
    // Save options to zip file
    public static void saveOptions(DynOptions options, String path, String fileName) {
		File saveFile = new File(path, fileName);
		try {
			saveOptionsTE(options, saveFile);
		} catch (IOException ex) {
			ex.printStackTrace();
           	System.err.println("Options.save -- IOException: "+ ex.toString());
		}
    }
    // Load options from file
    public static DynOptions loadOptions(String path, String fileName) {
    	DynOptions newOptions;
		File saveFile = new File(path, fileName);
		if (saveFile.exists()) {
			newOptions = loadOptionsTE(saveFile);
            if (newOptions == null) {
            	System.err.println("Bad option version: " + saveFile.getAbsolutePath());
            	newOptions = initMissingOptionFile(path, fileName);
            }
    	} else {
			System.err.println("File not found: " + saveFile.getAbsolutePath());
			newOptions = initMissingOptionFile(path, fileName);
		}
		return newOptions;
    }
    private static void saveOptionsTE(DynOptions options, File saveFile) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(saveFile));
        ZipEntry e = new ZipEntry("DynamicOptions.dat");
        out.putNextEntry(e);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOut = null;
        try {
            objOut = new ObjectOutputStream(bos);
            objOut.writeObject(options);
            objOut.flush();
            byte[] data = bos.toByteArray();
            out.write(data, 0, data.length);
        }
        finally {
            try {
            bos.close();
            out.close();
            }
            catch(IOException ex) {
    			ex.printStackTrace();
            	System.err.println("Options.save -- IOException: "+ ex.toString());
            }            
        }
    }
    // Options files initialization
    private static DynOptions initMissingOptionFile(String path, String fileName) {
    	Toolkit.getDefaultToolkit().beep();
    	DynOptions newOptions = new DynOptions();
    	saveOptions(new DynOptions(), path, fileName);			
		return newOptions;    	
    }
    private static DynOptions loadOptionsTE(File saveFile) {
    	DynOptions newOptions;
    	try (ZipFile zipFile = new ZipFile(saveFile)) {
            ZipEntry ze = zipFile.entries().nextElement();
            InputStream zis = zipFile.getInputStream(ze);
            newOptions = loadObjectData(zis);
        }
        catch(IOException e) {
        	System.err.println("Bad option version " + saveFile.getAbsolutePath());
        	newOptions = null;
        }
		return newOptions;
    }
    private static DynOptions loadObjectData(InputStream is) {
        try {
        	DynOptions newOptions;
            try (InputStream buffer = new BufferedInputStream(is)) {
                ObjectInput input = new ObjectInputStream(buffer);
                newOptions = (DynOptions) input.readObject();
            }
            return newOptions;
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }	
}
