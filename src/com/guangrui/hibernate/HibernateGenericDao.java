package com.guangrui.hibernate;



import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.guangrui.util.BeanUtils;
import com.guangrui.util.Page;

/**
 * Hibernate Dao的泛型基�?.
 * <p/>
 * 继承于Spring�?<code>HibernateDaoSupport</code>,提供分页函数和若干便捷查询方法，并对返回值作了泛型类型转�?.
 *
 * @author springside
 * @see HibernateDaoSupport
 * @see HibernateEntityDao
 */
public class HibernateGenericDao extends HibernateDaoSupport {

    /**
     * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
     */
    public <T> T get(Class<T> entityClass, Serializable id) {
        return getHibernateTemplate().load(entityClass, id);
    }

    /**
     * 获取全部对象.
     */
    public <T> List<T> getAll(Class<T> entityClass) {
        return getHibernateTemplate().loadAll(entityClass);
    }

    /**
     * 获取全部对象,带排序字段与升降序参�?.
     */
    public <T> List<T> getAll(Class<T> entityClass, String orderBy,
                              boolean isAsc) {
        Assert.hasText(orderBy);
        if (isAsc)
            return (List<T>) getHibernateTemplate().findByCriteria(
                    DetachedCriteria.forClass(entityClass).addOrder(
                            Order.asc(orderBy)));
        else
            return (List<T>) getHibernateTemplate().findByCriteria(
                    DetachedCriteria.forClass(entityClass).addOrder(
                            Order.desc(orderBy)));
    }

    /**
     * 保存对象.
     */
    public void save(Object o) {
        getHibernateTemplate().saveOrUpdate(o);
    }

