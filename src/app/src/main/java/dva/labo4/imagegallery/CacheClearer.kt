import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class CacheClearer(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // Delete all files in the cache directory
        val files = applicationContext.cacheDir.listFiles()
        for (file in files) {
            file.delete()
        }

        return Result.success()
    }
}
