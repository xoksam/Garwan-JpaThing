package garwan.Project.controller.rest;

import garwan.Project.model.entities.Customer;
import garwan.Project.model.entities.Order;
import garwan.Project.model.exceptions.CustomNotFoundException;
import garwan.Project.model.repository.MyRepo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static garwan.Project.controller.rest.RestConstants.API_PREFIX;
import static garwan.Project.controller.rest.RestConstants.ORDER_PATH;

@RestController
@RequestMapping(API_PREFIX + ORDER_PATH)
public class OrderRestController {

    private final MyRepo<Order> orderRepo;
    private final MyRepo<Customer> customerRepo;

    public OrderRestController(MyRepo<Order> orderRepo, MyRepo<Customer> customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

    @GetMapping("/all")
    @Transactional
    public List<Order> getAllOrders() {

        return orderRepo.listAll();
    }

    @PostMapping("/create/{customerId}")
    @Transactional
    public Order createOrder(@PathVariable Long customerId,
                             @RequestBody String title) {

        Customer customer = customerRepo.findById(customerId);

        if (customer == null) {
            throw new CustomNotFoundException("User with id " + customerId + " not found");
        }

        return orderRepo.create(new Order(title, customer));
    }
}
