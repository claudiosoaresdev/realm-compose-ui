package br.com.claudiosoaresdev.realmcomposeui.core.di

import br.com.claudiosoaresdev.realmcomposeui.core.network.RealmNetwork
import br.com.claudiosoaresdev.realmcomposeui.core.network.SduiApi
import br.com.claudiosoaresdev.realmcomposeui.core.repository.SduiRepository
import br.com.claudiosoaresdev.realmcomposeui.core.repository.SduiRepositoryImpl
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val coreModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC },
            )
            .build()
    }
    single { Moshi.Builder().build() }
    single {
        Retrofit.Builder()
            .baseUrl(RealmNetwork.BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .build()
    }
    single { get<Retrofit>().create(SduiApi::class.java) }
    single<SduiRepository> { SduiRepositoryImpl(api = get(), dispatcher = Dispatchers.IO) }
}
