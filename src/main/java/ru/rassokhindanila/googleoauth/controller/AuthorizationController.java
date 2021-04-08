package ru.rassokhindanila.googleoauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import ru.rassokhindanila.googleoauth.service.Impl.GoogleWebClient;
import ru.rassokhindanila.googleoauth.service.OAuthWebClient;
import ru.rassokhindanila.googleoauth.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Controller for authorization process
 */
@Controller
//Include language file
@PropertySource("classpath:lang.properties")
public class AuthorizationController {

    @Autowired
    private OAuthWebClient oAuthWebClient;

    @Autowired
    private Environment env;

    /**
     * Controller for Sign In page
     * @return Sign in page
     */
    @GetMapping("/signin")
    public String getSignInPage(HttpSession httpSession, Model model)
    {
        //Trying to get username from session
        Object usernameObj = httpSession.getAttribute("username");
        if(usernameObj != null)
        {
            //User is authorized
            String username = (String)usernameObj;
            //Preparing greeting message
            HashMap<String, String> replacementData = new HashMap<>();
            replacementData.put("name", username);
            String greeting = StringUtils.replaceVariables(
                    env.getProperty("already_authorized"),
                    replacementData
            );
            //Preparing model
            model.addAttribute("is_authorized", true);
            model.addAttribute("already_authorized", greeting);
            model.addAttribute("home_button", env.getProperty("home_button"));
        }else{
            //User is not authorized
            //Preparing model
            model.addAttribute("is_authorized", false);
            model.addAttribute("sign_in_button_google", env.getProperty("sign_in_button_google"));
        }
        return "sign_in";
    }

    /**
     * Starts Google OAuth authorization
     */
    @GetMapping("/proceedSignInGoogle")
    public String proceedSignIn()
    {
        //Getting authorization url
        String authUrl = oAuthWebClient.getAuthUrl();
        //Redirecting user to authorization url
        return "redirect:"+authUrl;
    }


    /**
     * @param code Authorization code from Google OAuth
     * @param error Error message
     * Completes Google OAuth Authorization
     */
    @GetMapping("/completeOAuthGoogle")
    public DeferredResult<String> completeOAuthGoogle(@RequestParam(name = "code", required = false) String code,
                                              @RequestParam(name = "error", required = false) String error,
                                              Model model)
    {
        DeferredResult<String> response = new DeferredResult<>();
        //Received error param after authorization
        if(error != null)
        {
            //Preparing model to show error
            model.addAttribute("error", "An error has occurred: "+error);
            response.setResult("error");
        }
        //Didn't receive authorization code
        if(code == null)
        {
            //Showing error
            model.addAttribute("error", "Wrong request data");
            response.setResult("error");
        }
        GoogleWebClient googleWebClient = (GoogleWebClient)oAuthWebClient;
        if(googleWebClient != null)
        {
            //Exchanging authorization code to access token
            googleWebClient.getTokenInfo(code,
                    googleTokenInfo -> {
                        response.setResult("redirect:/completeSignInGoogle?access_token=" + googleTokenInfo.getAccessToken());
                    },
                    e -> {
                        model.addAttribute("error",
                                "Can't exchange authorization code to access token");
                        response.setResult("error");
                    }
            );
        }else{
            model.addAttribute("error", "GoogleWebClient error");
            response.setResult("error");
        }
        return response;
    }

    /**
     * @param token Token received after authorization code exchange
     */
    @GetMapping("/completeSignInGoogle")
    public DeferredResult<String> completeSignInGoogle(@RequestParam("access_token") String token,
                                       HttpSession httpSession, Model model)
    {
        DeferredResult<String> response = new DeferredResult<>();
        GoogleWebClient googleWebClient = (GoogleWebClient)oAuthWebClient;
        if(googleWebClient != null) {
            //Retrieving user information from Google
            googleWebClient.getUserInfo(token,
                    googleUserInfo -> {
                        httpSession.setAttribute("username", googleUserInfo.getName());
                        response.setResult("redirect:/home");
                    },
                    e -> {
                        model.addAttribute("error", "Failed retrieving user info");
                        response.setResult("error");
                    }
            );
        }
        return response;
    }

    /**
     * Makes user unauthorized
     */
    @GetMapping("/signout")
    public String proceedSignOut(HttpSession httpSession)
    {
        httpSession.setAttribute("username", null);
        return "redirect:/home";
    }
}