package com.alemdar_energy.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alemdar_energy.backend.model.Cart;
import com.alemdar_energy.backend.model.User;
import com.alemdar_energy.backend.service.CartService;
import com.alemdar_energy.backend.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;
  private final UserService userService;

  @Autowired
  public CartController(CartService cartService, UserService userService) {
    this.cartService = cartService;
    this.userService = userService;
  }

  // Test kullanıcı doğrulama
  @GetMapping("/testUser")
  public String testUser(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "redirect:/login"; // Giriş yapmamışsa login sayfasına yönlendir
    }
    return "redirect:/cart/"; // Eğer kullanıcı giriş yapmışsa sepete yönlendir
  }

  // Kullanıcının sepetini al ve göster
  @GetMapping("/")
  public String showCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {

    if (userDetails == null) {
      return "redirect:/login"; // Giriş yapmamışsa login sayfasına yönlendir
    }

    User user = userService.findByUsername(userDetails.getUsername());
    if (user == null) {
      model.addAttribute("error", "Kullanıcı bulunamadı.");
      return "error"; // Hata sayfasına yönlendir
    }

    // Kullanıcının sepetini al
    Cart cart = cartService.getCartByUser(user);

    // Toplam fiyatı hesaplamak için getTotalPrice metodunu kullanıyoruz
    double totalPrice = cart.getTotalPrice();

    model.addAttribute("cart", cart);
    model.addAttribute("totalPrice", totalPrice); // Toplam fiyatı modelde ekliyoruz

    return "products/cart"; // Sepet sayfasını göster
  }

  // Sepete ürün ekleme işlemi
  @GetMapping("/add")
  public String addToCart(@AuthenticationPrincipal UserDetails userDetails,
      @RequestParam Long productId,
      @RequestParam int quantity,
      Model model) {
    if (userDetails == null) {
      return "redirect:/login"; // Giriş yapmamışsa login sayfasına yönlendir
    }

    User theUser = userService.findByUsername(userDetails.getUsername());
    if (theUser == null) {
      model.addAttribute("error", "Kullanıcı bulunamadı.");
      return "error"; // Hata sayfasına yönlendir
    }

    // Sepete ürün ekleme işlemi
    try {
      cartService.addItemToCart(theUser, productId, quantity);
    } catch (Exception e) {
      model.addAttribute("error", "Ürün eklenirken bir hata oluştu: " + e.getMessage());
      return "error"; // Hata sayfasına yönlendir
    }

    return "redirect:/cart/"; // Sepet sayfasına yönlendir
  }

  // Sepetten ürün çıkarma işlemi
  @PostMapping("/remove")
  public String removeFromCart(@AuthenticationPrincipal UserDetails user,
      @RequestParam Long productId,
      Model model) {
        User tempUser = userService.findByUsername(user.getUsername());
        System.out.println("REmove methodundaki user  : "+user);
    if (tempUser == null) {
      // Kullanıcı giriş yapmamışsa login sayfasına yönlendir
      return "redirect:/login"; // Hata sayfasına gitmek yerine login sayfasına yönlendirelim
    }

    try {
      cartService.removeItemFromCart(tempUser, productId);
    } catch (Exception e) {
      model.addAttribute("error", "Ürün çıkarılı rken bir hata oluştu: " + e.getMessage());
      return "error"; // Hata sayfasına yönlendir
    }
    

    return "redirect:/cart/"; // Sepet sayfasına yönlendir
  }

  // Sepeti temizleme işlemi
  @PostMapping("/clear")
  public String clearCart(@AuthenticationPrincipal UserDetails user, Model model) {
    
    User tempUser = userService.findByUsername(user.getUsername());

    if (tempUser == null) {
      System.out.println(user);
      // Kullanıcı giriş yapmamışsa login sayfasına yönlendir
      return "redirect:/login"; // Hata sayfasına gitmek yerine login sayfasına yönlendirelim
    }

    try {
      cartService.clearCart(tempUser);
    } catch (Exception e) {
      model.addAttribute("error", "Sepet temizlenirken bir hata oluştu: " + e.getMessage());
      return "error"; // Hata sayfasına yönlendir
    }

    return "redirect:/cart/"; // Sepet sayfasına yönlendir
  }

}
