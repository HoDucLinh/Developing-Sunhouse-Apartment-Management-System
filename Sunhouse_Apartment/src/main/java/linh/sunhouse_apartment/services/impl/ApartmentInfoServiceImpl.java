package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.response.ApartmentImageResponse;
import linh.sunhouse_apartment.dtos.response.ApartmentInfoResponse;
import linh.sunhouse_apartment.entity.ApartmentImage;
import linh.sunhouse_apartment.entity.ApartmentInfo;
import linh.sunhouse_apartment.repositories.ApartmentInfoRepository;
import linh.sunhouse_apartment.services.ApartmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApartmentInfoServiceImpl implements ApartmentInfoService {
    @Autowired
    ApartmentInfoRepository apartmentInfoRepository;

    @Override
    public ApartmentInfo addApartmentInfo(ApartmentInfo apartmentInfo) {
        if(apartmentInfo != null){
            return apartmentInfoRepository.addApartmentInfo(apartmentInfo);
        }
        return null;
    }


    @Override
    public ApartmentInfoResponse getApartmentInfo(Integer apartmentInfoId) {

        if (apartmentInfoId == null)
            throw new IllegalArgumentException("Apartment ID cannot be null");

        ApartmentInfo apartmentInfo = apartmentInfoRepository.getApartmentInfo(apartmentInfoId);
        List<ApartmentImageResponse> imageResponses =
                apartmentInfo.getImages()
                        .stream()
                        .map(img -> new ApartmentImageResponse(
                                img.getId(),
                                img.getImageUrl(),
                                apartmentInfo.getId()
                        ))
                        .toList();

        return new ApartmentInfoResponse(apartmentInfo.getId(),apartmentInfo.getName(),apartmentInfo.getAddress(),apartmentInfo.getHotline(),
                apartmentInfo.getEmail(),apartmentInfo.getDescription(),imageResponses);
    }

    @Override
    public ApartmentInfo updateApartmentInfo(Integer id, ApartmentInfo updatedData) {

        if (id == null)
            throw new IllegalArgumentException("Apartment ID cannot be null");

        ApartmentInfo existing = apartmentInfoRepository.getApartmentInfo(id);

        if (existing == null)
            throw new RuntimeException("Apartment not found");

        // cập nhật field
        existing.setName(updatedData.getName());
        existing.setAddress(updatedData.getAddress());
        existing.setHotline(updatedData.getHotline());
        existing.setEmail(updatedData.getEmail());
        existing.setDescription(updatedData.getDescription());

        return apartmentInfoRepository.updateApartmentInfo(existing);
    }
}
