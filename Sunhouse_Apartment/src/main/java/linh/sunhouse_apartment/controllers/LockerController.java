package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LockerController {

    @Autowired
    LockerService lockerService;

    @GetMapping("/manage-locker")
    public String manageLocker(@RequestParam(value = "kw", required = false) String keyword,Model model){
        List<Locker> lockers = lockerService.getAllLockers(keyword);
        model.addAttribute("lockers",lockers);
        model.addAttribute("kw", keyword);
        return "manageLocker";
    }
    @GetMapping("/locker/{id}")
    public String viewLockerDetail(@PathVariable("id") int id, Model model) {
        Locker locker = lockerService.getLockerById(id);
        if (locker == null) {
            return "redirect:/manage-locker";
        }
        model.addAttribute("locker", locker);
        return "list_packages";
    }
}
