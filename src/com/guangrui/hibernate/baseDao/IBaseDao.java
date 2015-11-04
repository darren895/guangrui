package com.guangrui.hibernate.baseDao;


import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;

import com.guangrui.util.Page;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author
 * 
 *         �ṩhibernate dao�����в���,
 *         ʵ������springע��HibernateEntityDao��HibernateEntityExtendDao��ʵ��
 *         ����޶ȵĽ���hibernate�־ò�Ĳ���
 */
public interface IBaseDao<T> {

	/**
	 * ����ID��ȡ����.
	 * 
	 * @see HibernateGenericDao#getId(Class,Object)
	 */
	public T get(Serializable id);

	/**
	 * ��ȡȫ������
	 * 
	 * @see HibernateGenericDao#getAll(Class)
	 */
	public List<T> getAll();

	/**
	 * ��ȡȫ������,���������.
	 * 
	 * @see HibernateGenericDao#getAll(Class,String,boolean)
	 */
	public List<T> getAll(String orderBy, boolean isAsc);

	/**
	 * ����ID�Ƴ�����.
	 * 
	 * @see HibernateGenericDao#removeById(Class,Serializable)
	 */
	public void removeById(Serializable id);

	/**
	 * ȡ��Entity��Criteria.
	 * 
	 * @see HibernateGenericDao#createCriteria(Class,Criterion[])
	 */
	public Criteria createCriteria(Criterion... criterions);

	/**
	 * ȡ��Entity��Criteria,���������.
	 * 
	 * @see HibernateGenericDao#createCriteria(Class,String,boolean,Criterion[])
	 */
	public Criteria createCriteria(String orderBy, boolean isAsc,
			Criterion... criterions);

	/**
	 * ����������������ֵ��ѯ����.
	 * 
	 * @return ���������Ķ����б�
	 * @see HibernateGenericDao#findBy(Class,String,Object)
	 */
	public List<T> findBy(String propertyName, Object value);

	/**
	 * ����������������ֵ��ѯ����,���������.
	 * 
	 * @return ���������Ķ����б�
	 * @see HibernateGenericDao#findBy(Class,String,Object,String,boolean)
	 */
	public List<T> findBy(String propertyName, Object value, String orderBy,
			boolean isAsc);

	/**
	 * ����������������ֵ��ѯ��������.
	 * 
	 * @return ����������Ψһ���� or null
	 * @see HibernateGenericDao#findUniqueBy(Class,String,Object)
	 */
	public T findUniqueBy(String propertyName, Object value);

	/**
	 * �ж϶���ĳЩ���Ե�ֵ�����ݿ���Ψһ.
	 * 
	 * @param uniquePropertyNames
	 *            ��POJO�ﲻ���ظ��������б�,�Զ��ŷָ� ��"name,loginid,password"
	 * @see HibernateGenericDao#isUnique(Class,Object,String)
	 */
	public boolean isUnique(Object entity, String uniquePropertyNames);

	/**
	 * ������ Hibernate Session �Ĺ���
	 * 
	 * @param entity
	 */
	public void evit(Object entity);

	/*******************************************************************************************/


	/**
	 * �����ݿ���ص�У��,�����ж����������ݿ�����û���ظ�, �ڱ���ʱ������,����������.
	 * 
	 * @see #save(Object)
	 */
	public void onValid(T entity);

	/*******************************************************************************************/

	/**
	 * ����ID��ȡ����. ʵ�ʵ���Hibernate��session.load()��������ʵ�����proxy����. ������󲻴��ڣ��׳��쳣.
	 */
	public T get(Class<T> entityClass, Serializable id);

	/**
	 * ��ȡȫ������.
	 */
	public List<T> getAll(Class<T> entityClass);

	/**
	 * ��ȡȫ������,�������ֶ������������.
	 */
	public List<T> getAll(Class<T> entityClass, String orderBy, boolean isAsc);

	/**
	 * �������.
	 */
	public void save(Object o);
	
	/**
	 * �������.
	 * @throws Exception 
	 */
	public void saveWithMerge(Object o) throws Exception;

	/**
	 * ɾ������.
	 */
	public void remove(Object o);
	
	/**
	 * ɾ������.
	 */
	public void removeWithMerge(Object o);

	public void flush();

	public void clear();

