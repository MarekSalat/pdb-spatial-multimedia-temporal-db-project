package cz.vutbr.fit.pdb.nichcz.model.spatial;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import oracle.spatial.geometry.JGeometry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * User: Marek Salát
 * Date: 5.12.13
 * Time: 14:25
 */
public class SpatialMapperTest {


    private Context ctx;
    private SpatialDBMapper mapper;
    @Before
    public void setUp() throws Exception {
        ctx = new Context();
        ctx.load();

        mapper = new SpatialDBMapper(ctx);
    }

    @After
    public void tearDown() throws Exception {
        ctx.close();
        ctx = null;
        mapper = null;
    }

    @Test
    public void testPoint() throws Exception {
        Point2DShape point = new Point2DShape(20, 100);
        SpatialEntity entity = mapper.findById(Long.valueOf(13));
        entity.setObjectType(SpatialEntity.TYPE.FEEDING_RACK);
        entity.setGeometry(point);

        mapper.save(entity);
    }

    //@Test
    public void testCircle() throws Exception {
        Ellipse2D ellipse = new Ellipse2D.Double(135, 125, 50, 50);

        SpatialEntity entity = mapper.findById(Long.valueOf(69));

        entity.setObjectType(SpatialEntity.TYPE.HUNTING_AREA);
        entity.setGeometry(ellipse);

        mapper.save(entity);
    }

    //@Test
    public void testMultiLine() throws Exception {
        SpatialEntity e = mapper.findById(Long.valueOf(42));
        System.out.println(e);

        Path2D path2D = new Path2D.Double();
        path2D.moveTo(50, 50);
        path2D.lineTo(100, 50);
        path2D.lineTo(125, 25);

        path2D.moveTo(130, 50);
        path2D.lineTo(125, 75);

        e.setObjectType(SpatialEntity.TYPE.STREAM);
        e.setGeometry(path2D);

        mapper.save(e);
    }

    //@Test
    public void testPolygon() throws Exception {
        SpatialEntity e = mapper.findById(Long.valueOf(83));
        Polygon p = new Polygon();

        p.addPoint(50, 50);
        p.addPoint(50, 100);
        p.addPoint(125, 100);
        p.addPoint(125, 75);

        e.setObjectType(SpatialEntity.TYPE.FOREST);
        e.setGeometry(p);

        mapper.save(e);
    }

    //@Test
    public void testName() throws Exception {
        Polygon polygon = new Polygon();
        polygon.addPoint(0, 0);
        polygon.addPoint(50, 0);
        polygon.addPoint(0, 50);
        JGeometry jgPolygon = SpatialDBMapper.shape2jGeometry(polygon);
        System.out.println(jgPolygon.createShape());

        Path2D.Double path = new Path2D.Double();
        path.moveTo(0,0);
        path.lineTo(50, 50);
        JGeometry jgPath = SpatialDBMapper.shape2jGeometry(path);
        System.out.println(jgPath.createShape());
    }




}
