package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

interface GetUserWeightTrackigCallBack {
    fun onCallback(userWeights: ArrayList<WeightTrackValue>)
}