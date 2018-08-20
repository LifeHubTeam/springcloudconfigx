package cn.lifehub.config.service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.jpa.impl.JPAQueryFactory;

public abstract class BaseDslService {

	@Autowired
	@PersistenceContext
	protected EntityManager entityManager;

	protected JPAQueryFactory jpaQueryFactory;

	@PostConstruct
	public void initDsl() {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}
}
