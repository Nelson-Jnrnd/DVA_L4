package dva.labo4.imagegallery

import CacheClearer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        val recyclerViewAdapter = RecyclerViewAdapter(lifecycleScope,cacheDir)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val workManager = WorkManager.getInstance(applicationContext)

        val clearer = PeriodicWorkRequestBuilder<CacheClearer>(15, TimeUnit.MINUTES)
            .build()
        workManager.enqueue(clearer)
    }
}