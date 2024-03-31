package by.khodyko.different.securities.basic.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }
}
