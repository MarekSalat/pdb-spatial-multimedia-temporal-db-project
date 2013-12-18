package cz.vutbr.fit.pdb.nichcz.model;

import java.util.List;

/**
 * User: Marek Salát
 * Date: 2.12.13
 * Time: 23:20
 *
 * Rozhrani tridy slouzici ke komunikaci s databazi.
 *
 * @param <E>     Entita.
 * @param <PK>    Primarni klic entity.
 */
public interface Mapper<E extends Entity, PK> {
    /**
     * Vytvori novy zaznam v databazi.
     * @return Vraci entitu z databaze.
     */
    public E create();

    /**
     * Ulozi entitu do databaze.
     * @param entity Entita na ulozeni.
     */
    public void save(E entity);

    /**
     * Smaze entitu z databaze.
     * @param entity Entita na smazani.
     */
    public void delete(E entity);


    /**
     * Vyhleda entitu v databazi podle klice ID.
     * @param id ID entity.
     * @return Vraci nalezenou entitu.
     */
    public E findById(PK id);

    /**
     * Vyhleda vsechny zaznamy v databazi.
     * @return Vraci seznam entit.
     */
    public List<E> findAll();

    /**
     * Vyhleda zaznamy podle podminky WHERE.
     * @param where Podminka hledani.
     * @return Vraci seznam entit.
     */
    public List<E> findWhere(String where);
}
