package garwan.Project.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String title;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Getter
    @Setter
    private Customer customer;

    public Order(String title, Customer customer) {
        this.title = title;
        this.customer = customer;
    }
}
