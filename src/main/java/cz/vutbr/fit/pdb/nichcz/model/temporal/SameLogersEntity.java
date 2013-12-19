package cz.vutbr.fit.pdb.nichcz.model.temporal;

import java.util.Date;

/**
 * User: Petr Přikryl
 * Date: 19.12.13
 * Time: 0:03
 *
 * Trida pro reprezentaci spolecnosti, ktere tezi ve stejne casove periode ve stejne oblasti.
 */
public class SameLogersEntity {

    String loggingArea;
    String FirstCompany;
    String SecondCompany;
    Integer FirstLoggsPerDay;
    Integer SecondLoggsPerDay;
    Date FirstValidFrom;
    Date SecondValidFrom;
    Date FirstValidTo;
    Date SecondValidTo;

    public String getLoggingArea() {
        return loggingArea;
    }

    public void setLoggingArea(String loggingArea) {
        this.loggingArea = loggingArea;
    }

    public String getFirstCompany() {
        return FirstCompany;
    }

    public void setFirstCompany(String firstCompany) {
        FirstCompany = firstCompany;
    }

    public String getSecondCompany() {
        return SecondCompany;
    }

    public void setSecondCompany(String secondCompany) {
        SecondCompany = secondCompany;
    }

    public Integer getFirstLoggsPerDay() {
        return FirstLoggsPerDay;
    }

    public void setFirstLoggsPerDay(Integer firstLoggsPerDay) {
        FirstLoggsPerDay = firstLoggsPerDay;
    }

    public Integer getSecondLoggsPerDay() {
        return SecondLoggsPerDay;
    }

    public void setSecondLoggsPerDay(Integer secondLoggsPerDay) {
        SecondLoggsPerDay = secondLoggsPerDay;
    }

    public Date getFirstValidFrom() {
        return FirstValidFrom;
    }

    public void setFirstValidFrom(Date firstValidFrom) {
        FirstValidFrom = firstValidFrom;
    }

    public Date getSecondValidFrom() {
        return SecondValidFrom;
    }

    public void setSecondValidFrom(Date secondValidFrom) {
        SecondValidFrom = secondValidFrom;
    }

    public Date getFirstValidTo() {
        return FirstValidTo;
    }

    public void setFirstValidTo(Date firstValidTo) {
        FirstValidTo = firstValidTo;
    }

    public Date getSecondValidTo() {
        return SecondValidTo;
    }

    public void setSecondValidTo(Date secondValidTo) {
        SecondValidTo = secondValidTo;
    }
}
