package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.CardRequest;
import linh.sunhouse_apartment.dtos.response.CardResponse;
import linh.sunhouse_apartment.entity.Card;

import java.util.List;

public interface CardService {
    CardResponse addCard(CardRequest cardRequest);
    List<CardResponse> getCardsByUserId(int userId);
    boolean deleteCard(int cardId);
    List<Card> getAllCards(String keyword);
}
