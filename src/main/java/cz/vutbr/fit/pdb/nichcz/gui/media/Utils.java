package cz.vutbr.fit.pdb.nichcz.gui.media;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 7.12.13
 * Time: 2:24
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public final static String bmp = "bmp";
    public final static String cal = "cal";
    public final static String fpx = "fpx";
    public final static String gif = "gif";
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";

    public final static String pbm = "pbm";
    public final static String pgm = "pgm";
    public final static String ppm = "ppm";
    public final static String pnm = "pnm";

    public final static String pcx = "pcx";
    public final static String pct = "pct";
    public final static String png = "png";
    public final static String rpx = "rpx";
    public final static String ras = "ras";
    public final static String tga = "tga";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String wbmp = "wbmp";

    //Get extension of file
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
