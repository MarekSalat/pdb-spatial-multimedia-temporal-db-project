package cz.vutbr.fit.pdb.nichcz.model.temporal;

import java.util.List;

/**
 * User: Petr Přikryl
 * Date: 16.12.13
 * Time: 19:42
 *
 * Rozhrani pro temporalni dotazovani nad databazi.
 */
public interface TemporalMapper <E extends TemporalEntity, ID, VALID_FROM, VALID_TO> {
    public void create(E entity);
    public void save(E entity);
    public void delete(E entity, VALID_FROM from, VALID_TO to);

    public E findByPk(ID id, VALID_FROM validFrom, VALID_TO validTo);
    public List<E> findAll();
    public List<E> findWhere(String where);
}
