package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.AbstractDBMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 15.12.13
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class CompanyDBMapper extends TemporalDBMapper<CompanyEntity, Long, Date, Date> {

    public CompanyDBMapper(Context ctx) {
        super(ctx);
    }

    @Override
    public void create(CompanyEntity entity) {
        try ( PreparedStatement stmt = getConnection()
                .prepareStatement("insert into " + entity.getTable() +
                        "       (id,name,valid_from,valid_to) " +
                        "VALUES (?, ?,   ?,         ?)");
        ){
            int i=1;
            stmt.setString(i++, String.valueOf(entity.getId()));
            stmt.setString(i++, String.valueOf(entity.getName()));

            stmt.setLong(i++, utils.dateToDays(entity.getValidFrom()));
            stmt.setLong(i++, utils.dateToDays(entity.getValidTo()));

            stmt.executeUpdate();
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    @Override
    public List<CompanyEntity> findWhere(String where) {
        if(!where.isEmpty()) where = "where " + where;

        List<CompanyEntity> res = new ArrayList<>();
        try (
                ResultSet rset = getConnection().prepareStatement("select * from " + CompanyEntity.TABLE + " " + where).executeQuery();
        ){
            while (rset.next()) {
                CompanyEntity e = new CompanyEntity();
                resultSetToEntity(rset, e);
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }

    private void resultSetToEntity(ResultSet rset, CompanyEntity e) throws Exception {
        e.setId(Long.valueOf(rset.getString("id")));
        e.setName(rset.getString("name"));
        e.setValidFrom(utils.daysToDate(rset.getLong("valid_from")));
        e.setValidTo(utils.daysToDate(rset.getLong("valid_to")));
    }

}
