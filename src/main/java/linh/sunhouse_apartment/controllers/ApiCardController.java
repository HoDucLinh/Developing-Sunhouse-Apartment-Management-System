package linh.sunhouse_apartment.controllers;


import linh.sunhouse_apartment.dtos.request.CardRequest;
import linh.sunhouse_apartment.dtos.response.CardResponse;
import linh.sunhouse_apartment.entity.Card;
import linh.sunhouse_apartment.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/card")
@CrossOrigin(origins = "*")
public class ApiCardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/get-cards/{userId}")
    public ResponseEntity<?> getCards(@PathVariable Integer userId) {
        try{
            List<CardResponse> cards = cardService.getCardsByUserId(userId);
            return  ResponseEntity.ok(cards);
        }
        catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/create-card")
    public ResponseEntity<?> createCard(@RequestBody CardRequest cardRequest) {
        try {
            CardResponse card = cardService.addCard(cardRequest);
            if (card == null)
                return ResponseEntity.badRequest().body("Bad request!!!");
            return  ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            if ("Card already exists".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", e.getMessage()));
            }
            if ("Relative not found".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Lỗi server: " + e.getMessage()));
        }
    }
}
