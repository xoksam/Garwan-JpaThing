package garwan.Project.model.repository;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


public class MyRepoImpl<T> implements MyRepo<T> {

    private EntityManager em;
    private Class<T> clazz;

    public MyRepoImpl(EntityManager em, Class<T> clazz) {
        this.em = em;
        this.clazz = clazz;
    }

    @Override
    public T create(T t) {
        em.persist(t);
        return t;
    }

    @Override
    public T update(T t) {
        return em.merge(t);
    }

    public T findById(Long id) {
        return em.find(clazz, id);
    }

    @Override
    public T delete(long id) {
        T t = findById(id);
        em.remove(t);
        return t;
    }

    @Override
    public List<T> listAll() {
        var query = em.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz);
        
        return query.getResultList();
    }
}
