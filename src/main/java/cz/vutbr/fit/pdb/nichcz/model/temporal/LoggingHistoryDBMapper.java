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

    public List<LoggingHistoryEntity> findAll(String orderBy) {
        List<LoggingHistoryEntity> res = new ArrayList<>();

        String query = ("select c.id as company_id, c.name as company_name, h.id, h.valid_from, h.valid_to, h.logging_area, h.logs_per_day" +
            " from pdb_company c, pdb_logging_history h  " +
            " where c.id = h.company_id " +
            " and c.valid_from = h.valid_from "+
            " and c.valid_to = h.valid_to" +
            " order by " + orderBy);

        try (
                ResultSet rset = getConnection().prepareStatement(query).executeQuery();
        ){
            while (rset.next()) {
                LoggingHistoryEntity e = new LoggingHistoryEntity();
                resultSetToEntity(rset, e);
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
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

    public void createWithBound(CompanyEntity c, LoggingHistoryEntity entity) {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        List<TemporalEntity> foreigns = getBounds(c);
        propagateSave(foreigns, entity.getValidFrom(), entity.getValidTo());
        create(entity);

        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}
    }

    public List<SameLogersEntity> getSameLoggers() {
        String query = "" +
            "select h1.logging_area, " +
            "c1.name as company_name_1, c2.name as company_name_2, " +
            "h1.logs_per_day as company_1_loggs_per_day, h2.logs_per_day as company_2_loggs_per_day, " +
            "h1.valid_from as company_1_valid_from, h2.valid_from as company_2_valid_from, " +
            "h1.valid_to as company_1_valid_to, h2.valid_to as company_2_valid_to " +

            "from pdb_company c1, pdb_company c2, pdb_logging_history h1, pdb_logging_history h2 " +

            "where c1.id <> c2.id " +
            "and c1.valid_from >= c2.valid_from and c1.valid_from <= c2.valid_to " +
            "and h1.logging_area = h2.logging_area " +
            "and h1.company_id = c1.id and h1.valid_from = c1.valid_from and h1.valid_to = c1.valid_to " +
            "and h2.company_id = c2.id and h2.valid_from = c2.valid_from and h2.valid_to = c2.valid_to ";

        List<SameLogersEntity> res = new ArrayList<>();
        try (
                ResultSet rset = getConnection().prepareStatement(query).executeQuery();
        ){
            while (rset.next()) {
                SameLogersEntity e = new SameLogersEntity();
                sameLoggerToEntity(rset, e);
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }

    private void sameLoggerToEntity(ResultSet rset, SameLogersEntity e) throws Exception {
        e.setLoggingArea(rset.getString("logging_area"));
        e.setFirstCompany(rset.getString("company_name_1"));
        e.setSecondCompany(rset.getString("company_name_2"));
        e.setFirstLoggsPerDay(rset.getInt("company_1_loggs_per_day"));
        e.setSecondLoggsPerDay(rset.getInt("company_2_loggs_per_day"));
        e.setFirstValidFrom(utils.daysToDate(rset.getLong("company_1_valid_from")));
        e.setSecondValidFrom(utils.daysToDate(rset.getLong("company_2_valid_from")));
        e.setFirstValidTo(utils.daysToDate(rset.getLong("company_1_valid_to")));
        e.setSecondValidTo(utils.daysToDate(rset.getLong("company_2_valid_to")));
    }

    public List<LoggingHistoryEntity> getProduction(Date from, Date to) {
        Boolean applyPeriod = false;
        Long from_ = 0L;
        Long to_ = 0L;
        if (from != null && to != null) {
            applyPeriod = true;
            from_ = utils.dateToDays(from);
            to_ = utils.dateToDays(to);
        }

        String query = "" +
            "select c.name, avg(h.logs_sum) as logs_avg " +
            "from pdb_company c, ( " +
            "   select company_id, (valid_to - valid_from) * logs_per_day as logs_sum " +
            "   from pdb_logging_history ";
        if (applyPeriod) {
            query +=
            "   where valid_from >= " + from_ + " AND valid_to <= " + to_;
        }
        query +=
            " ) h " +
            "where c.id = h.company_id ";
        if (applyPeriod) {
            query +=
            "and valid_from >= " + from_ + " AND valid_to <= " + to_ + " ";
        }
        query +=
            "group by c.id, c.name " +
            "order by logs_avg desc";

        List<LoggingHistoryEntity> res = new ArrayList<>();
        try (
                ResultSet rset = getConnection().prepareStatement(query).executeQuery();
        ){
            while (rset.next()) {
                LoggingHistoryEntity e = new LoggingHistoryEntity();
                e.setCompanyName(rset.getString("name"));
                e.setLogsPerDay(rset.getInt("logs_avg"));
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }
}
