package cz.vutbr.fit.pdb.nichcz.services;

import java.util.Map;
import java.util.TreeMap;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 22:38
 *
 * Trida baliku sluzeb.
 */
public class ServiceContainer implements Service{

    private Map<String, ServiceFactory> registered = new TreeMap<>();
    private Map<String, Service> created = new TreeMap<>();

    /**
     * Zjisti, zda je v baliku sluzba.
     * @param name Jmeno sluzby.
     * @return Vrati, zda je v baliku sluzba.
     */
    public boolean hasServices(String name){
        return registered.containsKey(name) ;
    }

    /**
     * Zjisti, zda je v baliku sluzba vatvorena.
     * @param name Jmeno sluzby.
     * @return Vrati, zda je v baliku sluzba vytvorena.
     */
    public boolean isCreated(String name){
        return created.containsKey(name);
    }

    /**
     * Prida sluzbu do baliku.
     * @param name Jmeno sluzby.
     * @param service Sluzba.
     */
    public void addService(String name, ServiceFactory service){
        if(hasServices(name))
            throw new IllegalStateException("Service with '" + name + "'name has been already registered.");

        this.registered.put(name, service);
    }

    /**
     * Ziska sluzbu.
     * @param name Jmeno sluzby.
     * @return Vrati sluzbu.
     */
    public Service get(String name) {
        if(isCreated(name))
            return created.get(name);

        if (!hasServices(name))
            throw new ServiceNotFoundException(name);

        synchronized (this){
            if(isCreated(name))
                return created.get(name);

            Service service = registered.get(name).create();
            created.put(name, service);
            return service;
        }
    }

    private boolean isClosed = false;
    @Override
    public void close() {
        if(isClosed) throw new ServiceCloseException("ServiceContainer cannot be closed twice.");

        isClosed = true;

        for(Map.Entry<String, Service> entry : created.entrySet()) {
            String name = entry.getKey();
            Service service = entry.getValue();

            try {
                service.close();
                System.out.println("Service " + name + " was closed successfully.");
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                throw new ServiceCloseException("Service can not be closed.", e);
            }
        }
    }
}
