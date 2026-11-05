package hu.berenice.meriem.forex.notebook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The React build lives in src/main/resources/static/react. Spring does not serve index.html for
 * the folder URL by itself, so /react forwards to it (this is what the "React" menu links to).
 */
@Controller
public class ReactController {

    @GetMapping("/react")
    public String react() {
        return "forward:/react/index.html";
    }
}