package garwan.Project.controller.rest;

import garwan.Project.model.entities.Customer;
import garwan.Project.model.repository.MyRepo;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static garwan.Project.controller.rest.RestPathVariables.API_PREFIX;
import static garwan.Project.controller.rest.RestPathVariables.CUSTOMER_PATH;


@RestController
@RequestMapping(API_PREFIX + CUSTOMER_PATH)
public class CustomerRestController {

    private final MyRepo<Customer, Filter> customerRepo;

    public CustomerRestController(MyRepo<Customer, Filter> customerRepo) {
        this.customerRepo = customerRepo;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Customer deleteCustomer(@PathVariable Long id) {
//        if(customer == null) throw new CustomNotFoundException("Customer with id " + id + " not found");
        return customerRepo.delete(id);
    }

    @GetMapping("/all")
    @Transactional
    public List<Customer> getAllFilteredCustomers(Filter filter) {

        return customerRepo.listAll(filter);
    }

    @PutMapping("/update/{id}")
    @Transactional
    public Customer updateCustomer(@RequestBody Customer customer,
                                   @PathVariable Long id) {
        customer.setId(id);

        return customerRepo.update(customer);
    }

    @PostMapping("/create")
    public Customer createCustomer(@RequestBody Customer customer) {

        return customerRepo.create(customer);
    }

    @Data
    public static class Filter {
        public String firstName;
        public String lastName;
        public Long id;
    }

}
