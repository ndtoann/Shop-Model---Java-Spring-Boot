package webapp.shopmohinh.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import webapp.shopmohinh.dto.*;
import webapp.shopmohinh.global.GlobalCart;
import webapp.shopmohinh.model.*;
import webapp.shopmohinh.service.*;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;

    @GetMapping({ "/them-gio-hang/{id}" })
    public String addToCart(HttpSession session, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Product product = productService.getById(id);

        GlobalCart cart = (GlobalCart) session.getAttribute("cart");

        if (cart == null) {
            cart = new GlobalCart();
            session.setAttribute("cart", cart);
        }

        CartItem cartItem = new CartItem(product, 1);
        cart.addItem(cartItem);

        redirectAttributes.addFlashAttribute("successAddCart", "Thêm sản phẩm thành công");
        return "redirect:/gio-hang";
    }

    @GetMapping("/xoa-san-pham/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        GlobalCart cart = (GlobalCart) session.getAttribute("cart");

        if (cart != null) {
            cart.removeItem(productId);
        }

        redirectAttributes.addFlashAttribute("successDelCart", "Đã xóa sản phẩm");
        return "redirect:/gio-hang";
    }

    @GetMapping({ "/gio-hang" })
    public String listAction(Model model, HttpSession session) {
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);

        GlobalCart cart = (GlobalCart) session.getAttribute("cart");

        if (cart == null) {
            cart = new GlobalCart();
            session.setAttribute("cart", cart);
            model.addAttribute("cartEmpty", "Giỏ hàng của bạn chưa có gì");
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        model.addAttribute("totalPrice", cart.getTotalPrice());

        model.addAttribute("content", "cart");

        return "layout";
    }

    @GetMapping({ "/thanh-toan" })
    public String detailAction(Model model, HttpSession session) {
        List<Category> listCate = categoryService.getAll();
        model.addAttribute("listCate", listCate);

        GlobalCart cart = (GlobalCart) session.getAttribute("cart");

        if (cart == null) {
            return "redirect:/";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        model.addAttribute("totalPrice", cart.getTotalPrice());

        OrderDto order = new OrderDto();
        model.addAttribute("order", order);

        model.addAttribute("content", "checkout");
        return "layout";
    }

    @PostMapping({ "/postCheckout" })
    public String postCheckout(@Valid @ModelAttribute("order") OrderDto orderDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model, HttpSession session) {
        GlobalCart cart = (GlobalCart) session.getAttribute("cart");
        if (cart == null) {
            return "redirect:/";
        }
        if (result.hasErrors()) {
            System.out.println("Lỗi khi thanh toán");
            model.addAttribute("order", orderDto);
            model.addAttribute("content", "checkout");
            return "layout";
        }

        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for (CartItem item : cart.getItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct_name(item.getProduct().getProductName());
            orderDetail.setProduct_image(item.getProduct().getProduct_image());
            orderDetail.setPrice(item.getProduct().getPrice());
            orderDetail.setQuantity(item.getQuantity());

            orderDetails.add(orderDetail);
        }

        try {
            orderService.save(orderDto, orderDetails, cart.getTotalPrice());

            cart.clearItems();

            redirectAttributes.addFlashAttribute("successCheckout", "Đặt hàng thành công");
            return "redirect:/gio-hang";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorCheckout", "Đặt hàng không thành công");
            return "redirect:/gio-hang";
        }
    }

}
