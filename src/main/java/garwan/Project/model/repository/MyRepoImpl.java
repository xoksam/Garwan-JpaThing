package garwan.Project.model.repository;

import garwan.Project.model.exceptions.CustomNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class MyRepoImpl<T, F> implements MyRepo<T, F> {

    private EntityManager em;
    private Class<T> clazz;

    public MyRepoImpl(EntityManager em, Class<T> clazz) {
        this.em = em;
        this.clazz = clazz;
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
        String whereStatement = (createWhereStatement(filter));

        var query = em.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e " + whereStatement, clazz);

        return query.getResultList();
    }

    @Override
    public Page<T> listByPage(Pageable pageable) {

        return null;
    }

    @Override
    public Page<T> listByPage(F filter, Pageable pageable) {
        
        return null;
    }

    private boolean isNumber(Object o) {
        return (o instanceof Number);
    }

    private boolean isString(Object o) {
        return o instanceof String;
    }

    //TODO: GENERIFY
    private Long parseToLong(Object o) {
        return (Long) o;
    }

    private String parseToString(Object val) {
        return (String) val;
    }

    private String createWhereStatement(F filter) {
        if (filter == null) return "";

        StringBuilder builder = new StringBuilder();
        builder.append("where ");

        List<String> comparisons = new ArrayList<>();

        try {
            for (Field f : filter.getClass().getFields()) {
                f.setAccessible(true);
                Object val = f.get(filter);
                String value = "";

                if (isNumber(val)) {
                    value = parseToLong(val).toString();
                } else if (isString(val)) {
                    value = parseToString(val);
                }

                if (value == null || value.isBlank()) {
                    continue;
                }
                comparisons.add("e." + f.getName() + " = '" + value + "'");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (comparisons.size() > 1) {
            IntStream.range(0, comparisons.size())
                    .forEach(index -> {
                        builder.append(comparisons.get(index));
                        if (index < comparisons.size() - 1)
                            builder.append(" and ");
                    });
            // So there is no 'where' statement
        } else if (comparisons.isEmpty()) {
            return "";
        } else {
            builder.append(comparisons.get(0));
        }

        return builder.toString();
    }

}