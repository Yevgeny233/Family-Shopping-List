package com.mypetprojectbyme.familyshoppinglist

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.mypetprojectbyme.familyshoppinglist.Constants.TAG_CREATE_NOTE_
import com.mypetprojectbyme.familyshoppinglist.Constants.TAG_SERVICE


class Utils {

    companion object {
        fun printAuthLog(massage: String) {
            Log.i(Constants.TAG_MASSAGE_AUTH, massage)
        }

        fun fireStoreLog(massage: String) {
            Log.i(Constants.TAG_FIRESTORE, massage)
        }

        fun makeToast(context: Context?, massage: String) {
            Toast.makeText(context, massage, Toast.LENGTH_SHORT).show()
        }

        fun printNoteViewModelLog(s: String) {
            Log.i(Constants.TAG_NOTE_VIEW_MODEL_LOG, s)
        }

        fun printUserLog(s: String) {
            Log.i(Constants.TAG_LOG_USER_INFO_, s)
        }

        fun createNoteLog(s: String) {
            Log.i(TAG_CREATE_NOTE_, s)
        }

        fun serviceLog(message: String) {
            Log.i(TAG_SERVICE, message)
        }

        fun notificationLog(msg: String) {
            Log.i(Constants.TAG_NOTIFY, msg)
        }
    }
}
