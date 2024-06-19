package com.cataract.detection.connection.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val photo: String,
    val title: String,
    val description: String,
    val writer: String = "Admin",
    val section: String, // 'portrait' or 'landscape'
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "Title",
        parcel.readString() ?: "Description",
        parcel.readString() ?: "Admin",
        parcel.readString() ?: "portrait",
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(photo)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(writer)
        parcel.writeString(section)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleModel> {
        override fun createFromParcel(parcel: Parcel): ArticleModel {
            return ArticleModel(parcel)
        }

        override fun newArray(size: Int): Array<ArticleModel?> {
            return arrayOfNulls(size)
        }
    }
}
