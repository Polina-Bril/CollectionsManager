package collectionsManager.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import collectionsManager.model.Item;

@Repository
@Transactional
public class ItemSearch {
	@PersistenceContext
	private EntityManager entityManager;

	public List<Item> search(String text) {

		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
				.getFullTextEntityManager(entityManager);

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Item.class)
				.get();

		org.apache.lucene.search.Query query = queryBuilder.keyword().onFields("name", "tags").matching(text)
				.createQuery();

		org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Item.class);

		List<Item> results = jpaQuery.getResultList();

		return results;
	}
}