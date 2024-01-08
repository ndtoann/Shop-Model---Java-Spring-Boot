package webapp.shopmohinh.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import webapp.shopmohinh.model.*;
import webapp.shopmohinh.service.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping({ "/admin/don-hang" })
    public String index(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        List<Order> orders = orderService.getAll();
        model.addAttribute("listOrder", orders);
        model.addAttribute("content", "admin/order/index");
        return "admin/layout";
    }

    @GetMapping({ "/admin/chi-tiet-don-hang" })
    public String detail(@RequestParam("id") Long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        model.addAttribute("content", "admin/order/detail");
        return "admin/layout";
    }

    @GetMapping({ "/admin/duyet-don-hang/{id}" })
    public String updateStatus(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes,
            Model model) {
        Order order = orderService.getById(id);
        if (order == null) {
            return "redirect:/admin/don-hang";
        }
        try {
            orderService.updateStatus(id);

            redirectAttributes.addFlashAttribute("success", "Đã cập nhật trạng thái đơn hàng");
            return "redirect:/admin/don-hang";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xảy ra lỗi, thử lại sau");
            return "redirect:/admin/don-hang";
        }
    }

    @GetMapping({ "/admin/xoa-don-hang/{id}" })
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes,
            Model model) {
        Order order = orderService.getById(id);
        if (order == null) {
            return "redirect:/admin/don-hang";
        }
        try {
            orderService.deleteById(id);

            redirectAttributes.addFlashAttribute("success", "Đã xóa đơn hàng");
            return "redirect:/admin/don-hang";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xảy ra lỗi, thử lại sau");
            return "redirect:/admin/don-hang";
        }
    }

}
