package linh.sunhouse_apartment.services.impl;

import com.cloudinary.Cloudinary;
import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.repositories.LockerRepository;
import linh.sunhouse_apartment.repositories.PackageRepository;
import linh.sunhouse_apartment.services.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private LockerRepository lockerRepository;

    @Override
    public Package addPackage(String name,  MultipartFile file, int lockerId) {
        Locker locker = lockerRepository.getLockerByID(lockerId);
        if (locker == null) {
            throw new RuntimeException("Locker không tồn tại");
        }

        Package newPackage = new Package();
        newPackage.setName(name);
        newPackage.setStatus(Package.Status.PENDING);
        newPackage.setCreatedAt(new Date());
        newPackage.setLockerId(locker);

        if (file != null && !file.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
                newPackage.setImage(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Upload image failed", e);
            }
        }

        return packageRepository.addPackage(newPackage);
    }

    @Override
    public int changeStatusPackage(int packageID, Package.Status newStatus) {
        return packageRepository.changeStatusPackage(packageID, newStatus);
    }

    @Override
    public int deletePackage(int packageID) {
        return packageRepository.deletePackage(packageID);
    }

    @Override
    public List<Package> getPackages(int lockerId) {
        Locker locker = lockerRepository.getLockerByID(lockerId);
        return packageRepository.findAllPackagesById(locker);
    }
}
