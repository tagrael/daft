package com.daftmobile.a4bhomework2

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contactButton.setOnClickListener(this::chooseEmail)
        mailButton.setOnClickListener(this::sendEmail)
    }

    private fun retrieveEmailFromContactUri(uri: Uri): String {
        val projection = arrayOf(CommonDataKinds.Email.ADDRESS)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)
            val address = cursor.getString(columnIndex)
            cursor.close()
            address
        }
        else {
            ""
        }
    }

    private fun chooseEmail(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(CommonDataKinds.Email.CONTENT_TYPE)
        val componentName = intent.resolveActivity(packageManager)
        if (componentName != null)
            startActivityForResult(intent, REQUEST_SELECT_CONTACT)

    }

    private fun sendEmail(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_EMAIL, emailTextView.toString())
                .putExtra(Intent.EXTRA_SUBJECT, "Wiadomość z pracy domowej")
        val componentName = intent.resolveActivity(packageManager)
        if (componentName != null)
            startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.getData() ?: return
                val address = retrieveEmailFromContactUri(uri)
                emailTextView.setText(address)
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                return
            }
        }
    }


    companion object {
        private const val REQUEST_SELECT_CONTACT = 102
    }
}
