package cz.vutbr.fit.pdb.nichcz.model.media;

import cz.vutbr.fit.pdb.nichcz.model.Entity;
import oracle.ord.im.OrdImage;

import java.util.UUID;

/**
 * User: Michal Pracuch
 * Date: 6.12.13
 * Time: 16:24
 *
 * Trida popisujici reprezentujici entity z databaze.
 */

/*
 DROP TABLE PDB_MEDIA;

 CREATE TABLE PDB_MEDIA
 (
     ID VARCHAR2(64) PRIMARY KEY NOT NULL,
     NAME VARCHAR2(64),
     PHOTO ORDSYS.ORDImage,
     PHOTO_SI ORDSYS.SI_StillImage,
     PHOTO_AC ORDSYS.SI_AverageColor,
     PHOTO_CH ORDSYS.SI_ColorHistogram,
     PHOTO_PC ORDSYS.SI_PositionalColor,
     PHOTO_TX ORDSYS.SI_Texture
 );

 COMMIT;
 */

public class MediaEntity implements Entity<Long> {
    public static final String TABLE = "PDB_MEDIA";

    private Long id; // in database is mapped to string, because oracle does not know how to store long
    private String name;

    private OrdImage imgProxy;

    private boolean nameChanged = false;


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


    /**
     * Vytvori novou MediaEntity reprezentujici entitu z databaze.
     */
    public MediaEntity() {
        this.id = UUID.randomUUID().getMostSignificantBits();
    }


    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getTable() {
        return TABLE;
    }
}
