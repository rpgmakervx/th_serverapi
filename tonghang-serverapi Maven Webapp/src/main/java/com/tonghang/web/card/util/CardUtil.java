package com.tonghang.web.card.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tonghang.web.card.pojo.Card;
@Component("cardUtil")
public class CardUtil {

	public Map<String,Object> cardToMapConvertor(Card card){
		Map<String,Object> cardmap = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String, Object>();
		cardmap.put("companyname", card.getCompanyname());
		cardmap.put("position", card.getPosition());
		cardmap.put("work_date", card.getWork_date());
		cardmap.put("email", card.getEmail());
		cardmap.put("phone", card.getPhone());
		cardmap.put("schoolname", card.getSchoolname());
		cardmap.put("major", card.getMajor());
		cardmap.put("diploma", card.getDiploma());
		cardmap.put("school_date", card.getSchool_date());
		result.put("card", cardmap);
		return result;
	}
}
