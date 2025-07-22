package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;

import java.util.List;

public interface PackageRepository {
    Package addPackage(Package p);
    int changeStatusPackage(int packageID, Package.Status newStatus);
    int deletePackage(int packageID);
    List<Package> findAllPackagesById(Locker l, String kw);
}
