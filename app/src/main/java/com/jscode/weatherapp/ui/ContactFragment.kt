package com.jscode.weatherapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jscode.weatherapp.R
import com.jscode.weatherapp.databinding.FragmentContactBinding

class ContactFragment : Fragment() {
    companion object{
        val devMail = arrayOf<String>("jscslg27@gmail.com")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentContactBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_contact,container,false)
        requireActivity().title = "Contact Us"
        binding.devMailButton.setOnClickListener {
            emailAt(devMail)
        }
        return binding.root
    }
    private fun emailAt(mail: Array<String>) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, mail)
        }
        startActivity(intent)
    }
}