package com.tonghang.web.card.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.card.dao.CardDao;
import com.tonghang.web.card.pojo.Card;
import com.tonghang.web.card.pojo.CardHistory;

@Repository("cardDao")
public class CardDaoImpl implements CardDao {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	@Override
	public void save(Card card) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(card);
	}

	@Override
	public void saveOrUpdate(Card card) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(card);
	}

	@Override
	public Card findCardByClient_id(String client_id) {
		// TODO Auto-generated method stub
		Card card = (Card) sessionFactory.getCurrentSession().get(Card.class, client_id);
		return card;
	}

	@Override
	public void addHistory(CardHistory history) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(history);
	}

}
