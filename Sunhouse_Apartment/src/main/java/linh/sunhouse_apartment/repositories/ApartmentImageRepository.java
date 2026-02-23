package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.ApartmentImage;

import java.util.List;

public interface ApartmentImageRepository {
    ApartmentImage addApartmentImage(ApartmentImage apartmentImage);
    List<ApartmentImage> getApartmentImages(Integer apartmentInfoId);
    ApartmentImage updateApartmentImage(ApartmentImage apartmentImage);
    Integer deleteApartmentImage(ApartmentImage apartmentImage);
    ApartmentImage getApartmentImage(Integer imageId);
}
