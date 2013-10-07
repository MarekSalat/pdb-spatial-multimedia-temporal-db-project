package cz.vutbr.fit.pdb.nichcz.context;

import cz.vutbr.fit.pdb.nichcz.services.ServiceContainer;
import cz.vutbr.fit.pdb.nichcz.services.ServiceContainerLoader;
import cz.vutbr.fit.pdb.nichcz.setting.Setting;
import cz.vutbr.fit.pdb.nichcz.setting.SettingLoader;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 22:32
 */
public class Context {
    public Setting setting = new AppSettingLoader().load();
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

    public ServiceContainer services = new AppServiceContainerLoader().load();
    private class AppServiceContainerLoader implements ServiceContainerLoader {

        @Override
        public ServiceContainer load() {
            ServiceContainer sc = new ServiceContainer();
            // ... initialization
            // sc.addService("xxx", new XXXServiceFactory());
            // ...
            return sc;
        }
    }
}
