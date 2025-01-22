package ch.grab777.examprep.screens

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.grab777.examprep.ui.Button
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable //the contentResolver is needed for reading contacts
fun ContactsScreen(
    innerPadding: PaddingValues,
    contentResolver: ContentResolver,
) {
    val contacts = remember { mutableStateListOf<String>() }
    val contactPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_CONTACTS
    ) {
        if (it) {
            readContacts(contentResolver, contacts)
        }
    }
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            text = "Kontakte lesen",
            onClick = {
                permissionReadContacts(
                    contactPermissionState,
                    contentResolver,
                    contacts
                )
            })
        Text(text = contactPermissionState.status.toString())
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(contacts) { index, contact ->
                Text(text = contact)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun permissionReadContacts(
    contactPermissionState: PermissionState,
    contentResolver: ContentResolver,
    contacts: MutableList<String>
) {
    if (!contactPermissionState.status.isGranted) {
        contactPermissionState.launchPermissionRequest()
    } else {
        readContacts(contentResolver, contacts)
    }
}

fun readContacts(
    contentResolver: ContentResolver,
    contacts: MutableList<String>
) {
    contacts.clear()
    val uri: Uri = ContactsContract.Contacts.CONTENT_URI
    //the array defines the fields to be returned
    val cursor: Cursor? =
        contentResolver.query(
            uri, arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ),
            null, null, null
        )
    if (cursor != null) {
        var res: Boolean = cursor.moveToFirst()
        while (res) {
            try {
                //read the id (id == 0) first
                val id = cursor.getString(0)
                //the name (id == 1)
                var text: String = cursor.getString(1) ?: "no name"
                //unfortunately, there is no "getBool"
                if (cursor.getString(2) == "1") {
                    text += " " + getPhoneNumberForUser(contentResolver, id)
                }
                Log.i("ContactExample", text)
                contacts.add(text)
            } catch (exception: NullPointerException) {
                Log.e("Contact loading", exception.message ?: "")
            }
            res = cursor.moveToNext()
        }
        cursor.close()
    }
}

fun getPhoneNumberForUser(
    contentResolver: ContentResolver,
    contactId: String
): String {
    var number = ""
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                contactId, null, null
    )
    if (cursor!!.moveToFirst()) {
        while (!cursor.isAfterLast) {
            val index = cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )
            number += cursor.getString(index) + " "
            cursor.moveToNext()
        }
    }
    cursor.close()
    return number
}