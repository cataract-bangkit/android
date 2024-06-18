package com.cataract.detection.connection.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cataract.detection.connection.model.ArticleModel

@Database(entities = [ArticleModel::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    companion object {
        @Volatile
        private var INSTANCE: ArticleDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): ArticleDatabase {
            if (INSTANCE == null) {
                synchronized(ArticleDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ArticleDatabase::class.java, "article_database")
                        .createFromAsset("database/article.db")
                        .build()
                }
            }
            return INSTANCE as ArticleDatabase
        }
    }
}