package ufrn.com.comercioeaj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = {"/", "/home","/index"}, method = RequestMethod.GET)
    public String getIndex() {
        return "index.html";
    }

    @RequestMapping(value = {"/sobre"}, method = RequestMethod.GET)
    public String getSobre() {
        return "sobre.html";
    }
}
