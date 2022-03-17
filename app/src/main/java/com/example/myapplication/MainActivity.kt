package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.CardAdapter
import com.example.myapplication.database.AppDatabase
import com.example.networkingexam.model.Card
import com.example.myapplication.networking.ApiClient
import com.example.myapplication.networking.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var service: Service
    private lateinit var ivAddCard: ImageView
    private lateinit var rvCards: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        window.statusBarColor = Color.parseColor("#393939")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        service = ApiClient.createService(Service::class.java)
        appDatabase = AppDatabase.getInstance(this)

        initViews()
    }

    private fun initViews() {
        rvCards = findViewById(R.id.rvCards)
        ivAddCard = findViewById(R.id.ivAddCard)
        cardAdapter = CardAdapter()
        getCards()
        refreshAdapter()

        ivAddCard.setOnClickListener {
            addCard()
        }
    }

    val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent? = it.data
            val cardToAdd = data?.getParcelableExtra<Card>("card")

            saveCard(cardToAdd!!)
        }
    }

    private fun saveCard(card: Card) {
        if (isInternetAvailable()) {
            service.addCard(card).enqueue(object : Callback<Card> {
                override fun onResponse(call: Call<Card>, response: Response<Card>) {
                    card.isAvailable = true
                    saveToDatabase(card)
                    cardAdapter.addCard(response.body()!!)
                    Toast.makeText(this@MainActivity, "Card saved", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Card>, t: Throwable) {

                }
            })
        } else {
            card.isAvailable = false
            saveToDatabase(card)
        }
    }

    private fun saveToDatabase(card: Card) {
        appDatabase.cardDao().addCard(card)
    }

    private fun addCard() {
        val intent = Intent(this, AddCardActivity::class.java)
        detailLauncher.launch(intent)
    }

    private fun refreshAdapter() {
        rvCards.adapter = cardAdapter
    }

    private fun getCards() {
        if (isInternetAvailable()) {
            service.getCards().enqueue(object : Callback<List<Card>> {
                override fun onResponse(call: Call<List<Card>>, response: Response<List<Card>>) {
                    cardAdapter.submitData(response.body()!!)
                }

                override fun onFailure(call: Call<List<Card>>, t: Throwable) {

                }
            })
        } else {
            cardAdapter.submitData(appDatabase.cardDao().getCards())
        }
    }

    private fun isInternetAvailable(): Boolean {
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val infoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val infoWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return infoMobile!!.isConnected || infoWifi!!.isConnected
    }
}