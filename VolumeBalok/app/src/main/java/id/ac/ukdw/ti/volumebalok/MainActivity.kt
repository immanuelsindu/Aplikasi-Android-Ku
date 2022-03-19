package id.ac.ukdw.ti.volumebalok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import id.ac.ukdw.ti.volumebalok.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityMainBinding

//    private lateinit var edtPanjang : EditText
//    private lateinit var edtLebar : EditText
//    private lateinit var edtTinggi : EditText
//    private lateinit var txtHasil : TextView

    companion object {
        private const val STATE_RESULT = "state_result"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, binding.txtHasil.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        edtPanjang  = findViewById(R.id.edtPanjang)
//        edtLebar  = findViewById(R.id.edtLebar)
//        edtTinggi = findViewById(R.id.edtTinggi)
//        txtHasil = findViewById(R.id.txtHasil)



//        var btnHitung : Button = findViewById(R.id.btnHitung)
//        btnHitung.setOnClickListener (this)
        binding.btnHitung.setOnClickListener(this)

        if(savedInstanceState != null){
            val result = savedInstanceState.getString(STATE_RESULT)
            binding.txtHasil.text = result
        }
    }

    override fun onClick(v: View?) {
        var cek = ""
        if(v?.id ?:  null == R.id.btnHitung){

            var edtPanjang2 = binding.edtPanjang.text.toString()
            var edtLebar2 = binding.edtLebar.text.toString()
            var edtTinggi2 = binding.edtTinggi.text.toString()

            if(edtPanjang2.isEmpty()){
               binding.edtPanjang.error = "Field ini tidak boleh kosong"
            }else if(edtLebar2.isEmpty()){
                binding.edtLebar.error = "Field ini tidak boleh kosong"
            }else if(edtTinggi2.isEmpty()){
               binding.edtTinggi.error = "Field ini tidak boleh kosong"
            }else{
                cek = "aman"
            }

            if(cek == "aman"){
                var hasil = edtPanjang2.toDouble() * edtLebar2.toDouble() * edtTinggi2.toDouble()
                binding.txtHasil.text = hasil.toString()
            }

        }
    }




}