package cz.vutbr.fit.pdb.nichcz.model.spatial.test;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.model.Mapper;
import cz.vutbr.fit.pdb.nichcz.services.impl.ConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * User: Marek Salát
 * Date: 3.12.13
 * Time: 11:50

 TestDBMapper testDBMapper = new TestDBMapper(context);

 Test e = testDBMapper.findById(Integer.valueOf("1"));
 System.out.println(e);

 e.setC2("bla bla + " + (Math.random()*1000));
 e.setC1((int) (Math.random()*1000));
 testDBMapper.save(e);

 e = testDBMapper.findById(Integer.valueOf("1"));
 System.out.println(e);

 Test e2= testDBMapper.create();
 e2.setC2("new " + (Math.random()*1000));
 e2.setC1((int) (Math.random()*1000));
 testDBMapper.save(e2);
 System.out.println(e2);
 e2 = testDBMapper.findById(e2.getId());
 System.out.println(e2);

 e2 = testDBMapper.findById(42);  // null
 System.out.println(e2);

 */
public class TestDBMapper implements Mapper<Test, Integer> {
    protected Context ctx;

    protected ConnectionService connectionService;

    public TestDBMapper(Context ctx) {
        this.ctx = ctx;
        this.connectionService = (ConnectionService) ctx.services.get("connection");
    }

    private Connection con;
    protected Connection connection(){
        if(con == null)
            con = connectionService.getConnection();
        return con;
    }

    @Override
    public Test create() {
        Test entity = new Test();

        entity.id = Long.valueOf(UUID.randomUUID().getMostSignificantBits()).intValue();

        try ( PreparedStatement stmt = connection()
                .prepareStatement("insert into " + Test.TABLE + " (id, c1, c2) VALUES ( ?, ?, ? )");
        ){
            stmt.setInt(1, entity.getId());
            stmt.setInt(2, entity.getC1());
            stmt.setString(3, entity.getC2());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }

        return entity;
    }

    @Override
    public void save(Test entity) {
        try ( PreparedStatement stmt = connection()
                .prepareStatement("update " + Test.TABLE + " set c1=?, c2=? where id=?");
        ){
            stmt.setInt( 1, entity.getC1() );
            stmt.setString(2, entity.getC2() );
            stmt.setInt(3, entity.getId().intValue() );
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Test entity) {
        try ( PreparedStatement stmt = connection().prepareStatement("delete from " + Test.TABLE + " where id = ?")){
            stmt.setInt(1, entity.getId());
            stmt.execute();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Test findById(Integer id) {
        Iterator<Test> iterator = findWhere("id=" + id).iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public List<Test> findAll() {
        return findWhere("");
    }

    @Override
    public List<Test> findWhere(String where) {
        if(!where.isEmpty()) where = "where " + where;

        List<Test> res = new ArrayList<>();
        try (
            ResultSet rset = connection().prepareStatement("select * from " + Test.TABLE + " " + where).executeQuery();
        ){
            while (rset.next()) {
                Test e = new Test();
                e.id = rset.getInt("id");
                e.c1 = rset.getInt("c1");
                e.c2 = rset.getString("c2");

                res.add(e);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }

        return res;
    }
}
