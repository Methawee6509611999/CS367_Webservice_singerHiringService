package dev.methawee.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = "/")
    public String redirect() {
        return "forward:/index.html";
    }
}
