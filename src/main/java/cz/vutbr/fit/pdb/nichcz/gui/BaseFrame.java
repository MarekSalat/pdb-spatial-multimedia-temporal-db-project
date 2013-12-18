package cz.vutbr.fit.pdb.nichcz.gui;

import cz.vutbr.fit.pdb.nichcz.context.Context;

import javax.swing.*;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 17:10
 *
 * Zakladni formular aplikace s ulozenym kontextem.
 */
public abstract class BaseFrame extends JFrame {
    protected Context context;

    /**
     * Vytvori novy BaseFrame.
     */
    public BaseFrame(){
        super();
    }

    /**
     * Vytvori novy BaseFrame.
     * @param ctx Kontext s pripojenim na databazi.
     */
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
