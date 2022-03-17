package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.networkingexam.model.Card
import com.google.android.material.button.MaterialButton

class AddCardActivity : AppCompatActivity() {

    private lateinit var tvCardNumber: TextView
    private lateinit var tvCardHolder: TextView
    private lateinit var tvExpireDate: TextView
    private lateinit var edtNumber: EditText
    private lateinit var edtDay: EditText
    private lateinit var edtMonth: EditText
    private lateinit var edtCvv: EditText
    private lateinit var edtHolder: EditText

    private lateinit var btnAdd: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {

        window.statusBarColor = Color.parseColor("#393939")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        initViews()
    }

    private fun initViews() {
        tvCardNumber = findViewById(R.id.tvCardNumber)
        tvCardHolder = findViewById(R.id.tvCardHolder)
        tvExpireDate = findViewById(R.id.tvExpireDate)

        edtNumber = findViewById(R.id.edtNumber)
        edtDay = findViewById(R.id.edtDay)
        edtMonth = findViewById(R.id.edtMonth)
        edtCvv = findViewById(R.id.edtCvv)
        edtHolder = findViewById(R.id.edtHolder)

        btnAdd = findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            if (allFieldsAdded()) {
                addCard(
                    Card(
                        1,
                        edtNumber.text.toString().toLong(),
                        edtHolder.text.toString(),
                        "${edtDay.text}/${edtMonth.text}",
                        edtCvv.text.toString().toInt(),
                        false
                    )
                )
            }
        }

    }

    private fun allFieldsAdded(): Boolean {
        return edtNumber.text.isNotBlank() && edtDay.text.isNotBlank() && edtMonth.text.isNotBlank() && edtCvv.text.isNotBlank() && edtHolder.text.isNotBlank()
    }

    private fun addCard(card: Card) {
        val intent = Intent()
        intent.putExtra("card", card)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}