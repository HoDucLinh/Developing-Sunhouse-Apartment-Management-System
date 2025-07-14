package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.auth.CustomUserDetail;
import linh.sunhouse_apartment.dtos.request.EditProfileRequest;
import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.services.FloorService;
import linh.sunhouse_apartment.services.RoomService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    FloorService floorService;

    @GetMapping("/manage-user")
    public String manageUser(){
        return "manageUser";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        model.addAttribute("floors", floorService.findAll());
        model.addAttribute("rooms", List.of());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(
            @ModelAttribute("user") User user,
            @RequestParam(value = "roomIdParam", required = false) Integer roomId,
            @RequestParam(value = "floorId", required = false) Integer floorId
    ) {
        if (user.getRole() == User.Role.RESIDENT && roomId != null) {
            Room room = roomService.findById(roomId);
            user.setRoomId(room);
        } else {
            user.setRoomId(null);
        }

        user.setIsActive(true);
        user.setCreatedAt(new Date());
        userService.createUser(user);

        return "redirect:/manage-user";
    }

    @GetMapping("/register/rooms")
    public String getRoomsByFloor(@RequestParam("floorId") Integer floorId, Model model) {
        List<Room> rooms = roomService.findByFloorId(floorId);
        model.addAttribute("rooms", rooms);
        return "fragments/room-options :: roomOptions";
    }


    @GetMapping("/edit-profile")
    public String showEditForm(Authentication authentication, Model model) {
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Integer id = userDetails.getId();

        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "editProfile"; // Thymeleaf file
    }

    @PostMapping("/edit-profile")
    public String editUserProfile(
            @ModelAttribute EditProfileRequest request,
            @RequestParam("file") MultipartFile file,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        try {
            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
            Integer id = userDetails.getId();

            User updatedUser = userService.editProfile(id, request, file);

            CustomUserDetail updatedDetails = new CustomUserDetail(updatedUser);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedDetails, authentication.getCredentials(), updatedDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(newAuth);

            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/edit-profile";
    }
}
