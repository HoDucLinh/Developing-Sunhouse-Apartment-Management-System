package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.response.ApartmentImageResponse;
import linh.sunhouse_apartment.entity.ApartmentImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentImageService {
    List<ApartmentImage> getApartmentImages(Integer apartmentInfoId);
    ApartmentImageResponse addApartmentImage(Integer apartmentInfoId, MultipartFile file);
    ApartmentImage updateApartmentImage(ApartmentImage apartmentImage);
    Integer deleteApartmentImage(ApartmentImage apartmentImage);
    ApartmentImage getApartmentImage(Integer imageId);
}
