package linh.sunhouse_apartment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InvoiceController {

    @GetMapping("/manage-invoice")
    public String manageInvoice(){
        return "manageInvoice";
    }
}
