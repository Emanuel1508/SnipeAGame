package com.example.snipeagame.utils

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun SwipeRefreshLayout.hideRefresh() {
    this.isEnabled = false
    this.isRefreshing = false
}

fun SwipeRefreshLayout.showRefresh() {
    this.isEnabled = true
    this.isRefreshing = true
}
