package cz.vutbr.fit.pdb.nichcz.model.spatial;

import org.junit.Test;

/**
 * User: Marek Salát
 * Date: 3.12.13
 * Time: 20:24
 */
public class SpatialEntityTest {
    @Test
    public void testGetId() throws Exception {

        System.out.println( SpatialEntity.TYPE.FEEDING_RACK );
        System.out.println( SpatialEntity.TYPE.valueOf("FOREST") == SpatialEntity.TYPE.FOREST );
        //System.out.println( SpatialEntity.TYPE.);

    }
}
