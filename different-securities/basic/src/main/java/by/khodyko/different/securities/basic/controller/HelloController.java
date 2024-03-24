package by.khodyko.different.securities.basic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

    @GetMapping("/user/hello")
    public ModelAndView userHello() {
        ModelAndView mav = new ModelAndView("user_hello");
        mav.addObject("message", "Hello, User!");
        return mav;
    }

    @GetMapping("/admin/hello")
    public ModelAndView adminHello() {
        ModelAndView mav = new ModelAndView("admin_hello");
        mav.addObject("message", "Hello, Admin!");
        return mav;
    }
}
