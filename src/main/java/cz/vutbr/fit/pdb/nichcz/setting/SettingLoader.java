package cz.vutbr.fit.pdb.nichcz.setting;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 21:11
 */
public interface SettingLoader {
    public Setting load();
    public void save();
}
