package hu.berenice.meriem.forex.notebook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Table "processor" (id, manufacturer, type). */
@Entity
public class Processor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String manufacturer;
    private String type;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}