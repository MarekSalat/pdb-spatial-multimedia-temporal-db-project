package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:48
 *
 * Rozhrani pro oznacitelny objekt na platne.
*/
public interface Selectable extends Drawable {
    /**
     * Pri oznaceni objektu.
     */
    public void onSelected();

    /**
     * Pri odznaceni objektu.
     */
    public void onUnselected();
}
