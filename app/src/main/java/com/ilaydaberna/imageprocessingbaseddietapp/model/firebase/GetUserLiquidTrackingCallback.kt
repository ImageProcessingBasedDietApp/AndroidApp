package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

interface GetUserLiquidTrackingCallback {
    fun onCallback(userLiquidTrackValueList: ArrayList<LiquidTrackValue>)
}