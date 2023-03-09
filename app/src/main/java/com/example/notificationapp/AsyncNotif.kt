package com.example.notificationapp

import android.os.AsyncTask
import java.lang.ref.WeakReference

class AsyncNotif(listener: OnAsyncNotif) : AsyncTask<Unit, Unit, Unit>() {
    private val listener: WeakReference<OnAsyncNotif> = WeakReference(listener)

    override fun doInBackground(vararg params: Unit?) {
        listener.get()?.onAsyncNotifDoInBackground()
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        listener.get()?.onAsyncNotifPostExecute()
    }

    interface OnAsyncNotif {
        //Interfaz a implementar en SecondActivity
        fun onAsyncNotifDoInBackground()
        fun onAsyncNotifPostExecute()
    }
}
