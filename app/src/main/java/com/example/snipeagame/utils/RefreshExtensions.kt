package com.example.snipeagame.utils

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun SwipeRefreshLayout.hideRefresh() {
    this.isRefreshing = false
}

fun SwipeRefreshLayout.showRefresh() {
    this.isRefreshing = true
}
