package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.response.PackageResponse;
import linh.sunhouse_apartment.entity.Feedback;
import linh.sunhouse_apartment.entity.Package;
import linh.sunhouse_apartment.services.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/package")
@CrossOrigin(origins = "*")
public class ApiPackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping("/get-packages/{lockerId}")
    public ResponseEntity<?> getPackagesByLockerId(@PathVariable("lockerId") Integer lockerId,
                                                                       @RequestParam(required = false) String kw) {
        try {
            List<Package> packages = packageService.getPackages(lockerId, kw);

            List<PackageResponse> responses = packages.stream()
                    .map(pkg -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(pkg.getCreatedAt());
                        cal.add(Calendar.DAY_OF_MONTH, 7); // cộng 7 ngày
                        return new PackageResponse(
                                pkg.getId(),
                                pkg.getName(),
                                pkg.getImage().toString(),
                                pkg.getStatus().name(),
                                cal.getTime()
                        );
                    })
                    .toList();

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", responses);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());

            return ResponseEntity
                    .status(500)
                    .body(error);
        }
    }
}
