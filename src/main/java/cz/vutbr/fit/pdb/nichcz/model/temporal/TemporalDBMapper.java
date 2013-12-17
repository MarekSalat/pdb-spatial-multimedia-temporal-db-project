package cz.vutbr.fit.pdb.nichcz.model.temporal;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.AbstractDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.Entity;
import cz.vutbr.fit.pdb.nichcz.model.Mapper;
import cz.vutbr.fit.pdb.nichcz.services.impl.ConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public TemporalDBMapper(Context ctx) {
        this.ctx = ctx;
        this.connectionService = (ConnectionService) ctx.services.get("connection");
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
        Utils.MATCH_TYPE match;
        String where_ = " ID = '" + entity.getId().toString()+"'";

        for (E e : findWhere(where_)) {
            match = utils.findMatch(e.getValidFrom(), e.getValidTo(), (Date) from, (Date) to);

            System.out.println(match);

            if (match == Utils.MATCH_TYPE.INSIDE) {
                delete(e);
            }
            else if (match == Utils.MATCH_TYPE.LEFT_OVERLAP) {
                delete(e);

                e.setValidTo((Date) from);
                create(e);
            }
            else if (match == Utils.MATCH_TYPE.RIGHT_OVERLAP) {
                delete(e);
                e.setValidFrom((Date) to);
                create(e);
            }
            else if (match == Utils.MATCH_TYPE.CONTAINS) {
                delete(e);
                E entityClone = (E) e.clone(utils);

                e.setValidTo((Date) from);
                create(e);
                entityClone.setValidFrom((Date) to);
                create(entityClone);
            }

        }
    }

    public void delete(E e) {
        String where = " AND VALID_FROM = " + utils.dateToDays(e.getValidFrom()) + " AND VALID_TO = " + utils.dateToDays(e.getValidTo());
        try ( PreparedStatement stmt = getConnection().prepareStatement("delete from " + e.getTable() + " where id = ? "+where)){
            stmt.setString(1, String.valueOf(e.getId()));
            stmt.execute();
        } catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }
    }

    @Override
    public void save(E entity) {
        Utils.MATCH_TYPE match;
        String where_ = " ID = '" + entity.getId().toString()+"'";

        for (E e : findWhere(where_)) {
            match = utils.findMatch(e.getValidFrom(), e.getValidTo(), entity.getValidFrom(), entity.getValidTo());

            System.out.println(match);

            if (match == Utils.MATCH_TYPE.INSIDE) {
                delete(e);
            }
            else if (match == Utils.MATCH_TYPE.LEFT_OVERLAP) {
                delete(e);

                e.setValidTo(entity.getValidFrom());
                create(e);
            }
            else if (match == Utils.MATCH_TYPE.RIGHT_OVERLAP) {
                delete(e);
                e.setValidFrom(entity.getValidTo());
                create(e);
            }
            else if (match == Utils.MATCH_TYPE.CONTAINS) {
                delete(e);
                E entityClone = (E) e.clone(utils);

                e.setValidTo(entity.getValidFrom());
                create(e);
                entityClone.setValidFrom(entity.getValidTo());
                create(entityClone);
            }

        }

        create(entity);
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
