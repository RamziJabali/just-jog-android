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
import kotlin.coroutines.cancellation.CancellationException


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

        val job = viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    dao.addUpdateWorkout(jogEntry).also {
                        deferred.complete(Unit)
                    }
                }
            } catch (e: Exception) {
                Log.d("MockVM::Class.java", "Exception: ${e.message}")
                // Handle cancellation if needed
            }
        }

        deferred.invokeOnCompletion { cause ->
            if (cause != null) {
                if (cause is CancellationException) {
                    Log.d("MockVM::Class.java", "addJog: Coroutine canceled: ${cause.message}")
                } else {
                    Log.e("MockVM::Class.java", "addJog: Coroutine failed with: $cause")
                }
                job.cancel() // Cancel the job explicitly
            } else {
                Log.d("MockVM::Class.java", "addJog: ${deferred.isCompleted}")
            }
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