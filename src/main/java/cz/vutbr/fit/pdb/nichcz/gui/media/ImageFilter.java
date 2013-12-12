package cz.vutbr.fit.pdb.nichcz.gui.media;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * User: Michal Pracuch
 * Date: 7.12.13
 * Time: 2:31
 *
 * Trida slouzi k filtrovani souboru pri jejich vyberu pro nahrani do databaze.
 */
public class ImageFilter extends FileFilter {

    @Override
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

    @Override
    public String getDescription() {
        return "Just Images";
    }
}
