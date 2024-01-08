package webapp.shopmohinh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import webapp.shopmohinh.model.*;
import webapp.shopmohinh.service.*;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping({ "/blogs"})
    public String listAction(Model model) {
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);

        List<Blog> blogList = blogService.getAll();

        model.addAttribute("blogList", blogList);
        model.addAttribute("content", "blogs");

        return "layout";
    }

    @GetMapping({ "/blog"})
    public String detailAction(Model model, @RequestParam("id") long id) {
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);

        // Blog blog = blogService.getById(id);

        // model.addAttribute("blog", blog);
        model.addAttribute("content", "blog-detail");

        return "layout";
    }
}
