package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.context.Context;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 16.12.13
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class LoggingHistoryDBMapper extends TemporalDBMapper<LoggingHistoryEntity, Long, Date, Date> {

    public LoggingHistoryDBMapper(Context ctx) {
        super(ctx);
    }

    @Override
    public void create(LoggingHistoryEntity entity) {
        try ( PreparedStatement stmt = getConnection()
                .prepareStatement("insert into " + entity.getTable() +
                        "       (id,company_id,company_name,logging_area,logs_per_day,valid_from,valid_to) " +
                        "VALUES (?, ?,         ?,           ?,           ?,           ?,          ?)");
        ){
            int i=1;
            stmt.setString(i++, String.valueOf(entity.getId()));
            stmt.setString(i++, String.valueOf(entity.getCompanyId()));
            stmt.setString(i++, entity.getCompanyName());
            stmt.setString(i++, entity.getLoggingArea());
            stmt.setInt(i++, entity.getLogsPerDay());

            stmt.setLong(i++, utils.dateToDays(entity.getValidFrom()));
            stmt.setLong(i++, utils.dateToDays(entity.getValidTo()));

            stmt.executeUpdate();
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    @Override
    public List<LoggingHistoryEntity> findWhere(String where) {
        if(!where.isEmpty()) where = "where " + where;

        List<LoggingHistoryEntity> res = new ArrayList<>();
        try (
                ResultSet rset = getConnection().prepareStatement("select * from " + LoggingHistoryEntity.TABLE + " " + where).executeQuery();
        ){
            while (rset.next()) {
                LoggingHistoryEntity e = new LoggingHistoryEntity();
                resultSetToEntity(rset, e);
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }

    private void resultSetToEntity(ResultSet rset, LoggingHistoryEntity e) throws Exception {
        e.setId(Long.valueOf(rset.getString("id")));
        e.setCompanyId(Long.valueOf(rset.getString("company_id")));
        e.setCompanyName(rset.getString("company_name"));
        e.setLoggingArea(rset.getString("logging_area"));
        e.setLogsPerDay(rset.getInt("logs_per_day"));
        e.setValidFrom(utils.daysToDate(rset.getLong("valid_from")));
        e.setValidTo(utils.daysToDate(rset.getLong("valid_to")));
    }
}
