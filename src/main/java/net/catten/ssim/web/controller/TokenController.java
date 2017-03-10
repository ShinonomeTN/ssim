package net.catten.ssim.web.controller;

import net.catten.ssim.web.services.SimpleUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cattenlinger on 2017/3/9.
 */
@Controller
@RequestMapping("/secure/token")
public class TokenController {

    private SimpleUserServices userServices;

    @Autowired
    public void setUserServices(SimpleUserServices userServices) {
        this.userServices = userServices;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Model getToken(@RequestParam("username") String username, @RequestParam("password") String password, Model model){
        String token = userServices.generateToken(username,password);
        model.addAttribute("token",token);
        model.addAttribute("expireDate",userServices.tokenExpireDate(token));
        return model;
    }

}
