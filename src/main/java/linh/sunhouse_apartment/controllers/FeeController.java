package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class FeeController {
    @Autowired
    private FeeService feeService;

    // Hiển thị danh sách Fee
    @GetMapping("/manage-fee")
    public String listFees(Model model,
                           @RequestParam Map<String, String> params) {
        model.addAttribute("fees", feeService.getFees(params));
        model.addAttribute("params", params); // để giữ lại giá trị trên form search
        return "manageFee";
    }

    // Xử lý thêm Fee mới
    @PostMapping("/add-fee")
    public String addFee(@ModelAttribute("fee") Fee fee,
                         RedirectAttributes redirectAttributes) {
        try {
            feeService.addFee(fee);
            redirectAttributes.addFlashAttribute("success", "Thêm phí thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/manage-fee";
    }
    // Hiển thị form tạo mới Fee
    @GetMapping("/add-fee")
    public String showAddFeeForm(Model model) {
        model.addAttribute("fee", new Fee());
        return "addFee"; // trỏ tới file addFee.html
    }

}
