package edu.ualberta.cs.papersdb.server.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.ualberta.cs.papersdb.persist.dao.AuthorDAO;
import edu.ualberta.cs.papersdb.persist.dao.PaperDAO;
import edu.ualberta.cs.papersdb.persist.dao.hibernate.AuthorDAOHibernateImpl;
import edu.ualberta.cs.papersdb.persist.dao.hibernate.PaperDAOHibernateImpl;

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
