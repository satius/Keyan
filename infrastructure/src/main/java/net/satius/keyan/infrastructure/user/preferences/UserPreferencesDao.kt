package net.satius.keyan.infrastructure.user.preferences

import androidx.room.*

@Dao
interface UserPreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(preferences: UserPreferencesEntity)

    @Update
    fun update(preferences: UserPreferencesEntity)

    @Delete
    fun delete(preferences: UserPreferencesEntity)

    @Query("""
        SELECT * FROM UserPreferencesEntity
        WHERE updateAt=(
            SELECT MAX(updateAt)
            FROM UserPreferencesEntity
        )
        LIMIT 1
    """)
    fun find(): UserPreferencesEntity?
}
