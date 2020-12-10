package com.example.testproject.mvvm;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

//设置为实体，也就是一个表
@Entity(tableName = "学生表",    //设置表名
        primaryKeys = "这是主键设置为：id", //设置表主键
        indices = {@Index("这个索引是:index")} //这是表索引

)
public class ARoom {
    @PrimaryKey  //设置主键
    public int id;
    @ColumnInfo(name = "index")  //在数据库中的名称是index
    public int suoyin;
    @Embedded
    public User user;

}

class User {
    public String name;
    public String age;
}
//@Dao
// interface UserDao
//{
//        @Query("SELECT * FROM ARoom WHERE id == :myid")
//        ARoom[] loadByFirstName(String myid);
//}
