package cz.vutbr.fit.pdb.nichcz.model;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.services.impl.ConnectionService;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

/**
 * User: Marek Salát
 * Date: 3.12.13
 * Time: 18:51
 *
 * Trida slouzi ke komunikaci s databazi.
 */
public abstract class AbstractDBMapper<E extends Entity, PK> implements Mapper<E, PK> {
    private Context ctx;
    private ConnectionService connectionService;
    private Connection connection;

    /**
     * Vytvori novy AbstractDBMapper.
     * @param ctx Kontext s pripojenim na databazi.
     */
    public AbstractDBMapper(Context ctx) {
        this.ctx = ctx;
        this.connectionService = (ConnectionService) ctx.services.get("connection");
    }

    @Override
    public E findById(PK id) {
        Iterator<E> iterator = findWhere("id=" + id).iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public List<E> findAll() {
        return findWhere("");
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
