package cz.vutbr.fit.pdb.nichcz.model;

/**
 * User: Marek Salát
 * Date: 2.12.13
 * Time: 23:31
 *
 * Rozhrani vytvoreni parametricke sluzby.
 * @param <E>    Parametricka sluzba.
 */
public interface Factory<E> {
    /**
     * Vytvori parametrickou sluzbu.
     * @return Vraci sluzbu.
     */
    public E create();
}
