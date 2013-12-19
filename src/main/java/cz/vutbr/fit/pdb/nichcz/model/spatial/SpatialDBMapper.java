package cz.vutbr.fit.pdb.nichcz.model.spatial;

import cz.vutbr.fit.pdb.nichcz.Util;
import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.AbstractDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.temporal.Utils;
import oracle.spatial.geometry.JGeometry;

import java.awt.*;
import java.awt.geom.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Marek Salát
 * Date: 3.12.13
 * Time: 21:05
 */
public class SpatialDBMapper extends AbstractDBMapper<SpatialEntity, Long> {
    private static final int SRID = 0;
    private static final AffineTransform DEFAULT_AT = new AffineTransform();
    public Utils utils = new Utils();

    public SpatialDBMapper(Context ctx) {
        super(ctx);
    }

    @Override
    public SpatialEntity create() {
        SpatialEntity e = new SpatialEntity();
        e.setValidFrom(utils.getActualDate());
        e.setValidTo(utils.getInfinity());
        e.setCreated(new Date());

        Date d = new Date();

        try ( PreparedStatement stmt = getConnection()
            .prepareStatement("insert into " + e.getTable() +
                "       (id, object_type,valid_from,valid_to,created) " +
                "VALUES (?,  ?,          ?,         ?,       ?)");
        ){
            int i=1;
            stmt.setString(i++, String.valueOf(e.getId()));
            stmt.setString(i++, String.valueOf(e.getObjectType()));
            stmt.setLong(i++, utils.dateToDays(e.getValidFrom()));
            stmt.setLong(i++, utils.dateToDays(e.getValidTo()));
            stmt.setDate(i++, new java.sql.Date(e.getCreated().getTime()) );

            stmt.executeUpdate();
        } catch (SQLException ex) {ex.printStackTrace();  throw new RuntimeException(ex); }

        return e;
    }

