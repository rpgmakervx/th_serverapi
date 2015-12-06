package com.tonghang.web.question.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.question.pojo.Question;
import com.tonghang.web.question.service.QuestionService;
import com.tonghang.web.room.service.RoomService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Controller("questionController")
@RequestMapping("question")
public class QuestionController {

	@Resource(name="roomService")
	private RoomService roomService;
	@Resource(name="questionService")
	private QuestionService questionService;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 创建时间：2015-11-24
	 * 业务功能：房间成员向主播发问
	 * @param asker_id		发问者client_id
	 * @param anchor_id	主播client_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("{asker_id}/ask/{anchor_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> askQuestion(@PathVariable String asker_id,@PathVariable String anchor_id,@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		String content = (String)map.get("content");
		questionService.sendQuestionRequest(asker_id, anchor_id,content);
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 添加时间：2015-11-24
	 * 业务功能：主播回答提问者的问题
	 * @param asker_id
	 * @param anchor_id
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("{anchor_id}/answer/{asker_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> answerQuestion(@PathVariable String asker_id,@PathVariable String anchor_id,@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		String content = (String)map.get("content");
		User asker = userService.findUserById(asker_id);
		User anchor = userService.findUserById(anchor_id);
		Question question = new Question();
		question.setAnchor(anchor);
		question.setAsker(asker);
		question.setCreated_at(new Date());
		question.setContent(content);
		question.setQuestion_id(SecurityUtil.getMD5(asker_id+content+new Date().getTime()));
		questionService.sendAnswerRequest(question, asker_id, anchor_id);
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
}
