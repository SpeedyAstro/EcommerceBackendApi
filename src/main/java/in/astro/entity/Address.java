package in.astro.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long addressId;
    private String street;
    private String buildingName;
    private String city;
    private String state;
    private String country;
    private String pincode;
    @ManyToMany(mappedBy = "addresses")
    private List<User> user = new ArrayList<>();
    public Address(String country, String state, String city, String pincode, String street, String buildingName) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.street = street;
        this.buildingName = buildingName;
    }
}
