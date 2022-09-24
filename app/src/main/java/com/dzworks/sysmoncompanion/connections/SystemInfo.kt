package com.dzworks.sysmoncompanion.connections


import com.google.gson.annotations.SerializedName

data class SystemInfo(
    @SerializedName("cpuClock") var cpuClock: String? = "",
    @SerializedName("cpuName") var cpuName: String? = "",
    @SerializedName("cpuTempMax") var cpuTempMax: String? = "",
    @SerializedName("cpuTempVal") var cpuTempVal: String? = "",
    @SerializedName("cpuUsageMax") var cpuUsageMax: String? = "",
    @SerializedName("cpuUsageVal") var cpuUsageVal: String? = "",
    @SerializedName("gpuName") var gpuName: String? = "",
    @SerializedName("gpuTempMax") var gpuTempMax: String? = "",
    @SerializedName("gpuTempVal") var gpuTempVal: String? = "",
    @SerializedName("gpuUsageMax") var gpuUsageMax: String? = "",
    @SerializedName("gpuUsageVal") var gpuUsageVal: String? = "",
    @SerializedName("pcName") var pcName: String? = "",
    @SerializedName("ramFree") var ramFree: String? = "",
    @SerializedName("ramLoad") var ramLoad: String? = "",
    @SerializedName("ramTotal") var ramTotal: String? = "",
    @SerializedName("ramUsage") var ramUsage: String? = ""
) {
    constructor() : this(null, null, null, null, null, null, null, null,null, null, null, null, null, null, null, null)
}