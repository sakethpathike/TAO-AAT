package com.sakethh.tao_aat.domain

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity("ad_attribution")
data class Attribution(
    // I'd use UUIDv7 here,
    // for this assigmnet/example we should be fine with this
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: Type,
    val status: Status,
    val insertedAt: Long,
    val sessionId: String,
    val nextRetryAt: Long? = null
) {
    enum class Status {
        PROCESSED, PROCESSING, RETRY
    }

    enum class Type {
        INSTALL, VISIT, ADD_TO_CART, PURCHASE
    }
}
