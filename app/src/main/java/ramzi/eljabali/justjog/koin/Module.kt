package ramzi.eljabali.justjog.koin

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ramzi.eljabali.justjog.repository.room.database.JustJogDataBase
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntryDAO
import ramzi.eljabali.justjog.repository.room.jogsummary.JogSummaryDAO
import ramzi.eljabali.justjog.repository.room.jogsummarytemp.JogSummaryTempDAO
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.viewmodel.JogViewModel

val statisticsModule = module {
    viewModel { JogViewModel() }
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
    single { JogUseCase(get(), get(), get()) }
}