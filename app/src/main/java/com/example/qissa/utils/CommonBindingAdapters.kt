/*
 * Developed by: @ImaginativeShohag
 *
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@gmail.com
 *
 * MVVM Pattern Source: https://github.com/ImaginativeShohag/Simple-MVVM
 */

package com.example.qissa.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.qissa.interfaces.BindableAdapter

// ----------------------------------------------------------------
// RecyclerView
// ----------------------------------------------------------------

/**
 * Set data to recyclerView adapter.
 *
 * Help: https://android.jlelse.eu/how-to-bind-a-list-of-items-to-a-recyclerview-with-android-data-binding-1bd08b4796b4
 */
@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun <T> RecyclerView.items(items: LiveData<T>?) {

//    Timber.e("fun <T> RecyclerView.items(postsResponse: LiveData<T>?) {")

    items?.apply {

//        Timber.e("postsResponse?.apply {")

        if (adapter is BindableAdapter<*>) {

            (adapter as BindableAdapter<T>).setItems(items.value)
        }
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun <T> RecyclerView.items(items: T?) {
    items?.apply {
        if (adapter is BindableAdapter<*>) {
            (adapter as BindableAdapter<T>).setItems(items)
        }
    }
}

/**
 * Load image from url.
 */
// @BindingAdapter("srcUrlProfile")
// fun ImageView.setImage(url: String) {
//    GlideApp.with(context)
//        .load(url)
//        .profilePhoto()
//        .into(this)
// }

/**
 * Update view visibility.
 */
@BindingAdapter("visibility")
fun View.setVisibility(visible: Boolean?) {
    visible?.apply {
        if (this) {
            this@setVisibility.visibility = View.VISIBLE
        } else {
            this@setVisibility.visibility = View.GONE
        }
    }
}
