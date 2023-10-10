package com.example.flickrbrowserapp

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.flickrbrowserapp.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {

    private val TAG = "SearchActivity"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySearchBinding

    private var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,".onCreate: starts")
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_search)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Log.d(TAG,".onCreate: ends")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG,".onCreateOptionsMenu: starts")
        menuInflater.inflate(R.menu.menu_search,menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.app_bar_search).actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)
//        Log.d(TAG,".onCreateOptionsMenu: $componentName")
//        Log.d(TAG,".onCreateOptionsMenu: hint is ${searchView?.queryHint}")
//        Log.d(TAG,".onCreateOptionsMenu: $searchableInfo")

        searchView?.isIconified = false

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?):Boolean {
                Log.d(TAG,".onCreateOptionsMenu: called")

                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                sharedPref.edit().putString(FLICKR_QUERY,query).apply()
                searchView?.clearFocus()

                finish()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView?.setOnCloseListener {
            finish()
            false
        }

        Log.d(TAG,".onCreateOptionsMenu: returning")
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_search)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}