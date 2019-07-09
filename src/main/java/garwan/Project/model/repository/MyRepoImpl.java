package garwan.Project.model.repository;

import garwan.Project.model.utilities.JPAQueryBuilder;
import garwan.Project.model.exceptions.CustomNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


public class MyRepoImpl<T, F> implements MyRepo<T, F> {

    private EntityManager em;
    private Class<T> clazz;

    private final JPAQueryBuilder<T, F> queryBuilder;

    public MyRepoImpl(EntityManager em, Class<T> clazz) {
        this.em = em;
        this.clazz = clazz;
        this.queryBuilder = new JPAQueryBuilder<>(clazz, em);
    }

    @Override
    @Transactional
    public T create(T t) {
        em.persist(t);
        return t;
    }

    @Override
    public T update(T t) {
        return em.merge(t);
    }

    public T findById(Long id) {
        T t = em.find(clazz, id);

        if (t == null) throw new CustomNotFoundException(clazz, "id", id.toString());
        return t;
    }

    @Override
    @Transactional
    public T delete(long id) {
        T t = findById(id);

        em.remove(t);
        return t;
    }

    @Override
    public List<T> listAll(F filter) {

        return queryBuilder.createAllFilteredQuery(filter).getResultList();
    }

    @Override
    public Page<T> listByPage(Pageable pageable) {
        return listByPage(null, pageable);
    }

    @Override
    public Page<T> listByPage(F filter, Pageable pageable) {

        Long count = (Long) queryBuilder.createCountQueryOfAllFilteredElements(filter).getSingleResult();

        var query = queryBuilder.getFilteredOrderByQuery(filter, pageable);

        if (pageable.isPaged()) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<T> content = query.getResultList();

        return new PageImpl<>(content, pageable, count);
    }

}