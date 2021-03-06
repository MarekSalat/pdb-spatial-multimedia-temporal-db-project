package cz.vutbr.fit.pdb.nichcz.services.impl;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.services.Service;
import cz.vutbr.fit.pdb.nichcz.services.ServiceCreatingException;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: Marek Salát
 * Date: 3.12.13
 * Time: 15:03
 *
 * Trida sluzby spojeni s databazi.
 */
public class ConnectionService implements Service {

    OracleDataSource ods = null;
    Connection conn = null;
    private Context ctx;

    /**
     * Vytvori nove ConnectionService.
     * @param ctx Kontext s pripojenim na databazi.
     */
    public ConnectionService(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Ziska spojeni s databazi.
     * @return Vraci spojeni.
     */
    public Connection getConnection(){
        if(conn != null) return conn;

        if(!ctx.isUserLogged())
            throw new RuntimeException("Connection can not be create, because required credential has not been filled yet.");


        try {
            ods = new OracleDataSource();
            ods.setURL("jdbc:oracle:thin:@berta.fit.vutbr.cz:1522:dbfit");
            ods.setUser(ctx.setting.user.name);
            ods.setPassword(ctx.setting.user.password);
            conn = ods.getConnection();
        } catch (SQLException e) {
            throw new ServiceCreatingException("Service getConnection exception.",
                    new CreateDBconnectionException("Connection can not be established", e)
            );
        }

        return conn;
    }

    @Override
    public void close() throws Exception {
        if(conn == null) return;
        conn.close();
    }
}
