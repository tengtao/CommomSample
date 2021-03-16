package com.tt.sample.function.room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * 设置表名，和gid唯一
 */
@Entity(tableName = "gyexpand", indices = {@Index(value = {"gid"}, unique = true)})
public class GyExpandBean {
    //设置主键，并且定义自增增
    @PrimaryKey(autoGenerate = true)
    private int id;
    //字段映射具体的数据表字段名
    @NonNull
    @ColumnInfo(name = "gid")
    private String gid;
    @NonNull
    @ColumnInfo(name = "isExpand")
    private boolean isExpand;

    /**
     * room必须要有个无参构造方法
     */
    public GyExpandBean() {

    }

    @Ignore
    public GyExpandBean(String gid, boolean expand) {
        this.gid = gid;
        this.isExpand = expand;
    }

    @Override
    public String toString() {
        return "GyExpandBean{" +
                "id=" + id +
                ", gid=" + gid +
                ", isExpand=" + isExpand +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