    public void insert(SpatialEntity e) {
        e.setCreated(new Date());

        try ( PreparedStatement stmt = getConnection()
                .prepareStatement("insert into " + e.getTable() +
                        "       (id, object_type,category,geometry,name,admin,owner,note,valid_from,valid_to,created,modified) " +
                        "VALUES (?,  ?,          ?,       ?,       ?,    ?,   ?,    ?,   ?,         ?,       ?,      ?)");
        ){
            int i=1;
            stmt.setString(i++, String.valueOf(e.getId()));
            stmt.setString(i++, String.valueOf(e.getObjectType()));
            stmt.setString(i++, String.valueOf(e.getCategory()));
            JGeometry jGeometry = shape2jGeometry(e.getGeometry(), e.getObjectType());
            stmt.setObject(i++,  jGeometry != null ? JGeometry.store(getConnection(), jGeometry) : null);
            stmt.setString(i++, e.getName());
            stmt.setString(i++, e.getAdmin());
            stmt.setString(i++, e.getOwner());
            stmt.setString(i++, e.getNote());

            stmt.setLong(i++, utils.dateToDays(e.getValidFrom()));
            stmt.setLong(i++, utils.dateToDays(e.getValidTo()));
            stmt.setDate(i++, new java.sql.Date(e.getCreated().getTime()) );
            stmt.setDate(i++, new java.sql.Date(e.getModified().getTime()) );

            stmt.executeUpdate();
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    @Override
    public void save(SpatialEntity e) {

        temporalUpdate(e);

    }

    @Override
    public void delete(SpatialEntity e) {
        String where = " AND VALID_FROM = " + utils.dateToDays(e.getValidFrom()) + " AND VALID_TO = " + utils.dateToDays(e.getValidTo());
        try ( PreparedStatement stmt = getConnection().prepareStatement("delete from " + e.getTable() + " where id = ? "+where)){
            stmt.setString(1, String.valueOf(e.getId()));
            stmt.execute();
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    public List<SpatialEntity> findBy(SpatialEntity.TYPE t, String where){
        return findWhere("object_type='"+t+"' " + where);
    }

    @Override
    public List<SpatialEntity> findWhere(String where) {
        if(!where.isEmpty()) where = "where " + where;

        List<SpatialEntity> res = new ArrayList<>();
        try (
            ResultSet rset = getConnection().prepareStatement("select * from " + SpatialEntity.TABLE + " " + where).executeQuery();
        ){
            while (rset.next()) {
                SpatialEntity e = new SpatialEntity();
                resultSetToEntity(rset, e);
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }

    private void resultSetToEntity(ResultSet rset, SpatialEntity e) throws Exception {
        e.setId(Long.valueOf(rset.getString("id")));
        e.setObjectType(SpatialEntity.TYPE.valueOf(rset.getString("object_type").toUpperCase()));
        e.setCategory(rset.getString("category"));

        byte[] image = rset.getBytes("geometry");
        JGeometry jGeometry = JGeometry.load(image);
        e.setGeometry( jGeometry2shape(jGeometry) );

        e.setName(rset.getString("name"));
        e.setAdmin(rset.getString("admin"));
        e.setOwner(rset.getString("owner"));
        e.setNote(rset.getString("note"));

        e.setValidFrom(utils.daysToDate(rset.getLong("valid_from")));
        e.setValidTo(utils.daysToDate(rset.getLong("valid_to")));
        e.setCreated(rset.getDate("created"));
        e.setModified(rset.getDate("modified"));
    }

    public void temporalDelete(SpatialEntity e, Date from, Date to) {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        Utils.MATCH_TYPE match;
        String where_ = " ID = '" + e.getId().toString()+"'";

        for (SpatialEntity entity : findWhere(where_)) {
            match = utils.findMatch(entity.getValidFrom(), entity.getValidTo(), from, to);

            if (match == Utils.MATCH_TYPE.INSIDE) {
                delete(entity);
            }
            else if (match == Utils.MATCH_TYPE.LEFT_OVERLAP) {
                delete(entity);

                entity.setValidTo(from);
                insert(entity);
            }
            else if (match == Utils.MATCH_TYPE.RIGHT_OVERLAP) {
                delete(entity);
                entity.setValidFrom(to);
                insert(entity);
            }
            else if (match == Utils.MATCH_TYPE.CONTAINS) {
                delete(entity);
                SpatialEntity entityClone = entity.clone(utils);

                entity.setValidTo(from);
                insert(entity);
                entityClone.setValidFrom(to);
                insert(entityClone);
            }

        }

        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}
    }

    public void temporalUpdate(SpatialEntity e) {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        Utils.MATCH_TYPE match;
        String where_ = " ID = '" + e.getId().toString()+"'";

        for (SpatialEntity entity : findWhere(where_)) {
            match = utils.findMatch(entity.getValidFrom(), entity.getValidTo(), e.getValidFrom(), e.getValidTo());

            if (match == Utils.MATCH_TYPE.INSIDE) {
                delete(entity);
            }
            else if (match == Utils.MATCH_TYPE.LEFT_OVERLAP) {
                delete(entity);

                entity.setValidTo(e.getValidFrom());
                insert(entity);
            }
            else if (match == Utils.MATCH_TYPE.RIGHT_OVERLAP) {
                delete(entity);
                entity.setValidFrom(e.getValidTo());
                insert(entity);
            }
            else if (match == Utils.MATCH_TYPE.CONTAINS) {
                delete(entity);
                SpatialEntity entityClone = entity.clone(utils);

                entity.setValidTo(e.getValidFrom());
                insert(entity);
                entityClone.setValidFrom(e.getValidTo());
                insert(entityClone);
            }

        }

        e.setModified(new Date());
        insert(e);

        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}
    }

    public void ordinaryUpdate(SpatialEntity e, String where) {

        try ( PreparedStatement stmt = getConnection()
                .prepareStatement("update " + e.getTable() + " set " +
                        "object_type=?, category=?,geometry=?,name=?,admin=?,owner=?,note=?,valid_from=?,valid_to=?,modified=? where id=? " + where)
        ){
            int i=1;
            stmt.setString(i++, String.valueOf(e.getObjectType()));
            stmt.setString(i++, e.getCategory());
            JGeometry jGeometry = shape2jGeometry(e.getGeometry(), e.getObjectType());
            stmt.setObject(i++,  jGeometry != null ? JGeometry.store(getConnection(), jGeometry) : null);
            stmt.setString(i++, e.getName());
            stmt.setString(i++, e.getAdmin());
            stmt.setString(i++, e.getOwner());
            stmt.setString(i++, e.getNote());

            Date validFrom = e.getValidFrom();
            stmt.setLong(i++, validFrom != null ? utils.dateToDays(validFrom) : null);

            Date validTo = e.getValidTo();
            stmt.setLong(i++, validTo != null ? utils.dateToDays(validTo) : null);

            e.setModified(new Date());
            stmt.setDate(i++, new java.sql.Date(e.getModified().getTime()));

            stmt.setString(i++, String.valueOf(e.getId()));
            stmt.executeUpdate();
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }

    }

    public List<SpatialEntity> selectValidFuse(String where, String fuseBy) {

        List<SpatialEntity> res = new ArrayList<>();
        String order = " order by ID asc, VALID_FROM asc, VALID_TO asc";
        try (
                ResultSet rset = getConnection().prepareStatement("select * from " + SpatialEntity.TABLE + " " + where + order).executeQuery();
        ){
            while (rset.next()) {
                SpatialEntity e = new SpatialEntity();
                resultSetToEntity(rset, e);
                res.add(e);
            }
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        List<SpatialEntity> res2 = new ArrayList<>();
        SpatialEntity en1 = null, en2;
        Integer size = res.size();

        for (Integer i = 0, e = 1; e < size; i++, e++) {
            en1 = res.get(i);
            en2 = res.get(e);

            if (en1.getId().longValue() == en2.getId().longValue()
                    && en1.getValidTo().compareTo(en2.getValidFrom()) == 0) {
                en2.setValidFrom(en1.getValidFrom());
            }
            else {
                res2.add(en1);

            }
            en1 = null;
        }

        if (en1 == null && size > 0) {
            res2.add(res.get(size - 1));
        }

        return res2;
    }

    public double getGeometryArea(SpatialEntity entity){
        try (
                PreparedStatement stmt = getConnection().prepareStatement("select SDO_GEOM.SDO_AREA(geometry, 1, '') as area from PDB_SPATIAL where id=?");
        ){
            stmt.setString(1, String.valueOf(entity.getId()));
            ResultSet rst = stmt.executeQuery();
            rst.next();
            return rst.getDouble("area");

        } catch (SQLException e) {
            e.printStackTrace(); throw new RuntimeException(e);
        }
    }

    public double getGeometryLenght(SpatialEntity entity){
        try (
                PreparedStatement stmt = getConnection().prepareStatement("select SDO_GEOM.SDO_LENGTH(geometry, 1, '') as area from PDB_SPATIAL where id=?");
        ){
            stmt.setString(1, String.valueOf(entity.getId()));
            ResultSet rst = stmt.executeQuery();
            rst.next();
            return rst.getDouble("area");

        } catch (SQLException e) {
            e.printStackTrace(); throw new RuntimeException(e);
        }
    }

    public double getGeometriesShortestDistance(SpatialEntity e1, SpatialEntity e2){
        try (
            PreparedStatement stmt = getConnection().prepareStatement(
                "select SDO_GEOM.SDO_DISTANCE(g1.geometry, g2.geometry, 1, '') as area from PDB_SPATIAL g1, pdb_spatial g2 where g1.id=? and g2.id=?"
            );
        ){
            stmt.setString(1, String.valueOf(e1.getId()));
            stmt.setString(2, String.valueOf(e2.getId()));
            ResultSet rst = stmt.executeQuery();
            rst.next();
            return rst.getDouble("area");

        } catch (SQLException e) { e.printStackTrace(); throw new RuntimeException(e); }
    }

    public double getTotalAreaOf(SpatialEntity.TYPE t){
        try (
            PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT SDO_GEOM.SDO_AREA(SDO_AGGR_UNION(SDO_GEOM.SDOAGGRTYPE(geometry, 1)), 1) area FROM PDB_SPATIAL WHERE OBJECT_TYPE = ?"
            );
        ){
            stmt.setString(1, String.valueOf(t.toString()));
            ResultSet rst = stmt.executeQuery();
            rst.next();
            return rst.getDouble("area");
        } catch (SQLException e) { e.printStackTrace(); throw new RuntimeException(e); }
    }

    public double getTotalLengthOf(SpatialEntity.TYPE t){
        try (
            PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT SDO_GEOM.SDO_LENGTH(SDO_AGGR_UNION(SDO_GEOM.SDOAGGRTYPE(geometry, 1)), 1) area FROM PDB_SPATIAL WHERE OBJECT_TYPE = ?"
            );
        ){
            stmt.setString(1, String.valueOf(t.toString()));
            ResultSet rst = stmt.executeQuery();
            rst.next();
            return rst.getDouble("area");
        } catch (SQLException e) { e.printStackTrace(); throw new RuntimeException(e); }
    }

    public SpatialEntity getBiggestAreaEntity(SpatialEntity.TYPE t){
        try (
            PreparedStatement stmt= getConnection().prepareStatement(
                "SELECT * " +
                "FROM pdb_spatial img " +
                "WHERE (img.OBJECT_TYPE = ?) AND NOT EXISTS ( " +
                "    SELECT 1 FROM PDB_SPATIAL cmp " +
                "    WHERE (cmp.object_type = ?) AND " +
                "          (SDO_GEOM.SDO_AREA(cmp.geometry, 1) > SDO_GEOM.SDO_AREA(img.geometry, 1)) AND " +
                "          (img.id <> cmp.id) " +
                ")"
            );
        ){
            stmt.setString(1, t.toString());
            stmt.setString(2, t.toString());
            ResultSet rset = stmt.executeQuery();
            rset.next();
            SpatialEntity e = new SpatialEntity();
            resultSetToEntity(rset, e);

            return  e;
        } catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}
    }

    public static Shape jGeometry2shape(JGeometry jGeometry) {
        if(jGeometry == null) return null;

        Shape shape;
        switch (jGeometry.getType()) {
            case JGeometry.GTYPE_MULTIPOLYGON:
            case JGeometry.GTYPE_POLYGON:

            case JGeometry.GTYPE_MULTICURVE:
            case JGeometry.GTYPE_CURVE:

                shape = jGeometry.createDoubleShape();
                break;
            case JGeometry.GTYPE_POINT:
                Point2D point = jGeometry.getJavaPoint();
                shape = new Point2DShape(point.getX(), point.getY());
                break;
            default:
                throw new RuntimeException("Unsupported JGeometry type " + jGeometry.getType());
        }
        return shape;
    }

    public static JGeometry shape2jGeometry(Shape s, SpatialEntity.TYPE t){
        if(s == null || t == SpatialEntity.TYPE.UNKNOWN) return null;
        switch (t){
            // polygons
            case FOREST:
            case WATER:
            case LOGGING_AREA:
            case FIELD:
                return polygon2jGeometry(s);

            // lines
            case TRACK:
            case STREAM:
                return path2jGeometry(new Path2D.Double(s));

            // circles
            case HUNTING_AREA:
                return ellipse2jGeometry((Ellipse2D) s);

            // points
            case VIEW:
            case FEEDING_RACK:
                if(s instanceof Ellipse2D){
                    Ellipse2D el = (Ellipse2D) s;
                    return point2jGeometry(new Point2DShape(el.getCenterX(), el.getCenterY()));
                }
                return point2jGeometry((Point2DShape) s);
            default:
                return shape2jGeometry(s);
        }
    }

    public static JGeometry shape2jGeometry(Shape s) {
        if(s == null) return null;

        if(s instanceof Point2DShape) return point2jGeometry((Point2DShape) s);
        if(s instanceof Ellipse2D) return ellipse2jGeometry((Ellipse2D) s);
        if(s instanceof Rectangle2D) return rectangle2jGeometry((Rectangle2D) s);
        if(s instanceof Path2D) return path2jGeometry((Path2D) s);

        return polygon2jGeometry(s);
    }

    public static JGeometry polygon2jGeometry(Shape shape){
        Path2D path = new Path2D.Double(shape);
        return JGeometry.createLinearPolygon(getPathPointArray(path), 2, SRID);
    }

    public static JGeometry path2jGeometry(Path2D path){
        return JGeometry.createLinearMultiLineString(getPathPointArray(path), 2, SRID);
    }

    private static double[][] getPathPointArray(Path2D path){
        PathIterator pathIterator = path.getPathIterator(DEFAULT_AT);
        List<List<Double>> lineList = new ArrayList<>();
        double [] segment = new double[6];

        List<Double> currLine = null;
        for (;!pathIterator.isDone(); pathIterator.next()){
            int type = pathIterator.currentSegment(segment);
            if(type == PathIterator.SEG_CLOSE) break;
            if(type == PathIterator.SEG_MOVETO) {
                currLine = new ArrayList<>();
                lineList.add(currLine);
            }
            currLine.add(segment[0]);
            currLine.add(segment[1]);
        }

        double [][] lines = new double[lineList.size()][];
        for (int i = 0, lineListSize = lineList.size(); i < lineListSize; i++) {
            List<Double> l = lineList.get(i);
            lines[i] = Util.convertDoubleListToPrimitive(l);
        }

        return lines;
    }

    public static JGeometry point2jGeometry(Point2DShape p){
        return point2jGeometry(new Point2D.Double(p.getX(), p.getY()));
    }

    public static JGeometry point2jGeometry(Point2D p){
        return new JGeometry(p.getX(), p.getY(), SRID);
    }

    public static JGeometry rectangle2jGeometry(Rectangle2D r){
        return  new JGeometry(r.getMinX(), r.getMinY(),   r.getMaxX(), r.getMaxY(), SRID);
    }

    // TODO: only circles are supported now
    public static JGeometry ellipse2jGeometry(Ellipse2D e){
        return JGeometry.createCircle(e.getCenterX(), e.getCenterY(), e.getWidth()/2, SRID);
    }


}

