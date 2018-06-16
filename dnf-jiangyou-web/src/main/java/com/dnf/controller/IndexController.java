package com.dnf.controller;

import com.dnf.bean.Users;
import com.dnf.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * DESCRIPTION:
 * Create on:2018/3/7.
 *
 * @author MACHUNHUI
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private UsersService uService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginview(){
        return "loginview";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@ModelAttribute("username") String username, @ModelAttribute("password") String password){
        if(username.equals("jeff") && password.equals("123")){
            return "index";
        }
        return "loginview";
    }
    @RequestMapping(value = "/login2", method = RequestMethod.GET)
    public String login2(@ModelAttribute("username") String username, @ModelAttribute("password") String password){
        System.out.println("-------控制层-------UserName："+username+"password："+ password);
        Users user = uService.getUsersByUsername(username);
        System.out.println("-------控制层----------"+user+"-----------------");
        if(user != null && user.getPassword().equals(password)){
            return "hello";
        }
        return "loginview";
    }
    @RequestMapping(value = "/login3", method = RequestMethod.GET)
    public String login3(){
        System.out.println("-------控制层-------login3开始发送");
        uService.sendMessage();
        System.out.println("-------控制层-------login3发送结束");
        return "loginview";
    }
}
