package ro.tuc.ds2023.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Device implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    private String address;

    private Double maxHourlyEnergyConsumption;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;
}