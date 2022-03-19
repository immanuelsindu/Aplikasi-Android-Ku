package id.ac.ukdw.ti.myintentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class MoveWithData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_data)

        val txtHaloo = findViewById<TextView>(R.id.txtHaloo)
        val nama = intent.getStringExtra("Nama")
        val umur = intent.getIntExtra("Umur",0)

        txtHaloo.text = "Nama = $nama, Umur = $umur"


    }
}