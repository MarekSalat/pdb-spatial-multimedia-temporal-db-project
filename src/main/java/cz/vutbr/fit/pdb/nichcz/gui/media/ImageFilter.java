package cz.vutbr.fit.pdb.nichcz.gui.media;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 7.12.13
 * Time: 2:31
 * To change this template use File | Settings | File Templates.
 */
public class ImageFilter  extends FileFilter {

    //Accept all directories and image files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.bmp) ||
                    extension.equals(Utils.cal) ||
                    extension.equals(Utils.fpx) ||
                    extension.equals(Utils.gif) ||
                    extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.jpg) ||
                    extension.equals(Utils.png) ||
                    extension.equals(Utils.jpg) ||
                    extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.tga) ||
                    extension.equals(Utils.tiff) ||
                    extension.equals(Utils.tif)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }
}
