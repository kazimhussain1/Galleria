package com.example.galleria.home.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.galleria.R
import com.example.galleria.common.AppConstants
import com.example.galleria.common.BaseActivity
import com.example.galleria.common.SpaceItemDecoration
import com.example.galleria.databinding.ActivityHomeBinding
import com.example.galleria.detail.view.DetailActivity
import com.example.galleria.home.adapters.HomeRecyclerAdapter
import com.example.galleria.home.api.response.ImageItem
import com.example.galleria.home.viewmodel.HomeViewModel
import com.example.galleria.utilities.Utilities
import kotlin.math.hypot


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(),
    HomeRecyclerAdapter.OnItemClickListener {


    private var data: List<ImageItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setViewModel(HomeViewModel::class.java)

        val adapter = HomeRecyclerAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dimen_main)))


        setObservers(adapter)
        setListeners(adapter)

        viewModel.getImages("")


    }

    private fun setListeners(adapter: HomeRecyclerAdapter) {
        binding.searchButton.setOnClickListener {

            viewModel.toggleSearchVisible()

        }

        binding.searchView.apply {
            setOnFocusChangeListener { _: View, b: Boolean ->

                binding.btnBack.isVisible = b
                if (b)
                    showSoftKeyboard()
                else
                    hideSoftKeyboard(binding.searchView)

            }

            setOnEditorActionListener(object :
                TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    // If triggered by an enter key, this is the event; otherwise, this is null.
                    if (event != null) {
                        // if shift key is down, then we want to insert the '\n' char in the TextView;
                        // otherwise, the default action is to send the message.
                        if (!event.isShiftPressed) {
                            viewModel.getImages(v?.text.toString())
                            return true
                        }
                        return false
                    }

                    viewModel.getImages(v?.text.toString())
                    return true
                }

            })
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!binding.recyclerView.canScrollVertically(RecyclerView.VERTICAL) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    viewModel.getImagesNextPage()
//                    Utilities.showToast(this@HomeActivity,"scroll end")

                }
            }
        })

        binding.btnBack.setOnClickListener {
            viewModel.setSearchVisible(false)
        }

        adapter.listener = this
    }

    private fun setObservers(adapter: HomeRecyclerAdapter) {
        viewModel.imageItemResult.observe(this, {
            adapter.submitList(it)
            this.data = it
        })

        viewModel.searchVisible.observe(this, {
            if (it)
                revealSearch()
            else
                hideSearch()
        })

        viewModel.progressBarVisible.observe(this, {
            if (it) showProgressbar() else hideProgressbar()
        })

        viewModel.error.observe(this, {
            Utilities.showToast(this, it)
        })
    }

    override fun onPause() {
        hideSoftKeyboard()
        super.onPause()
    }


    private fun revealSearch() {
        val view: View = binding.searchContainer

        view.post {
            val cx: Int = view.width / 2
            val cy: Int = view.height /2
            val finalRadius =
                hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim: Animator =
                ViewAnimationUtils.createCircularReveal(view, cx * 2, 0, 0f, finalRadius * 2)

//            binding.view.isVisible = true

            binding.searchButton.setImageResource(R.drawable.baseline_close_24)
            binding.searchButton.setColorFilter(
                getColor(R.color.black),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            view.visibility = View.VISIBLE
            anim.start()
            binding.searchView.requestFocus()
        }


    }

    private fun hideSearch() {
        val view: View = binding.searchContainer

        view.post {
            val cx: Int = view.width / 2
            val cy: Int = view.height / 2
            val initialRadius =
                hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim: Animator =
                ViewAnimationUtils.createCircularReveal(view, cx * 2, 0, initialRadius * 2, 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            })
            anim.start()
            binding.searchButton.setImageResource(R.drawable.baseline_search_24)
            binding.searchButton.setColorFilter(
                getColor(R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

//            binding.view.isVisible = false
        }

    }

    override fun onItemClick(position: Int) {

        val intent = Intent(this, DetailActivity::class.java)
        intent.putParcelableArrayListExtra(AppConstants.DATA_KEY, ArrayList(data))
        intent.putExtra(AppConstants.POSITION_KEY, position)

        startActivity(intent)
    }


//    fun expand(v: View, duration: Int, targetHeight: Int) {
//        val prevHeight = v.height
//        v.visibility = View.VISIBLE
//        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
//        valueAnimator.addUpdateListener { animation ->
//            v.layoutParams.height = animation.animatedValue as Int
//            v.requestLayout()
//        }
//        valueAnimator.interpolator = DecelerateInterpolator()
//        valueAnimator.duration = duration.toLong()
//        valueAnimator.start()
//
//    }
//
//    fun collapse(v: View, duration: Int, targetHeight: Int) {
//        val prevHeight = v.height
//        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
//        valueAnimator.interpolator = DecelerateInterpolator()
//        valueAnimator.addUpdateListener { animation ->
//            v.layoutParams.height = animation.animatedValue as Int
//            v.requestLayout()
//        }
//        valueAnimator.interpolator = DecelerateInterpolator()
//        valueAnimator.duration = duration.toLong()
//        valueAnimator.start()
//
//    }
}