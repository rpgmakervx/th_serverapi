package com.tonghang.web.card.service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.tonghang.web.user.util.UserUtil;

@Service("cardService")
@Transactional
public class CardService {
	
	@Resource(name="cardDao")
	private CardDao cardDao;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="cardUtil")
	private CardUtil cardUtil;
	@Resource(name="userUtil")
	private UserUtil userUtil;
	@Resource(name="userDao")
	private UserDao userDao;
	/**
	 * 业务功能：保存用户的名片信息
	 * @param card
	 * @return
	 */
	public Map<String,Object> addCard(Card card){
		Map<String,Object> result = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> success = new HashMap<String, Object>();
		cardDao.saveOrUpdate(card);
		success.put("success", result);
		return success;
	}
	/**
	 * 业务功能：修改名片信息
	 * @param client_id
	 * @param key
	 * @return
	 */
	public Map<String,Object> modifyCard(String client_id,Map<String,Object> key){
		Map<String,Object> success = new HashMap<String, Object>();
		Card c = cardDao.findCardByClient_id(client_id);
		User user = userService.findUserById(client_id);
		CardHistory history = new CardHistory();
		if(!c.getRealname().equals((String) key.get("realname")))
			history.setRealname((String) key.get("realname"));
		c.setRealname((String) key.get("realname"));
		if(!c.getCompanyname().equals((String) key.get("companyname")))
			history.setCompanyname((String) key.get("companyname"));
		c.setCompanyname((String) key.get("companyname"));
		if(!c.getPosition().equals((String) key.get("position")))
			history.setPosition((String) key.get("position"));
		c.setPosition((String)key.get("position"));
		if(!c.getWork_date().equals((String)key.get("work_date")))
			history.setWork_date((String)key.get("work_date"));
		c.setWork_date((String)key.get("work_date"));
		if(!c.getEmail().equals((String)key.get("email")))
			history.setEmail((String)key.get("email"));
		c.setEmail((String)key.get("email"));
		if(!c.getPhone().equals((String)key.get("phone")))
			history.setPhone((String)key.get("phone"));
		c.setPhone((String)key.get("phone"));
		if(!c.getSchoolname().equals((String)key.get("schoolname")))
			history.setSchoolname((String)key.get("schoolname"));
		c.setSchoolname((String)key.get("schoolname"));
		if(!c.getMajor().equals((String)key.get("major")))
			history.setMajor((String)key.get("major"));
		c.setMajor((String)key.get("major"));
		if(!c.getDiploma().equals((String)key.get("diploma")))
			history.setDiploma((String)key.get("diploma"));
		c.setDiploma((String)key.get("diploma"));
		if(!c.getSchool_date().equals((String)key.get("school_date")))
			history.setSchool_date((String)key.get("school_date"));
		c.setSchool_date((String)key.get("school_date"));
		history.setUser(user);
		history.setChange_at(TimeUtil.getFormatString(new Date()));
		cardDao.saveOrUpdate(c);
		cardDao.addHistory(history);
		System.out.println("update successfully");
		success.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return success;
	}
	/**
	 * 业务功能：通过用户ID获取该用户的名片信息
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> findCardByUser(String client_id){
		Map<String,Object> result = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> success = new HashMap<String, Object>();
		Card card = cardDao.findCardByClient_id(client_id);
		User user = userDao.findUserById(client_id);
		result.putAll(cardUtil.cardToMapConvertor(card));
		result.putAll(userUtil.userToMapConvertor(user, client_id));
		success.put("success", result);
		return success;
	}
	/**
	 * 业务功能：添加名片交换次数（双向添加）
	 * @param self_id
	 * @param other_id
	 * @return
	 */
	public Map<String,Object> addExchangeTimes(String self_id,String other_id){
		Map<String,Object> success = new HashMap<String, Object>();
		Card self_card = cardDao.findCardByClient_id(self_id);
		Card other_card = cardDao.findCardByClient_id(other_id);
		self_card.setExchange_times(self_card.getExchange_times()+1);
		other_card.setExchange_times(other_card.getExchange_times()+1);
		cardDao.saveOrUpdate(self_card);
		cardDao.saveOrUpdate(other_card);
		success.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return success;
	}
	
	/**
	 * 业务功能：创建交换名片请求。
	 * @param self_id		请求发起方ID
	 * @param other_id		请求接收方ID
	 * @return
	 * notice:给接收方发送推送
	 */
	public Map<String,Object> createRequest(String self_id,String other_id){
		Map<String,Object> success = new HashMap<String, Object>();
		User self = userService.findUserById(self_id);
		JPushUtil.push(other_id, self_id, self.getUsername(), Constant.REQUESTCARD, self.getUsername()+Constant.EXCHANGE_CARD_MSG);
		success.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return success;
	}
	/**
	 * 业务功能：对方同意交换名片
	 * @param self_id		请求发起方ID
	 * @param other_id		请求接收方ID
	 * @return
	 */
	public Map<String,Object> agreeExchange(String self_id,String other_id){
		Map<String,Object> success = new HashMap<String, Object>();
		User self = userService.findUserById(self_id);
		JPushUtil.push(other_id, self_id, self.getUsername(), Constant.AGREEEXCHANGECARD,self.getUsername()+Constant.AGREE_CARD_MSG);
		success.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return success;
	}
	
	private void setCardAttribute(Map<String,Object> key){
		
		
	}
	
	private void setCardHistoryAttribute(CardHistory history){
		
	}
	
}

class Attribute{
	protected Card card;
	protected CardHistory history;
	public Attribute(){
		
	}
	void setCardAttribute(String attr){
	}
	void setCardHisoryAttribute(String attr){
	}
}
class RealName extends Attribute {
	
	@Override
	void setCardAttribute(String attr){
		super.card.setRealname(attr);
	}
	@Override
	void setCardHisoryAttribute(String attr){
		super.history.setRealname(attr);
	}
}
class CompanyName extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setCompanyname(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setCompanyname(attr);
	}
}
class Position extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setPosition(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setPosition(attr);
	}
}
class WorkDate extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setWork_date(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setWork_date(attr);
	}
}
class Email extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setEmail(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setEmail(attr);
	}
}
class Phone extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setPhone(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setPhone(attr);
	}
}
class SchoolName  extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setSchoolname(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setSchoolname(attr);
	}
}
class Major extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setMajor(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setMajor(attr);
	}
}
class Diploma extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setDiploma(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setDiploma(attr);
	}
}
class SchoolDate extends Attribute{

	@Override
	void setCardAttribute(String attr) {
		// TODO Auto-generated method stub
		super.card.setSchool_date(attr);
	}

	@Override
	void setCardHisoryAttribute(String attr) {
		// TODO Auto-generated method stub
		super.history.setSchool_date(attr);
	}
}
class Strategy{
	
	void setCardAttribute(Attribute attr,String card) {
		// TODO Auto-generated method stub
		attr.setCardAttribute(card);
	}
	void setCardHisoryAttribute(Attribute attr,String history) {
		// TODO Auto-generated method stub
		attr.setCardHisoryAttribute(history);
	}
	
}

