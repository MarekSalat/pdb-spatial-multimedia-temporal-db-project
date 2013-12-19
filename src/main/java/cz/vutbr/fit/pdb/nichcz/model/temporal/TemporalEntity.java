package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.model.Entity;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 15.12.13
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
public interface TemporalEntity<ID, VALID_FROM, VALID_TO> extends Entity {
    public Date getValidFrom();
    public Date getValidTo();
    public void setValidFrom(Date validFrom);
    public void setValidTo(Date validTo);
    public TemporalEntity<ID, VALID_FROM, VALID_TO> clone(Utils utils);
}
