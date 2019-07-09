package garwan.Project.model;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JPAQueryBuilder<T, F> {

    private String SELECT_ALL_STATEMENT;
    private String COUNT_ALL_STATEMENT;

    private Class<T> clazz;
    private EntityManager em;

    public JPAQueryBuilder(Class<T> clazz, EntityManager em) {
        this.clazz = clazz;
        this.em = em;

        this.SELECT_ALL_STATEMENT = "SELECT e FROM " + clazz.getSimpleName() + " e ";
        this.COUNT_ALL_STATEMENT = "SELECT COUNT(e) FROM " + clazz.getSimpleName() + " e ";
    }

    public Query createAllFilteredQuery(F filter) {
        String whereStatement = createWhereStatement(filter);
        return em.createQuery(SELECT_ALL_STATEMENT + whereStatement, clazz);
    }

    public Query createCountQueryOfAllFilteredElements(F filter) {
        String whereStatement = createWhereStatement(filter);
        return em.createQuery(COUNT_ALL_STATEMENT + whereStatement);
    }

    public Query getFilteredOrderByQuery(F filter, Pageable pageable) {

        String sortString = createOrderByStatement(pageable);
        String whereStatement = createWhereStatement(filter);

        return em.createQuery(SELECT_ALL_STATEMENT + whereStatement + sortString);
    }

    private String createOrderByStatement(Pageable pageable) {
        List<String> sortConditions = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            sortConditions.add(order.getProperty() + " " + order.getDirection().toString());
        }

        var sortClause = String.join(", ", sortConditions);

        return (sortClause.isBlank() ? "" : "ORDER BY " + sortClause);
    }

    private String createWhereStatement(F filter) {
        if (filter == null) return "";

        List<String> comparisons = new ArrayList<>();

        for (Field f : filter.getClass().getFields()) {
            Object val = getValue(filter, f);

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
        // So there is no 'where' statement
        if (comparisons.isEmpty()) {
            return "";
        }
        return "where " + String.join(" and ", comparisons);
    }

    private Object getValue(F filter, Field f) {
        try {
            f.setAccessible(true);
            return f.get(filter);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

}
