package garwan.Project.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyRepo<T, F> {
    T create(T t);

    T update(T t);

    T delete(Long id);

    List<T> listAll(F filter);

    Page<T> listByPage(Pageable pageable);

    Page<T> listByPage(F filter, Pageable pageable);

    T findById(Long id);
}
