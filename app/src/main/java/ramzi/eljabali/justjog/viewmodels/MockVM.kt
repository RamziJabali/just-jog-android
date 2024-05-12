package ramzi.eljabali.justjog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntry
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntryDAO


class MockVM(private val dao: JogEntryDAO) : ViewModel() {
    private val _userStateFlow = MutableStateFlow(UserState())
    val userStateFlow: StateFlow<UserState> = _userStateFlow

    fun userClickJogButton() {
        _userStateFlow.update {
            it.copy(
                didUserPressJogButton = true
            )
        }
    }

    fun addJog(jogEntry: JogEntry) {
        val deferred = CompletableDeferred<Unit>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000)
                dao.addUpdateWorkout(jogEntry).also {
                    deferred.complete(Unit)
                }
            }
            Log.d("MockVM::Class.java", "added Jog: $deferred")
        }
    }

    fun getAllJogs() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = viewModelScope.async { dao.getAll() }.await()
                list.collect {
                    Log.d("MockVM::Class.java", "getAllJogs: $it")
                }
            }
        }
    }

    fun needUserPermissions() {
        _userStateFlow.update {
            it.copy(
                didUserPressJogButton = false,
                doesNeedToCheckPermission = true
            )
        }
    }

    fun userClickedGetAllJogButton() {
        _userStateFlow.update {
            it.copy(
                didUserClickGetAllJogsButton = true
            )
        }
    }

    data class UserState(
        val didUserPressJogButton: Boolean = false,
        val doesNeedToCheckPermission: Boolean = false,
        val didUserClickGetAllJogsButton: Boolean = false
    )
}