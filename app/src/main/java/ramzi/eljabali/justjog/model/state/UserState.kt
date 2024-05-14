package ramzi.eljabali.justjog.model.state

data class UserState(
    val didUserPressJogButton: Boolean = false,
    val doesNeedToCheckPermission: Boolean = false,
    val didUserClickGetAllJogsButton: Boolean = false
)