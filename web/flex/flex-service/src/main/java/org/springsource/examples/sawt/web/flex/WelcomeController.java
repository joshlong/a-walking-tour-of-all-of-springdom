package org.springsource.examples.sawt.web.flex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WelcomeController {

    @RequestMapping(value = "/welcome")
    public void welcome(HttpServletRequest r, Model m) {
        m.addAttribute("contextRoot", r.getContextPath());
    }

}
