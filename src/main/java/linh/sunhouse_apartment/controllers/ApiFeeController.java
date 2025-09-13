package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.response.FeeResponse;
import linh.sunhouse_apartment.dtos.response.UtilityResponse;
import linh.sunhouse_apartment.entity.Fee;
import linh.sunhouse_apartment.services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fee")
@CrossOrigin(origins = "*")
public class ApiFeeController {

    @Autowired
    private FeeService feeService;

    @GetMapping("/utilities")
    public ResponseEntity<?> getUtilities(@RequestParam Map<String,String> params) {
        try {
            List<Fee> utilities = feeService.getUtilities(params);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Lấy danh sách tiện ích thành công",
                    "data", utilities
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Đã xảy ra lỗi khi lấy danh sách tiện ích",
                    "error", ex.getMessage()
            ));
        }
    }

    @GetMapping("/get-utilities-of-user")
    public ResponseEntity<?> getUtilitiesOfUser(@RequestParam Integer userId) {
        try {
            List<UtilityResponse> utilities = feeService.getUtilitiesOfUser(userId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Lấy danh sách tiện ích của user thành công",
                    "data", utilities
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Đã xảy ra lỗi khi lấy danh sách tiện ích của user",
                    "error", ex.getMessage()
            ));
        }
    }

}
