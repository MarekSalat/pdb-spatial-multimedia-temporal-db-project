package cz.vutbr.fit.pdb.nichcz;

import java.util.List;

/**
 * User: Marek Salát
 * Date: 5.12.13
 * Time: 14:04
 */
public class Util {
    public static double[] convertDoubleListToPrimitive(List<Double> pointList) {
        double [] doubles = new double[pointList.size()];
        for (int i = 0; i < pointList.size(); i++) {
            Double coord = pointList.get(i);
            doubles[i] = coord;
        }
        return doubles;
    }

    public static int[] convertIntegerListToPrimitive(List<Integer> pointList) {
        int [] ints = new int[pointList.size()];
        for (int i = 0; i < pointList.size(); i++) {
            int coord = pointList.get(i);
            ints[i] = coord;
        }
        return ints;
    }
}
