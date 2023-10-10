package com.example.flickrbrowserapp

import android.content.Intent
import android.location.Criteria
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickrbrowserapp.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : BaseActivity(),
    GetRawData.OnDownloadComplete,
    GetFlickJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {

    private val TAG = "MainActivity"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val FlickrRecyclerViewAdaptor = FlickrRecyclerViewAdaptor(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate called")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activateToolbar(false)
//        setSupportActionBar(binding.toolbar)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view,this))
        recycler_view.adapter = FlickrRecyclerViewAdaptor

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne","android,oreo","en-us",true)
        val getRawData = GetRawData(this)
        getRawData.execute(url)

        Log.d(TAG,"onCreate ends")
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG,".onItemClick: starts")
        Toast.makeText(this,"Normal tap at position $position",Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG,".onItemLongClick: starts")
//        Toast.makeText(this,"Long tap at position $position",Toast.LENGTH_LONG).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this,PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }
    }

    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean): String {
        Log.d(TAG,".createUri starts")

        return Uri.parse(baseURL).
        buildUpon().
                appendQueryParameter("tags",searchCriteria).
                appendQueryParameter("tagmode",if (matchAll) "ALL" else "ANY").
                appendQueryParameter("lang",lang).
                appendQueryParameter("format","json").
                appendQueryParameter("nojsoncallback","1").
                build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG,"onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG,"onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

//    companion object {
//        private const val TAG = "MainActivity"
//    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG,"onDownloadComplete called, data is $data")

            val getFlickJsonData = GetFlickJsonData(this)
            getFlickJsonData.execute(data)
        } else {
            Log.d(TAG,"onDownloadComplete failed with status $status. Error message is $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG,"onDataAvailable called, data is $data")
        flickrReclyclerViewAdapter.loadNewData(data)
        Log.d(TAG,"onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG,"onError zcalled with ${exception.message}")
    }

    override fun onResume() {
        Log.d(TAG,".onResume starts")
        super.onResume()
    }
}