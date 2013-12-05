package cz.vutbr.fit.pdb.nichcz.model.spatial;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Path2D;
import java.util.Date;

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
    public void testTest() throws Exception {
        SpatialEntity e = mapper.findById(Long.valueOf(42));
        System.out.println(e);

        Path2D path2D = new Path2D.Double();
        path2D.moveTo(50, 50);
        path2D.lineTo(100, 50);
        path2D.lineTo(125, 25);

        path2D.moveTo(130, 50);
        path2D.lineTo(125, 75);

        e.setGeometry(path2D);

        e.setValidTo(null);
        e.setValidFrom(new Date());
        mapper.save(e);
        System.out.println(e);
    }
}
