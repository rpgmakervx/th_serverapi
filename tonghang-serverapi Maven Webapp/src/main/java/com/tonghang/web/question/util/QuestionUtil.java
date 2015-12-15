package com.tonghang.web.question.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.question.pojo.Question;

@Component("questionUtil")
public class QuestionUtil {
	/**
	 * 原始的 一个问题封装成一个map
	 * @param question
	 * @return
	 */
	public Map<String,Object> questionToMapConterter(Question question){
		Map<String,Object> questmap = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String, Object>();
		questmap.put("content", question.getContent());
		questmap.put("created_at", TimeUtil.getFormatString(question.getCreated_at()));
		questmap.put("times", question.getAnswer_times());
		questmap.put("question_id", question.getQuestion_id());
		questmap.put("anchor_id", question.getAnchor().getClient_id());
		questmap.put("asker_id", question.getAsker().getClient_id());
		questmap.put("voice", Constant.VOICE_PATH+question.getAnchor().getClient_id()+"/"+question.getQuestion_id()+"/");
		result.put("question", questmap);
		return result;
	}
	/**
	 * 原始的一组问题转换为list<map>
	 * @param qs
	 * @return
	 */
	public Map<String,Object> questionsToMapConterter(List<Question> qs){
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> questmsgs = new ArrayList<Map<String,Object>>();
		for(Question question:qs){
			Map<String,Object> questmap = new HashMap<String, Object>();
			questmap.put("content", question.getContent());
			questmap.put("times", question.getAnswer_times());
			questmap.put("anchor_id", question.getAnchor().getClient_id());
			questmap.put("asker_id", question.getAsker().getClient_id());
			questmap.put("question_id", question.getQuestion_id());
			questmap.put("voice", Constant.VOICE_PATH+question.getAnchor().getClient_id()+"/"+question.getQuestion_id()+"/");
			questmap.put("created_at", TimeUtil.getFormatString(question.getCreated_at()));
			questmsgs.add(questmap);
		}
		result.put("questions", questmsgs);
		return result;
	}
	/**
	 * 只需要封装问题内容和封装
	 * @param question
	 * @return
	 */
	public Map<String,Object> questionContentConverter(Question question){
		Map<String,Object> questmap = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String, Object>();
		questmap.put("content", question.getContent());
		questmap.put("times", question.getAnswer_times());
		result.put("question", questmap);
		return result;
	}
}
