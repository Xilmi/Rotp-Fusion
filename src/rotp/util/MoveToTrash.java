package rotp.util;

import java.io.File;
import java.io.IOException;

import com.sun.jna.platform.FileUtils;

public class MoveToTrash {

    public static boolean moveToTrash(File... files){
        FileUtils fileUtils = FileUtils.getInstance();
        if (fileUtils.hasTrash()) {
            try {
                fileUtils.moveToTrash(files);
                return true;
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("No Trash available");
            return false;
        }
    }
}