package collectionsManager.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import collectionsManager.model.Collection;

@Repository
@Transactional
public class CollectionSearch {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Collection> search(String text) {

		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
				.getFullTextEntityManager(entityManager);

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(Collection.class).get();

		org.apache.lucene.search.Query query = queryBuilder.keyword().onFields("name", "about").matching(text)
				.createQuery();

		org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query,
				Collection.class);

		List<Collection> results = jpaQuery.getResultList();

		return results;
	}

}