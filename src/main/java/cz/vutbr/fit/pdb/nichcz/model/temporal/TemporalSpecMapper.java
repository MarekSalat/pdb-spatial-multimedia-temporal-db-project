package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.AbstractDBMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 13.12.13
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class TemporalSpecMapper extends AbstractDBMapper<TemporalSpecEntity, Long> {
    public TemporalSpecMapper(Context ctx) {
        super(ctx);
    }

    @Override
    public TemporalSpecEntity create() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void save(TemporalSpecEntity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(TemporalSpecEntity entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TemporalSpecEntity> findWhere(String where) {
        if(!where.isEmpty()) where = "where " + where;

        List<TemporalSpecEntity> res = new ArrayList<>();
        try (
                ResultSet rset = getConnection().prepareStatement("select * from " + TemporalSpecEntity.TABLE + " " + where).executeQuery();
        ){
            while (rset.next()) {
                TemporalSpecEntity e = new TemporalSpecEntity();
                resultSetToEntity(rset, e);
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }

    private void resultSetToEntity(ResultSet rset, TemporalSpecEntity e) throws Exception {

        e.setTableName(rset.getString("table_name"));
        e.setValidTime(TemporalSpecEntity.VALID_TIME.valueOf(rset.getString("valid_time").toUpperCase()));
        e.setValidTimeScale(TemporalSpecEntity.VALID_TIME_SCALE.valueOf(rset.getString("valid_time_scale").toUpperCase()));
        e.setTransactionTime(rset.getString("transaction_time"));
        e.setVacuumCutoff(rset.getInt("vacuum_cutoff"));
        e.setVacuumCutoffRelative(rset.getInt("vacuum_cutoff_relative"));

    }
}
