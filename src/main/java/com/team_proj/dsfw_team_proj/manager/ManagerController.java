package com.team_proj.dsfw_team_proj.manager;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagerController {


    @GetMapping("/homepage")
    public ModelAndView homepage() {
        ModelAndView mav = new ModelAndView("manager/manager-homepage");
        return mav;
    }
}
