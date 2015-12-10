package com.tonghang.web.question.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.question.dao.QuestionDao;
import com.tonghang.web.question.pojo.Question;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Service("questionService")
@Transactional
public class QuestionService {

	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="questionDao")
	private QuestionDao questionDao;
	
	/**
	 * 发问者向主播发送提问问题的推送
	 * @param asker_id
	 * @param anchor_id
	 */
	public void sendQuestionRequest(String asker_id,String anchor_id,String content){
		User asker = userService.findUserById(asker_id);
		System.out.println("发问者："+asker.getUsername()+"问题内容："+content);
		JPushUtil.push(anchor_id, asker_id, asker.getUsername(),content, Constant.ASK_QUESTION, asker.getUsername()+Constant.ASK_QUESTION_MSG+content);
	}
	
	/**
	 * 主播向问问题的人
	 * @param asker_id
	 * @param anchor_id
	 */
	public void sendAnswerRequest(Question question,String asker_id,String anchor_id){
		User anchor = userService.findUserById(asker_id);
		JPushUtil.push(anchor_id, asker_id, anchor.getUsername(),question.getContent(), Constant.ANSWER_QUESTION, anchor.getUsername()+Constant.ANSWER_QUESTION_MSG+question.getContent());
		questionDao.save(question);
	}
	
	/**
	 * 查询某主播回答的所有问题，按照时间倒序
	 * @param anchor_id
	 * @return
	 */
	public List<Question> findQuestionByAnchor(String anchor_id){
		return questionDao.findQuestionByTime(anchor_id);
	}
}
