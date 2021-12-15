package app.hmprinter.com.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import app.hmprinter.com.Helpers.DataStoreManager
import app.hmprinter.com.R


class SplashScreenFragment : Fragment() {
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        checkIfLoggedIn()
        return view
    }

    /**
     * This method checks if user is logged in & redirect to based on login status
     */
    private fun checkIfLoggedIn() {
        dataStoreManager.isLoggedIn.asLiveData().observe(viewLifecycleOwner, Observer {
            if (it) {
                redirectToSelectPrinterScreen()
            } else {
                redirectToLoginScreen()
            }
        })
    }

    private fun redirectToSelectPrinterScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController()
                .navigate(R.id.action_splash_screen_to_printer_selection_screen)
        }, delayInMili)
    }

    private fun redirectToLoginScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController()
                .navigate(R.id.action_splash_screen_to_login_screen)
        }, delayInMili)
    }

    companion object {
        private const val delayInMili: Long = 500
    }
}