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
public class ReviewController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping({ "/admin/danh-gia" })
    public String index(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        List<Review> reviews = reviewService.getAll();
        model.addAttribute("listReview", reviews);
        model.addAttribute("content", "admin/review/index");
        return "admin/layout";
    }

    @GetMapping({ "/admin/duyet-danh-gia/{id}" })
    public String updateStatus(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes,
            Model model) {
        Review review = reviewService.getById(id);
        if (review == null) {
            return "redirect:/admin/danh-gia";
        }
        try {
            reviewService.updateStatus(id);

            redirectAttributes.addFlashAttribute("success", "Đã duyệt đánh giá");
            return "redirect:/admin/danh-gia";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xảy ra lỗi, thử lại sau");
            return "redirect:/admin/danh-gia";
        }
    }

    @GetMapping({ "/admin/xoa-danh-gia/{id}" })
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes,
            Model model) {
        Review review = reviewService.getById(id);
        if (review == null) {
            return "redirect:/admin/danh-gia";
        }
        try {
            reviewService.deleteById(id);

            redirectAttributes.addFlashAttribute("success", "Đã xóa đánh giá");
            return "redirect:/admin/danh-gia";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xảy ra lỗi, thử lại sau");
            return "redirect:/admin/danh-gia";
        }
    }

}
