package edu.ualberta.cs.papersdb.server.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.ualberta.cs.papersdb.server.dao.AuthorDAO;
import edu.ualberta.cs.papersdb.server.dao.PaperDAO;

@Configuration
public class DAOConfig {

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Bean
	public PaperDAO paperDAO() {
		PaperDAOHibernateImpl dao = new PaperDAOHibernateImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}

	@Bean
	public AuthorDAO authorDAO() {
		AuthorDAOHibernateImpl dao = new AuthorDAOHibernateImpl();
		dao.setSessionFactory(sessionFactory);
		return dao;
	}
}