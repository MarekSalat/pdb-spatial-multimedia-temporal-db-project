package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.model.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 13.12.13
 * Time: 18:00
 * To change this template use File | Settings | File Templates.

 CREATE TABLE "_TEMPORAL_SPEC"
 (
 TABLE_NAME VARCHAR (128) PRIMARY KEY NOT NULL,
 VALID_TIME VARCHAR (5) NOT NULL,
 VALID_TIME_SCALE VARCHAR(6) NOT NULL,
 TRANSACTION_TIME VARCHAR(5) NOT NULL,
 VACUUM_CUTOFF NUMBER(20) NOT NULL,
 VACUUM_CUTOFF_RELATIVE NUMBER(1) NOT NULL,

 CONSTRAINT VALID_TIME_CHECK CHECK(VALID_TIME IN ('STATE', 'EVENT', 'NONE')),
 CONSTRAINT TRANSACTION_TIME_CHECK CHECK(TRANSACTION_TIME IN ('STATE', 'NONE')),
 CONSTRAINT VALID_TIME_SCALE_CHECK CHECK(VALID_TIME_SCALE IN ('SECOND', 'MINUTE', 'HOUR', 'DAY', 'MONTH', 'YEAR'))
 );

 INSERT INTO "_TEMPORAL_SPEC" (TABLE_NAME, VALID_TIME, VALID_TIME_SCALE, TRANSACTION_TIME, VACUUM_CUTOFF, VACUUM_CUTOFF_RELATIVE)
 VALUES ('PDB_SPATIAL', 'STATE', 'DAY', 'NONE', 0, 0);

 */
public class TemporalSpecEntity implements Entity<String> {
    public static String TABLE="_TEMPORAL_SPEC";

    public enum VALID_TIME {
        STATE,
        EVENT,
        NONE
    }

    public enum VALID_TIME_SCALE {
        SECOND,
        MINUTE,
        HOUR,
        DAY,
        MONTH,
        YEAR
    }

    public String tableName;
    public VALID_TIME validTime;
    public VALID_TIME_SCALE validTimeScale;
    public String transactionTime;
    public Integer vacuumCutoff;
    public Integer vacuumCutoffRelative;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getTable() {
        return TABLE;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public VALID_TIME getValidTime() {
        return validTime;
    }

    public void setValidTime(VALID_TIME validTime) {
        this.validTime = validTime;
    }

    public VALID_TIME_SCALE getValidTimeScale() {
        return validTimeScale;
    }

    public void setValidTimeScale(VALID_TIME_SCALE validTimeScale) {
        this.validTimeScale = validTimeScale;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Integer getVacuumCutoff() {
        return vacuumCutoff;
    }

    public void setVacuumCutoff(Integer vacuumCutoff) {
        this.vacuumCutoff = vacuumCutoff;
    }

    public Integer getVacuumCutoffRelative() {
        return vacuumCutoffRelative;
    }

    public void setVacuumCutoffRelative(Integer vacuumCutoffRelative) {
        this.vacuumCutoffRelative = vacuumCutoffRelative;
    }
}
