package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.services.PackageService;
import linh.sunhouse_apartment.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private LockerService lockerService;

    // Hiển thị danh sách packages của 1 locker
    @GetMapping("/packages/{lockerId}")
    public String getPackages(@PathVariable("lockerId") int lockerId, Model model) {
        List<Package> packages = packageService.getPackages(lockerId);
        model.addAttribute("packages", packages);
        model.addAttribute("lockerId", lockerId);
        return "list_packages";
    }

    // Hiển thị form tạo mới package
    @GetMapping("/create-package")
    public String showCreateForm(@RequestParam("lockerId") int lockerId, Model model) {
        model.addAttribute("lockerId", lockerId);
        return "create-package";
    }

    // Submit tạo mới package
    @PostMapping("/create-package")
    public String addPackage(
            @RequestParam("lockerId") int lockerId,
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file
    ) {
        Package newPackage = packageService.addPackage(name, file, lockerId);
        newPackage.setLockerId(lockerService.getLockerById(lockerId)); // gán locker cho package
        return "redirect:/packages/" + lockerId;
    }

    // Xoá package
    @GetMapping("/delete-package/{id}")
    public String deletePackage(@PathVariable("id") int packageId, @RequestParam("lockerId") int lockerId) {
        packageService.deletePackage(packageId);
        return "redirect:/locker/" + lockerId;
    }

    @PostMapping("/change-package-status")
    public String changePackageStatus(
            @RequestParam("packageId") int packageId,
            @RequestParam("newStatus") Package.Status newStatus,
            @RequestParam("lockerId") int lockerId
    ) {
        packageService.changeStatusPackage(packageId, newStatus);
        return "redirect:/packages/" + lockerId;
    }
}
