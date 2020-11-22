package com.myweb.lab9mysqlupdatedelete

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_students.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditStudentsActivity : AppCompatActivity() {
    val createClient = StudentAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_students)

        val mId = intent.getStringExtra("mId")
        val mName = intent.getStringExtra("mName")
        val mAge = intent.getStringExtra("mAge")

        edt_id.setText(mId)
        edt_id.isEnabled =false
        edt_name.setText(mName)
        edt_age.setText(mAge)
    }
    fun saveStudent(v: View){
        createClient.updateStudent(
            edt_id.text.toString(),
            edt_name.text.toString(),
            edt_age.text.toString().toInt()).enqueue(object : Callback<Student> {

            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "SuccessFully Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(applicationContext,"ERROR", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun deleteStudent(v: View) {
        val mId = intent.getStringExtra("mId")
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            createClient.deleteStudent(mId)
                .enqueue(object : Callback<Student> {
                    override fun onResponse(call: Call<Student>, response: Response<Student>) {
                        if (response.isSuccessful()) {
                            Toast.makeText(applicationContext, "Successfully Deleted", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Student>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }
                })
            finish()
        }
        val negativeButtonClick = { dialog: DialogInterface, which :Int-> dialog.cancel()
        }

        builder.setTitle("Warning")
        builder.setMessage("Do you want to delete the student?")
        builder.setPositiveButton("Yes", positiveButtonClick)
        builder.setNegativeButton("No",negativeButtonClick)
        builder.show() }
}

