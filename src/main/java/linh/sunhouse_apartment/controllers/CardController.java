package linh.sunhouse_apartment.controllers;

import linh.sunhouse_apartment.entity.Card;
import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.services.CardService;
import linh.sunhouse_apartment.services.RelativeService;
import linh.sunhouse_apartment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    private RelativeService relativeService;

    @GetMapping("/manage-card")
    public String manageCard(){
        return "manageCard";
    }

    @GetMapping("/create-new-card")
    public String showCreateCardForm(Model model) {
        List<User> users = userService.getUsers(null);
        model.addAttribute("users", users);
        model.addAttribute("card", new Card());
        return "createCard";
    }

    @PostMapping("/create-new-card")
    public String createCard(@RequestParam("userId") int userId,
                             @RequestParam(name = "useRelative", required = false) boolean useRelative,
                             @RequestParam(name = "relativeId", required = false) Integer relativeId) {

        Card card = new Card();
        card.setUserId(userService.getUserById(userId));

        if (useRelative && relativeId != null) {
            card.setRelativeId(relativeService.getRelativeById(relativeId));
        }

        cardService.addCard(card);
        return "redirect:/manage-card";
    }


    @GetMapping("/get-relatives")
    @ResponseBody
    public List<Relative> getRelatives(@RequestParam("userId") int userId) {
        return relativeService.getRelativesByUserId(userId);
    }
}
