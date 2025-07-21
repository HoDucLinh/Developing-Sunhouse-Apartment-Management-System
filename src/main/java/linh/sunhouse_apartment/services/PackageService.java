package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PackageService {
    Package addPackage(String name, MultipartFile file, int lockerId);
    int changeStatusPackage(int packageID, Package.Status newStatus);
    int deletePackage(int packageID);
    List<Package> getPackages(int lockerId);
}
