package cz.vutbr.fit.pdb.nichcz.services;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 22:38
 */
public class ServiceContainer implements Service{

    private Map<String, ServiceFactory> registered = new TreeMap<String, ServiceFactory>();
    private Map<String, Service> created = new TreeMap<String, Service>();

    public boolean hasServices(String name){
        return registered.containsKey(name) ;
    }

    public boolean isCreated(String name){
        return created.containsKey(name);
    }

    public void addService(String name, ServiceFactory service){
        if(hasServices(name))
            throw new InvalidStateException("Service with '" + name + "'name has been already registered.");

        this.registered.put(name, service);
    }

    public Service get(String name){
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

    @Override
    public void close() throws IOException {
        for(Map.Entry<String, Service> entry : created.entrySet()) {
            String name = entry.getKey();
            Service service = entry.getValue();

            service.close();
        }
    }
}
