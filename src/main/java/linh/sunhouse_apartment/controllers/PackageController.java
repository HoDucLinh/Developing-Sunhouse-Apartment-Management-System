package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.PackageRequest;
import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.services.EmailService;
import linh.sunhouse_apartment.services.PackageService;
import linh.sunhouse_apartment.services.LockerService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private LockerService lockerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    // Hiển thị danh sách packages của 1 locker
    @GetMapping("/packages/{lockerId}")
    public String getPackages(@PathVariable("lockerId") int lockerId,@RequestParam(value = "kw", required = false) String kw, Model model) {
        Locker locker = lockerService.getLockerById(lockerId);
        if(locker != null){
            List<Package> packages = packageService.getPackages(locker.getId(),kw);
            model.addAttribute("packages", packages);
            model.addAttribute("lockerId", lockerId);
            return "list_packages";
        }
        return "redirect:/error";
    }

    // Hiển thị form tạo mới package
    @GetMapping("/create-package")
    public String showCreateForm(@RequestParam("lockerId") int lockerId, Model model) {
        model.addAttribute("lockerId", lockerId);
        return "create-package";
    }

    // Submit tạo mới package
    @PostMapping("/create-package")
    public String addPackage(@ModelAttribute PackageRequest packageRequest, Principal principal) {
        User sender = userService.getUserByUsername(principal.getName());
        if(sender == null){
            throw new RuntimeException("Username not found");
        }
        Package newPackage = packageService.addPackage(packageRequest, sender);
        User user = userService.getUserById(packageRequest.getLockerId());
        if(sender != null && sender.getEmail() != null) {
            try {
                emailService.sendNewPackageNotification(user.getEmail(), packageRequest.getName(), newPackage.getCreatedAt());
            } catch (jakarta.mail.MessagingException e) {
                e.printStackTrace(); // log lỗi
                return "Lỗi khi gửi thông báo";
            }
        }

        return "redirect:/packages/" + packageRequest.getLockerId();
    }

    // Xoá package
    @GetMapping("/delete-package/{id}")
    public String deletePackage(@PathVariable("id") int packageId, @RequestParam("lockerId") int lockerId) {
        packageService.deletePackage(packageId);
        return "redirect:/packages/" + lockerId;
    }

    @PostMapping("/change-package-status")
    public String changePackageStatus(
            @RequestParam("packageId") int packageId,
            @RequestParam("newStatus") Package.Status newStatus,
            @RequestParam("lockerId") int lockerId,
            Principal principal
    ) {
        User receiver = userService.getUserByUsername(principal.getName());
        if(receiver == null){
            throw new RuntimeException("Username not found");
        }
        packageService.changeStatusPackage(packageId, newStatus, receiver);
        return "redirect:/packages/" + lockerId;
    }
}
