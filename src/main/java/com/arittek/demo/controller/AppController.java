package com.arittek.demo.controller;

import com.arittek.demo.models.User;
import com.arittek.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AppController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepo;

    @GetMapping({"", "/login"})
    public String viewLoginPage() {
        // custom logic before showing login page...

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }else{
            return "redirect:/contacts";
        }

    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", new User());
        model.addAttribute("success", true);
        if(authentication==null || authentication instanceof AnonymousAuthenticationToken){
            model.addAttribute("authorized",false);
        }else{
            model.addAttribute("authorized",true);
        }
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user, Model model) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User userFromDb=null;

        userFromDb = userRepo.findByEmail(user.getEmail());
        String response = null;
        if(userFromDb != null){
            logger.info(" User Email changing "+userFromDb.getEmail());
            model.addAttribute("user-already-exists", user);
            model.addAttribute("isExistsUser", true);
            model.addAttribute("emailAlreadyExists","This Email is already registered ! "+userFromDb.getEmail());
            response=  "signup_form";

        }else{

            userRepo.save(user);
          //  model.addAttribute("User","");
            response= "register_success";
        }
        return response;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }
}
