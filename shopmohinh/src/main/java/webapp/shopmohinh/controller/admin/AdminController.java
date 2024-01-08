package webapp.shopmohinh.controller.admin;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import webapp.shopmohinh.model.*;
import webapp.shopmohinh.service.*;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping({ "/admin/dashboard" })
    public String homeAdmin(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());
        model.addAttribute("content", "admin/index");

        return "admin/layout";
    }

    @GetMapping({ "/admin" })
    public String redirct() {
        return "redirect:/admin/dashboard";
    }
}
