package app.hmprinter.com.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.hmprinter.com.R


class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        redirectToLoginScreen()
        return view
    }

    private fun redirectToLoginScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController()
                .navigate(R.id.action_splash_screen_to_login_screen)
        }, 100)
    }

    companion object{

    }
}