package com.example.demoproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home controller for serving the main application pages
 */
@Controller
public class HomeController {

    /**
     * Serve the home page
     * @return index view
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    /**
     * Serve the login page
     * @return login view
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * Serve the lucky draw page
     * @return luckydraw view
     */
    @GetMapping("/luckydraw")
    public String luckyDraw() {
        return "luckydraw";
    }
    
    /**
     * Serve the calculator page
     * @return calculator view
     */
    @GetMapping("/calculator")
    public String calculator() {
        return "calculator";
    }
    
    /**
     * Serve the admin page
     * @return admin view
     */
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    

} 