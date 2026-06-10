package com.example.laza.di

import androidx.room.Room
import com.example.laza.data.api.ApiService
import com.example.laza.data.database.AppDatabase
import com.example.laza.data.repos.CartRepository
import com.example.laza.data.repos.CategoryRepository
import com.example.laza.data.repos.PaymentRepository
import com.example.laza.data.repos.ProductsRepository
import com.example.laza.data.repos.WishlistRepository
import com.example.laza.ui.viewmodels.CartViewModel
import com.example.laza.ui.viewmodels.CategoryViewModel
import com.example.laza.ui.viewmodels.PaymentViewModel
import com.example.laza.ui.viewmodels.ProductDetailsViewModel
import com.example.laza.ui.viewmodels.ProductsViewModel
import com.example.laza.ui.viewmodels.WishlistViewModel
import com.example.laza.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// di/NetworkModule.kt
val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())                              // get() → OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(ApiService::class.java) }
}

// di/RepositoryModule.kt
val repositoryModule = module {
    single { ProductsRepository(get()) }
    single { CategoryRepository(get()) }
    single { CartRepository(get()) }
    single { WishlistRepository(get()) }
    single { PaymentRepository(get()) }
}

// di/ViewModelModule.kt
val viewModelModule = module {
    viewModel { ProductsViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }
    viewModel { CartViewModel(get()) }
    viewModel { WishlistViewModel(get()) }
    viewModel { PaymentViewModel(get()) }
}

// di/dataBaseModule.kt
val dataBaseModule = module {
    // ✅ Room database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "laza_db"
        ).fallbackToDestructiveMigration() // <--- ADD THIS LINE
            .build()
    }
    // ✅ CartDao
    single { get<AppDatabase>().cartDao() }
    single { get<AppDatabase>().wishlistDao() }
    single { get<AppDatabase>().paymentDao() }

}