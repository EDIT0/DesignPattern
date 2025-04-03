package com.my.mvistudymultimodule.core.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.my.mvistudymultimodule.core.model.MovieDetailModel

object MovieDetailConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromBelongsToCollection(value: MovieDetailModel.BelongsToCollection?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBelongsToCollection(value: String): MovieDetailModel.BelongsToCollection? {
        return gson.fromJson(value, MovieDetailModel.BelongsToCollection::class.java)
    }

    @TypeConverter
    fun fromGenreList(value: List<MovieDetailModel.Genre>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toGenreList(value: String): List<MovieDetailModel.Genre>? {
        val listType = object : TypeToken<List<MovieDetailModel.Genre>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromProductionCompanyList(value: List<MovieDetailModel.ProductionCompany>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toProductionCompanyList(value: String): List<MovieDetailModel.ProductionCompany>? {
        val listType = object : TypeToken<List<MovieDetailModel.ProductionCompany>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromProductionCountryList(value: List<MovieDetailModel.ProductionCountry>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toProductionCountryList(value: String): List<MovieDetailModel.ProductionCountry>? {
        val listType = object : TypeToken<List<MovieDetailModel.ProductionCountry>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromSpokenLanguageList(value: List<MovieDetailModel.SpokenLanguage>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSpokenLanguageList(value: String): List<MovieDetailModel.SpokenLanguage>? {
        val listType = object : TypeToken<List<MovieDetailModel.SpokenLanguage>>() {}.type
        return gson.fromJson(value, listType)
    }
}