package cz.vutbr.fit.pdb.nichcz.services;


/**
 * User: Marek Salát
 * Date: 7.10.13
 * Time: 16:52
 */
public class MockService implements Service{


    int created = 0;
    boolean closed = false;

    public MockService(){
        created++;
    }

    public int doSomething(){
        // I am doing something. I am answering question to the answer to life the universe and everything
        return 42;
    }

    @Override
    public void close() {
        // Towel is not needed anymore.
        closed = true;
    }
}