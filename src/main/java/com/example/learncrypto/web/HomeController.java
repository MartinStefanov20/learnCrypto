package com.example.learncrypto.web;

import com.example.learncrypto.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @GetMapping("/home")
    public ModelAndView home(Principal principal) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");


        //List<GrantedAuthority> authorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

//        modelAndView.setViewName('home');
//        boolean doctor = false;
//        boolean admin = false;
//
//        for (GrantedAuthority authority : authorities) {
//            if (authority.getAuthority().equals("ROLE_DOCTOR")){
//                doctor = true;
//            }
//            if (authority.getAuthority().equals("ROLE_ADMIN")){
//                admin = true;
//            }
//        }
//
//        if (doctor && !admin) {
//            modelAndView.setViewName("doctors/doctors-home");
//        }
//        if (doctor && admin) {
//            modelAndView.setViewName("admin/admin-home");
//        }
//        if (!doctor && admin){
//            modelAndView.setViewName("admin/admin-home");
//        }

        return modelAndView;
    }

    @PostMapping("/home")
    public String homePost() {
        return "redirect:/home";
    }

//    @GetMapping("/admin/user")
//    public String logAdminAsUser() {
//        return "home";
//    }
//
//    @GetMapping("/admin/doctor")
//    public String logAdminAsDoctor() {
//        return "doctors/doctors-home";
//    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "about-us";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }

    @GetMapping("/basics")
    public String basics(){
        return "basics";
    }

    @GetMapping("/earn-crypto")
    public String earnCrypto(){
        return "earn-crypto";
    }
    @GetMapping("/trade-crypto")
    public String tradeCrypto(){
        return "trade-crypto";
    }
    @GetMapping("/use-crypto")
    public String useCrypto(){
        return "use-crypto";
    }



}
