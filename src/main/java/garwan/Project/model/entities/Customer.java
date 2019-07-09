package garwan.Project.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    @Positive
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @Getter
    @Setter
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @Getter
    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Order> orders;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
