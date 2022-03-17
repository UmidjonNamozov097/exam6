package com.example.myapplication.database

import androidx.room.*
import androidx.room.Dao
import com.example.networkingexam.model.Card

@Dao
interface Dao {
    @Insert()
    fun addCard(card: Card)

    @Query("SELECT * FROM cards")
    fun getCards(): List<Card>
}