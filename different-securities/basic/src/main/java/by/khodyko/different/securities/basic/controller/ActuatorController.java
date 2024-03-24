package by.khodyko.different.securities.basic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller(value = "/actuator")
public class ActuatorController {

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String login() {
        return "actuator/health";
    }
}
