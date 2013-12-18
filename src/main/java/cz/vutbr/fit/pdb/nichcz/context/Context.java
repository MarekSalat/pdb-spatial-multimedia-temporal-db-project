package cz.vutbr.fit.pdb.nichcz.context;

import cz.vutbr.fit.pdb.nichcz.services.Service;
import cz.vutbr.fit.pdb.nichcz.services.ServiceContainer;
import cz.vutbr.fit.pdb.nichcz.services.ServiceContainerLoader;
import cz.vutbr.fit.pdb.nichcz.services.ServiceFactory;
import cz.vutbr.fit.pdb.nichcz.services.impl.ConnectionService;
import cz.vutbr.fit.pdb.nichcz.setting.Setting;
import cz.vutbr.fit.pdb.nichcz.setting.SettingLoader;

import java.util.logging.Logger;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 22:32
 *
 * Kontext aplikace, ve kterem je jeji nastaveni a sluzby pripojeni na databazi.
 */
public class Context {
    public Setting setting;
    public ServiceContainer services;
    public Logger log = Logger.getLogger("PDB");

    /**
     * Vytvori novy Context.
     * @return Vraci novy Context.
     */
    public Context load(){
        setting = new AppSettingLoader().load();
        services = new AppServiceContainerLoader().load();
        return this;
    }

    /**
     * Uzavre spojeni sluzeb.
     */
    public void close() {
        this.services.close();
        System.out.println("Context was closed successfully.");
    }

    /**
     * Kontroluje, zda je uzivatel prihlasen.
     * @return Vraci, zda je uzivatel prihlasen.
     */
    public boolean isUserLogged(){
        return setting.user.logged;
    }

    private class AppSettingLoader implements SettingLoader {
        @Override
        public Setting load() {
            Setting s = new Setting();
            // ... initialization
            return s;
        }

        @Override
        public void save() {
            // pass; not implemented yet
        }
    }

    private class AppServiceContainerLoader implements ServiceContainerLoader {

        @Override
        public ServiceContainer load() {
            ServiceContainer sc = new ServiceContainer();
            // ... initialization
            // sc.addService("xxx", new XXXServiceFactory());
            // ...

            sc.addService("connection", new ServiceFactory() {
                @Override
                public Service create() {
                    return new ConnectionService(Context.this);
                }
            });

            return sc;
        }
    }
}
