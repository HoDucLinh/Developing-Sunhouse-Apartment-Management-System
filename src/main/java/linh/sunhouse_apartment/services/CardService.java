package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.entity.Card;

import java.util.List;

public interface CardService {
    Card addCard(Card card);
    List<Card> getCardsByUserId(int userId);
    boolean deleteCard(int cardId);
    List<Card> getAllCards();
}
