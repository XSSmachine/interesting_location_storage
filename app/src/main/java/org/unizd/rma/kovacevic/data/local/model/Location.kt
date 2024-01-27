package org.unizd.rma.kovacevic.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val title:String,
    val content:String,
    val category: String,
    val createdDate:String,
    val imagePath: String,
    val latitude:Double,
    val longitude:Double
)
