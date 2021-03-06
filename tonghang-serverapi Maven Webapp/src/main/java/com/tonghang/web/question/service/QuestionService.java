package com.tonghang.web.question.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.question.dao.QuestionDao;
import com.tonghang.web.question.pojo.Question;
import com.tonghang.web.question.util.QuestionUtil;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Service("questionService")
@Transactional
public class QuestionService {

	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="questionDao")
	private QuestionDao questionDao;
	@Resource(name="questionUtil")
	private QuestionUtil questionUtil;
	/**
	 * 保存问题信息
	 * @param question
	 */
	public void save(Question question){
		questionDao.save(question);
	}
	/**
	 * 更新问题信息
	 * @param question
	 */
	public void update(Question question){
		questionDao.saveOrUpdate(question);
	}
	/**
	 * 根据问题id查找问题
	 * @param question_id
	 * @return
	 */
	public Question findQuestionById(String question_id){
		return questionDao.findQuestionById(question_id);
	}
	/**
	 * 发问者向主播发送提问问题的推送
	 * @param asker_id
	 * @param anchor_id
	 */
	public void sendQuestionRequest(String asker_id,String anchor_id,String content){
		User asker = userService.findUserById(asker_id);
		System.out.println("发问者："+asker.getUsername()+"问题内容："+content);
		Question question = new Question();
		question.setContent(content);
		JPushUtil.pushQuestion(anchor_id, asker_id, asker.getUsername(),questionUtil.questionContentConverter(question), Constant.ASK_QUESTION, asker.getUsername()+Constant.ASK_QUESTION_MSG+content);
	}
	
	/**
	 * 主播向问问题的人
	 * @param asker_id
	 * @param anchor_id
	 */
	public Map<String,Object> sendAnswerRequest(Question question,String asker_id,String anchor_id){
		User anchor = userService.findUserById(anchor_id);
		JPushUtil.pushQuestion(asker_id, anchor_id, anchor.getUsername(),questionUtil.questionToMapConterter(question), Constant.ANSWER_QUESTION, anchor.getUsername()+Constant.ANSWER_QUESTION_MSG+question.getContent());
		System.out.println("SEND: "+questionUtil.questionToMapConterter(question));
		return questionUtil.questionToMapConterter(question);
	}
	
	/**
	 * 查询某主播回答的所有问题，按照时间倒序
	 * @param anchor_id
	 * @return
	 */
	public List<Question> findQuestionByAnchor(String anchor_id){
		return questionDao.findQuestionByTime(anchor_id);
	}
	/**
	 * 增加回答某问题的次数
	 * @param question
	 */
	public void addTimes(Question question){
		question.setAnswer_times(question.getAnswer_times()+1);
		update(question);
	}
}
