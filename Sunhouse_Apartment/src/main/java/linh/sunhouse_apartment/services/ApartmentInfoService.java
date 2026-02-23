package linh.sunhouse_apartment.services;


import linh.sunhouse_apartment.dtos.response.ApartmentInfoResponse;
import linh.sunhouse_apartment.entity.ApartmentInfo;

public interface ApartmentInfoService {
    ApartmentInfo addApartmentInfo(ApartmentInfo apartmentInfo);
    ApartmentInfoResponse getApartmentInfo(Integer apartmentInfoId);
    ApartmentInfo updateApartmentInfo(Integer id, ApartmentInfo updatedData);
}
