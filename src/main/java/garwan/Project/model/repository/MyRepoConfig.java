package garwan.Project.model.repository;

import garwan.Project.model.entities.Customer;
import garwan.Project.model.entities.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class MyRepoConfig {

    @Bean
    public MyRepo<Customer> customerMyRepoBean(EntityManager entityManager) {
        return new MyRepoImpl<>(entityManager, Customer.class);
    }

    @Bean
    public MyRepo<Order> orderMyRepo(EntityManager entityManager) {
        return new MyRepoImpl<>(entityManager, Order.class);
    }
}
