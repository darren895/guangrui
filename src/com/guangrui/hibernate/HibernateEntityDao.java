package com.guangrui.hibernate;


import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.guangrui.util.GenericsUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <pre>
 * public class CaseManager extends HibernateEntityDao&lt;Case&gt; {
 * }
 * </pre>
 * 
 * @author springside
 * 
 * @see HibernateGenericDao
 */
public class HibernateEntityDao<T> extends HibernateGenericDao implements IEntityDao<T> {

	protected Class<T> entityClass;

	public HibernateEntityDao() {
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	public HibernateEntityDao(Class<T> type) {
		this.entityClass = type;
	}

	protected Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> type) {
		this.entityClass = type;
	}

	public T get(Serializable id) {
		return get(getEntityClass(), id);
	}

	public List<T> getAll() {
		return getAll(getEntityClass());
	}

	public List<T> getAll(String orderBy, boolean isAsc) {
		return getAll(getEntityClass(), orderBy, isAsc);
	}

	public void removeById(Serializable id) {
		removeById(getEntityClass(), id);
	}

	public Criteria createCriteria(Criterion... criterions) {
		return createCriteria(getEntityClass(), criterions);
	}

	public Criteria createCriteria(String orderBy, boolean isAsc,
			Criterion... criterions) {
		return createCriteria(getEntityClass(), orderBy, isAsc, criterions);
	}

	public List<T> findBy(String propertyName, Object value) {
		return findBy(getEntityClass(), propertyName, value);
	}

	public List<T> findBy(String propertyName, Object value, String orderBy,
			boolean isAsc) {
		return findBy(getEntityClass(), propertyName, value, orderBy, isAsc);
	}

	public T findUniqueBy(String propertyName, Object value) {
		return findUniqueBy(getEntityClass(), propertyName, value);
	}

	public boolean isUnique(Object entity, String uniquePropertyNames) {
		return isUnique(getEntityClass(), entity, uniquePropertyNames);
	}

	public void evit(Object entity) {
		getHibernateTemplate().evict(entity);
	}
}
