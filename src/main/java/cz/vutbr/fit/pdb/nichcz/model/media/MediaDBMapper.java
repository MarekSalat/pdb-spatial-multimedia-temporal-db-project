package cz.vutbr.fit.pdb.nichcz.model.media;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.AbstractDBMapper;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 6.12.13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class MediaDBMapper extends AbstractDBMapper<MediaEntity, Long> {

    private static final double SIM_MAX = 20;

    List<MediaEntity> entities = new ArrayList<>();

    public MediaDBMapper(Context ctx) {
        super(ctx);
    }

    public List<MediaEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<MediaEntity> entities) {
        this.entities = entities;
    }


    public void loadImageFromFile(MediaEntity e, String filePath)
    {
        try {
            boolean autoCommit = getConnection().getAutoCommit();
            getConnection().setAutoCommit(false);

            try {


                try {
                    e.getImgProxy().loadDataFromFile(filePath);
                    e.getImgProxy().setProperties();
                }
                catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
                catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }

                getConnection().commit();
            } finally {
                getConnection().setAutoCommit(autoCommit);
            }
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }

    }


    @Override
    public MediaEntity create() {
        MediaEntity e = new MediaEntity();

        try ( PreparedStatement stmt = getConnection()
                .prepareStatement("insert into " + e.getTable()
                        + "       (id, photo) "
                        + "VALUES (?, ordsys.ordimage.init())");
        ){
            int i=1;
            stmt.setString(i++, String.valueOf(e.getId()));

            stmt.executeUpdate();
        } catch (SQLException ex) {ex.printStackTrace();  throw new RuntimeException(ex); }

        return findByIdForUpdate(e.getId());
    }


    @Override
    public void save(MediaEntity e) {
//        OrdImage imgProxy = null;


        try {
            boolean autoCommit = getConnection().getAutoCommit();
            getConnection().setAutoCommit(false);

            try {
//                try ( PreparedStatement stmt = getConnection()
//                        .prepareStatement("select photo from " + e.getTable() + " where id=?  for update")
//                ){
//                    int i=1;
//
//                    stmt.setString(i++, String.valueOf(e.getId()));
//
//                    try (OracleResultSet rset = (OracleResultSet) stmt.executeQuery()) {
//                        if (rset.next()) {
//                            imgProxy = (OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory());
//
//                            imgProxy.loadDataFromFile(e.filePath);
//                            imgProxy.setProperties();
//                        }
//                    }
//                }
////                catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
//                catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }


                try ( OraclePreparedStatement stmt = (OraclePreparedStatement) getConnection()
                        .prepareStatement("update " + e.getTable() + " set "
                                + "name=?, photo=? where id=?")
                ){
                    int i=1;
                    stmt.setString(i++, e.getName());
                    stmt.setORAData(i++, e.getImgProxy());

                    stmt.setString(i++, String.valueOf(e.getId()));
                    stmt.executeUpdate();
                }
                catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
//                catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }


                try ( PreparedStatement stmt = getConnection()
                        .prepareStatement("update " + e.getTable() + " t set "
                                + "t.photo_si = SI_StillImage(t.photo.getContent()) where id=?")
                ){
                    int i=1;

                    stmt.setString(i++, String.valueOf(e.getId()));
                    stmt.executeUpdate();
                }
                catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
//                catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }


                try ( PreparedStatement stmt = getConnection()
                        .prepareStatement("update " + e.getTable() + " set "
                                + "photo_ac = SI_AverageColor(photo_si), "
                                + "photo_ch = SI_ColorHistogram(photo_si), "
                                + "photo_pc = SI_PositionalColor(photo_si), "
                                + "photo_tx = SI_Texture(photo_si) "
                                + " where id=?")
                ){
                    int i=1;

                    stmt.setString(i++, String.valueOf(e.getId()));
                    stmt.executeUpdate();
                }
                catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
//                catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }


                getConnection().commit();
            } finally {
                getConnection().setAutoCommit(autoCommit);
            }
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    @Override
    public void delete(MediaEntity e) {
        try ( PreparedStatement stmt = getConnection().prepareStatement("delete from " + e.getTable()
                + " where id = ?")){
            stmt.setString(1, String.valueOf(e.getId()));
            stmt.execute();
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    @Override
    public List<MediaEntity> findWhere(String where) {
        if(!where.isEmpty()) where = "where " + where;

        List<MediaEntity> res = new ArrayList<>();
        try (
                OracleResultSet rset = (OracleResultSet) getConnection().prepareStatement("select * from "
                        + MediaEntity.TABLE + " "
                        + where /*+ " for update"*/).executeQuery();
        ){
            while (rset.next()) {
                MediaEntity e = new MediaEntity();
                e.setId(Long.valueOf(rset.getString("id")));
                e.setName(rset.getString("name"));
                e.setImgProxy((OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory()));

                res.add(e);
            }
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
//        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }

    public MediaEntity findByIdForUpdate(Long id) {
        MediaEntity e = new MediaEntity();

        try ( PreparedStatement stmt = getConnection()
                .prepareStatement("select * from " + MediaEntity.TABLE + " where id=? for update")
        ){
            int i=1;

            stmt.setString(i++, String.valueOf(id));

            try (OracleResultSet rset = (OracleResultSet) stmt.executeQuery()) {
                if (rset.next()) {
                    e.setId(Long.valueOf(rset.getString("id")));
                    e.setName(rset.getString("name"));
                    e.setImgProxy((OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory()));
                }
            }
        }
//                catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }

        return e;
    }

    public List<MediaEntity> findSimilar(Long id) {
        List<MediaEntity> res = new ArrayList<>();

        try (
                PreparedStatement stmt = getConnection().prepareStatement(
                        "SELECT dst.id, dst.name, dst.photo, SI_ScoreByFtrList("
                                + "new SI_FeatureList(src.photo_ac,?,src.photo_ch,?,src.photo_pc,?,src.photo_tx,?), dst.photo_si)"
                                + " as similarity FROM PDB_MEDIA src, PDB_MEDIA dst "
                                + "WHERE src.id <> dst.id "
                                + "AND src.id = ? ORDER BY similarity ASC");
        ){
            int i=1;

            double weightAC = 0.3;
            double weightCH = 0.3;
            double weightPC = 0.1;
            double weightTX = 0.3;

            stmt.setDouble(i++, weightAC);
            stmt.setDouble(i++, weightCH);
            stmt.setDouble(i++, weightPC);
            stmt.setDouble(i++, weightTX);
            stmt.setString(i++, String.valueOf(id));

            try (OracleResultSet rset = (OracleResultSet) stmt.executeQuery()) {
                while (rset.next()) {
                    if (rset.getDouble("similarity") < SIM_MAX) {
                        MediaEntity e = new MediaEntity();
                        e.setId(Long.valueOf(rset.getString("id")));
                        e.setName(rset.getString("name"));
                        e.setImgProxy((OrdImage) rset.getORAData("photo", OrdImage.getORADataFactory()));

//                        System.out.println(rset.getString("name") + " " + rset.getDouble("similarity"));

                        res.add(e);
                    }
                }
            }
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
//        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }
}