    /**
     * 保存对象，主要用在更新多个相连的对象�?
     *
     * @throws Exception
     */
    public void saveWithMerge(Object o) throws Exception {
        try {
            getHibernateTemplate()
                    .saveOrUpdate(getHibernateTemplate().merge(o));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 删除对象.
     */
    public void remove(Object o) {
        getHibernateTemplate().delete(o);
    }

    /**
     * 删除对象.
     */
    public void removeWithMerge(Object o) {
        getHibernateTemplate().delete(getHibernateTemplate().merge(o));
    }

    /**
     * 根据ID删除对象.
     */
    public <T> void removeById(Class<T> entityClass, Serializable id) {
        remove(get(entityClass, id));
    }

    public void flush() {
        getHibernateTemplate().flush();
    }

    public void clear() {
        getHibernateTemplate().clear();
    }

    /**
     * 创建Query对象.
     * 对于�?要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设�?.
     * 留意可以连续设置,如下�?
     * <p/>
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * <p/>
     * 调用方式如下�?
     * <p/>
     * <pre>
     *        dao.createQuery(hql)
     *        dao.createQuery(hql,arg0);
     *        dao.createQuery(hql,arg0,arg1);
     *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
     * </pre>
     *
     * @param values 可变参数.
     */
    public Query createQuery(String hql, Object... values) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query;
    }

    /**
     * 通过sql语句创建query对象
     *
     * @param hql
     * @param values
     * @return
     */
    public SQLQuery createSQLQuery(String hql, Object... values) {
        Assert.hasText(hql);
        SQLQuery query = getSession().createSQLQuery(hql);
        for (int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }
        return query;
    }

    /**
     * 创建Criteria对象.
     *
     * @param criterions 可变的Restrictions条件列表,见{@link #createQuery(String, Object...)}
     */
    public <T> Criteria createCriteria(Class<T> entityClass,
                                       Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    /**
     * 创建Criteria对象，带排序字段与升降序字段.
     *
     * @see #createCriteria(Class, Criterion[])
     */
    public <T> Criteria createCriteria(Class<T> entityClass, String orderBy,
                                       boolean isAsc, Criterion... criterions) {
        Assert.hasText(orderBy);

        Criteria criteria = createCriteria(entityClass, criterions);

        if (isAsc)
            criteria.addOrder(Order.asc(orderBy));
        else
            criteria.addOrder(Order.desc(orderBy));

        return criteria;
    }

    /**
     * 创建Criteria对象，带排序字段与升降序字段.
     *
     * @see #createCriteria(Class, Criterion[])
     */
    public <T> Criteria createCriteria(Class<T> entityClass, String[] orderBy,
                                       boolean[] isAsc, Criterion... criterions) {
        Assert.hasText(orderBy.toString());
        Assert.isTrue(orderBy.length == isAsc.length);

        Criteria criteria = createCriteria(entityClass, criterions);
        if (orderBy.length > 0) {
            for (int i = 0; i < orderBy.length; i++) {
                if (isAsc[i]) {
                    criteria.addOrder(Order.asc(orderBy[i]));
                } else {
                    criteria.addOrder(Order.desc(orderBy[i]));
                }
            }
        }

        return criteria;
    }

    /**
     * 根据hql查询,直接使用HibernateTemplate的find函数.
     *
     * @param values 可变参数,见{@link #createQuery(String, Object...)}
     */
    public List find(String hql, Object... values) {
        Assert.hasText(hql);
        return getHibernateTemplate().find(hql, values);
    }

    /**
     * 根据属�?�名和属性�?�查询对�?.
     *
     * @return 符合条件的对象列�?
     */
    public <T> List<T> findBy(Class<T> entityClass, String propertyName,
                              Object value) {
        Assert.hasText(propertyName);
        return createCriteria(entityClass, Restrictions.eq(propertyName, value))
                .list();
    }

    /**
     * 根据属�?�名和属性�?�查询对�?,带排序参�?.
     */
    public <T> List<T> findBy(Class<T> entityClass, String propertyName,
                              Object value, String orderBy, boolean isAsc) {
        Assert.hasText(propertyName);
        Assert.hasText(orderBy);
        return createCriteria(entityClass, orderBy, isAsc,
                Restrictions.eq(propertyName, value)).list();
    }

    /**
     * 根据属�?�名和属性�?�查询唯�?对象.
     *
     * @return 符合条件的唯�?对象 or null if not found.
     */
    public <T> T findUniqueBy(Class<T> entityClass, String propertyName,
                              Object value) {
        Assert.hasText(propertyName);
        return (T) createCriteria(entityClass,
                Restrictions.eq(propertyName, value)).setCacheable(true).uniqueResult();
    }

    /**
     * 分页查询函数，使用hql.
     *
     * @param pageNo 页号,�?1�?�?.
     */
    public Page pagedQuery(String hql, int pageNo, int pageSize,
                           Object... values) {
        Assert.hasText(hql);
        Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
        // Count查询
        String countQueryString = "select count(*) "
                + removeSelect(removeOrders(hql));
        List countList = getHibernateTemplate().find(countQueryString, values);
        long totalCount = (Long) countList.get(0);

        if (totalCount < 1)
            return new Page();
        // 实际查询返回分页对象
        int startIndex = Page.getStartOfPage(pageNo, pageSize);
        Query query = createQuery(hql, values);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
                .list();

        return new Page(startIndex, totalCount, pageSize, list);
    }

    /**
     * 查询总记录数
     *
     * @param hql
     * @param values
     * @return
     */
    public long getTotalCount(String hql, Object... values) {
        Assert.hasText(hql);
        // Count查询
        String countQueryString = "select count(*) " + removeSelect(removeOrders(hql));
        List countList = getHibernateTemplate().find(countQueryString, values);
        long totalCount = (Long) countList.get(0);
        return totalCount;
    }

    /**
     * @param hql      查询sql
     * @param start    分页从哪�?条数据开�?
     * @param pageSize 每一个页面的大小
     * @param values   查询条件
     * @return page对象
     * @throws Exception
     * @author Scott.wanglei
     * @since 2008-7-21
     */
    public Page dataQuery(String hql, int start, int pageSize, Object... values)
            throws Exception {
        try {

            // Count查询
            String countQueryString = " select count (*) "
                    + removeSelect(removeOrders(hql));
            List countList = getHibernateTemplate().find(countQueryString,
                    values);
            long totalCount = (Long) countList.get(0);

            if (totalCount < 1)
                return new Page();
            // 实际查询返回分页对象
            int startIndex = start;
            Query query = createQuery(hql, values);
            List list = query.setFirstResult(startIndex)
                    .setMaxResults(pageSize).list();
            return new Page(startIndex, totalCount, pageSize, list);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Page dataQuery(String hql, int realStart, int virtualstart,
                          int pageSize, Object... values) throws Exception {
        try {

            // Count查询
            String countQueryString = " select count (*) "
                    + removeSelect(removeOrders(hql));
            List countList = getHibernateTemplate().find(countQueryString,
                    values);
            long totalCount = (Long) countList.get(0);

            if (totalCount < 1)
                return new Page();
            // 实际查询返回分页对象
            int startIndex = realStart;
            Query query = createQuery(hql, values);
            List list = query.setFirstResult(startIndex)
                    .setMaxResults(pageSize).list();
            return new Page(virtualstart, totalCount, pageSize, list);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param hql      查询sql
     * @param start    从哪�?条数据开�?
     * @param pageSize 每一个页面的大小
     * @param values   查询条件
     * @return list对象
     * @author lilin
     */
    public <T> List<T> listQuery(String hql, int start, int pageSize,
                                 Object... values) {
        // Count查询
        String countQueryString = " select count (*) "
                + removeSelect(removeOrders(hql));
        List countList = getHibernateTemplate().find(countQueryString, values);
        long totalCount = (Long) countList.get(0);

        if (totalCount < 1)
            return new ArrayList();
        // 实际查询返回分页对象
        int startIndex = start;
        Query query = createQuery(hql, values);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
                .list();
        return list;
    }

    /**
     * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
     *
     * @param start 起始索引�?,�?0�?�?.
     * @return 含�?�记录数和当前页数据的Page对象.
     */
    public Page pagedQueryFromStart(Criteria criteria, int start, int pageSize) {
        Assert.notNull(criteria);
        Assert.isTrue(start >= 0, "start should start from 1");
        CriteriaImpl impl = (CriteriaImpl) criteria;

        // 先把Projection和OrderBy条件取出�?,清空两�?�来执行Count操作
        Projection projection = impl.getProjection();
        List<CriteriaImpl.OrderEntry> orderEntries;
        try {
            orderEntries = (List) BeanUtils.forceGetProperty(impl,
                    "orderEntries");
            BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }

		/*
         *
		 * // 执行查询 int totalCount = (Integer) criteria.setProjection(
		 * Projections.rowCount()).uniqueResult();
		 */

        // 执行查询
        long totalCount = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();

        // 将之前的Projection和OrderBy条件重新设回�?
        criteria.setProjection(projection);
        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }

        try {
            BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }

        // 返回分页对象
        if (totalCount < 1)
            return new Page();

        List list = criteria.setFirstResult(start).setMaxResults(pageSize)
                .list();
        return new Page(start, totalCount, pageSize, list);
    }

    /**
     * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
     *
     * @param pageNo 页号,�?0�?�?.
     * @return 含�?�记录数和当前页数据的Page对象.
     */
    public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
        Assert.notNull(criteria);
        Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
        CriteriaImpl impl = (CriteriaImpl) criteria;

        // 先把Projection和OrderBy条件取出�?,清空两�?�来执行Count操作
        Projection projection = impl.getProjection();
        List<CriteriaImpl.OrderEntry> orderEntries;
        try {
            orderEntries = (List) BeanUtils.forceGetProperty(impl,
                    "orderEntries");
            BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }

		/*
         *
		 * // 执行查询 int totalCount = (Integer) criteria.setProjection(
		 * Projections.rowCount()).uniqueResult();
		 */

        // 执行查询
        long totalCount = (Long) criteria.setProjection(Projections.rowCount())
                .uniqueResult();

        // 将之前的Projection和OrderBy条件重新设回�?
        criteria.setProjection(projection);
        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }

        try {
            BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }

        // 返回分页对象
        if (totalCount < 1)
            return new Page();

        int startIndex = Page.getStartOfPage(pageNo, pageSize);
        if (startIndex >= 4985) {
            startIndex = 4985;
        }
        List list = criteria.setFirstResult(startIndex).setMaxResults(pageSize)
                .list();
        return new Page(startIndex, totalCount, pageSize, list);
    }

    /**
     * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
     *
     * @param pageNo 页号,�?1�?�?.
     * @return 含�?�记录数和当前页数据的Page对象.
     */
    public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
                           Criterion... criterions) {
        Criteria criteria = createCriteria(entityClass, criterions);
        return pagedQuery(criteria, pageNo, pageSize);
    }

    /**
     * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
     *
     * @param start 起始索引�?,�?0�?�?.
     * @return 含�?�记录数和当前页数据的Page对象.
     */
    public Page pagedQueryFromStart(Class entityClass, int start, int pageSize,
                                    Criterion... criterions) {
        Criteria criteria = createCriteria(entityClass, criterions);
        return pagedQueryFromStart(criteria, start, pageSize);
    }

    /**
     * 分页查询函数，根据entityClass和查询条件参�?,排序参数创建默认�?<code>Criteria</code>.
     *
     * @param pageNo 页号,�?1�?�?.
     * @return 含�?�记录数和当前页数据的Page对象.
     */
    public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
                           String orderBy, boolean isAsc, Criterion... criterions) {
        Criteria criteria = createCriteria(entityClass, orderBy, isAsc,
                criterions);
        return pagedQuery(criteria, pageNo, pageSize);
    }



    /**
     * 判断对象某些属�?�的值在数据库中是否唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属�?�列�?,以�?�号分割 �?"name,loginid,password"
     */
    public <T> boolean isUnique(Class<T> entityClass, Object entity,
                                String uniquePropertyNames) {
        Assert.hasText(uniquePropertyNames);
        Criteria criteria = createCriteria(entityClass).setProjection(
                Projections.rowCount());
        String[] nameList = uniquePropertyNames.split(",");
        try {
            // 循环加入唯一�?
            for (String name : nameList) {
                criteria.add(Restrictions.eq(name,
                        PropertyUtils.getProperty(entity, name)));
            }

            // 以下代码为了如果是update的情�?,排除entity自身.

            String idName = getIdName(entityClass);

            // 取得entity的主键�??
            Serializable id = getId(entityClass, entity);

            // 如果id!=null,说明对象已存�?,该操作为update,加入排除自身的判�?
            if (id != null)
                criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return (Integer) criteria.uniqueResult() == 0;
    }

    /**
     * 取得对象的主键�??,辅助函数.
     */
    public Serializable getId(Class entityClass, Object entity)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Assert.notNull(entity);
        Assert.notNull(entityClass);
        return (Serializable) PropertyUtils.getProperty(entity,
                getIdName(entityClass));
    }

    /**
     * 取得对象的主键名,辅助函数.
     */
    public String getIdName(Class clazz) {
        Assert.notNull(clazz);
        ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
        Assert.notNull(meta, "Class " + clazz
                + " not define in hibernate session factory.");
        String idName = meta.getIdentifierPropertyName();
        Assert.hasText(idName, clazz.getSimpleName()
                + " has no identifier property define.");
        return idName;
    }

    /**
     * 去除hql的select 子句，未考虑union的情�?,用于pagedQuery.
     *
     * @see #pagedQuery(String, int, int, Object[])
     */
    private static String removeSelect(String hql) {
        Assert.hasText(hql);
        int beginPos = hql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " hql : " + hql
                + " must has a keyword 'from'");
        return hql.substring(beginPos);
    }

    /**
     * 去除hql的orderBy 子句，用于pagedQuery.
     *
     * @see #pagedQuery(String, int, int, Object[])
     */
    private static String removeOrders(String hql) {
        Assert.hasText(hql);
        Pattern p = Pattern.compile("order//s*by[//w|//W|//s|//S]*",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }
}