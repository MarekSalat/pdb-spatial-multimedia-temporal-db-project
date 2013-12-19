package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.AbstractDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.Entity;
import cz.vutbr.fit.pdb.nichcz.model.Mapper;
import cz.vutbr.fit.pdb.nichcz.services.impl.ConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 16.12.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class TemporalDBMapper<E extends TemporalEntity, ID, VALID_FROM, VALID_TO> implements TemporalMapper<E, ID, VALID_FROM, VALID_TO> {

    private Context ctx;
    private ConnectionService connectionService;
    private Connection connection;
    protected Utils utils = new Utils();
    protected List<TemporalBound> bounds = new ArrayList<>();

    public TemporalDBMapper(Context ctx) {
        this.ctx = ctx;
        this.connectionService = (ConnectionService) ctx.services.get("connection");
    }

    public void addBound(TemporalBound bound) {
        bounds.add(bound);
    }

    @Override
    public E findByPk(ID id, VALID_FROM validFrom, VALID_TO validTo) {
        Iterator<E> iterator = findWhere("id=" + id + " AND valid_from=" + validFrom + " AND valid_to=" + validTo ).iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public List<E> findAll() {
        return findWhere("");
    }

    @Override
    public void delete(E entity, VALID_FROM from, VALID_TO to) {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        Utils.MATCH_TYPE match;
        String where_ = " ID = '" + entity.getId().toString()+"'";

        for (E e : findWhere(where_)) {
            match = utils.findMatch(e.getValidFrom(), e.getValidTo(), (Date) from, (Date) to);

            if (match == Utils.MATCH_TYPE.AFTER || match == Utils.MATCH_TYPE.BEFORE) {
                continue;
            }

            List<TemporalEntity> foreigns = getBounds(e);

            delete(e);

            if (bounds.get(0).type == TemporalBound.TYPE.PRIMARY_KEY) {
                // cascade delete constraint
                propagateDelete(foreigns, entity.getValidFrom(), entity.getValidTo());
            }
            else { // foreign key
                propagateSave(foreigns, entity.getValidFrom(), entity.getValidTo());
            }

            if (match == Utils.MATCH_TYPE.INSIDE) {

            }
            else if (match == Utils.MATCH_TYPE.LEFT_OVERLAP) {

                // old left
                e.setValidTo((Date) from);
                create(e);

            }
            else if (match == Utils.MATCH_TYPE.RIGHT_OVERLAP) {

                // old right
                e.setValidFrom((Date) to);
                create(e);

            }
            else if (match == Utils.MATCH_TYPE.CONTAINS) {

                E clone = (E) e.clone(utils);
                // renew left
                clone.setValidTo((Date) from);
                create(clone);
                // renew right
                clone.setValidFrom((Date) to);
                clone.setValidTo(e.getValidTo());
                create(clone);

            }

        }

        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}
    }

    public void delete(E e) {
        String where = " AND VALID_FROM = " + utils.dateToDays(e.getValidFrom()) + " AND VALID_TO = " + utils.dateToDays(e.getValidTo());
        try ( PreparedStatement stmt = getConnection().prepareStatement("delete from " + e.getTable() + " where id = ? "+where)){
            stmt.setString(1, String.valueOf(e.getId()));
            stmt.execute();
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    public List<TemporalEntity> getBounds(TemporalEntity e) {
        List result = new ArrayList<>();

        for (TemporalBound b : bounds) {
            String where = ( b.foreignColumn +"="+ e.getId() +
                    " AND valid_from=" + utils.dateToDays(e.getValidFrom()) +
                    " AND valid_to=" + utils.dateToDays(e.getValidTo()) );
            result.addAll(b.mapper.findWhere(where));
        }
        return result;
    }

    @Override
    public void save(E entity) {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

        Utils.MATCH_TYPE match;
        String where_ = " ID = '" + entity.getId().toString()+"'";

        Integer updates = 0;

        for (E e : findWhere(where_)) {
            match = utils.findMatch(e.getValidFrom(), e.getValidTo(), entity.getValidFrom(), entity.getValidTo());

            if (match == Utils.MATCH_TYPE.AFTER || match == Utils.MATCH_TYPE.BEFORE) {
                continue;
            }

            List<TemporalEntity> foreigns = getBounds(e);

            delete(e);

            propagateSave(foreigns, entity.getValidFrom(), entity.getValidTo());

            if (match == Utils.MATCH_TYPE.INSIDE) {

                E clone = (E) entity.clone(utils);

                clone.setValidFrom(e.getValidFrom());
                clone.setValidTo(e.getValidTo());

                // update mid intersection
                create(clone);

            }
            else if (match == Utils.MATCH_TYPE.LEFT_OVERLAP) {

                E clone = (E) entity.clone(utils);
                // update mid
                clone.setValidTo(e.getValidTo());
                create(clone);
                // old left
                e.setValidTo(entity.getValidFrom());
                create(e);
            }
            else if (match == Utils.MATCH_TYPE.RIGHT_OVERLAP) {

                E clone = (E) entity.clone(utils);
                // update mid
                clone.setValidFrom(e.getValidFrom());
                create(clone);
                // old right
                e.setValidFrom(entity.getValidTo());
                create(e);
            }
            else if (match == Utils.MATCH_TYPE.CONTAINS) {

                E clone = (E) e.clone(utils);
                // update mid
                create(entity);
                // old left
                clone.setValidTo(entity.getValidFrom());
                create(clone);
                // old right
                clone.setValidFrom(entity.getValidTo());
                clone.setValidTo(e.getValidTo());
                create(clone);

            }

            updates++;

        }

        if (updates == 0) {
            // AFTER AND BEFORE
            create(entity);
        }

        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex);}

    }

    public void propagateSave(List<TemporalEntity> foreigns, Date from, Date to) {
        for (TemporalEntity foreign : foreigns) {
            foreign.setValidFrom(from);
            foreign.setValidTo(to);
            bounds.get(0).mapper.save(foreign);
        }
    }

    public void propagateDelete(List<TemporalEntity> foreigns, Date from, Date to) {
        for (TemporalEntity foreign : foreigns) {
            bounds.get(0).mapper.delete(foreign, from, to);
        }
    }

    public Connection getConnection(){
        if(connection == null)
            connection = connectionService.getConnection();
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Context getContext() {
        return ctx;
    }

    public void setContext(Context ctx) {
        this.ctx = ctx;
    }
}
