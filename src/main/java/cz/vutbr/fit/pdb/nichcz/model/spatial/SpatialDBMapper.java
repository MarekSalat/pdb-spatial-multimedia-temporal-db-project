package cz.vutbr.fit.pdb.nichcz.model.spatial;

import cz.vutbr.fit.pdb.nichcz.Util;
import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.AbstractDBMapper;
import oracle.spatial.geometry.JGeometry;

import java.awt.*;
import java.awt.geom.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public SpatialDBMapper(Context ctx) {
        super(ctx);
    }

    @Override
    public SpatialEntity create() {
        SpatialEntity e = new SpatialEntity();
        e.setValidFrom(new Date());
        try ( PreparedStatement stmt = getConnection()
            .prepareStatement("insert into " + e.getTable() +
                "       (id, object_type,valid_from) " +
                "VALUES (?,  ?,          ?)");
        ){
            int i=1;
            stmt.setString(i++, String.valueOf(e.getId()));
            stmt.setString(i++, String.valueOf(e.getObjectType()));
            e.setValidFrom(new Date());
            stmt.setDate(i++, new java.sql.Date(e.getValidFrom().getTime()) );

            stmt.executeUpdate();
        } catch (SQLException ex) {ex.printStackTrace();  throw new RuntimeException(ex); }

        return e;
    }

    @Override
    public void save(SpatialEntity e) {
        e.setModified(new Date());

        try ( PreparedStatement stmt = getConnection()
            .prepareStatement("update " + e.getTable() + " set " +
                    "object_type=?, category=?,geometry=?,name=?,admin=?,owner=?,note=?,valid_to=?,modified=? where id=?")
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

            Date validTo = e.getValidTo();
            stmt.setDate(i++, validTo != null ? new java.sql.Date(validTo.getTime()) : null);

            e.setModified(new Date());
            stmt.setDate(i++, new java.sql.Date(e.getModified().getTime()));

            stmt.setString(i++, String.valueOf(e.getId()));
            stmt.executeUpdate();
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    @Override
    public void delete(SpatialEntity e) {
        try ( PreparedStatement stmt = getConnection().prepareStatement("delete from " + e.getTable() + " where id = ?")){
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

                e.setValidFrom(rset.getDate("valid_from"));
                e.setValidTo(rset.getDate("valid_to"));
                e.setModified(rset.getDate("modified"));

                res.add(e);
            }
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
        catch (Exception ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        return res;
    }

    public static Shape jGeometry2shape(JGeometry jGeometry) {
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

