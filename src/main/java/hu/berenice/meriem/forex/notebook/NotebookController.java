package hu.berenice.meriem.forex.notebook;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** REST API used by the React page: full CRUD for notebooks + read-only lists for the drop-downs. */
@RestController
@RequestMapping("/api")
public class NotebookController {

    private final NotebookRepository notebooks;
    private final ProcessorRepository processors;
    private final OpsystemRepository opsystems;

    public NotebookController(NotebookRepository notebooks,
                              ProcessorRepository processors,
                              OpsystemRepository opsystems) {
        this.notebooks = notebooks;
        this.processors = processors;
        this.opsystems = opsystems;
    }

    // ---- Read ----

    @GetMapping("/notebooks")
    public List<Notebook> list() {
        return notebooks.findAll(Sort.by("id"));
    }

    @GetMapping("/notebooks/{id}")
    public Notebook get(@PathVariable(name = "id") Long id) {
        return notebooks.findById(id).orElseThrow(() -> notFound(id));
    }

    // ---- Create ----

    @PostMapping("/notebooks")
    public ResponseEntity<Notebook> create(@RequestBody Notebook notebook) {
        validate(notebook);
        notebook.setId(null); // always let the database generate the id
        return ResponseEntity.status(HttpStatus.CREATED).body(notebooks.save(notebook));
    }

    // ---- Update ----

    @PutMapping("/notebooks/{id}")
    public Notebook update(@PathVariable(name = "id") Long id, @RequestBody Notebook notebook) {
        if (!notebooks.existsById(id)) {
            throw notFound(id);
        }
        validate(notebook);
        notebook.setId(id);
        return notebooks.save(notebook);
    }

    // ---- Delete ----

    @DeleteMapping("/notebooks/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        if (!notebooks.existsById(id)) {
            throw notFound(id);
        }
        notebooks.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Lists for the drop-downs ----

    @GetMapping("/processors")
    public List<Processor> processors() {
        return processors.findAll(Sort.by("id"));
    }

    @GetMapping("/opsystems")
    public List<Opsystem> opsystems() {
        return opsystems.findAll(Sort.by("id"));
    }

    // ---- helpers ----

    private void validate(Notebook n) {
        if (isBlank(n.getManufacturer()) || isBlank(n.getType())) {
            throw badRequest("Manufacturer and type are required.");
        }
        if (n.getDisplay() == null || n.getDisplay() <= 0
                || n.getMemory() == null || n.getMemory() <= 0
                || n.getHarddisk() == null || n.getHarddisk() <= 0) {
            throw badRequest("Display, memory and hard disk must be positive numbers.");
        }
        if (n.getPrice() == null || n.getPrice() < 0 || n.getPieces() == null || n.getPieces() < 0) {
            throw badRequest("Price and pieces must be zero or more.");
        }
        if (n.getProcessorId() == null || !processors.existsById(n.getProcessorId())) {
            throw badRequest("Unknown processor.");
        }
        if (n.getOpsystemId() == null || !opsystems.existsById(n.getOpsystemId())) {
            throw badRequest("Unknown operating system.");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    private static ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Notebook " + id + " not found");
    }
}