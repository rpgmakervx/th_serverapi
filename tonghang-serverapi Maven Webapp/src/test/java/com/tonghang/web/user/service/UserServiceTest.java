package com.tonghang.web.user.service;

import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@Service("userServiceTest")
public class UserServiceTest {

	@Resource(name="userService")
	private UserService userService;
	@Test
	public void test(){
		System.out.println("Hello World");
		Map<String,Object> map = userService.userMessage("0058b773814cfe21ea8209cb920066822d3c695a", "0058b773814cfe21ea8209cb920066822d3c695a");
		JSONObject obj = new JSONObject(map);
		System.out.println(obj.toString());
	}
	
}
