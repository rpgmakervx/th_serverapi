package com.tonghang.web.label.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.label.dao.LabelDao;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;

@Service("labelService")
@Transactional
public class LabelService {

	@Resource(name="labelDao")
	private LabelDao labelDao;
	
//	public List<Map<String,List<Map<String,Object>>>> findLabelByUser(User user){
//		List<Label> labels = labelDao.findLabelByUser(user);
//		List<Map<String,List<Map<String,Object>>>> result = new ArrayList<Map<String,List<Map<String,Object>>>>();
//		Map<String,List<Map<String,Object>>> usermap = new HashMap<String, List<Map<String,Object>>>();
//		 List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();
//		for(Label l:labels){
//			Map<String,Object> msg = new HashMap<String, Object>();
//		}
//	}
	/**
	 * 添加时间：2015-12-12
	 * 根据标签名 模糊查找标签
	 * @param label_name
	 * @return
	 */
	public List<Label> findLabelByName(String label_name){
		return labelDao.findLabelByName(label_name);
	}
	
	/**
	 * 添加时间：2015-12-12
	 * 根据标签名构建标签对象，数据库中不存在的标签则保存下来
	 * @param list
	 * @return
	 */
	public Set<Label> makeLabelByLabelnames(List<String> list){
		List<Label> labels = new ArrayList<Label>();
		Set<Label> labellist = new HashSet<Label>();
		for(String label_name : list){
			Label label = labelDao.findLabelById(label_name);
			if(label==null){
				label = new Label();
				label.setLabel_name(label_name);
				labelDao.save(label);				
			}
			labels.add(label);
		}
		labellist.addAll(labels);
		return labellist;
	}
	
}
