package net.satius.keyan.infrastructure.sesame.device


import androidx.room.*

@Dao
interface SesameDeviceAndroidBootLaunchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(account: SesameDeviceAndroidBootLaunchEntity)

    @Delete
    fun delete(account: SesameDeviceAndroidBootLaunchEntity)

    @Query("SELECT * FROM SesameDeviceAndroidBootLaunchEntity")
    fun findAll(): List<SesameDeviceAndroidBootLaunchEntity>

}
