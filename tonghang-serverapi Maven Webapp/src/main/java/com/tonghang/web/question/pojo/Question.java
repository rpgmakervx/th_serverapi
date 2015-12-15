package com.tonghang.web.question.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import com.tonghang.web.common.builder.Builder;
import com.tonghang.web.user.pojo.User;

@Component("question")
@Entity
@Table(name="questions")
public class Question {

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	public String question_id;
	
	@ManyToOne()
	@JoinColumn(name="anchor_id")
	public User anchor;
	
	@ManyToOne()
	@JoinColumn(name="asker_id")
	public User asker;
	
	@Column(name="content")
	private String content;
	
	@Column(name="answer_times")
	private int answer_times;
	
	@Column(name="created_at")
	private Date created_at;

	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

	public User getAnchor() {
		return anchor;
	}

	public void setAnchor(User anchor) {
		this.anchor = anchor;
	}

	public User getAsker() {
		return asker;
	}

	public void setAsker(User asker) {
		this.asker = asker;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public int getAnswer_times() {
		return answer_times;
	}

	public void setAnswer_times(int answer_times) {
		this.answer_times = answer_times;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	
	public static class QuestionBuilder implements Builder<Question>{

		private Question question;
		public QuestionBuilder setQuestion_id(String question_id) {
			question.question_id = question_id;
			return this;
		}

		public QuestionBuilder setAnchor(User anchor) {
			question.anchor = anchor;
			return this;
		}

		public QuestionBuilder setAsker(User asker) {
			question.asker = asker;
			return this;
		}

		public QuestionBuilder setContent(String content) {
			question.content = content;
			return this;
		}

		public QuestionBuilder setAnswer_times(int answer_times) {
			question.answer_times = answer_times;
			return this;
		}

		public QuestionBuilder setCreated_at(Date created_at) {
			question.created_at = created_at;
			return this;
		}
		@Override
		public QuestionBuilder create() {
			// TODO Auto-generated method stub
			question = new Question();
			return this;
		}

		@Override
		public Question build() {
			// TODO Auto-generated method stub
			return question;
		}
		
	}
}
