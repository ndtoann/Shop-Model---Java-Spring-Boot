package webapp.shopmohinh.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import webapp.shopmohinh.dto.ChangePassUserDto;
import webapp.shopmohinh.model.User;
import webapp.shopmohinh.service.UserService;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({ "/admin/tai-khoan" })
    public String index(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        List<User> listUser = userService.getAll();
        model.addAttribute("listUser", listUser);

        model.addAttribute("content", "admin/account/index");
        return "admin/layout";
    }

    @GetMapping({ "/admin/doi-mat-khau" })
    public String changePass(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        ChangePassUserDto userdto = new ChangePassUserDto();
        model.addAttribute("user", userdto);

        model.addAttribute("content", "admin/account/change-password");
        return "admin/layout";
    }

    @PostMapping({ "/admin/luu-tai-khoan" })
    public String postChangePass(@Valid @ModelAttribute("user") ChangePassUserDto oldUser,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("user", oldUser);
            model.addAttribute("content", "admin/account/change-password");
            return "admin/layout";
        }
        User currentUser = userService.findByEmail(oldUser.getEmail());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không chính xác");
            return "redirect:/admin/doi-mat-khau";
        }
        Long id;
        String password;
        if(passwordEncoder.matches(oldUser.getOld_password(), currentUser.getPassword())){
            id = currentUser.getId();
            password = oldUser.getNew_password();
        }else{
            redirectAttributes.addFlashAttribute("error", "Mật khẩu không chính xác");
            return "redirect:/admin/doi-mat-khau";
        }

        try {
            userService.changePass(id, currentUser.getFullname(), currentUser.getEmail(), password);;
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
            return "redirect:/admin/doi-mat-khau";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xảy ra lỗi");
            return "redirect:/admin/doi-mat-khau";
        }
    }
}
