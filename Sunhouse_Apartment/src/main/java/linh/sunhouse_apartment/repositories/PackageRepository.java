package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Locker;
import linh.sunhouse_apartment.entity.Package;

import java.util.List;

public interface PackageRepository {
    Package addPackage(Package p);
    Integer update (Package pk);
    int deletePackage(int packageID);
    List<Package> findAllPackagesById(Locker l, String kw);
    Package findPackageById(int packageID);
}
