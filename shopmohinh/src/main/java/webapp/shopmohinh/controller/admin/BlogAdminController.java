package webapp.shopmohinh.controller.admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import webapp.shopmohinh.dto.*;
import webapp.shopmohinh.model.*;
import webapp.shopmohinh.service.*;

@Controller
public class BlogAdminController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    private Set<String> VALID_IMAGE_TYPES = new HashSet<>(Set.of("image/jpeg", "image/png", "image/webp", "image/jpg"));

    @GetMapping({ "/admin/blog" })
    public String list(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        List<Blog> listBlogs = blogService.getAll();
        model.addAttribute("listBlogs", listBlogs);
        model.addAttribute("content", "admin/blog/index");

        return "admin/layout";
    }

    @GetMapping({ "/admin/chi-tiet-blog" })
    public String detail(@RequestParam("id") Long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        Blog blog = blogService.getById(id);
        if (blog == null) {
            return "redirect:/admin";
        }
        model.addAttribute("blog", blog);
        model.addAttribute("content", "admin/blog/detail");

        return "admin/layout";
    }

    @GetMapping({ "/admin/them-blog" })
    public String viewAdd(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        BlogDto blogDto = new BlogDto();
        model.addAttribute("blog", blogDto);
        model.addAttribute("content", "admin/blog/add");

        return "admin/layout";
    }

    @PostMapping({ "/admin/luu-blog" })
    public String postAdd(@Valid @ModelAttribute("blog") BlogDto blogDto,
            BindingResult result,
            @RequestParam("blog_image") MultipartFile blogImageFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("Đang thêm blog...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String timestamp = dateFormat.format(new Date());

        String contentTypeBlog = blogImageFile.getContentType();
        String newBlogImage = "";

        if (!(contentTypeBlog != null && VALID_IMAGE_TYPES.contains(contentTypeBlog))) {
            result.rejectValue("blog_image", null, "Ảnh không hợp lệ");
        } else {
            String originalBlogImage = blogImageFile.getOriginalFilename();
            newBlogImage = StringUtils.stripFilenameExtension(originalBlogImage) +
                    "-" + timestamp +
                    "." + StringUtils.getFilenameExtension(originalBlogImage);
            try {
                File saveFile = new ClassPathResource("static/uploads/blog").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newBlogImage);
                Files.copy(blogImageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result.hasErrors()) {
            System.out.println("Lỗi dữ liệu blog");
            model.addAttribute("blog", blogDto);
            model.addAttribute("content", "admin/blog/add");
            return "admin/layout";
        }
        try {
            blogService.save(null, blogDto, newBlogImage);

            redirectAttributes.addFlashAttribute("success", "Blog đã được thêm thành công");
            return "redirect:/admin/blog";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Thêm blog không thành công");
            return "redirect:/admin/them-blog";
        }
    }

    @GetMapping({ "/admin/cap-nhat-blog" })
    public String viewUpdate(@RequestParam("id") Long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());
        Blog oldBlog = blogService.getById(id);
        if (oldBlog == null) {
            return "redirect:/admin";
        }
        BlogDto newBlog = new BlogDto();
        newBlog.setTitle(oldBlog.getTitle());
        newBlog.setBlog_content(oldBlog.getBlog_content());
        newBlog.setStatus(oldBlog.getStatus());
        newBlog.setId(oldBlog.getId());

        model.addAttribute("blog", newBlog);
        model.addAttribute("content", "admin/blog/update");

        return "admin/layout";
    }

    @PostMapping({ "/admin/postUpdateBlog" })
    public String postUpdate(@Valid @ModelAttribute("blog") BlogDto blogDto,
            BindingResult result,
            @RequestParam("blog_image") MultipartFile blogImageFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("Đang cập nhật blog...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String timestamp = dateFormat.format(new Date());

        String contentTypeBlog = blogImageFile.getContentType();
        Long id = blogDto.getId();
        Blog oldBlog = blogService.getById(id);
        String newBlogImage = "";

        if (!(blogImageFile != null && !blogImageFile.isEmpty())) {
            newBlogImage = oldBlog.getBlog_image();
        } else {
            if (!VALID_IMAGE_TYPES.contains(contentTypeBlog)) {
                result.rejectValue("blog_image", null, "Ảnh không hợp lệ");
            } else {
                String originalBlogImage = blogImageFile.getOriginalFilename();
                newBlogImage = StringUtils.stripFilenameExtension(originalBlogImage) +
                        "-" + timestamp +
                        "." + StringUtils.getFilenameExtension(originalBlogImage);
                try {
                    File saveFile = new ClassPathResource("static/uploads/Blog").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newBlogImage);
                    Files.copy(blogImageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (result.hasErrors()) {
            System.out.println("Lỗi dữ liệu blog");
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại dữ liệu");
            return "redirect:/admin/cap-nhat-blog?id=" + id;
        }
        try {
            blogService.save(id, blogDto, newBlogImage);

            redirectAttributes.addFlashAttribute("success", "Blog đã được cập nhật");
            return "redirect:/admin/cap-nhat-blog?id=" + id;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Cập nhật blog không thành công");
            return "redirect:/admin/cap-nhat-blog?id=" + id;
        }
    }

    @GetMapping({ "/admin/xoa-blog/{id}" })
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes,
            Model model) {
        Blog oldBlog = blogService.getById(id);
        if (oldBlog == null) {
            return "redirect:/admin";
        }
        try {
            blogService.deleteById(id);

            redirectAttributes.addFlashAttribute("success", "Đã xóa blog");
            return "redirect:/admin/blog";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xóa blog không thành công");
            return "redirect:/admin/blog";
        }
    }
}
