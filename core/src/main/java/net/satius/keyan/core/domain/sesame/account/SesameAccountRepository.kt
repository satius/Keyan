package net.satius.keyan.core.domain.sesame.account

interface SesameAccountRepository {
    suspend fun upsert(name: String, authToken: String): SesameAccount
    suspend fun rename(accountId: Long): SesameAccount?
    suspend fun delete(accountId: Long): SesameAccount?
    suspend fun getAll(): List<SesameAccount>
    suspend fun get(accountId: Long): List<SesameAccount>
}
