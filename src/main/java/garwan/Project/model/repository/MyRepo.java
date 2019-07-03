package garwan.Project.model.repository;

import java.util.List;

public interface MyRepo<T> {
    T create(T t);

    T update(T t);

    T delete(long id);

    List<T> listAll();

    T findById(Long id);
}
