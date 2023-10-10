package com.example.flickrbrowserapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.flickrbrowserapp.databinding.ActivityPhotoDetailsBinding
import com.squareup.picasso.Picasso

class PhotoDetailsActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activateToolbar(true)

//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photo = intent.extras?.getParcelable<Photo>(PHOTO_TRANSFER) as Photo

//        photo_title.text = "Title: " + photo.title
//        photo_tags.text = "Tags: " + photo.tags
        photo_title.text = resources.getString(R.string.photo_title_text,photo.title)
        photo_tags.text = resources.getString(R.string.photo_tags_text,photo.tags)

//        photo_author.text = "Author: " + photo.author

        photo_author.text = resources.getString(R.string.photo_author_text,"my","red","car")

        Picasso.with(this.thumbnail.context).load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photo_image)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_photo_details)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}