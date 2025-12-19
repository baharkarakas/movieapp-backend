package movieapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/how-to-use")
    public String howToUse() {
        return "how-to-use"; // templates/how-to-use.html
    }
}
