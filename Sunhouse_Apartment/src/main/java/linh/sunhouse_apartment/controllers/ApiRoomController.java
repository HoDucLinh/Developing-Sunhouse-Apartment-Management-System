package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.response.UnpaidRoomResponse;
import linh.sunhouse_apartment.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiRoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/api/unpaid-rooms")
    @ResponseBody
    public List<UnpaidRoomResponse> getUnpaidRooms(@RequestParam Integer feeId) {
        return roomService.getUnpaidRooms(feeId);
    }

}
