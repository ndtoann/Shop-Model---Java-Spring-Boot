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
public class ProductAdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    private Set<String> VALID_IMAGE_TYPES = new HashSet<>(Set.of("image/jpeg", "image/png", "image/webp", "image/jpg"));

    @GetMapping({ "/admin/san-pham" })
    public String list(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        List<Product> listProducts = productService.getAll();
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("content", "admin/product/index");

        return "admin/layout";
    }

    @GetMapping({ "/admin/chi-tiet-san-pham" })
    public String detail(@RequestParam("id") Long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        Product product = productService.getById(id);
        if (product == null) {
            return "redirect:/admin";
        }
        model.addAttribute("product", product);
        model.addAttribute("content", "admin/product/detail");

        return "admin/layout";
    }

    @GetMapping({ "/admin/them-san-pham" })
    public String viewAdd(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());

        ProductDto productDto = new ProductDto();
        model.addAttribute("product", productDto);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("content", "admin/product/add");

        return "admin/layout";
    }

    @PostMapping({ "/admin/luu-san-pham" })
    public String postAdd(@Valid @ModelAttribute("product") ProductDto productDto,
            BindingResult result,
            @RequestParam("product_image") MultipartFile productImageFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("Đang thêm sản phẩm...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String timestamp = dateFormat.format(new Date());

        String contentTypeProduct = productImageFile.getContentType();
        String newProductImage = "";

        if (!(contentTypeProduct != null && VALID_IMAGE_TYPES.contains(contentTypeProduct))) {
            result.rejectValue("product_image", null, "Ảnh không hợp lệ");
        } else {
            String originalProductImage = productImageFile.getOriginalFilename();
            newProductImage = StringUtils.stripFilenameExtension(originalProductImage) +
                    "-" + timestamp +
                    "." + StringUtils.getFilenameExtension(originalProductImage);
            try {
                File saveFile = new ClassPathResource("static/uploads/product").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newProductImage);
                Files.copy(productImageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result.hasErrors()) {
            System.out.println("Lỗi dữ liệu sản phẩm");
            model.addAttribute("product", productDto);
            model.addAttribute("content", "admin/product/add");
            return "admin/layout";
        }
        try {
            productService.saveProductWithImages(productDto, newProductImage);

            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được thêm thành công");
            return "redirect:/admin/san-pham";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Thêm sản phẩm không thành công");
            return "redirect:/admin/them-san-pham";
        }
    }

    @GetMapping({ "/admin/cap-nhat-san-pham" })
    public String viewUpdate(@RequestParam("id") Long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()); 
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullname", user.getFullname());
        Product oldProduct = productService.getById(id);
        if (oldProduct == null) {
            return "redirect:/admin";
        }
        ProductDto newProduct = new ProductDto();
        newProduct.setProduct_name(oldProduct.getProductName());
        newProduct.setPrice(oldProduct.getPrice());
        newProduct.setDescription(oldProduct.getDescription());
        newProduct.setDetail(oldProduct.getDetail());
        newProduct.setStatus(oldProduct.getStatus());
        newProduct.setCategory(oldProduct.getCategory());
        newProduct.setId(oldProduct.getId());

        model.addAttribute("product", newProduct);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("content", "admin/product/update");

        return "admin/layout";
    }

    @PostMapping({ "/admin/postUpdateProduct" })
    public String postUpdate(@Valid @ModelAttribute("product") ProductDto productDto,
            BindingResult result,
            @RequestParam("product_image") MultipartFile ProductImageFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("Đang cập nhật sản phẩm...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String timestamp = dateFormat.format(new Date());

        String contentTypeProduct = ProductImageFile.getContentType();
        Long id = productDto.getId();
        Product oldProduct = productService.getById(id);
        String newProductImage = "";

        if (!(ProductImageFile != null && !ProductImageFile.isEmpty())) {
            newProductImage = oldProduct.getProduct_image();
        } else {
            if (!VALID_IMAGE_TYPES.contains(contentTypeProduct)) {
                result.rejectValue("product_image", null, "Ảnh không hợp lệ");
            } else {
                String originalProductImage = ProductImageFile.getOriginalFilename();
                newProductImage = StringUtils.stripFilenameExtension(originalProductImage) +
                        "-" + timestamp +
                        "." + StringUtils.getFilenameExtension(originalProductImage);
                try {
                    File saveFile = new ClassPathResource("static/uploads/product").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newProductImage);
                    Files.copy(ProductImageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (result.hasErrors()) {
            System.out.println("Lỗi dữ liệu sản phẩm");
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại dữ liệu");
            return "redirect:/admin/cap-nhat-san-pham?id=" + id;
        }
        try {
            productService.save(id, productDto, newProductImage);

            redirectAttributes.addFlashAttribute("success", "sản phẩm đã được cập nhật");
            return "redirect:/admin/cap-nhat-san-pham?id=" + id;
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Cập nhật sản phẩm không thành công");
            return "redirect:/admin/cap-nhat-san-pham?id=" + id;
        }
    }

    @GetMapping({ "/admin/xoa-san-pham/{id}" })
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes,
            Model model) {
        Product oldProduct = productService.getById(id);
        if (oldProduct == null) {
            return "redirect:/admin";
        }
        try {
            productService.deleteById(id);

            redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm");
            return "redirect:/admin/san-pham";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xóa sản phẩm không thành công");
            return "redirect:/admin/san-pham";
        }
    }

    @GetMapping({ "/admin/xoa-anh-san-pham/{productId}" })
    public String deleteImage(@PathVariable(value = "productId") Long productId, RedirectAttributes redirectAttributes,
            Model model) {
        try {
            productService.deleteImageById(productId);

            redirectAttributes.addFlashAttribute("success", "Đã xóa ảnh sản phẩm");
            return "redirect:/admin/san-pham";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Xóa ảnh sản phẩm không thành công");
            return "redirect:/admin/san-pham";
        }
    }
}
