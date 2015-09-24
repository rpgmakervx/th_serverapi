package com.tonghang.web.card.service;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.card.dao.CardDao;
import com.tonghang.web.card.pojo.Card;
import com.tonghang.web.card.pojo.CardHistory;
import com.tonghang.web.card.util.CardUtil;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Service("cardService")
@Transactional
public class CardService {
	
	@Resource(name="cardDao")
	private CardDao cardDao;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="cardUtil")
	private CardUtil cardUtil;
	/**
	 * 业务功能：保存用户的名片信息
	 * @param card
	 * @return
	 */
	public Map<String,Object> addCard(Card card){
		Map<String,Object> result = CommonMapUtil.baseMsgToMapConvertor();
		cardDao.saveOrUpdate(card);
		return result;
	}
	/**
	 * 业务功能：修改名片信息
	 * @param client_id
	 * @param key
	 * @return
	 */
	public Map<String,Object> modifyCard(String client_id,Map<String,Object> key){
		Card c = cardDao.findCardByClient_id(client_id);
		User user = userService.findUserById(client_id);
		CardHistory history = new CardHistory();
		for(String attr_name:key.keySet()){
			System.out.println("attribute_name : "+attr_name);
			if(attr_name!=null){
				if(attr_name.equals("companyname")){
					c.setCompanyname((String) key.get("companyname"));
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
	/**
	 * 业务功能：通过用户ID获取该用户的名片信息
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> findCardByUser(String client_id){
		Map<String,Object> result = CommonMapUtil.baseMsgToMapConvertor();
		Card card = cardDao.findCardByClient_id(client_id);
		result.putAll(cardUtil.cardToMapConvertor(card));
		return result;
	}
	/**
	 * 业务功能：添加名片交换次数（双向添加）
	 * @param self_id
	 * @param other_id
	 * @return
	 */
	public Map<String,Object> addExchangeTimes(String self_id,String other_id){
		Card self_card = cardDao.findCardByClient_id(self_id);
		Card other_card = cardDao.findCardByClient_id(other_id);
		self_card.setExchange_times(self_card.getExchange_times()+1);
		other_card.setExchange_times(other_card.getExchange_times()+1);
		cardDao.saveOrUpdate(self_card);
		cardDao.saveOrUpdate(other_card);
		return CommonMapUtil.baseMsgToMapConvertor();
	}
	
	/**
	 * 业务功能：创建交换名片请求。
	 * @param self_id		请求发起方ID
	 * @param other_id		请求接收方ID
	 * @return
	 * notice:给接收方发送推送
	 */
	public Map<String,Object> createRequest(String self_id,String other_id){
		User self = userService.findUserById(self_id);
		JPushUtil.push(other_id, self_id, self.getUsername(), Constant.REQUESTCARD, self.getUsername()+Constant.EXCHANGE_CARD_MSG);
		return CommonMapUtil.baseMsgToMapConvertor();
	}
	/**
	 * 业务功能：对方同意交换名片
	 * @param self_id		请求发起方ID
	 * @param other_id		请求接收方ID
	 * @return
	 */
	public Map<String,Object> agreeExchange(String self_id,String other_id){
		User self = userService.findUserById(self_id);
		JPushUtil.push(other_id, self_id, self.getUsername(), Constant.AGREEEXCHANGECARD,self.getUsername()+Constant.AGREE_CARD_MSG);
		return CommonMapUtil.baseMsgToMapConvertor();
	}
	
}
