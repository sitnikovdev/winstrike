package ru.prsolution.winstrike.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.prsolution.winstrike.BuildConfig
import ru.prsolution.winstrike.data.datasource.ArenaCacheDataSource
import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.data.datasource.CityCacheDataSource
import ru.prsolution.winstrike.data.datasource.CityRemoteDataSource
import ru.prsolution.winstrike.data.repository.ArenaRepositoryImpl
import ru.prsolution.winstrike.data.repository.CityRepositoryImpl
import ru.prsolution.winstrike.datasource.cache.ArenaCacheDataSourceImpl
import ru.prsolution.winstrike.datasource.cache.CityCacheDataSourceImpl
import ru.prsolution.winstrike.datasource.remote.ArenaApi
import ru.prsolution.winstrike.datasource.remote.ArenaRemoteDataSourceImpl
import ru.prsolution.winstrike.datasource.remote.CityApi
import ru.prsolution.winstrike.datasource.remote.CityRemoteDataSourceImpl
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.domain.repository.ArenaRepository
import ru.prsolution.winstrike.domain.repository.CityRepository
import ru.prsolution.winstrike.domain.usecases.ArenaUseCase
import ru.prsolution.winstrike.domain.usecases.CityUseCase
import ru.prsolution.winstrike.networking.createNetworkClient
import ru.prsolution.winstrike.presentation.utils.cache.Cache
import ru.prsolution.winstrike.viewmodel.ArenaViewModel
import ru.prsolution.winstrike.viewmodel.CityViewModel

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
            viewModelModule,
            useCaseModule,
            repositoryModule,
            dataSourceModule,
            networkModule,
            cacheModule
    )
}

val viewModelModule: Module = module {
    viewModel { CityViewModel(cityUseCase = get()) }
    viewModel { ArenaViewModel(arenaUseCase = get()) }
}

val useCaseModule: Module = module {
    factory { CityUseCase(cityRepository = get()) }
    factory { ArenaUseCase(arenaRepository = get()) }
}

val repositoryModule: Module = module {
    single { CityRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as CityRepository }
    single { ArenaRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as ArenaRepository }
}

val dataSourceModule: Module = module {
    single { CityCacheDataSourceImpl(cache = get(CITY_CACHE)) as CityCacheDataSource }
    single { CityRemoteDataSourceImpl(api = cityApi) as CityRemoteDataSource }

    single { ArenaCacheDataSourceImpl(cache = get(CITY_CACHE)) as ArenaCacheDataSource }
    single { ArenaRemoteDataSourceImpl(api = arenaApi) as ArenaRemoteDataSource }
}

val networkModule: Module = module {
    single { cityApi }
}

val cacheModule: Module = module {
    single(name = CITY_CACHE) { Cache<List<City>>() }
}

private const val BASE_URL = BuildConfig.DEBUGURL

private val retrofit: Retrofit = createNetworkClient(BASE_URL, BuildConfig.DEBUG)

private val cityApi: CityApi = retrofit.create(CityApi::class.java)
private val arenaApi: ArenaApi = retrofit.create(ArenaApi::class.java)

private const val CITY_CACHE = "CITY_CACHE"
