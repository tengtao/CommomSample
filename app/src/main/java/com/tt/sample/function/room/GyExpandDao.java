package com.tt.sample.function.room;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GyExpandDao {


    //------------------------query------------------------
    //简单sql语句，查询user表所有的column
    @Query("SELECT * FROM gyexpand")
    List<GyExpandBean> getAll();

    @Query("DELETE FROM  gyexpand")
    void delAll();

//    //根据条件查询，方法参数和注解的sql语句参数一一对应
//    @Query("SELECT * FROM equip_type WHERE id IN (:userIds)")
//    List<GyExpandBean> loadAllByIds(int[] userIds);


    //-----------------------insert----------------------

    // OnConflictStrategy.REPLACE表示如果已经有数据，那么就覆盖掉
    //数据的判断通过主键进行匹配，也就是uid，非整个GyExpandBean对象
    //返回Long数据表示，插入条目的主键值（uid）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GyExpandBean GyExpandBean);

    //返回List<Long>数据表示被插入数据的主键uid列表
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(GyExpandBean... GyExpandBeans);

    //返回List<Long>数据表示被插入数据的主键uid列表
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<GyExpandBean> GyExpandBeanList);


    //---------------------update------------------------
    //更新已有数据，根据主键（uid）匹配，而非整个user对象
    //返回类型int代表更新的条目数目，而非主键uid的值。
    //表示更新了多少条目
    @Update()
    int update(GyExpandBean GyExpandBean);

    //同上
    @Update()
    int updateAll(GyExpandBean... GyExpandBeans);

    //同上
    @Update()
    int updateAll(List<GyExpandBean> GyExpandBeanList);

    //
    @Update()
    int updateByGid(GyExpandBean GyExpandBean);

    //-------------------delete-------------------
    //删除user数据，数据的匹配通过主键uid实现。
    //返回int数据表示删除了多少条。非主键uid值。
    @Delete
    int delete(GyExpandBean GyExpandBean);

    //同上
    @Delete
    int deleteAll(List<GyExpandBean> GyExpandBeanList);

    //同上
    @Delete
    int deleteAll(GyExpandBean... GyExpandBeans);


}
