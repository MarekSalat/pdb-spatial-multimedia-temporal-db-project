package cz.vutbr.fit.pdb.nichcz.services;

/**
 * User: Marek Salát
 * Date: 7.10.13
 * Time: 16:30
 *
 * Rozhrani nahrani baliku sluzeb.
 */
public interface ServiceContainerLoader {
    /**
     * Nahraje balik sluzeb.
     * @return Vraci balik sluzeb.
     */
    public ServiceContainer load();
}
