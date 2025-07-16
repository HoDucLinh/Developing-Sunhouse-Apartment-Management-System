package linh.sunhouse_apartment.services.impl;

import linh.sunhouse_apartment.entity.Card;
import linh.sunhouse_apartment.repositories.CardRepository;
import linh.sunhouse_apartment.repositories.UserRepository;
import linh.sunhouse_apartment.services.CardService;
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

    @Override
    public Card addCard(Card card) {
        if (userRepo.getUserById(card.getUserId().getId()) != null ) {
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
