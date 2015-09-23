package com.tonghang.web.card.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tonghang.web.card.pojo.Card;
import com.tonghang.web.card.service.CardService;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Controller("cardController")
@RequestMapping("card")
public class CardController {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="cardService")
	private CardService cardService;
	/**
	 * 业务功能：保存用户的名片信息
	 * @param client_id  用户的client_id
	 * @param mapstr  包含名片所有信息
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 
	 * notice:名片的交换次数exchange_times在addCard时默认为0.
	 */
	@RequestMapping("{client_id}/add")
	@ResponseBody public ResponseEntity<Map<String,Object>> addCard(@PathVariable String client_id,@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map<String,Object> map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Card card = new Card();
		User user = userService.findUserById(client_id);
		card.setCompanyname((String) map.get("companyname"));
		card.setDiploma((String)map.get("diploma"));
		card.setEmail((String) map.get("email"));
		card.setExchange_times(0);
		card.setMajor((String)map.get("major"));
		card.setPhone((String)map.get("phone"));
		card.setPosition((String)map.get("position"));
		card.setSchool_date(TimeUtil.getFormatShortDate((String)map.get("datetime")));
		card.setSchoolname((String)map.get("schoolname"));
		card.setUser(user);
		return new ResponseEntity<Map<String,Object>>(cardService.addCard(card),HttpStatus.OK);
	}
	
	@RequestMapping("{client_id}/update")
	@ResponseBody public ResponseEntity<Map<String,Object>> updateCard(@PathVariable String client_id,@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map<String,Object> key = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(cardService.modifyCard(client_id, key),HttpStatus.OK);
	}
}
