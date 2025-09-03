package linh.sunhouse_apartment.controllers;

import jakarta.persistence.EntityNotFoundException;
import linh.sunhouse_apartment.dtos.request.RoomRequest;
import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.entity.Room;
import linh.sunhouse_apartment.services.FloorService;
import linh.sunhouse_apartment.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    FloorService floorService;

    @GetMapping("/manage-room")
    public String manageRoom(@RequestParam(value = "kw", required = false) String keyword, Model model) {
        List<RoomResponse> rooms = roomService.findAll(keyword);
        model.addAttribute("rooms", rooms);
        return "manageRoom";
    }

    @GetMapping("/{id}/members")
    public String viewMembers(@PathVariable Integer id, Model model) {
        RoomResponse room = roomService.getRoomWithUsers(id);
        model.addAttribute("room", room);
        return "room_members";
    }

    @PostMapping("/{id}/change-head")
    public String changeRoomHead(@PathVariable Integer id,
                                 @RequestParam("newHeadId") Integer newHeadId) {
        roomService.changeRoomHead(id, newHeadId);
        return "redirect:/" + id + "/members";
    }

    @GetMapping("/manage-room/edit/{id}")
    public String editRoomForm(@PathVariable Integer id, Model model) {
        Room room = roomService.findById(id);
        if (room == null) {
            return "redirect:/manage-room"; // nếu không tìm thấy thì quay về danh sách
        }

        model.addAttribute("room", room);
        return "edit_room"; // trang form edit
    }

    @PostMapping("/manage-room/edit/{id}")
    public String updateRoom(@PathVariable Integer id,
                             @ModelAttribute RoomRequest roomRequest) {
        roomService.updateRoom(id, roomRequest);
        return "redirect:/manage-room";
    }


    @GetMapping("/add-room")
    public String addRoomForm(Model model) {
        model.addAttribute("roomRequest", new RoomRequest());
        model.addAttribute("floors", floorService.findAll());
        return "add_room";
    }

    @PostMapping("/add-room")
    public String addRoom(@ModelAttribute("roomRequest") RoomRequest roomRequest,
                          Model model) {
        try {
            roomService.addRoom(roomRequest);
            return "redirect:/manage-room";
        } catch (EntityNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("floors", floorService.findAll());
            return "add_room";
        }
    }

}
