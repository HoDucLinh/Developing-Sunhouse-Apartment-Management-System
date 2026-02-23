package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.ApartmentInfo;

public interface ApartmentInfoRepository {
    ApartmentInfo addApartmentInfo(ApartmentInfo apartment);
    ApartmentInfo getApartmentInfo(Integer apartmentInfoId);
    ApartmentInfo updateApartmentInfo(ApartmentInfo apartment);
}
