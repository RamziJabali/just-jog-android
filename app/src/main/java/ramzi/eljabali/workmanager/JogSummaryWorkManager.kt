package ramzi.eljabali.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JogSummaryWorkManager(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            try {

                Result.success()
            } catch (e: Exception) {
                Log.e(this.javaClass.simpleName, "Exception: ${e.message}")
                Result.failure()
            }
        }
}