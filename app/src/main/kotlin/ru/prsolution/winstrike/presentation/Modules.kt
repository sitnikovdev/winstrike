package ru.prsolution.winstrike.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.prsolution.winstrike.data.datasource.CityCacheDataSource
import ru.prsolution.winstrike.datasource.cache.CityCacheDataSourceImpl
import ru.prsolution.winstrike.datasource.model.city.CityEntity
import ru.prsolution.winstrike.presentation.utils.cache.Cache
import ru.prsolution.winstrike.viewmodel.CityViewModel

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        viewModelModule
//        useCaseModule,
//        repositoryModule,
//        dataSourceModule,
//        networkModule,
//        cacheModule
    )
}

val viewModelModule: Module = module {
    viewModel { CityViewModel() }
//    viewModel { PostDetailsViewModel(userPostUseCase = get(), commentsUseCase = get()) }
}

/*val useCaseModule: Module = module {
    factory { UsersPostsUseCase(userRepository = get(), postRepository = get()) }
    factory { UserPostUseCase(userRepository = get(), postRepository = get()) }
    factory { CommentsUseCase(commentRepository = get()) }
}*/

val repositoryModule: Module = module {
/*    single { UserRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as UserRepository }
    single { PostRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as PostRepository }
    single { CommentRepositoryImpl(cacheDataSource = get(), remoteDataSource = get()) as CommentRepository }*/
}

val dataSourceModule: Module = module {
    single { CityCacheDataSourceImpl(cache = get(CITY_CACHE)) as CityCacheDataSource }
/*    single { UserRemoteDataSourceImpl(api = usersApi) as UserRemoteDataSource }
    single { PostCacheDataSourceImpl(cache = get(POST_CACHE)) as PostCacheDataSource }
    single { PostRemoteDataSourceImpl(api = postsApi) as PostRemoteDataSource }
    single { CommentCacheDataSourceImpl(cache = get(COMMENT_CACHE)) as CommentCacheDataSource }
    single { CommentRemoteDataSourceImpl(api = commentsApi) as CommentRemoteDataSource }*/
}

/*val networkModule: Module = module {
    single { usersApi }
    single { postsApi }
    single { commentsApi }
}*/

val cacheModule: Module = module {
    single(name = CITY_CACHE) { Cache<List<CityEntity>>() }
//    single(name = POST_CACHE) { Cache<List<PostEntity>>() }
//    single(name = COMMENT_CACHE) { Cache<List<Comment>>() }
}

//private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

//private val retrofit: Retrofit = createNetworkClient(BASE_URL, BuildConfig.DEBUG)

//private val postsApi: PostsApi = retrofit.create(PostsApi::class.java)
//private val usersApi: UsersApi = retrofit.create(UsersApi::class.java)
//private val commentsApi: CommentsApi = retrofit.create(CommentsApi::class.java)

private const val CITY_CACHE = "CITY_CACHE"
//private const val POST_CACHE = "POST_CACHE"
//private const val COMMENT_CACHE = "COMMENT_CACHE"
