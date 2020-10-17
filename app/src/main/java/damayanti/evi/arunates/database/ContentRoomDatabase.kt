package damayanti.evi.arunates.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import damayanti.evi.arunates.models.DataContent

@Database(entities = arrayOf(DataContent::class), version = 1, exportSchema = false)
abstract class ContentRoomDatabase : RoomDatabase() {

    abstract fun contentDao(): ContentDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ContentRoomDatabase? = null

        fun getDatabase(context: Context): ContentRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContentRoomDatabase::class.java,
                    "content_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
