package linh.sunhouse_apartment.controllers;
import linh.sunhouse_apartment.dtos.response.ApartmentImageResponse;
import linh.sunhouse_apartment.entity.ApartmentImage;
import linh.sunhouse_apartment.entity.ApartmentInfo;
import linh.sunhouse_apartment.services.ApartmentImageService;
import linh.sunhouse_apartment.services.ApartmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/apartment")
@CrossOrigin(origins = "*")
public class ApiApartmentInfoController {
    @Autowired
    private ApartmentInfoService apartmentInfoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getApartmentInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(
                apartmentInfoService.getApartmentInfo(id)
        );
    }


}
