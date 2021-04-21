package com.tt.sample.function.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * 原文链接：https://blog.csdn.net/tianhongfan10106/article/details/103181875
 * <p>
 * //===================================
 * 多个实体类可以在entities，标签里面写
 *
 * @Database(entities = {GyExpandBean.class,DemoBean.class}, version = 1, exportSchema = false)
 */
@Database(entities = {GyExpandBean.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract GyExpandDao getGyExpandDao();

    //更新表的逻辑
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
////            database.execSQL("ALTER TABLE article_type ADD COLUMN age INTEGER NOT NULL DEFAULT 0");
//        }
//    };
}

