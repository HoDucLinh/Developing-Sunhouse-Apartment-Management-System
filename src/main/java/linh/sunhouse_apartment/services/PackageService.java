package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.PackageRequest;
import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PackageService {
    Package addPackage(PackageRequest packageRequest, User sender);
    int changeStatusPackage(int packageID, Package.Status newStatus, User user);
    int deletePackage(int packageID);
    List<Package> getPackages(int lockerId, String kw);
}
