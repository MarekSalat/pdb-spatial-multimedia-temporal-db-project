package cz.vutbr.fit.pdb.nichcz.model.temporal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * User: Petr Přikryl
 * Date: 15.12.13
 * Time: 13:27
 *
 * Trida pro reprezentaci spolecnosti.
 */

/*
 CREATE TABLE PDB_COMPANY (
 ID VARCHAR(64),
 NAME VARCHAR(100),
 VALID_FROM NUMBER(20),
 VALID_TO NUMBER(20)
 );
 */
public class CompanyEntity implements TemporalEntity<Long, Date, Date> {

    public static String TABLE="PDB_COMPANY";

    public Long id;

    public String name;

    public Date validFrom;
    public Date validTo;

    public CompanyEntity() {
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
       this.validFrom = DateUtils.trim(validFrom);
    }

    @Override
    public void setValidTo(Date validTo) {
        this.validTo = DateUtils.trim(validTo);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyEntity clone(Utils utils) {
        CompanyEntity entity = new CompanyEntity();
        entity.setId(getId());
        entity.setName(getName());
        entity.setValidFrom( utils.daysToDate(utils.dateToDays(getValidFrom())) );
        entity.setValidTo( utils.daysToDate(utils.dateToDays(getValidTo())) );
        return entity;
    }

    @Override
    public String toString() {
        SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");
        return dtf.format(getValidFrom()) +" - "+ dtf.format(getValidTo()) +" | "+ getName();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CompanyEntity)) {
            return false;
        }
        if ( ((CompanyEntity) o).getName().equals(getName()) &&
                ((CompanyEntity) o).getId().longValue() == getId().longValue() &&
                ((CompanyEntity) o).getValidFrom().getTime() == getValidFrom().getTime() &&
                ((CompanyEntity) o).getValidTo().getTime() == getValidTo().getTime() )
        {
            return true;
        }
        return false;
    }
}
