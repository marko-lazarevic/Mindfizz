package com.example.quiztesting

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CreateQuizActivity: ComponentActivity() {

    // ovako se ne uzimaju vrednosti, crash-uje apl
    // popraviti
    //val imeKviza = findViewById<EditText>(R.id.imeKviza)
    //val opisKviza = findViewById<EditText>(R.id.opisKviza)

    //val imeText = imeKviza.text
    //val opisText = opisKviza.text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data.add(ItemsViewModel(R.drawable.itemimage, "Item" + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }


}