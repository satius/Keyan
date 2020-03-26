package net.satius.keyan.core.di

import android.content.Context
import net.satius.keyan.core.domain.notification.NotificationRepository
import net.satius.keyan.core.domain.sesame.account.SesameAccountRepository
import net.satius.keyan.core.domain.sesame.api.SesameWebApiRepository
import net.satius.keyan.core.domain.sesame.device.SesameDeviceRepository
import net.satius.keyan.infrastructure.sesame.account.SesameAccountRepositoryImpl
import net.satius.keyan.infrastructure.sesame.api.SesameWebApiRepositoryImpl
import net.satius.keyan.infrastructure.sesame.device.SesameDeviceRepositoryImpl
import org.koin.dsl.module

fun createRepositoryModules(applicationContext: Context) = listOf(
    module { single<SesameAccountRepository> { SesameAccountRepositoryImpl() } },
    module { single<SesameDeviceRepository> { SesameDeviceRepositoryImpl() } },
    module { single<SesameWebApiRepository> { SesameWebApiRepositoryImpl() } }
)