	/**
	 * ����Query����.
	 * ������Ҫfirst,max,fetchsize,cache,cacheRegion��������õĺ���,�����ڷ���Query����������.
	 * ���������������,���£�
	 * 
	 * <pre>
	 * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * </pre>
	 * 
	 * ���÷�ʽ���£�
	 * 
	 * <pre>
	 *        dao.createQuery(hql) 
	 *        dao.createQuery(hql,arg0); 
	 *        dao.createQuery(hql,arg0,arg1); 
	 *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
	 * </pre>
	 * 
	 * @param values
	 *            �ɱ����.
	 */
	public Query createQuery(String hql, Object... values);

	/**
	 * ����Criteria����.
	 * 
	 * @param criterions
	 *            �ɱ��Restrictions�����б�,��{@link #createQuery(String,Object...)}
	 */
	public Criteria createCriteria(Class<T> entityClass,
			Criterion... criterions);

	/**
	 * ����Criteria���󣬴������ֶ����������ֶ�.
	 * 
	 * @see #createCriteria(Class,Criterion[])
	 */
	public Criteria createCriteria(Class<T> entityClass, String orderBy,
			boolean isAsc, Criterion... criterions);

	/**
	 * ����hql��ѯ,ֱ��ʹ��HibernateTemplate��find����.
	 * 
	 * @param values
	 *            �ɱ����,��{@link #createQuery(String,Object...)}
	 */
	@SuppressWarnings("rawtypes")
	public List find(String hql, Object... values);

	/**
	 * ����������������ֵ��ѯ����.
	 * 
	 * @return ���������Ķ����б�
	 */
	public List<T> findBy(Class<T> entityClass, String propertyName,
			Object value);

	/**
	 * ����������������ֵ��ѯ����,���������.
	 */
	public List<T> findBy(Class<T> entityClass, String propertyName,
			Object value, String orderBy, boolean isAsc);
	
	public List<T> listQuery(String hql, int start, int pageSize, Object... values);

	/**
	 * ����������������ֵ��ѯΨһ����.
	 * 
	 * @return ����������Ψһ���� or null if not found.
	 */
	public T findUniqueBy(Class<T> entityClass, String propertyName,
			Object value);

	/**
	 * ��ҳ��ѯ������ʹ��hql.
	 * 
	 * @param pageNo
	 *            ҳ��,��1��ʼ.
	 */
	public Page pagedQuery(String hql, int pageNo, int pageSize,
			Object... values);

	/**
	 * @author Scott.wanglei
	 * @since 2008-7-21
	 * @param hql
	 *            ��ѯsql
	 * @param start
	 *            ��ҳ����һ�����ݿ�ʼ
	 * @param pageSize
	 *            ÿһ��ҳ��Ĵ�С
	 * @param values
	 *            ��ѯ����
	 * @return page����
	 * @throws Exception 
	 */
	public Page dataQuery(String hql, int start, int pageSize, Object... values) throws Exception;

	
	public Page dataQuery(String hql, int reaStart, int virtualStart, int pageSize, Object... values) throws Exception;
	
	/**
	 * ��ҳ��ѯ������ʹ������ò�ѯ�����������<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            ҳ��,��1��ʼ.
	 * @return ���ܼ�¼���͵�ǰҳ���ݵ�Page����.
	 */
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize);

	/**
	 * ��ҳ��ѯ����������entityClass�Ͳ�ѯ������������Ĭ�ϵ�<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            ҳ��,��1��ʼ.
	 * @return ���ܼ�¼���͵�ǰҳ���ݵ�Page����.
	 */
	@SuppressWarnings("rawtypes")
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			Criterion... criterions);

	/**
	 * ��ҳ��ѯ����������entityClass�Ͳ�ѯ��������,�����������Ĭ�ϵ�<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            ҳ��,��1��ʼ.
	 * @return ���ܼ�¼���͵�ǰҳ���ݵ�Page����.
	 */
	@SuppressWarnings("rawtypes")
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			String orderBy, boolean isAsc, Criterion... criterions);

	/**
	 * �ж϶���ĳЩ���Ե�ֵ�����ݿ����Ƿ�Ψһ.
	 * 
	 * @param uniquePropertyNames
	 *            ��POJO�ﲻ���ظ��������б�,�Զ��ŷָ� ��"name,loginid,password"
	 */
	public boolean isUnique(Class<T> entityClass, Object entity,
			String uniquePropertyNames);

	/**
	 * ȡ�ö��������ֵ,��������.
	 */
	@SuppressWarnings("rawtypes")
	public Serializable getId(Class entityClass, Object entity)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException;

}