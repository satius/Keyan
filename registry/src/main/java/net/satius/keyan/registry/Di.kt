package net.satius.keyan.registry

import android.content.Context
import androidx.room.Room
import net.satius.keyan.core.domain.sesame.account.SesameAccountRepository
import net.satius.keyan.core.domain.sesame.api.SesameWebApiRepository
import net.satius.keyan.core.domain.sesame.device.SesameDeviceRepository
import net.satius.keyan.core.usecase.notification.NotificationUseCase
import net.satius.keyan.core.usecase.notification.NotificationUseCaseImpl
import net.satius.keyan.core.usecase.sesame.SesameUseCase
import net.satius.keyan.core.usecase.sesame.SesameUseCaseImpl
import net.satius.keyan.infrastructure.common.CryptoUtil
import net.satius.keyan.infrastructure.common.CryptoUtilImpl
import net.satius.keyan.infrastructure.common.KeyanDataBase
import net.satius.keyan.infrastructure.sesame.account.SesameAccountRepositoryImpl
import net.satius.keyan.infrastructure.sesame.api.SesameWebApiRepositoryImpl
import net.satius.keyan.infrastructure.sesame.device.SesameDeviceRepositoryImpl
import org.koin.dsl.module

fun createUseCaseModules() = listOf(
    module { single<NotificationUseCase> { NotificationUseCaseImpl() } },
    module { single<SesameUseCase> { SesameUseCaseImpl() } }
)

fun createRepositoryModules() = listOf(
    module { single<SesameAccountRepository> { SesameAccountRepositoryImpl() } },
    module { single<SesameDeviceRepository> { SesameDeviceRepositoryImpl() } },
    module { single<SesameWebApiRepository> { SesameWebApiRepositoryImpl() } }
)

fun createDataBaseModules(applicationContext: Context) = listOf(
    module {
        single {
            Room
                .databaseBuilder(
                    applicationContext,
                    KeyanDataBase::class.java,
                    "app.db"
                )
                .build()
        }
    }
)

fun createUtilModules(context: Context) = listOf(
    module {
        single<CryptoUtil> {
            CryptoUtilImpl(
                context
            )
        }
    }
)
