package garwan.Project.model.repository;

import java.util.List;

public interface MyRepo<T> {
    T create(T t);

    T update(T t);

    T delete(long id);

    List<T> listAll();

//    List<T> removeAll(List<T> list);

    T findById(Long id);
}
