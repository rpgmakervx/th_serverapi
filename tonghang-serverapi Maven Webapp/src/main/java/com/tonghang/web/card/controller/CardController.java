package com.tonghang.web.card.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
		card.setRealname((String)map.get("realname"));
		card.setExchange_times(0);
		card.setWork_date((String)map.get("work_date"));
		card.setMajor((String)map.get("major"));
		card.setPhone((String)map.get("phone"));
		card.setPosition((String)map.get("position"));
		card.setSchool_date((String)map.get("school_date"));
		card.setSchoolname((String)map.get("schoolname"));
		card.setUser(user);
		return new ResponseEntity<Map<String,Object>>(cardService.addCard(card),HttpStatus.OK);
	}
	/**
	 * 业务功能：修改名片信息
	 * @param client_id		用户的client_id
	 * @param mapstr		内含attribute和value。attribute表示名片的某属性名字，value是要修改的值
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * notice:现在每个请求的param中都带着token参数，直接获取会把token一并获取。
	 */
	@RequestMapping("{client_id}/update")
	@ResponseBody public ResponseEntity<Map<String,Object>> updateCard(@PathVariable String client_id,@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map<String,Object> key = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(cardService.modifyCard(client_id, key),HttpStatus.OK);
	}
	/**
	 * 业务功能：获得某用户的名片信息
	 * @param client_id
	 * @return
	 * notice: 返回数据的结构
	 * success:{
			code:200
			message:"***"
			card:{
			companyname:"天津行动科技有限公司",
			position:"Java工程师",
			work_date:"2015-03-18",
			email:"******@qq.com",
			phone:"136*******",
			schoolname:"天津职业技术师范大学",
			major:"软件工程",
			diploma:"本科学士",
			school_date:"2013-08-24"
			}
		}
	 */
	@RequestMapping("{client_id}/get")
	@ResponseBody public ResponseEntity<Map<String,Object>> getCardMessage(@PathVariable String client_id){
		return new ResponseEntity<Map<String,Object>>(cardService.findCardByUser(client_id),HttpStatus.OK);
	}
	/**
	 * 业务功能：交换名片（该请求主要用于记录交换双方的 名片交换次数）
	 * @param self_id	点击 ‘聊一聊’ 的一方
	 * @param other_id	被点击 ‘聊一聊’ 的一方
	 * @return
	 * 可能的问题：A点击聊一聊双方同时记一次数据，B也点击聊一聊，双方再次级一次数据，这样造成了AB交换名片次数*2
	 */
	@RequestMapping("{self_id}/chat/{other_id}")
	@ResponseBody public ResponseEntity<Map<String,Object>> chatAfterExchange(@PathVariable String self_id,@PathVariable String other_id){
		return new ResponseEntity<Map<String,Object>>(cardService.addChatTimes(self_id,other_id),HttpStatus.OK);
	}
	/**
	 * 业务功能：请求和某人交换名片
	 * @param self_id	自己的名片
	 * @param other_id	要交换的人的名片
	 * @return
	 */
	@RequestMapping("{self_id}/request/{other_id}")
	@ResponseBody public ResponseEntity<Map<String,Object>> requestExchangeCard(@PathVariable String self_id,@PathVariable String other_id){
		return new ResponseEntity<Map<String,Object>>(cardService.createRequest(self_id, other_id),HttpStatus.OK);
	}
	/**
	 * 业务功能：接收方同意和发起方交换名片
	 * @param self_id
	 * @param other_id
	 * @return
	 */
	@RequestMapping("{self_id}/agree/{other_id}")
	@ResponseBody public ResponseEntity<Map<String,Object>> agreeExchangeCard(@PathVariable String self_id,@PathVariable String other_id){
		return new ResponseEntity<Map<String,Object>>(cardService.agreeExchange(self_id, other_id),HttpStatus.OK);
	}
}
