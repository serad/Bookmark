package com.aplication.sergio.bookmark.fragments


import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aplication.sergio.bookmark.R
import android.widget.CheckBox
import android.widget.GridLayout
import kotlinx.android.synthetic.main.fragment_settings.view.*
import android.preference.PreferenceManager
import android.util.Log
import com.aplication.sergio.bookmark.helper.SQLHelper
import com.aplication.sergio.bookmark.model.db.UserPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        val gcontainer = rootView.gendersContainer as GridLayout

        gcontainer.removeAllViews()
        val res: Resources = resources

        val genderList = res.getStringArray(R.array.gendersArray)
        val column = 4
        val row = genderList.size / column
        gcontainer.columnCount = column
        gcontainer.rowCount = row + 1

        val dbHandler = SQLHelper(this.context!!, null, null, 1)
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid

        val userGendersArray = dbHandler.listUserPreferences(uid)

        genderList.forEachIndexed{ index, gender ->
            val found = userGendersArray!!.find { userGender -> userGender.userPreferenceId == index }
            val checkBox = CheckBox(rootView.context)
            checkBox.text = gender
            if(found != null) checkBox.isChecked = true
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                this.toggleGender(index, isChecked)
            }
            gcontainer.addView(checkBox)
        }
        for (value in genderList) {

        }
        return rootView
    }
    fun toggleGender( value: Int, isChecked: Boolean){
        val dbHandler = SQLHelper(this.context!!, null, null, 1)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val userPreference = UserPreference( uid, value)
        if(!isChecked){
            dbHandler.deleteUserPreference( userPreference )
        } else {
            dbHandler.addUserPreference( userPreference )
        }
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }





}
