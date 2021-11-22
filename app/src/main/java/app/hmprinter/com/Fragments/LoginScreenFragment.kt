package app.hmprinter.com.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.hmprinter.com.R
import androidx.activity.OnBackPressedCallback
import kotlin.system.exitProcess


class LoginScreenFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login_screen, container, false)
        initBackPressed()

        return view
    }

    private fun initBackPressed() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                 exitProcess(0)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

    companion object {

    }
}