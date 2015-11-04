package com.guangrui.hibernate.baseDao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;

import com.guangrui.hibernate.HibernateEntityDao;
import com.guangrui.util.Page;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author IBaseDao的实现类通过spring注入HibernateEntityDao和HibernateEntityExtendDao来实现
 */

public class BaseDao<T> implements IBaseDao<T> {

    protected Class<T> entityClass;// DAO所管理的Entity类型.
    private HibernateEntityDao<T> hedao;

    public void setHedao(HibernateEntityDao<T> hedao) {
        hedao.setEntityClass(entityClass);
        this.hedao = hedao;
    }

    public HibernateEntityDao<T> getHedao() {
        return hedao;
    }

    /**
     * 让spring提供构造函数注入
     */
    public BaseDao(Class<T> type) {
        this.entityClass = type;
    }

    public BaseDao() {
    }

    /**
     * 根据ID获取对象.
     *
     * @see HibernateGenericDao#getId(Class, Object)
     */
    public T get(Serializable id) {
        return hedao.get(id);
    }

    /**
     * 获取全部对象
     *
     * @see HibernateGenericDao#getAll(Class)
     */
    public List<T> getAll() {
        return hedao.getAll();
    }

    /**
     * 获取全部对象,带排序参数.
     *
     * @see HibernateGenericDao#getAll(Class, String, boolean)
     */
    public List<T> getAll(String orderBy, boolean isAsc) {
        return hedao.getAll(orderBy, isAsc);
    }

    /**
     * 根据ID移除对象.
     *
     * @see HibernateGenericDao#removeById(Class, Serializable)
     */
    public void removeById(Serializable id) {
        hedao.removeById(id);
    }

    /**
     * 取得Entity的Criteria.
     *
     * @see HibernateGenericDao#createCriteria(Class, Criterion[])
     */
    public Criteria createCriteria(Criterion... criterions) {
        return hedao.createCriteria(criterions);
    }

    /**
     * 取得Entity的Criteria,带排序参数.
     *
     * @see HibernateGenericDao#createCriteria(Class, String, boolean, Criterion[])
     */
    public Criteria createCriteria(String orderBy, boolean isAsc,
                                   Criterion... criterions) {
        return hedao.createCriteria(orderBy, isAsc, criterions);
    }

    /**
     * 根据属性名和属性值查询对象.
     *
     * @return 符合条件的对象列表
     * @see HibernateGenericDao#findBy(Class, String, Object)
     */
    public List<T> findBy(String propertyName, Object value) {
        return hedao.findBy(propertyName, value);
    }

    /**
     * 根据属性名和属性值查询对象,带排序参数.
     *
     * @return 符合条件的对象列表
     * @see HibernateGenericDao#findBy(Class, String, Object, String, boolean)
     */
    public List<T> findBy(String propertyName, Object value, String orderBy,
                          boolean isAsc) {
        return hedao.findBy(propertyName, value, orderBy, isAsc);
    }

    /**
     * 根据属性名和属性值查询单个对象.
     *
     * @return 符合条件的唯一对象 or null
     * @see HibernateGenericDao#findUniqueBy(Class, String, Object)
     */
    public T findUniqueBy(String propertyName, Object value) {
        return hedao.findUniqueBy(propertyName, value);
    }

    /**
     * 判断对象某些属性的值在数据库中唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     * @see HibernateGenericDao#isUnique(Class, Object, String)
     */
    public boolean isUnique(Object entity, String uniquePropertyNames) {
        return hedao.isUnique(entity, uniquePropertyNames);
    }

    /**
     * 消除与 Hibernate Session 的关联
     *
     * @param entity
     */
    public void evit(Object entity) {
        hedao.evit(entity);
    }

    /**
     * 与数据库相关的校验,比如判断名字在数据库里有没有重复, 在保存时被调用,在此可重写.
     *
     * @see #save(Object)
     */
    public void onValid(T entity) {

    }

    /**
     * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
     */
    public T get(Class<T> entityClass, Serializable id) {
        return hedao.get(entityClass, id);
    }

    /**
     * 获取全部对象.
     */
    public List<T> getAll(Class<T> entityClass) {
        return hedao.getAll(entityClass);
    }

    /**
     * 获取全部对象,带排序字段与升降序参数.
     */
    public List<T> getAll(Class<T> entityClass, String orderBy, boolean isAsc) {
        return hedao.getAll(entityClass, orderBy, isAsc);
    }

    /**
     * 保存对象.
     */
    public void save(Object o) {
        hedao.save(o);
    }

