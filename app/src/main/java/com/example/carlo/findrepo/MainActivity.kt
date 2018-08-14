package com.example.carlo.findrepo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.searchButton)
        val searchText = findViewById<EditText>(R.id.searchEditText)

        val buttonSearchUserRepo = findViewById<Button>(R.id.userRepoButton)
        val searchUserRepo = findViewById<EditText>(R.id.userRepoEditText)

        button.setOnClickListener {
            if (searchText.text.toString() == "") {
                Toast.makeText(this,"You need to write something",Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this,SearchResultActivity::class.java)
                intent.putExtra("searchTerm",searchText.text.toString())
                startActivity(intent)
            }
        }

        buttonSearchUserRepo.setOnClickListener {
            if (searchUserRepo.text.toString() == "") {
                Toast.makeText(this,"You need to write something",Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("username",searchUserRepo.text.toString())
                startActivity(intent)
            }
        }

    }
}
