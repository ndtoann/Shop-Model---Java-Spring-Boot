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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    private Set<String> VALID_IMAGE_TYPES = new HashSet<>(Set.of("image/jpeg", "image/png", "image/webp", "image/jpg"));

    @GetMapping({ "/admin/danh-muc" })
    public String list(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        List<Category> listCategories = categoryService.getAll();
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("content", "admin/category/index");

        return "admin/layout";
    }

    @GetMapping({ "/admin/them-danh-muc" })
    public String viewAdd(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("category", categoryDto);
        model.addAttribute("content", "admin/category/add");

        return "admin/layout";
    }

    @PostMapping({ "/admin/luu-danh-muc" })
    public String postAdd(@Valid @ModelAttribute("category") CategoryDto categoryDto,
            BindingResult result,
            @RequestParam("cate_image") MultipartFile cateImageFile,
            @RequestParam("banner") MultipartFile bannerFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("Đang thêm danh mục...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String timestamp = dateFormat.format(new Date());

        String contentTypeCate = cateImageFile.getContentType();
        String contentTypeBanner = bannerFile.getContentType();
        String newCateImage = "", newBannerImage = "";

        if (!(contentTypeCate != null && VALID_IMAGE_TYPES.contains(contentTypeCate))) {
            result.rejectValue("cate_image", null, "Ảnh không hợp lệ");
        } else {
            String originalCateImage = cateImageFile.getOriginalFilename();
            newCateImage = StringUtils.stripFilenameExtension(originalCateImage) +
                    "-" + timestamp +
                    "." + StringUtils.getFilenameExtension(originalCateImage);
            try {
                File saveFile = new ClassPathResource("static/uploads/category").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newCateImage);
                Files.copy(cateImageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!(contentTypeBanner != null && VALID_IMAGE_TYPES.contains(contentTypeBanner))) {
            result.rejectValue("banner", null, "Ảnh không hợp lệ");
        } else {
            String originalBanner = bannerFile.getOriginalFilename();
            newBannerImage = StringUtils.stripFilenameExtension(originalBanner) +
                    "-" + timestamp +
                    "." + StringUtils.getFilenameExtension(originalBanner);
            try {
                File saveFile = new ClassPathResource("static/uploads/category").getFile();
                Path path = Paths
                        .get(saveFile.getAbsolutePath() + File.separator + newBannerImage);
                Files.copy(bannerFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result.hasErrors()) {
            System.out.println("Lỗi dữ liệu danh mục");
            model.addAttribute("category", categoryDto);
            model.addAttribute("content", "admin/category/add");
            return "admin/layout";
        }
        try {
            categoryService.save(null, categoryDto, newCateImage, newBannerImage);

            redirectAttributes.addFlashAttribute("success", "Danh mục đã được thêm thành công");
            return "redirect:/admin/danh-muc";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Thêm danh mục không thành công");
            return "redirect:/admin/them-danh-muc";
        }
    }

    @GetMapping({ "/admin/cap-nhat-danh-muc" })
    public String viewUpdate(@RequestParam("id") Long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());
        Category oldCategory = categoryService.getById(id);
        if (oldCategory == null) {
            return "redirect:/admin";
        }
        CategoryDto newCategory = new CategoryDto();
        newCategory.setCate_name(oldCategory.getCate_name());
        newCategory.setStatus(oldCategory.getStatus());
        newCategory.setId(oldCategory.getId());

        model.addAttribute("category", newCategory);
        model.addAttribute("content", "admin/category/update");

        return "admin/layout";
    }

    @PostMapping({ "/admin/postUpdateCate" })
    public String postUpdate(@Valid @ModelAttribute("category") CategoryDto categoryDto,
            BindingResult result,
            @RequestParam("cate_image") MultipartFile cateImageFile,
            @RequestParam("banner") MultipartFile bannerFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("Đang cập nhật danh mục...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String timestamp = dateFormat.format(new Date());

        String contentTypeCate = cateImageFile.getContentType();
        String contentTypeBanner = bannerFile.getContentType();
        Long id = categoryDto.getId();
        Category oldCate = categoryService.getById(id);
        String newCateImage = "", newBannerImage = "";

        if (!(cateImageFile != null && !cateImageFile.isEmpty())) {
            newCateImage = oldCate.getCate_image();
        } else {
            if (!VALID_IMAGE_TYPES.contains(contentTypeCate)) {
                result.rejectValue("cate_image", null, "Ảnh không hợp lệ");
            } else {
                String originalCateImage = cateImageFile.getOriginalFilename();
                newCateImage = StringUtils.stripFilenameExtension(originalCateImage) +
                        "-" + timestamp +
                        "." + StringUtils.getFilenameExtension(originalCateImage);
                try {
                    File saveFile = new ClassPathResource("static/uploads/category").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newCateImage);
                    Files.copy(cateImageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!(bannerFile != null && !bannerFile.isEmpty())) {
            newBannerImage = oldCate.getBanner();
        } else {
            if (!VALID_IMAGE_TYPES.contains(contentTypeBanner)) {
                result.rejectValue("banner", null, "Ảnh không hợp lệ");
            } else {
                String originalBanner = bannerFile.getOriginalFilename();
                newBannerImage = StringUtils.stripFilenameExtension(originalBanner) +
                        "-" + timestamp +
                        "." + StringUtils.getFilenameExtension(originalBanner);
                try {
                    File saveFile = new ClassPathResource("static/uploads/category").getFile();
                    Path path = Paths
                            .get(saveFile.getAbsolutePath() + File.separator + newBannerImage);
                    Files.copy(bannerFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (result.hasErrors()) {
            System.out.println("Lỗi dữ liệu danh mục");
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại dữ liệu");
            return "redirect:/admin/cap-nhat-danh-muc?id=" + id;
        }
        try {
            categoryService.save(id, categoryDto, newCateImage, newBannerImage);

            redirectAttributes.addFlashAttribute("success", "Danh mục đã được cập nhật");
            return "redirect:/admin/cap-nhat-danh-muc?id=" + id;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Cập nhật danh mục không thành công");
            return "redirect:/admin/cap-nhat-danh-muc?id=" + id;
        }
    }

    @GetMapping({ "/admin/xoa-danh-muc/{id}" })
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes,
            Model model) {
        Category oldCategory = categoryService.getById(id);
        if (oldCategory == null) {
            return "redirect:/admin";
        }
        try {
            categoryService.deleteById(id);

            redirectAttributes.addFlashAttribute("success", "Đã xóa danh mục");
            return "redirect:/admin/danh-muc";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xóa danh mục không thành công");
            return "redirect:/admin/danh-muc";
        }
    }
}
