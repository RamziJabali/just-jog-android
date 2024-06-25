package ramzi.eljabali.justjog.model.permissioninformation

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class LocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        if (isPermanentlyDeclined) {
            return "You have permanently declined location permissions. " +
                    "To be able to track your jogs and statistics, You can enable them in the app settings."
        }
        return "We use your location to accurately track your jogging routes, distance, and pace. " +
                "This helps you monitor your progress and improve your performance over time."
    }
}

class NotificationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        if (isPermanentlyDeclined) {
            return "You have permanently declined notification permissions. " +
                    "Notifications keep you informed about the duration of your jog in real-time, " +
                    "You can enable them in the app settings."
        }
        return "We use notifications keep you informed about the duration of your jog in real-time. " +
                "This helps you stay on track and manage your jog efficiently."
    }
}