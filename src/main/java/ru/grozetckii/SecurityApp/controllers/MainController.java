package ru.grozetckii.SecurityApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.grozetckii.SecurityApp.security.PersonDetails;

import java.util.Map;

@RestController
@RequestMapping("/main")
public class MainController {
    @GetMapping("/showUserInfo")
    @ResponseBody
    public Map<String, String> showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return Map.of("username", personDetails.getUsername());
    }
}
