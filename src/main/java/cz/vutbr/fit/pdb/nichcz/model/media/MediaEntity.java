package cz.vutbr.fit.pdb.nichcz.model.media;

import cz.vutbr.fit.pdb.nichcz.model.Entity;
import oracle.ord.im.OrdImage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 6.12.13
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class MediaEntity implements Entity<Long> {
    public static String TABLE="PDB_MEDIA";

    public Long id; // in database is mapped to string, because oracle does not know how to store long
    public String name;

    public OrdImage imgProxy;

    public boolean nameChanged = false;


    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrdImage getImgProxy() {
        return imgProxy;
    }

    public void setImgProxy(OrdImage imgProxy) {
        this.imgProxy = imgProxy;
    }

    public boolean isNameChanged() {
        return nameChanged;
    }

    public void setNameChanged(boolean nameChanged) {
        this.nameChanged = nameChanged;
    }

    public MediaEntity() {
        this.id = UUID.randomUUID().getMostSignificantBits();
    }



    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getTable() {
        return this.TABLE;
    }
}
