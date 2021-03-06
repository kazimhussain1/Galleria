package com.example.galleria.detail.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.galleria.R
import com.example.galleria.common.AppConstants
import com.example.galleria.common.BaseActivity
import com.example.galleria.databinding.ActivityDetailBinding
import com.example.galleria.detail.adapter.DetailPagerAdapter
import com.example.galleria.detail.viewmodel.DetailViewModel
import com.example.galleria.home.api.response.ImageItem
import com.example.galleria.utilities.Utilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val PERMISSION_REQUEST_CODE: Int = 8547

class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>() {


    private val adapter: DetailPagerAdapter = DetailPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setViewModel(DetailViewModel::class.java)

        setupViewPager()
        setListeners()
        setObservers()

        viewModel.setImageData(
            intent.getParcelableArrayListExtra<ImageItem>(AppConstants.DATA_KEY)?.toList()
        )
        viewModel.setPositionIfNull(intent.getIntExtra(AppConstants.POSITION_KEY, 0))

    }

    private fun setObservers() {
        viewModel.imageData.observe(this, {

            adapter.submitList(it)

        })
        viewModel.position.observe(this, {
            binding.viewPager.currentItem = it
        })
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter

    }

    private fun setListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.btnShare.setOnClickListener {
            shareImage(adapter.getItem(binding.viewPager.currentItem))
        }
        binding.btnDownload.setOnClickListener {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                downloadImage(adapter.getItem(binding.viewPager.currentItem))
            else
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                viewModel.setPosition(position)
            }
        })


    }

    private fun shareImage(item: ImageItem) {
        CoroutineScope(Dispatchers.IO).launch {
            Utilities.shareImage(
                Glide.with(this@DetailActivity)
                    .asBitmap()
                    .load(item.largeImageURL) // sample image
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal) // need placeholder to avoid issue like glide annotations
                    .error(android.R.drawable.stat_notify_error) // need error to avoid issue like glide annotations
                    .submit()
                    .get(),
                this@DetailActivity
            )
        }
    }

    private fun downloadImage(item: ImageItem) {

        CoroutineScope(Dispatchers.IO).launch {

            Utilities.saveImage(
                Glide.with(this@DetailActivity)
                    .asBitmap()
                    .load(item.largeImageURL) // sample image
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal) // need placeholder to avoid issue like glide annotations
                    .error(android.R.drawable.stat_notify_error) // need error to avoid issue like glide annotations
                    .submit()
                    .get(),
                this@DetailActivity,
                "Galleria"
            )

            withContext(Dispatchers.Main) {
                Utilities.showToast(this@DetailActivity, "Saved in Pictures/Galleria")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            downloadImage(adapter.getItem(binding.viewPager.currentItem))
        }
    }
}