package garwan.Project.controller.rest;

import garwan.Project.model.entities.Customer;
import garwan.Project.model.entities.Order;
import garwan.Project.model.repository.MyRepo;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static garwan.Project.controller.rest.RestPathVariables.API_PREFIX;
import static garwan.Project.controller.rest.RestPathVariables.ORDER_PATH;

@RestController
@RequestMapping(API_PREFIX + ORDER_PATH)
public class OrderRestController {

    private final MyRepo<Order, Filter> orderRepo;
    private final MyRepo<Customer, CustomerRestController.Filter> customerRepo;

    public OrderRestController(MyRepo<Order, Filter> orderRepo, MyRepo<Customer, CustomerRestController.Filter> customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

    @GetMapping("/all")
    @Transactional
    public Page<Order> getAllOrders(Filter filter, Pageable pageable) {

        return orderRepo.listByPage(filter, pageable);
    }

    @PostMapping("/create/{customerId}")
    @Transactional
    public Order createOrder(@PathVariable Long customerId,
                             @RequestBody String title) {

        Customer customer = customerRepo.findById(customerId);

        return orderRepo.create(new Order(title, customer));
    }

    @Data
    public static class Filter {
        public String title;
        public Long id;
        public Customer customer;
    }
}
