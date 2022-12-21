package dva.labo4.imagegallery

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.io.File
import java.io.FileOutputStream


class RecyclerViewAdapter(var coroutineScope: CoroutineScope, var cacheDir: File ) : androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

   private fun readCache(url: String): Bitmap? {
       val file = File(cacheDir, url.hashCode().toString())
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.absolutePath)
        }
        return null
    }

    private fun writeCache(url: String, bitmap: Bitmap) {
        val file = File(cacheDir, url.hashCode().toString())
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.displayedImage)
        private var bmp :Bitmap ? = null
        private var onGoingJob : Job? = null

        fun bind(position: Int) {
            onGoingJob?.cancel()
            onGoingJob = coroutineScope.launch {
                bmp = readCache("https://daa.iict.ch/images/$position.jpg")
                if (bmp == null) {
                    bmp = downloadImage(position)
                    writeCache("https://daa.iict.ch/images/$position.jpg", bmp!!)
                }
                displayImage(image, bmp)
            }
        }
    }

    suspend fun downloadImage(nbImage : Int) : Bitmap = withContext(Dispatchers.IO) {
        try {
            val input: InputStream = URL("https://daa.iict.ch/images/$nbImage.jpg").openStream()
            val myBitmap = BitmapFactory.decodeStream(input)
            yield()
            myBitmap
        } catch (e: IOException) {
            Log.e(TAG, "Error while downloading image", e)
            yield()
            throw e
        }
    }
    suspend fun displayImage(myImage: ImageView, bmp : Bitmap?) = withContext(Dispatchers.Main) {
        if(bmp != null) {
            myImage.setImageBitmap(bmp)
            yield()
        }else{
            myImage.setImageResource(android.R.color.transparent)
            yield()
        }
    }
}