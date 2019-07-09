package garwan.Project.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    @Positive
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Size(min = 3, max = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Getter
    @Setter
    @NotNull
    @Valid
    private Customer customer;

    public Order(String title, Customer customer) {
        this.title = title;
        this.customer = customer;
    }

}
