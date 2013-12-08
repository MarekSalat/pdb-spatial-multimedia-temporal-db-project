package cz.vutbr.fit.pdb.nichcz.model.spatial;

import java.awt.geom.Ellipse2D;

/**
 * User: Marek Salát
 * Date: 5.12.13
 * Time: 17:32
 */
public class Point2DShape extends Ellipse2D.Double {
    Point2DShape() {
    }

    public Point2DShape(double x, double y) {
        super(x, y, 5, 5);
    }
}