    /**
     * 保存对象，主要用在更新多个相连的对象中
     *
     * @throws Exception
     */
    public void saveWithMerge(Object o) throws Exception {
        try {
            hedao.saveWithMerge(o);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除对象.
     */
    public void remove(Object o) {
        hedao.remove(o);
    }

    /**
     * 删除对象.
     */
    public void removeWithMerge(Object o) {
        hedao.removeWithMerge(o);
    }

    public void flush() {
        hedao.flush();
    }

    public void clear() {
        hedao.clear();
    }

    /**
     * 创建Query对象.
     * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
     * 留意可以连续设置,如下：
     * <p/>
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * <p/>
     * 调用方式如下：
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

        return hedao.createQuery(hql, values);
    }

    /**
     * 通过SQL语句创建Query对象
     *
     * @param sql
     * @param values
     * @return
     */
    public SQLQuery createSQLQuery(String sql, Object... values) {
        return hedao.createSQLQuery(sql, values);
    }

    /**
     * 创建Criteria对象.
     *
     * @param criterions 可变的Restrictions条件列表,见{@link #createQuery(String, Object...)}
     */
    public Criteria createCriteria(Class<T> entityClass,
                                   Criterion... criterions) {

        return hedao.createCriteria(entityClass, criterions);
    }

    /**
     * 创建Criteria对象，带排序字段与升降序字段.
     *
     * @see #createCriteria(Class, Criterion[])
     */
    public Criteria createCriteria(Class<T> entityClass, String orderBy,
                                   boolean isAsc, Criterion... criterions) {
        return hedao.createCriteria(entityClass, orderBy, isAsc, criterions);
    }

    /**
     * 创建Criteria对象，带排序字段与升降序字段.
     *
     * @see #createCriteria(Class, Criterion[])
     */
    public Criteria createCriteria(Class<T> entityClass, String[] orderBy,
                                   boolean[] isAsc, Criterion... criterions) {
        return hedao.createCriteria(entityClass, orderBy, isAsc, criterions);
    }

    /**
     * 根据hql查询,直接使用HibernateTemplate的find函数.
     *
     * @param values 可变参数,见{@link #createQuery(String, Object...)}
     */
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, Object... values) {
        return hedao.find(hql, values);
    }

    /**
     * 根据属性名和属性值查询对象.
     *
     * @return 符合条件的对象列表
     */
    public List<T> findBy(Class<T> entityClass, String propertyName,
                          Object value) {

        return hedao.findBy(entityClass, propertyName, value);
    }

    /**
     * 根据属性名和属性值查询对象,带排序参数.
     */
    public List<T> findBy(Class<T> entityClass, String propertyName,
                          Object value, String orderBy, boolean isAsc) {
        return hedao.findBy(entityClass, propertyName, value, orderBy, isAsc);
    }

    /**
     * 根据属性名和属性值查询唯一对象.
     *
     * @return 符合条件的唯一对象 or null if not found.
     */
    public T findUniqueBy(Class<T> entityClass, String propertyName,
                          Object value) {
        return hedao.findUniqueBy(propertyName, value);
    }

    /**
     * 分页查询函数，使用hql.
     *
     * @param pageNo 页号,从1开始.
     */
    public Page pagedQuery(String hql, int pageNo, int pageSize,
                           Object... values) {
        return hedao.pagedQuery(hql, pageNo, pageSize, values);
    }

    /**
     * @param hql      查询sql
     * @param start    分页从哪一条数据开始
     * @param pageSize 每一个页面的大小
     * @param values   查询条件
     * @return page对象
     * @author Scott.wanglei
     * @since 2008-7-21
     */
    public Page dataQuery(String hql, int start, int pageSize, Object... values)
            throws Exception {
        try {
            return hedao.dataQuery(hql, start, pageSize, values);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * realStart表示startIndex，而virtualStart用来表示当前页号
     */
    public Page dataQuery(String hql, int realStart, int virtualStart,
                          int pageSize, Object... values) throws Exception {
        try {
            return hedao.dataQuery(hql, realStart, virtualStart, pageSize,
                    values);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
     *
     * @param pageNo 页号,从1开始.
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
        return hedao.pagedQuery(criteria, pageNo, pageSize);
    }

    /**
     * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
     *
     * @param pageNo 页号,从1开始.
     * @return 含总记录数和当前页数据的Page对象.
     */
    @SuppressWarnings("rawtypes")
    public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
                           Criterion... criterions) {
        return hedao.pagedQuery(entityClass, pageNo, pageSize, criterions);
    }

    /**
     * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
     *
     * @param startIndex 起始索引号,从0开始.
     * @return 含总记录数和当前页数据的Page对象.
     */
    @SuppressWarnings("rawtypes")
    public Page pagedQueryFromStart(Class entityClass, int startIndex,
                                    int pageSize, Criterion... criterions) {
        return hedao.pagedQueryFromStart(entityClass, startIndex, pageSize,
                criterions);
    }

    @SuppressWarnings("rawtypes")
    public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
                           String orderBy, boolean isAsc, Criterion... criterions) {
        return hedao.pagedQuery(entityClass, pageNo, pageSize, orderBy, isAsc,
                criterions);
    }

    /**
     * 判断对象某些属性的值在数据库中是否唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     */
    public boolean isUnique(Class<T> entityClass, Object entity,
                            String uniquePropertyNames) {
        return hedao.isUnique(entity, uniquePropertyNames);
    }

    /**
     * 取得对象的主键值,辅助函数.
     */
    @SuppressWarnings("rawtypes")
    public Serializable getId(Class entityClass, Object entity)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        return hedao.getId(entityClass, entity);
    }

    /**
     * 取得对象的主键名,辅助函数.
     */
    @SuppressWarnings("rawtypes")
    public String getIdName(Class clazz) {
        return hedao.getIdName(clazz);
    }

    /**
     * 根据hql，start得到list对象
     */
    public List<T> listQuery(String hql, int start, int pageSize,
                             Object... values) {
        return hedao.listQuery(hql, start, pageSize, values);
    }

}