package garwan.Project.controller.rest;

import garwan.Project.model.entities.Customer;
import garwan.Project.model.repository.MyRepo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static garwan.Project.controller.rest.RestConstants.API_PREFIX;
import static garwan.Project.controller.rest.RestConstants.CUSTOMER_PATH;


@RestController
@RequestMapping(API_PREFIX + CUSTOMER_PATH)
public class CustomerRestController {

    private final MyRepo<Customer> repo;

    public CustomerRestController(MyRepo<Customer> repo) {
        this.repo = repo;
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public Customer deleteCustomer(@PathVariable Long id) {

        return repo.delete(id);
    }

    @GetMapping("/all")
    @Transactional
    public List<Customer> getAllCustomers() {

        return repo.listAll();
    }

    @PutMapping("/update/{id}")
    @Transactional
    public Customer updateCustomer(@RequestBody Customer customer,
                                   @PathVariable Long id) {
        customer.setId(id);

        return repo.update(customer);
    }

    @PostMapping("/create")
    @Transactional
    public Customer createCustomer(@RequestBody Customer customer) {

        return repo.create(customer);
    }

}
