package dva.labo4.imagegallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private val recyclerViewAdapter = RecyclerViewAdapter(listOf("Hello", "World", "Android", "Studio")/*, lifecycleScope*/)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemWidth = resources.getDimensionPixelSize(R.dimen.item_size)

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, getNbColumns(itemWidth))


    }

    private fun getNbColumns(itemWidth : Int): Int {
        val displayMetrics = android.util.DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        return screenWidth / itemWidth
    }
}