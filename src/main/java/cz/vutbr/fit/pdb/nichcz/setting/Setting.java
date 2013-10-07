package cz.vutbr.fit.pdb.nichcz.setting;

import java.io.Serializable;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 20:52
 */
public class Setting implements Serializable {
    public String user_name = "";
    public String user_password = "";
    public boolean user_logged = false;
    public boolean user_logInAutomaticaly = false;
    public boolean user_remeberCredentials = false;

    public String connectionString = "";
}
