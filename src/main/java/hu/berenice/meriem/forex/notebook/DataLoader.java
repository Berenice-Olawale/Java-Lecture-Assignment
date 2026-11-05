package hu.berenice.meriem.forex.notebook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fills an EMPTY database with a few SAMPLE rows so the page is not blank on first start.
 * (With a file database a data.sql would insert them again on every restart - see the seminar -
 * so we only insert when all three tables are empty.)
 * Replace these with the real Notebook data if you have it.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final NotebookRepository notebooks;
    private final ProcessorRepository processors;
    private final OpsystemRepository opsystems;

    public DataLoader(NotebookRepository notebooks, ProcessorRepository processors, OpsystemRepository opsystems) {
        this.notebooks = notebooks;
        this.processors = processors;
        this.opsystems = opsystems;
    }

    @Override
    public void run(String... args) {
        if (notebooks.count() > 0 || processors.count() > 0 || opsystems.count() > 0) {
            return;
        }
        List<Processor> p = processors.saveAll(List.of(
                processor("Intel", "Core i5-8250U"),
                processor("Intel", "Core i7-8550U"),
                processor("AMD", "Ryzen 5 3500U")));
        List<Opsystem> o = opsystems.saveAll(List.of(
                opsystem("Windows 11 Pro"),
                opsystem("Ubuntu 24.04"),
                opsystem("No operating system")));

        notebooks.saveAll(List.of(
                notebook("Lenovo", "ThinkPad T480", 14.0, 8192, 256, "Intel UHD 620", 320, p.get(0), o.get(0), 12),
                notebook("Dell", "Latitude 5490", 14.0, 16384, 512, "Intel UHD 620", 410, p.get(1), o.get(0), 7),
                notebook("HP", "EliteBook 840 G5", 14.0, 8192, 256, "Intel UHD 620", 355, p.get(0), o.get(1), 9),
                notebook("Acer", "Aspire 5", 15.6, 8192, 512, "AMD Radeon Vega 8", 290, p.get(2), o.get(1), 15),
                notebook("Asus", "VivoBook 15", 15.6, 4096, 128, "Intel UHD 620", 210, p.get(0), o.get(2), 3)));
    }

    private static Processor processor(String manufacturer, String type) {
        Processor p = new Processor();
        p.setManufacturer(manufacturer);
        p.setType(type);
        return p;
    }

    private static Opsystem opsystem(String name) {
        Opsystem o = new Opsystem();
        o.setOsname(name);
        return o;
    }

    private static Notebook notebook(String manufacturer, String type, double display, int memory, int harddisk,
                                     String video, int price, Processor p, Opsystem o, int pieces) {
        Notebook n = new Notebook();
        n.setManufacturer(manufacturer);
        n.setType(type);
        n.setDisplay(display);
        n.setMemory(memory);
        n.setHarddisk(harddisk);
        n.setVideocontroller(video);
        n.setPrice(price);
        n.setProcessorId(p.getId());
        n.setOpsystemId(o.getId());
        n.setPieces(pieces);
        return n;
    }
}