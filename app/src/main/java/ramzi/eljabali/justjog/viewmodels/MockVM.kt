package ramzi.eljabali.justjog.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class MockVM : ViewModel() {
    private val _userStateFlow = MutableStateFlow(UserState())
    val userStateFlow: StateFlow<UserState> = _userStateFlow

    fun userClickJogButton() {
        _userStateFlow.update {
            it.copy(
                didUserPressJogButton = true
            )
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
}

data class UserState(
    val didUserPressJogButton: Boolean = false,
    val doesNeedToCheckPermission: Boolean = false,
)