package ru.rassokhindanila.googleoauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.rassokhindanila.googleoauth.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Controller for home page
 */
@Controller
//Include language file
@PropertySource("classpath:lang.properties")
public class HomeController {

    @Autowired
    private Environment env;

    /**
     * Controller for home page
     * @return Home page
     */
    @GetMapping(value = {"/", "/home"})
    public String getHomePage(HttpSession httpSession, Model model)
    {
        //Trying to get username from session
        Object usernameObj = httpSession.getAttribute("username");
        model.addAttribute("is_authorized", usernameObj != null);
        if(usernameObj == null)
        {
            //User is not authorized
            //Preparing model
            model.addAttribute("greeting", env.getProperty("unauthorized_greeting"));
            model.addAttribute("sign_in_button", env.getProperty("sign_in_button"));
        }else{
            //User is authorized
            String username = (String)usernameObj;
            //Preparing greeting message
            HashMap<String, String> replacementData = new HashMap<>();
            replacementData.put("name", username);
            String greeting = StringUtils.replaceVariables(
                    env.getProperty("authorized_greeting"),
                    replacementData
            );
            //Preparing model
            model.addAttribute("greeting", greeting);
            model.addAttribute("sign_out_button", env.getProperty("sign_out_button"));
        }
        return "home";
    }
}