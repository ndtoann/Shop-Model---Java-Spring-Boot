package webapp.shopmohinh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import webapp.shopmohinh.dto.ReviewDto;
import webapp.shopmohinh.model.*;
import webapp.shopmohinh.service.*;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping({ "/danh-muc" })
    public String listAction(Model model, @RequestParam("id") long id) {
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);

        Category category = categoryService.getById(id);
        model.addAttribute("category", category);

        List<Product> products = productService.getByCategoryId(id);
        model.addAttribute("listProducts", products);

        model.addAttribute("content", "products");

        return "layout";
    }

    @GetMapping({ "/san-pham" })
    public String detailAction(Model model, @RequestParam("id") long id) {
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);

        Product product = productService.getById(id);
        model.addAttribute("product", product);
        for (ProductImage pi : product.getImages()) {
            System.out.println(pi.getImage() + " * ");
        }

        List<Product> listRelated = productService.getByCategoryId(product.getCategory().getId());
        model.addAttribute("listRelated", listRelated);

        ReviewDto review = new ReviewDto();
        model.addAttribute("review", review);

        model.addAttribute("content", "product-detail");

        return "layout";
    }

    @PostMapping({ "/saveReview" })
    public String postReview(@Valid @ModelAttribute("review") ReviewDto reviewDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            System.out.println("Lỗi khi đánh giá");
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại dữ liệu");
            return "redirect:/san-pham?id=" + reviewDto.getProduct_id();
        }
        Long product_id = Long.parseLong(request.getParameter("product_id"));
        reviewDto.setProduct_id(product_id);
        int rated = Integer.parseInt(request.getParameter("rated"));
        reviewDto.setRated(rated);
        try {
            reviewService.save(reviewDto);

            redirectAttributes.addFlashAttribute("success", "Gửi đánh giá thành công");
            return "redirect:/san-pham?id=" + reviewDto.getProduct_id();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xảy ra lỗi khi gửi đánh giá");
            return "redirect:/san-pham?id=" + reviewDto.getProduct_id();
        }
    }

}
