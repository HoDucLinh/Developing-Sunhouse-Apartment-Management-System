package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Card;

import java.util.List;

public interface CardRepository {
    Card addCard(Card card);
    List<Card> getCardsByUserId(int userId);
    boolean deleteCard(int cardId);
    List<Card> getAllCards(String keyword);
}
