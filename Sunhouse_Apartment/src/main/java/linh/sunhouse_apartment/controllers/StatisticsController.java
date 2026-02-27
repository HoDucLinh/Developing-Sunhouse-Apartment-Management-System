package linh.sunhouse_apartment.controllers;

import jakarta.servlet.http.HttpServletResponse;
import linh.sunhouse_apartment.auth.CustomUserDetail;
import linh.sunhouse_apartment.services.InvoiceService;
import linh.sunhouse_apartment.services.PdfService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatisticsController {

    @Autowired
    UserService userService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    PdfService pdfService;

    @GetMapping("/statistics")
    public String getStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "month") String period,
            Model model
    ) {
        if (year == null) {
            year = java.time.Year.now().getValue();
        }

        // --- Residents statistics ---
        Map<Integer, Long> residentStats = userService.getResidentStatistics(year, period);
        List<Integer> residentLabels = new ArrayList<>(residentStats.keySet());
        List<Long> residentValues = new ArrayList<>(residentStats.values());

        // --- Revenue statistics ---
        Map<Integer, BigDecimal> revenueStats = invoiceService.getRevenueStatistics(year, period);
        List<Integer> revenueLabels = new ArrayList<>(revenueStats.keySet());
        List<BigDecimal> revenueValues = new ArrayList<>(revenueStats.values());

        // Add to model
        model.addAttribute("year", year);
        model.addAttribute("period", period);

        model.addAttribute("residentStats", residentStats);
        model.addAttribute("residentLabels", residentLabels);
        model.addAttribute("residentValues", residentValues);

        model.addAttribute("revenueStats", revenueStats);
        model.addAttribute("revenueLabels", revenueLabels);
        model.addAttribute("revenueValues", revenueValues);

        return "statistics";
    }
    @GetMapping("/statistics/export-pdf")
    public void exportPdf(
            @RequestParam int year,
            @RequestParam String period,
            HttpServletResponse response) throws Exception {

        Map<Integer, Long> residentStats =
                userService.getResidentStatistics(year, period);

        Map<Integer, BigDecimal> revenueStats =
                invoiceService.getRevenueStatistics(year, period);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) auth.getPrincipal();

        String fullName = userDetails.getFullName();

        Map<String, Object> data = new HashMap<>();
        data.put("year", year);
        data.put("period", period);
        data.put("residentStats", residentStats);
        data.put("revenueStats", revenueStats);
        data.put("fullName", fullName);

        pdfService.exportStatisticsPdf(data, response);
    }
}
