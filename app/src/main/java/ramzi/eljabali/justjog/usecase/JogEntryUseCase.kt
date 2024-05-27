package ramzi.eljabali.justjog.usecase

import ramzi.eljabali.justjog.repository.room.database.JustJogDataBase
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntry

class JogEntryUseCase(private val dataBase: JustJogDataBase) {
    private val jogEntryDao = dataBase.jogEntryDao()

    suspend fun addJogEntry(jogEntry: JogEntry) {
        jogEntryDao.addEntry(jogEntry)
    }

    suspend fun getAllJogEntries() = jogEntryDao.getAll()

}