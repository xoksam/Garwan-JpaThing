package garwan.Project.model.utilities;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
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
        String whereStatement = createStatement(filter, null, StatementType.Where);

        return em.createQuery(SELECT_ALL_STATEMENT + whereStatement, clazz);
    }

    public Query createCountQueryOfAllFilteredElements(F filter) {
        String whereStatement = createStatement(filter, null, StatementType.Where);

        return em.createQuery(COUNT_ALL_STATEMENT + whereStatement);
    }

    public Query getFilteredOrderByQuery(F filter, Pageable pageable) {
        String orderByString = createStatement(filter, pageable, StatementType.OrderBy);
        String whereStatement = createStatement(filter, pageable, StatementType.Where);

        return em.createQuery(SELECT_ALL_STATEMENT + whereStatement + orderByString);
    }

    private String createStatement(F filter, Pageable pageable, StatementType type) {
        List<String> res = new ArrayList<>();

        if (type == StatementType.Where) {
            if (filter == null) return "";
            res = createComparisons(filter);
        } else if (type == StatementType.OrderBy) {
            res = createOrderByOperands(pageable);
        }

        if (res.isEmpty())
            return "";

        return type.value + String.join(type.delimiter, res);
    }

    private List<String> createOrderByOperands(Pageable pageable) {
        if (pageable == null)
            return Collections.emptyList();

        List<String> res = new ArrayList<>();
        for (Sort.Order order : pageable.getSort())
            res.add(order.getProperty() + " " + order.getDirection().toString());

        return res;
    }

    private List<String> createComparisons(Object obj) {
        return createComparisons(obj, "e");
    }

    private List<String> createComparisons(Object obj, String parent) {
        if (obj == null)
            return Collections.emptyList();

        var objClass = obj.getClass();
        var fields = objClass.getDeclaredFields();

        List<String> res = new ArrayList<>();
        for (var f : fields) {
            var fieldValue = getValueFromField(obj, f);

            if (fieldValue == null) continue;

            if (isComplex(fieldValue)) {
                res.addAll(createComparisons(fieldValue, parent + "." + f.getName()));
                continue;
            }

            res.add(createComparison(f.getName(), fieldValue.toString(), parent));
        }

        return res;
    }

    private boolean isComplex(Object obj) {
        return !(obj instanceof String || obj instanceof Number);
    }

    private String createComparison(String prop, String val, String parent) {
        parent = parent.isBlank() ? "" : parent + ".";

        var name = parent + prop;
        return name + " = " + "'" + val + "'";
    }

    private Object getValueFromField(Object filter, Field f) {
        try {
            f.setAccessible(true);
            return f.get(filter);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private enum StatementType {

        Where("where ", " and "),
        OrderBy("order by ", ", ");

        public String value;
        public String delimiter;

        StatementType(String value, String delimiter) {
            this.value = value;
            this.delimiter = delimiter;
        }
    }

}














