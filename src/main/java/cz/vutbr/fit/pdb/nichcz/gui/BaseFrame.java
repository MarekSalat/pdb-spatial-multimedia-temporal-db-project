package cz.vutbr.fit.pdb.nichcz.gui;

import cz.vutbr.fit.pdb.nichcz.context.Context;

import javax.swing.*;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 17:10
 */
public abstract class BaseFrame extends JFrame {
    protected Context context;

    public BaseFrame(){
        super();
    }

    public BaseFrame(Context ctx) {
        this();
        this.context = ctx;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
