package webapp.shopmohinh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import webapp.shopmohinh.model.*;
import webapp.shopmohinh.service.*;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/", "/index", "/trang-chu", "/home"})
    public String home(Model model){
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);

        List<Product> listOto = productService.getByCategoryId(1L);
        List<Product> listMoto = productService.getByCategoryId(3L);
        List<Product> listMayBay = productService.getByCategoryId(5L);
        List<Product> listOrder = productService.getAll();
        model.addAttribute("listOto", listOto);
        model.addAttribute("listMoto", listMoto);
        model.addAttribute("listMayBay", listMayBay);
        model.addAttribute("listOrder", listOrder);

        model.addAttribute("content", "index");
        
        return "layout";
    }

    @GetMapping({"/search"})
    public String postSearch(Model model, @RequestParam("s") String s) {
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);
        
        List<Product> products = productService.search(s);
        
        model.addAttribute("products", products);
        model.addAttribute("productCount", products.size());
         model.addAttribute("keySearch", s);
        model.addAttribute("content", "search");
        
        return "layout";
    }
    
}
