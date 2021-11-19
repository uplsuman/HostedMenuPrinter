package app.hmprinter.com.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.hmprinter.com.R
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.hmprinter.com.Models.RestaurantResponse
import app.hmprinter.com.ViewModel.LoginViewModel
import kotlin.system.exitProcess
import androidx.navigation.fragment.findNavController

import androidx.navigation.NavOptions


class LoginScreenFragment : Fragment() {
    private val TAG = LoginScreenFragment::class.java.name
    private lateinit var mViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login_screen, container, false)
        initBackPressed()
        initViewModel()
        initClicks()
        return view
    }

    private fun initClicks() {
        mViewModel.login("1572", "stationcafe@getnada.com")
    }

    private fun initViewModel() {
        val restaurantObserver: Observer<RestaurantResponse> = Observer { restaurant ->
            run {
                if (restaurant.error) {
                    Log.d(TAG, restaurant.message)
                } else {
                    Log.d(TAG, restaurant.message)
                    findNavController()
                        .navigate(R.id.action_login_screen_fragment_to_printer_selection_screen_fragment)
                }
            }
        }
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mViewModel.restaurantData.observe(requireActivity(), restaurantObserver)
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