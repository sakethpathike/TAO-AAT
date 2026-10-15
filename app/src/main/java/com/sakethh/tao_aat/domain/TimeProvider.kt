package com.sakethh.tao_aat.domain

interface TimeProvider {
    fun currentTimeMillis(): Long
}