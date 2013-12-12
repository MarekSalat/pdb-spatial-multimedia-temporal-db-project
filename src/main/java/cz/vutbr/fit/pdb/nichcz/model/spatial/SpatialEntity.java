package cz.vutbr.fit.pdb.nichcz.model.spatial;

import cz.vutbr.fit.pdb.nichcz.model.Entity;

import java.awt.*;
import java.awt.geom.Area;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * User: Marek Salát
 * Date: 3.12.13
 * Time: 18:03
 *

 DROP TABLE PDB_SPATIAL
 CREATE TABLE PDB_SPATIAL
 (
     ID VARCHAR2(64) PRIMARY KEY NOT NULL,
     OBJECT_TYPE VARCHAR2(16),
     CATEGORY nvarchar2(32),
     GEOMETRY varchar2(4000),
     NAME nvarchar2(32),
     ADMIN nvarchar2(32),
     OWNER nvarchar2(32),
     NOTE nvarchar2(512),
     VALID_FROM DATE NOT NULL,
     VALID_TO DATE,
     MODIFIED DATE,
     SPECIAL_FIELD INTEGER
 );

 DELETE FROM USER_SDO_GEOM_METADATA WHERE
 TABLE_NAME = 'PDB_SPATIAL' AND COLUMN_NAME = 'GEOMETRY';

 INSERT INTO USER_SDO_GEOM_METADATA VALUES (
    'PDB_SPATIAL', 'GEOMETRY',
    SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 2000, 1), SDO_DIM_ELEMENT('Y', 0, 2000, 1)),
    NULL
 );

 DROP INDEX pdb_spatial_geometry_sidx;
 CREATE INDEX pdb_spatial_geometry_sidx ON PDB_SPATIAL(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

 *
 */
public class SpatialEntity implements Entity<Long>{
    public static String TABLE="PDB_SPATIAL";

    public enum TYPE {
        UNKNOWN,

        // polygons
        FOREST,
        WATER,
        LOGGING_AREA,
        FIELD,

        // lines
        TRACK,
        STREAM,

        // circles
        HUNTING_AREA,

        // points
        VIEW,
        FEEDING_RACK
    }

    public Long id; // in database is mapped to string, because oracle does not know how to store long

    public TYPE objectType = TYPE.UNKNOWN;
    public String category;
    public Shape geometry;

    public String name;
    public String admin;
    public String owner;
    public String note;

    public Date validFrom;
    public Date validTo;
    public Date modified;

    public double specialField;

    public SpatialEntity() {
        id = UUID.randomUUID().getMostSignificantBits();
        validFrom = new Date();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTable() {
        return TABLE;
    }

    private Area area;
    @SuppressWarnings("UnusedDeclaration")
    public Area getArea(){
        if(area == null)
            area = new Area( geometry );
        return area;
    }

    // generated methods

    public void setId(Long id) {
        this.id = id;
    }

    public TYPE getObjectType() {
        return objectType;
    }

    public void setObjectType(TYPE objectType) {
        this.objectType = objectType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Shape getGeometry() {
        return geometry;
    }

    public void setGeometry(Shape geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @SuppressWarnings("UnusedDeclaration")
    public double getSpecialField() {
        return specialField;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSpecialField(double specialField) {
        this.specialField = specialField;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "SpatialEntity{" +
                "id=" + id +
                ", objectType=" + objectType +
                ", category='" + category + "'" +
                ", geometry=" + geometry +
                ", name='" + name + "'" +
                ", admin='" + admin + "'" +
                ", owner='" + owner + "'" +
                ", note='" + note + "'" +
                ", validFrom=" + (validFrom != null ? simpleDateFormat.format(validFrom) : "null") +
                ", validTo=" + (validTo != null ? simpleDateFormat.format(validTo)  : "null") +
                ", modified=" + (modified != null ? simpleDateFormat.format(modified)  : "null") +
                ", specialField=" + specialField +
                "}";
    }
}
