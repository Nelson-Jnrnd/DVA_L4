package dva.labo4.imagegallery

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class RecyclerViewAdapter(private val data: List<String>/*, private val owner: LifecycleOwner*/) : androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)

        //val holder = ViewHolder(view, owner)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: android.view.View/*, private val owner : LifecycleOwner*/) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<android.widget.TextView>(R.id.displayedText)

        fun bind() {
            textView.text = data[adapterPosition]
            /*
            scope.launch(Dispatchers.IO) {
                val bitmap = downloadImage( URL(" https://daa.iict.ch/images/$adapterPosition.jpg"))
                withContext(Dispatchers.Main) {
                    itemView.findViewById<android.widget.ImageView>(R.id.displayedImage).setImageBitmap(bitmap)
                }
            }
            */
        }
    }

    suspend fun downloadImage(url : URL) : Bitmap = withContext(Dispatchers.IO) {
        try {
            val stream = url.openStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            stream.close()
            bitmap
        } catch (e: IOException) {
            Log.e(TAG, "Error while downloading image", e)
            throw e
        }
    }
    suspend fun decodeImage(bytes : ByteArray?) : Bitmap? = withContext(Dispatchers.Default) {
        try {
            BitmapFactory.decodeByteArray(bytes, 0, bytes?.size ?: 0)
        } catch (e: IOException) {
            Log.w(TAG, "Exception while decoding image", e)
            null
        }
    }
    suspend fun displayImage(bmp : Bitmap?) = withContext(Dispatchers.Main) {
        /*
        if(bmp != null)
            myImage.setImageBitmap(bmp)
        else
            myImage.setImageResource(android.R.color.transparent)
         */
    }

}