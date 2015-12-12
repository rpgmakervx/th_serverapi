package com.tonghang.web.question.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.question.pojo.Question;

@Component("questionUtil")
public class QuestionUtil {

	public Map<String,Object> questionToMapConterter(Question question){
		Map<String,Object> questmap = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String, Object>();
		questmap.put("content", question.getContent());
		questmap.put("times", question.getAnswer_times());
		questmap.put("question_id", question.getQuestion_id());
		questmap.put("voice", Constant.VOICE_PATH+question.getAnchor().getClient_id()+File.separator+question.getQuestion_id()+File.separator);
		result.put("question", questmap);
		return result;
	}
	
	public Map<String,Object> questionsToMapConterter(List<Question> qs){
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> questmsgs = new ArrayList<Map<String,Object>>();
		for(Question question:qs){
			Map<String,Object> questmap = new HashMap<String, Object>();
			questmap.put("content", question.getContent());
			questmap.put("times", question.getAnswer_times());
			questmap.put("question_id", question.getQuestion_id());
			questmap.put("voice", Constant.VOICE_PATH+question.getAnchor().getClient_id()+File.separator+question.getQuestion_id()+File.separator);
			questmsgs.add(questmap);
		}
		result.put("questions", questmsgs);
		return result;
	}
}
