package com.tonghang.web.card.dao;

import com.tonghang.web.card.pojo.Card;
import com.tonghang.web.card.pojo.CardHistory;

public interface CardDao {
	
	public void save(Card card);
	
	public void saveOrUpdate(Card card);
	
	public Card findCardByClient_id(String client_id);
	
	public void addHistory(CardHistory history);
}
