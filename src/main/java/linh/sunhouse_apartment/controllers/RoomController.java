package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.dtos.response.RoomResponse;
import linh.sunhouse_apartment.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RoomController {

    @Autowired
    RoomService roomService;

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
}
