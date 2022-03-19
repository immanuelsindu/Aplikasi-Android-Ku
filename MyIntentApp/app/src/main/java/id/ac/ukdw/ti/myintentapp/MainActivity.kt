package id.ac.ukdw.ti.myintentapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPindahActivity: Button = findViewById(R.id.btnPindahActivity)
        btnPindahActivity.setOnClickListener(this)

        val btnPindahActivityDenganData : Button = findViewById(R.id.btnPindahActivityDenganData)
        btnPindahActivityDenganData.setOnClickListener(this)

        val btnMoveWithObject: Button = findViewById(R.id.btnPindahActivityDenganObject)
        btnMoveWithObject.setOnClickListener(this)

        val btnDialup : Button = findViewById(R.id.btnDialANumber)
        btnDialup.setOnClickListener(this)

        val btnPindahActivityUntukResult : Button = findViewById(R.id.btnPindahActivityDenganResult)
        btnPindahActivityUntukResult.setOnClickListener(this)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val txtHasil : TextView = findViewById(R.id.txtHasil)
        if (result.resultCode == MoveForResultActivity.RESULT_CODE && result.data != null) {
            val selectedValue =
                result.data?.getIntExtra(MoveForResultActivity.EXTRA_SELECTED_VALUE, 0)
            txtHasil.text = "Hasil : $selectedValue"
        }
    }

    override fun onClick(v: View?) {
        when(v?.id ){
            R.id.btnPindahActivity -> {
                val intent1 = Intent(this@MainActivity,MoveActivity2::class.java)
                startActivity(intent1)
            }
            R.id.btnPindahActivityDenganData-> {
                val intent1 = Intent(this@MainActivity, MoveWithData::class.java)
                intent1.putExtra("Nama", "Sindu")
                intent1.putExtra("Umur", 20)
                startActivity(intent1)
            }
            R.id.btnPindahActivityDenganObject->{
                val person = Person(
                    "Immanuel Sindu",
                    5,
                    "immanuelsindu15@gmail.com",
                    "Pati"
                )

                val intent1 = Intent(this@MainActivity, MoveWithObject::class.java)
                intent1.putExtra("person", person)
                startActivity(intent1)
            }
            R.id.btnDialANumber->{
                val phoneNumber = "087736619477"
                val dialPhone = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(dialPhone)
            }
            R.id.btnPindahActivityDenganResult->{
                val moveForResultIntent = Intent(this@MainActivity, MoveForResultActivity::class.java)
                resultLauncher.launch(moveForResultIntent)
            }

        }
    }
}