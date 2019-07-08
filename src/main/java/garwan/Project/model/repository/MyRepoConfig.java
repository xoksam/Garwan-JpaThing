package garwan.Project.model.repository;

import garwan.Project.controller.rest.CustomerRestController;
import garwan.Project.controller.rest.OrderRestController;
import garwan.Project.model.entities.Customer;
import garwan.Project.model.entities.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class MyRepoConfig {

    @Bean
    public MyRepo<Customer, CustomerRestController.Filter> customerMyRepoBean(EntityManager entityManager) {
    return new MyRepoImpl<>(entityManager, Customer.class);
}

    @Bean
    public MyRepo<Order, OrderRestController.Filter> orderMyRepo(EntityManager entityManager) {
        return new MyRepoImpl<>(entityManager, Order.class);
    }
}
