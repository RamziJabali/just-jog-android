package ramzi.eljabali.justjog.model.actions

sealed interface Actions {
    data object OnGraphPointClick : Actions
}