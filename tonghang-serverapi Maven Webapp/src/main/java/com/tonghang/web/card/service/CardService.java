package com.tonghang.web.card.service;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.card.dao.CardDao;
import com.tonghang.web.card.pojo.Card;
import com.tonghang.web.card.pojo.CardHistory;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Service("cardService")
public class CardService {
	
	@Resource(name="cardService")
	private CardDao cardDao;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 业务功能：保存用户的名片信息
	 * @param card
	 * @return
	 */
	public Map<String,Object> addCard(Card card){
		Map<String,Object> result = CommonMapUtil.baseMsgToMapConvertor();
		cardDao.save(card);
		return result;
	}
	
	public Map<String,Object> modifyCard(String client_id,Map<String,Object> key){
		Card c = cardDao.findCardByClient_id(client_id);
		User user = userService.findUserById(client_id);
		CardHistory history = new CardHistory();
		for(String attr_name:key.keySet()){
			if(attr_name!=null){
				if(attr_name.equals("companynae")){
					c.setCompanyname((String) key.get("companynae"));
				}else if(attr_name.equals("position")){
					c.setPosition((String)key.get("position"));
				}else if(attr_name.equals("work_date")){
					c.setWork_date(TimeUtil.getFormatShortDate((String)key.get("work_date")));
				}else if(attr_name.equals("email")){
					c.setEmail((String)key.get("email"));
				}else if(attr_name.equals("phone")){
					c.setPhone((String)key.get("phone"));
				}else if(attr_name.equals("schoolname")){
					c.setSchoolname((String)key.get("schoolname"));
				}else if(attr_name.equals("major")){
					c.setMajor((String)key.get("major"));
				}else if(attr_name.equals("diploma")){
					c.setDiploma((String)key.get("diploma"));
				}else if(attr_name.equals("school_date")){
					c.setSchool_date(TimeUtil.getFormatShortDate((String)key.get("school_date")));
				}
				history.setAttribute(attr_name);
				history.setValue((String)key.get(attr_name));
				history.setUser(user);
				history.setChange_at(new Date());
			}
		}
		cardDao.saveOrUpdate(c);
		cardDao.addHistory(history);
		return CommonMapUtil.baseMsgToMapConvertor();
	}

}
