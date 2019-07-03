package garwan.Project.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
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
    @Getter
    @Setter
    private Customer customer;

    public Order(String title, Customer customer) {
        this.title = title;
        this.customer = customer;
    }
}
