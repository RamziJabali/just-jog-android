package ramzi.eljabali.justjog.intent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ramzi.eljabali.justjog.model.state.UserState
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntry
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntryDAO
import kotlin.coroutines.cancellation.CancellationException


class MockVM(private val dao: JogEntryDAO) : ViewModel() {
    private val _userStateFlow = MutableStateFlow(UserState())
    val userStateFlow: StateFlow<UserState> = _userStateFlow


    fun addJog(jogEntry: JogEntry) {
        val deferred = CompletableDeferred<Unit>()

        val job = viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.addUpdateWorkout(jogEntry).also {
                    deferred.complete(Unit)
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
        val completableDeferred = CompletableDeferred<Unit>()
        val job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = viewModelScope.async { dao.getAll() }.await().also {
                    completableDeferred.complete(Unit)
                }
                list.collect {
                    Log.d("MockVM::Class.java", "getAllJogs: $it")
                }
            } catch(e:Exception) {
                Log.d("MockVM::Class.java", "getAllJogs: ${e.message}")
            }
        }
        completableDeferred.invokeOnCompletion { cause ->
        if (cause != null) {
                if (cause is CancellationException) {
                    Log.d("MockVM::Class.java", "getAllJogs: Coroutine canceled: ${cause.message}")
                } else {
                    Log.e("MockVM::Class.java", "getAllJogs: Coroutine failed with: $cause")
                }
                job.cancel() // Cancel the job explicitly
            } else {
            Log.d("MockVM::Class.java", "getAllJogs: ${completableDeferred.isCompleted}")
            }
        }
    }
}