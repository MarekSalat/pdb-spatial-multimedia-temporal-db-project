package cz.vutbr.fit.pdb.nichcz.model.temporal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 16.12.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 *
 CREATE TABLE PDB_LOGGING_HISTORY (
     ID VARCHAR(64),
     COMPANY_ID VARCHAR(64),
     COMPANY_NAME VARCHAR(100),
     LOGGING_AREA VARCHAR(300),
     LOGS_PER_DAY INTEGER,
     VALID_FROM NUMBER(20),
     VALID_TO NUMBER(20)
 );


 */
public class LoggingHistoryEntity implements TemporalEntity<Long, Date, Date> {
    public static String TABLE="PDB_LOGGING_HISTORY";

    public Long id;
    public Long companyId;

    public String companyName;

    public Integer logsPerDay;

    public String loggingArea;
    public Date validFrom;

    public Date validTo;
    public LoggingHistoryEntity() {
        id = UUID.randomUUID().getMostSignificantBits();
    }

    @Override
    public Date getValidFrom() {
        return validFrom;
    }

    @Override
    public Date getValidTo() {
        return validTo;
    }

    @Override
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    @Override
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTable() {
        return TABLE;
    }

    public String getLoggingArea() {
        return loggingArea;
    }

    public void setLoggingArea(String loggingArea) {
        this.loggingArea = loggingArea;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getLogsPerDay() {
        return logsPerDay;
    }

    public void setLogsPerDay(Integer logsPerDay) {
        this.logsPerDay = logsPerDay;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public LoggingHistoryEntity clone(Utils utils) {
        LoggingHistoryEntity entity = new LoggingHistoryEntity();
        entity.setId(getId());
        entity.setCompanyName(getCompanyName());
        entity.setCompanyId(getCompanyId());
        entity.setLoggingArea(getLoggingArea());
        entity.setLogsPerDay(getLogsPerDay());
        entity.setValidFrom( utils.daysToDate(utils.dateToDays(getValidFrom())) );
        entity.setValidTo( utils.daysToDate(utils.dateToDays(getValidTo())) );
        return entity;
    }

    @Override
    public String toString() {
        SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");
        return dtf.format(getValidFrom()) +" - "+ dtf.format(getValidTo()) +" | "+ getCompanyName();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LoggingHistoryEntity)) {
            return false;
        }
        if ( ((LoggingHistoryEntity) o).getCompanyName().equals(getCompanyName()) &&
                ((LoggingHistoryEntity) o).getId().longValue() == getId().longValue() ) {
            return true;
        }
        return false;
    }
}
