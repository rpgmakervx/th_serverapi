package com.tonghang.web.question.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller("questionController")
@RequestMapping("question")
public class QuestionController {

	@RequestMapping("{asker_id}/ask/{anchor_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> answerVoice(@PathVariable String asker_id,@PathVariable String anchor_id
															,HttpServletRequest request,@RequestParam CommonsMultipartFile voice){
		return null;
	}
}
