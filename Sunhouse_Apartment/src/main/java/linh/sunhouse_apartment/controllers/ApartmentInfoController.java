package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.response.ApartmentImageResponse;
import linh.sunhouse_apartment.dtos.response.ApartmentInfoResponse;
import linh.sunhouse_apartment.entity.ApartmentImage;
import linh.sunhouse_apartment.services.ApartmentImageService;
import linh.sunhouse_apartment.services.ApartmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ApartmentInfoController {
    @Autowired
    private ApartmentInfoService apartmentInfoService;

    @Autowired
    private ApartmentImageService apartmentImageService;

    @GetMapping("/setting")
    public String getApartmentInfo(Model model) {
        ApartmentInfoResponse apartmentInfo = apartmentInfoService.getApartmentInfo(1);
        model.addAttribute("apartmentInfo", apartmentInfo);
        return "setting";
    }
    @PostMapping("/add-image")
    public String addImage(@RequestParam("image") MultipartFile image, Model model) {
        ApartmentImageResponse apartmentImageResponse = apartmentImageService.addApartmentImage(1,image);
        return "redirect:/setting";
    }

    @DeleteMapping("delete-image")
    public String deleteImage(@RequestParam("imageId") Integer imageId) {
        ApartmentImage apartmentImage = apartmentImageService.getApartmentImage(imageId);
        if(apartmentImage != null) {
            Integer result = apartmentImageService.deleteApartmentImage(apartmentImage);
            if(result == 1) {
                return "redirect:/setting";
            }
            else
                return "Không thể xóa";
        }
        else
            return "Không tìm thấy hình ảnh";
    }
}
