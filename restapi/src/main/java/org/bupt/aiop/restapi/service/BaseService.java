package org.bupt.aiop.restapi.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 通用service
 * Created by zlren on 17/2/14.
 */
public abstract class BaseService<T> {

    // T指定泛型，Spring4支持泛型注入
    // 实际注入的是T对应的Mapper接口
    @Autowired
    private Mapper<T> mapper;

    public Mapper<T> getMapper() {
        return this.mapper;
    }

    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    public T queryById(Integer id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<T> queryAll() {
        return this.mapper.select(null);
    }

    /**
     * 根据条件查询一条数据，如果有多条数据会抛出异常
     *
     * @param record
     * @return
     */
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    /**
     * 根据条件查询数据列表
     *
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record) {
        return this.mapper.select(record);
    }

    /**
     * 分页查询
     * 可以无条件查询所有记录，可以根据某一个属性进行完全匹配的查询
     *
     * @param page
     * @param rows
     * @param record 为null表示查询所有
     * @return
     */
    public PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record) {
        // 设置分页条件
        PageHelper.startPage(page, rows);
        List<T> list = this.queryListByWhere(record);
        return new PageInfo<T>(list);
    }

    /**
     * 新增数据，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer save(T record) {
        // record.setCreated(new Date());
        // record.setUpdated(record.getCreated());
        return this.mapper.insert(record);
    }

    /**
     * 新增数据，使用不为null的字段，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer saveSelective(T record) {
        // record.setCreated(new Date());
        // record.setUpdated(record.getCreated());
        return this.mapper.insertSelective(record);
    }

    /**
     * 修改数据，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer update(T record) {
        return this.mapper.updateByPrimaryKey(record);
    }

    /**
     * 修改数据，使用不为null的字段，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer updateSelective(T record) {
        // record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    public Integer deleteById(Integer id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     *
     * @param clazz    传递进来的是类本身
     * @param property
     * @param values
     * @return
     */
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values); // 注意是 andIn
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据条件做删除
     *
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record) {
        return this.mapper.delete(record);
    }

    // /**
    //  * 根据某个属性值进行模糊查询(查询所有姓张的用户)
    //  *
    //  * @param page
    //  * @param rows
    //  * @param property
    //  * @param value
    //  * @return
    //  */
    // public PageInfo<T> queryPageListByPropertyFuzzy(Integer pageNow, Integer pageSize, String property, String
    // value, T
    // record) {
    //     String searchText = "%" + value + "%";
    //
    //     Example example = new Example(T.class);
    //     Example.Criteria criteria = example.createCriteria();
    //     criteria.andLike(property, searchText);
    //
    //     PageHelper.startPage(page, rows);
    //
    //     List<T> expertList = this.mapper.selectByExample(example);
    //
    //     return new PageInfo<>(expertList);
    // }

}
