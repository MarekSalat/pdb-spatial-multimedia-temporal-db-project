package cz.vutbr.fit.pdb.nichcz.setting;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 21:11
 *
 * Rozhrani pro nastaveni aplikace.
 */
public interface SettingLoader {
    /**
     * Nahraje nastaveni.
     * @return Vraci nastaveni.
     */
    public Setting load();

    /**
     * Ulozi nastaveni.
     */
    public void save();
}
