package net.satius.keyan.infrastructure.sesame.account

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.satius.keyan.core.domain.sesame.account.SesameAccount
import net.satius.keyan.core.domain.sesame.account.SesameAccountImpl
import net.satius.keyan.core.domain.sesame.account.SesameAccountRepository
import net.satius.keyan.infrastructure.common.CryptoUtil
import net.satius.keyan.infrastructure.common.KeyanDataBase
import org.koin.core.KoinComponent
import org.koin.core.inject

class SesameAccountRepositoryImpl : SesameAccountRepository, KoinComponent {

    private val cryptoUtil by inject<CryptoUtil>()
    private val db by inject<KeyanDataBase>()

    private val dao by lazy { db.sesameAccountDao() }

    override suspend fun upsert(name: String, authToken: String): SesameAccount {
        val allAccounts = withContext(Dispatchers.IO) {
            dao.findAll()
        }

        val alreadyExistAccount = allAccounts.firstOrNull { cryptoUtil.decrypt(it.authToken) == authToken }

        return alreadyExistAccount?.toSesameAccount() ?: run {
            val newAccount =
                SesameAccountEntity(
                    name = name,
                    authToken = cryptoUtil.encrypt(authToken)
                )
            withContext(Dispatchers.IO) {
                dao.create(newAccount)
            }
            newAccount.let {
                SesameAccountImpl(
                    name = it.name,
                    authToken = cryptoUtil.decrypt(it.authToken),
                    accountId = it.accountId
                )
            }
        }

    }

    override suspend fun rename(accountId: Long): SesameAccount? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(accountId: Long): SesameAccount? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAll(): List<SesameAccount> {
        return withContext(Dispatchers.IO) {
            dao.findAll().map {
                it.toSesameAccount()
            }
        }
    }

    override suspend fun get(accountId: Long): List<SesameAccount> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun SesameAccountEntity.toSesameAccount() = SesameAccountImpl(
        name = name,
        authToken = cryptoUtil.decrypt(authToken),
        accountId = accountId
    )
}
