package cz.vutbr.fit.pdb.nichcz.model.spatial.test;

import cz.vutbr.fit.pdb.nichcz.model.Entity;

import java.util.UUID;

/**
 * User: Marek Salát
 * Date: 3.12.13
 * Time: 8:30
 */
public class Test implements Entity<Integer> {
    public static final String TABLE = "TABLE_TEST";

    public Integer id;
    public int c1;
    public String c2 ;

    public Test() {
        this.id = Long.valueOf(UUID.randomUUID().getMostSignificantBits()).intValue();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getTable() {
        return this.TABLE;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getC1() {
        return c1;
    }

    public void setC1(int c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", c1='" + c1 + '\'' +
                ", c2='" + c2 + '\'' +
                '}';
    }
}
