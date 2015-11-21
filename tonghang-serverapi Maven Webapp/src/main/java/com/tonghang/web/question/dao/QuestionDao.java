package com.tonghang.web.question.dao;

import java.util.List;

import com.tonghang.web.question.pojo.Question;

public interface QuestionDao {
	
	public void save(Question question);
	
	public void saveOrUpdate(Question question);
	
	public Question findQuestionById(String question_id);
	
	public List<Question> findQuestionByTime(String anchor_id);
	
	
}
