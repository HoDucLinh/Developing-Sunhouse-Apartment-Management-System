package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.auth.CustomUserDetail;
import linh.sunhouse_apartment.dtos.request.EditProfileRequest;
import linh.sunhouse_apartment.entity.User;
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

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/manage-user")
    public String manageUser(){
        return "manageUser";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @PostMapping("/register")
    public String registerSubmit(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email
//            @RequestParam("role") String role,
//            @RequestParam(value = "roomId", required = false) Integer roomId,
//            @RequestParam(value = "floorId", required = false) Integer floorId, // thêm nếu cần
//            HttpSession session,
//            Model model // Thêm model để truyền lỗi
    ) {
        System.out.println("Processing /register POST with username: " + username);

        // Lưu lại thông tin form để hiển thị lại nếu có lỗi
//        Map<String, String> formData = new HashMap<>();
//        formData.put("username", username);
//        formData.put("fullName", fullName);
//        formData.put("phone", phone);
//        formData.put("email", email);
        //session.setAttribute("registerFormData", formData);

//        // Kiểm tra username đã tồn tại chưa
//        if (this.userService(username) != null) {
//            model.addAttribute("errorMsg", "Tên người dùng đã tồn tại!");
//            return register(floorId, session, model); // Gọi lại form với dữ liệu và thông báo lỗi
//        } else {

            // Nếu chưa tồn tại, tiếp tục đăng ký
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setEmail(email);

            boolean result = userService.createUser(user);
            if(result) {
                return "redirect:/manage-user";
            }
            else {
                return "redirect:/";
            }
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
