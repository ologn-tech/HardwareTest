package tech.ologn.hardwaretest.model

data class FeatureResult(
    val id: Int,
    val name: String,
    val iconRes: Int,
    val message: String = "",
)
