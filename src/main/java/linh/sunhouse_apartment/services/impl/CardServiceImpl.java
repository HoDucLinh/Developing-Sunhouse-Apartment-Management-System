package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.dtos.request.CardRequest;
import linh.sunhouse_apartment.entity.Card;
import linh.sunhouse_apartment.entity.Relative;
import linh.sunhouse_apartment.entity.User;
import linh.sunhouse_apartment.repositories.CardRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.CardService;
import linh.sunhouse_apartment.services.RelativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Card addCard(CardRequest cardRequest) {
        User u = userRepo.getUserById(cardRequest.getUserId());
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
            return cardRepo.addCard(card);
        }
        return null;
    }

    @Override
    public List<Card> getCardsByUserId(int userId) {
        if(userRepo.getUserById(userId) != null) {
            return cardRepo.getCardsByUserId(userId);
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
