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

import java.util.*;

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
        if (u == null)
            throw new RuntimeException("User not found");
        int expiration_month = 12;
        Relative relative = null;
        if (cardRequest.isUseRelative()){
            if(cardRequest.getRelativeId() == null)
                throw new RuntimeException("Relative Id is null");
            relative = relativeService.getRelativeById(cardRequest.getRelativeId());
            if(relative == null)
                throw new RuntimeException("Relative not found");
            expiration_month = 3;
        }
        //kiểm tra xem card đã tồn tại chưa

        List<Card> cards = cardRepo.getCardsByUserId(u.getId());
        for (Card card : cards) {
            // ✅ Kiểm tra null trước khi getId()
            if (card.getRelativeId() != null &&
                    card.getRelativeId().getId() == cardRequest.getRelativeId() &&
                    card.getExpirationDate().after(new Date())) {
                throw new RuntimeException("Card already exists");
            }
            if(card.getUserId().getId() == cardRequest.getUserId())
                throw new RuntimeException("Card already exists");
        }

        // ✅ Tạo thẻ mới
        Card card = new Card();
        card.setUserId(u);
        card.setRelativeId(relative);
        card.setStatus("active");
        card.setIssueDate(new Date());

        Calendar cal = Calendar.getInstance();
        cal.setTime(card.getIssueDate());
        cal.add(Calendar.MONTH, expiration_month);
        card.setExpirationDate(cal.getTime());
        card.setCardId(String.format("%08d",new Random().nextInt(100000000)));

        Card savedCard = cardRepo.addCard(card);

        return new CardResponse(
                savedCard.getId(),
                savedCard.getIssueDate(),
                savedCard.getExpirationDate(),
                savedCard.getStatus(),
                savedCard.getUserId() != null ? savedCard.getUserId().getId() : null,
                savedCard.getRelativeId() != null ? savedCard.getRelativeId().getId() : null,
                savedCard.getRelativeId() != null ? savedCard.getRelativeId().getFullName() : null,
                savedCard.getRelativeId() != null ? savedCard.getRelativeId().getRelationship() : null,
                savedCard.getCardId() != null ? savedCard.getCardId() : null
        );
    }


    @Override
    public List<CardResponse> getCardsByUserId(int userId) {
        if(userRepo.getUserById(userId) != null) {
            List<Card> cards = cardRepo.getCardsByUserId(userId);
            List<CardResponse> cardResponses = new ArrayList<>();
            for (Card card : cards) {
                Integer relativeId = null;
                String relativeName = null;
                Relative.EnumRelationship relationship = null;

                if (card.getRelativeId() != null) {
                    relativeId = card.getRelativeId().getId();
                    relativeName = card.getRelativeId().getFullName();
                    relationship = card.getRelativeId().getRelationship();
                }

                CardResponse cardResponse = new CardResponse(card.getId(),
                        card.getIssueDate(),
                        card.getExpirationDate(),
                        card.getStatus(),
                        card.getUserId() != null ? card.getUserId().getId() : null,
                        relativeId,
                        relativeName,
                        relationship,
                        card.getCardId() != null ? card.getCardId() : null
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
