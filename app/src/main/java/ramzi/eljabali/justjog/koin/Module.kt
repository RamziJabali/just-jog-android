package ramzi.eljabali.justjog.koin

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ramzi.eljabali.justjog.motivationalquotes.MotivationQuotesAPI
import ramzi.eljabali.justjog.repository.room.database.JustJogDataBase
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntryDAO
import ramzi.eljabali.justjog.repository.room.jogsummary.JogSummaryDAO
import ramzi.eljabali.justjog.repository.room.jogsummarytemp.JogSummaryTempDAO
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.viewmodel.CalendarViewModel
import ramzi.eljabali.justjog.viewmodel.MapViewModel
import ramzi.eljabali.justjog.viewmodel.StatisticsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val statisticsModule = module {
    viewModel { StatisticsViewModel(get<JogUseCase>(), get<MotivationQuotesAPI>(), get<Application>().applicationContext) }
}

val calendarModule = module {
    viewModel { CalendarViewModel(get<JogUseCase>()) }
}

val mapModule = module {
    viewModel { MapViewModel(get<JogUseCase>()) }
}

val jogDataBaseModule = module {
    single<JustJogDataBase> {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            JustJogDataBase::class.java, "just-jog-database"
        ).build()
    }
    single<JogEntryDAO> { get<JustJogDataBase>().jogEntryDao() }
    single<JogSummaryDAO> { get<JustJogDataBase>().jogSummaryDao() }
    single<JogSummaryTempDAO> { get<JustJogDataBase>().jogSummaryTempDao() }
}

val jogUseCaseModule = module {
    single { JogUseCase(get<JogEntryDAO>(), get<JogSummaryDAO>(), get<JogSummaryTempDAO>()) }
}

const val BASE_URL = "https://api.quotable.io/"
val networkModule = module {
    single<MotivationQuotesAPI> { get<Retrofit>().create(MotivationQuotesAPI::class.java) }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
