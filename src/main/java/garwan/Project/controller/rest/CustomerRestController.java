package garwan.Project.controller.rest;

import garwan.Project.model.entities.Customer;
import garwan.Project.model.repository.MyRepo;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return customerRepo.delete(id);
    }


    @GetMapping("/all")
    @Transactional
    public Page<Customer> getAllCustomers(Filter filter, Pageable pageable) {

        return customerRepo.listByPage(filter, pageable);
    }

    @PutMapping("/update/{id}")
    @Transactional
    public Customer updateCustomer(@RequestBody @Valid Customer customer,
                                   @PathVariable Long id) {
        customer.setId(id);

        return customerRepo.update(customer);
    }

    @PostMapping("/create")
    public Customer createCustomer(@RequestBody @Valid Customer customer) {

        return customerRepo.create(customer);
    }

    @Data
    public static class Filter {
        public String firstName;
        public String lastName;
        public Long id;
    }

}
