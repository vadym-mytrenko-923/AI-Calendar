package com.agents.app.demo.data.features.products.remote.model

import com.google.gson.annotations.SerializedName

enum class ProductCategoryDto {
    @SerializedName("beauty")
    BEAUTY,
    @SerializedName("fragrances")
    FRAGRANCES,
    @SerializedName("furniture")
    FURNITURE,
    @SerializedName("groceries")
    GROCERIES,
    @SerializedName("unknown")
    UNKNOWN
}
