package com.team_proj.dsfw_team_proj.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/overview")
    public String showManagerOverview(Model model) {
        List<FakeOverviewDTO> employees = managerService.getFakeManagerDataForOverview();

        model.addAttribute("employees", employees);

        return "manager_templates/overview";
    }

    @GetMapping("/homepage")
    public ModelAndView homepage() {
        ModelAndView mav = new ModelAndView("manager/manager-homepage");
        return mav;
    }
}