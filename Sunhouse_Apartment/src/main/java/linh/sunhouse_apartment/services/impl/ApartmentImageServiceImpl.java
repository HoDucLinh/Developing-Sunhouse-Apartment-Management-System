package linh.sunhouse_apartment.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import linh.sunhouse_apartment.dtos.response.ApartmentImageResponse;
import linh.sunhouse_apartment.entity.ApartmentImage;
import linh.sunhouse_apartment.entity.ApartmentInfo;
import linh.sunhouse_apartment.repositories.ApartmentImageRepository;
import linh.sunhouse_apartment.repositories.ApartmentInfoRepository;
import linh.sunhouse_apartment.services.ApartmentImageService;
import linh.sunhouse_apartment.services.ApartmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ApartmentImageServiceImpl implements ApartmentImageService {

    @Autowired
    private ApartmentImageRepository apartmentImageRepository;

    @Autowired
    private ApartmentInfoRepository apartmentInfoRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<ApartmentImage> getApartmentImages(Integer apartmentInfoId) {

        if (apartmentInfoId == null) {
            throw new IllegalArgumentException("Apartment ID cannot be null");
        }

        List<ApartmentImage> images =
                apartmentImageRepository.getApartmentImages(apartmentInfoId);

        if (images == null || images.isEmpty()) {
            throw new RuntimeException(
                    "No images found for apartment id = " + apartmentInfoId);
        }

        return images;
    }

    @Override
    public ApartmentImageResponse addApartmentImage(Integer apartmentInfoId,
                                                    MultipartFile file) {

        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("Image file cannot be null or empty");

        // 1️⃣ Kiểm tra apartment tồn tại
        ApartmentInfo apartment =
                apartmentInfoRepository.getApartmentInfo(apartmentInfoId);

        if (apartment == null)
            throw new RuntimeException("Apartment not found");

        try {
            Map<?, ?> res = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );

            String imageUrl = res.get("secure_url").toString();

            ApartmentImage apartmentImage = new ApartmentImage();
            apartmentImage.setImageUrl(imageUrl);
            apartmentImage.setApartment(apartment);

            apartmentImageRepository.addApartmentImage(apartmentImage);

            return new ApartmentImageResponse(apartmentImage.getId(),apartmentImage.getImageUrl(),apartmentImage.getApartment().getId());

        } catch (IOException ex) {
            throw new RuntimeException("Failed to upload image to Cloudinary", ex);
        }
    }

    @Override
    public ApartmentImage updateApartmentImage(ApartmentImage apartmentImage) {

        if (apartmentImage == null || apartmentImage.getId() == null) {
            throw new IllegalArgumentException("Invalid image data");
        }

        List<ApartmentImage> images =
                apartmentImageRepository.getApartmentImages(
                        apartmentImage.getApartment().getId()
                );

        ApartmentImage existing = images.stream()
                .filter(img -> img.getId().equals(apartmentImage.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Image not found with id = "
                                + apartmentImage.getId())
                );

        existing.setImageUrl(apartmentImage.getImageUrl());

        return apartmentImageRepository.updateApartmentImage(existing);
    }

    @Override
    public Integer deleteApartmentImage(ApartmentImage apartmentImage) {
        return apartmentImageRepository.deleteApartmentImage(apartmentImage);
    }

    @Override
    public ApartmentImage getApartmentImage(Integer imageId) {
        return apartmentImageRepository.getApartmentImage(imageId);
    }
}
