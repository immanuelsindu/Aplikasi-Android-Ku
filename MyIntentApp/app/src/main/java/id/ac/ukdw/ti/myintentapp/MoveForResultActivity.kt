package id.ac.ukdw.ti.myintentapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup

class MoveForResultActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_SELECTED_VALUE = "extra_selected_value"
        const val RESULT_CODE = 110
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_for_result)

        val btnPilih : Button = findViewById(R.id.btnPilih)

        btnPilih.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val rgNumber : RadioGroup = findViewById(R.id.rg_number)
        if(v?.id == R.id.btnPilih){
            if(rgNumber.checkedRadioButtonId > 0){
                var value = 0
                when(rgNumber.checkedRadioButtonId){
                    R.id.rdb50 -> value = 50
                    R.id.rdb100 -> value = 100
                    R.id.rdb150 -> value = 150
                    R.id.rdb200 -> value = 200
                }
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_SELECTED_VALUE, value)
                setResult(RESULT_CODE, resultIntent)
                finish()
            }
        }
    }
}