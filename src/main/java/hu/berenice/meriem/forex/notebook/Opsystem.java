package hu.berenice.meriem.forex.notebook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Table "opsystem" (id, osname). */
@Entity
public class Opsystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String osname;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOsname() { return osname; }
    public void setOsname(String osname) { this.osname = osname; }
}