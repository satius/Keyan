package net.satius.keyan.infrastructure.sesame.account

import androidx.room.*

@Dao
interface SesameAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(account: SesameAccountEntity)

    @Update
    fun update(account: SesameAccountEntity)

    @Delete
    fun delete(account: SesameAccountEntity)

    @Query("SELECT * FROM SesameAccountEntity")
    fun findAll(): List<SesameAccountEntity>

    @Query("SELECT * FROM SesameAccountEntity WHERE accountId = :id")
    fun find(id: Long): SesameAccountEntity?
}
