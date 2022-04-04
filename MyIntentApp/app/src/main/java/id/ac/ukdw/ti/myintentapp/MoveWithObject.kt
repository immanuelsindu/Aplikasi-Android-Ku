package id.ac.ukdw.ti.myintentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MoveWithObject : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_object)

        val txtHalooObject : TextView = findViewById(R.id.txtHalooObject)
        val person = intent.getParcelableExtra<Person>("person")
        txtHalooObject.text = "Nama = ${person?.name.toString()}\n" +
                "Email = ${person?.email.toString()}\n" +
                "City = ${person?.city.toString()}\n" +
                "Age = ${person?.age.toString()}"


    }
}