package damayanti.evi.arunates.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import damayanti.evi.arunates.models.DataContent


@Dao
interface ContentDao {

    @Query("SELECT * from content_table ORDER BY userId ASC")
    fun getAllData(): MutableList<DataContent>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: DataContent)

    @Query("DELETE FROM content_table")
    fun deleteAll()

    @Query("SELECT * from content_table WHERE title LIKE :search")
    fun findWithTitle(search: String): MutableList<DataContent>
}