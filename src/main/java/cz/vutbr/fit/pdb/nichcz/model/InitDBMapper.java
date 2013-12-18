package cz.vutbr.fit.pdb.nichcz.model;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.media.MediaDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.media.MediaEntity;
import cz.vutbr.fit.pdb.nichcz.services.impl.ConnectionService;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: Michal Pracuch
 * Date: 14.12.13
 * Time: 17:49
 *
 * Trida slouzi k inicializaci databaze.
 */
public class InitDBMapper {
    private Context ctx;
    private ConnectionService connectionService;
    private Connection connection;

    /**
     * Vytvori novy InitDBMapper
     * @param ctx Kontext s pripojenim na databazi.
     */
    public InitDBMapper(Context ctx) {
        this.ctx = ctx;
        this.connectionService = (ConnectionService) ctx.services.get("connection");
    }

    public Connection getConnection() {
        if (connection == null)
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


    /**
     * Inicializuje dabatabazi ze skriptu.
     * @return Vraci, zda inicializace probehla uspesne.
     */
    public boolean initDB() {
        System.out.println("initDB");

        boolean initOK = false;

        String s;
        StringBuilder sb = new StringBuilder();

        try {
            FileReader fr = new FileReader(new File(ClassLoader.getSystemResource("initDB.sql").toURI()));
            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();

            String[] inst = sb.toString().split(";");

            Statement stmt = getConnection().createStatement();

            for (int i = 0, instLength = inst.length; i < instLength; i++) {
                String anInst = inst[i];
                if (!anInst.trim().equals("")) {
                    System.out.println(i + ": " + anInst);
                    stmt.executeUpdate(anInst);
                }
            }

            initOK = true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
//            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
//            throw new RuntimeException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
//            throw new RuntimeException(ex);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return initOK;
    }

    /**
     * Nahraje obrazky do databaze.
     */
    public void loadMedia() {
        MediaDBMapper mapper = new MediaDBMapper(ctx);

        String[] fileNames = {
                "medved.jpg",
                "medved 2.jpg",
                "medved hnedy.jpg",
                "grizzly.jpg",
                "buk.jpg",
                "jedle.jpg",
                "jedle 2.jpg"
        };

        File file = null;

        try {
            boolean autoCommit = getConnection().getAutoCommit();
            getConnection().setAutoCommit(false);

            try {

                for (String fileName : fileNames) {
                    try {
                        file = new File(ClassLoader.getSystemResource(fileName).toURI());

                        System.out.println("Opening: " + file.getPath() + ".");

                        MediaEntity e = mapper.create();
                        e.setName(file.getName().substring(0, file.getName().lastIndexOf('.')));

                        mapper.loadImageFromFile(e, file.getPath());

                        mapper.save(e);
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }

//                    getConnection().commit();
                }

            } finally {
                getConnection().setAutoCommit(autoCommit);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
//            throw new RuntimeException(ex);
        }
    }
}
