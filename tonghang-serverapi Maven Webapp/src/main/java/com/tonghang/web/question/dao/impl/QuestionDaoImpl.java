package com.tonghang.web.question.dao.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.question.dao.QuestionDao;
import com.tonghang.web.question.pojo.Question;
import com.tonghang.web.room.pojo.Room;

@Repository("questionDao")
@Transactional
public class QuestionDaoImpl implements QuestionDao{

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public void save(Question question) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(question);
	}

	@Override
	public void saveOrUpdate(Question question) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(question);
	}

	@Override
	public Question findQuestionById(String question_id) {
		// TODO Auto-generated method stub
		Question question = (Question) sessionFactory.getCurrentSession().get(Question.class, question_id);
		return question;
	}

	@Override
	public List<Question> findQuestionByTime(String anchor_id) {
		// TODO Auto-generated method stub
		List<Question> questions = sessionFactory.getCurrentSession().createQuery("from Question as question where question.anchor.client_id = :client_id order by created_at desc")
										.setParameter("client_id", anchor_id).list();
		return questions;
	}

	@Override
	public List<Question> findQuestionByAsker(String asker_id) {
		// TODO Auto-generated method stub
		List<Question> questions = sessionFactory.getCurrentSession().createQuery("from Question as question where question.asker.client_id = :client_id order by created_at desc")
				.setParameter("client_id", asker_id).list();
return questions;
	}

}
