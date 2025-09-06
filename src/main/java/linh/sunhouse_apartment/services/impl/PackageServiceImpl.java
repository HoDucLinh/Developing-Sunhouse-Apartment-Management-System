package linh.sunhouse_apartment.services.impl;

import com.cloudinary.Cloudinary;
import linh.sunhouse_apartment.dtos.request.PackageRequest;
import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.entity.User;
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
    public Package addPackage(PackageRequest packageRequest, User sender) {
        Locker locker = lockerRepository.getLockerByID(packageRequest.getLockerId());
        if (locker == null) {
            throw new RuntimeException("Locker không tồn tại");
        }

        Package newPackage = new Package();
        newPackage.setName(packageRequest.getName());
        newPackage.setStatus(Package.Status.PENDING);
        newPackage.setCreatedAt(new Date());
        newPackage.setLockerId(locker);

        if (packageRequest.getFile() != null && !packageRequest.getFile().isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(packageRequest.getFile().getBytes(), Map.of());
                newPackage.setImage(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Upload image failed", e);
            }
        }
        newPackage.setSenderId(sender);

        return packageRepository.addPackage(newPackage);
    }

    @Override
    public int changeStatusPackage(int packageID, Package.Status newStatus, User user) {
        Package pk = packageRepository.findPackageById(packageID);
        if (pk == null) {
            throw new RuntimeException("Package not found");
        }
        pk.setStatus(newStatus);
        pk.setReceiverId(user);
        pk.setReceivedAt(new Date());
        packageRepository.update(pk);
        return 1;
    }

    @Override
    public int deletePackage(int packageID) {
        return packageRepository.deletePackage(packageID);
    }

    @Override
    public List<Package> getPackages(int lockerId, String kw) {
        Locker locker = lockerRepository.getLockerByID(lockerId);
        return packageRepository.findAllPackagesById(locker,kw);
    }
}
