package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.request.RelativeRequest;
import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.services.RelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/relative")
@CrossOrigin(origins = "*")
public class ApiRelativeController {

    @Autowired
    private RelativeService relativeService;

    @PostMapping("/add-relative")
    public ResponseEntity<?> createRelative(@RequestBody RelativeRequest relativeRequest) {
        try {
            Relative addRelative = relativeService.addRelative(relativeRequest);
            return ResponseEntity.ok(addRelative);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi tạo người thân: " + e.getMessage());
        }
    }
    @GetMapping("/get-relatives/{id}")
    public ResponseEntity<?> getRelative(@PathVariable Integer id) {
        try {
            List<Relative> relatives = relativeService.getRelativesByUserId(id);
            return ResponseEntity.ok(relatives);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi ấy danh sách người thân: " + e.getMessage());
        }
    }
}
