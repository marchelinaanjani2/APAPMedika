package apap.ti.appointment2206082612.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import apap.ti.appointment2206082612.service.AppointmentService;

@Controller
public class HomeController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/")
    public String home(Model model) {
        // int todayAppointmentCount = appointmentService.countTodayAppointments();
        model.addAttribute("todayAppointmentCount", 0);
        model.addAttribute("page", "home");
        return "home";
    }
}

