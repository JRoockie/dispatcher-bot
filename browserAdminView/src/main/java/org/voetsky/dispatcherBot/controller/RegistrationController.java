package org.voetsky.dispatcherBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.voetsky.dispatcherBot.repository.viewEntities.AdminUser.AdminUser;
import org.voetsky.dispatcherBot.repository.viewEntities.AdminUser.AdminUserRepo;

import java.util.Collections;
import java.util.Map;

import static org.voetsky.dispatcherBot.repository.viewEntities.Role.USER;

@Controller
public class RegistrationController {

    @Autowired
    private AdminUserRepo adminUserRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(AdminUser newUser, Map<String, Object> model){
        AdminUser fromDb = adminUserRepo.findAdminUserByUsername(newUser.getUsername());
        if (fromDb != null){
            model.put("message", "User exists!");
            return "registration";
        }

        newUser.setActive(true);
        newUser.setRoles(Collections.singleton(USER));
        adminUserRepo.save(newUser);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
