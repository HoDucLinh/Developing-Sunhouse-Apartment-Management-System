package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.InvoiceRequest;
import linh.sunhouse_apartment.dtos.response.InvoiceResponse;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.entity.Invoice;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.services.FeeService;
import linh.sunhouse_apartment.services.InvoiceService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    FeeService feeService;

    @GetMapping("/manage-invoice")
    public String manageInvoice(@RequestParam(required = false) String kw, Model model) {
        Map<String,String> params = new HashMap<>();
        if(kw!=null && !kw.isEmpty()){
            params.put("kw", kw);
        }
        List<Invoice> invoices = invoiceService.getAllInvoices(params);
        model.addAttribute("invoices", invoices);
        model.addAttribute("kw", kw);

        model.addAttribute("fees", feeService.getFeeOfFee());
        return "manageInvoice";
    }
    @GetMapping("/invoice/{id}")
    public String viewInvoiceDetail(@PathVariable("id") Integer id, Model model) {
        InvoiceResponse invoice = invoiceService.getInvoiceDetail(id);
        model.addAttribute("invoice", invoice);
        return "invoiceDetail";
    }

    @PutMapping("/invoice/cancel-invoice/{id}")
    public String cancelInvoice(@PathVariable("id") Integer id) {
        invoiceService.cancelInvoice(id);
        return "redirect:/manage-invoice";
    }

    @PostMapping("/invoice/auto-apartment")
    public String createInvoiceApartment(@RequestParam Integer feeId){
        invoiceService.createInvoicesForAllRoomHeads(feeId);
        return "redirect:/manage-invoice";
    }


}
