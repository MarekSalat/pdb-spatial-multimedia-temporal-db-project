package cz.vutbr.fit.pdb.nichcz.model;

import java.util.List;

/**
 * User: Marek Salát
 * Date: 2.12.13
 * Time: 23:20
 */
public interface Mapper<E extends Entity, PK> {
    public E create();
    public void save(E entity);
    public void delete(E entity);

    public E findById(PK id);
    public List<E> findAll();
    public List<E> findWhere(String where);
}
