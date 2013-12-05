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
        FEEDING_RACK;
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
        return this.TABLE;
    }

    private Area area;
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

    public double getSpecialField() {
        return specialField;
    }

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
