package hu.berenice.meriem.forex.notebook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Table "notebook" - the table the React CRUD works on.
 * processorId / opsystemId are plain foreign-key numbers (the React form offers drop-downs for them).
 */
@Entity
public class Notebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String manufacturer;
    private String type;
    private Double display;          // screen size
    private Integer memory;          // MiB
    private Integer harddisk;        // GB
    private String videocontroller;
    private Integer price;           // pounds
    private Long processorId;
    private Long opsystemId;
    private Integer pieces;          // in stock

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getDisplay() { return display; }
    public void setDisplay(Double display) { this.display = display; }
    public Integer getMemory() { return memory; }
    public void setMemory(Integer memory) { this.memory = memory; }
    public Integer getHarddisk() { return harddisk; }
    public void setHarddisk(Integer harddisk) { this.harddisk = harddisk; }
    public String getVideocontroller() { return videocontroller; }
    public void setVideocontroller(String videocontroller) { this.videocontroller = videocontroller; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Long getProcessorId() { return processorId; }
    public void setProcessorId(Long processorId) { this.processorId = processorId; }
    public Long getOpsystemId() { return opsystemId; }
    public void setOpsystemId(Long opsystemId) { this.opsystemId = opsystemId; }
    public Integer getPieces() { return pieces; }
    public void setPieces(Integer pieces) { this.pieces = pieces; }
}