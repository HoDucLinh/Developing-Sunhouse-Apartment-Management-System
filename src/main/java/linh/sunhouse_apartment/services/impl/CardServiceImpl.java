package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.CardRequest;
import linh.sunhouse_apartment.dtos.response.CardResponse;
import linh.sunhouse_apartment.entity.Card;
import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.CardRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.CardService;
import linh.sunhouse_apartment.services.RelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private RelativeService relativeService;

    @Override
    public CardResponse addCard(CardRequest cardRequest) {
        User u = userRepo.getUserById(cardRequest.getUserId());
        List<Card> cards = cardRepo.getCardsByUserId(u.getId());
        for  (Card card : cards) {
            if(card.getRelativeId().getId() == cardRequest.getRelativeId() && card.getExpirationDate().after(new Date())) {
                throw new RuntimeException("Card already exists");
            }
        }
        if (u != null ) {
            Card card = new Card();
            card.setUserId(u);

            if (cardRequest.isUseRelative() && cardRequest.getRelativeId() != null) {
                Relative relative = relativeService.getRelativeById(cardRequest.getRelativeId());
                card.setRelativeId(relative);
            }
            card.setStatus("active");
            card.setIssueDate(new Date());
            Calendar cal = Calendar.getInstance();
            cal.setTime(card.getIssueDate());
            cal.add(Calendar.MONTH, 3);
            card.setExpirationDate(cal.getTime());
            Card savedCard = cardRepo.addCard(card);
            return new CardResponse(
                    savedCard.getId(),
                    savedCard.getIssueDate(),
                    savedCard.getExpirationDate(),
                    savedCard.getStatus(),
                    savedCard.getUserId() != null ? savedCard.getUserId().getId() : null,
                    savedCard.getRelativeId() != null ? savedCard.getRelativeId().getId() : null,
                    card.getRelativeId().getFullName() != null ? card.getRelativeId().getFullName() : null,
                    card.getRelativeId().getRelationship() != null ? card.getRelativeId().getRelationship() : null
            );
        }
        return null;
    }

    @Override
    public List<CardResponse> getCardsByUserId(int userId) {
        if(userRepo.getUserById(userId) != null) {
            List<Card> cards = cardRepo.getCardsByUserId(userId);
            List<CardResponse> cardResponses = new ArrayList<>();
            for (Card card : cards) {
                CardResponse cardResponse = new CardResponse(card.getId(),
                        card.getIssueDate(),
                        card.getExpirationDate(),
                        card.getStatus(),
                        card.getUserId() != null ? card.getUserId().getId() : null,
                        card.getRelativeId() != null ? card.getRelativeId().getId() : null,
                        card.getRelativeId().getFullName() != null ? card.getRelativeId().getFullName() : null,
                        card.getRelativeId().getRelationship() != null ? card.getRelativeId().getRelationship() : null
                );
                cardResponses.add(cardResponse);
            }
            return cardResponses;
        }
        return null;
    }
    @Override
    public boolean deleteCard(int cardId) {
        return cardRepo.deleteCard(cardId);
    }
    @Override
    public List<Card> getAllCards(String keyword) {
        return cardRepo.getAllCards(keyword);
    }

}
