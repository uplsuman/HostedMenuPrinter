package app.hmprinter.com.Fragments

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.hmprinter.com.Helpers.DataStoreManager
import app.hmprinter.com.Models.RestaurantResponse
import app.hmprinter.com.R
import app.hmprinter.com.ViewModel.LoginViewModel
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class LoginScreenFragment : Fragment() {
    private val TAG = LoginScreenFragment::class.java.name
    private lateinit var mViewModel: LoginViewModel
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(requireActivity())
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
    }

    private fun initViewModel() {
        val restaurantObserver: Observer<RestaurantResponse> = Observer { restaurant ->
            run {
                if (restaurant.error) {
                    Log.d(TAG, restaurant.message)
                } else {
                    Log.d(TAG, restaurant.message)
                    lifecycleScope.launch {
                        dataStoreManager.updateDataToDataStore(true, restaurant.toString())
                    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val show_pass_btn: ImageView = view.findViewById(R.id.show_pass_btn)
        val et_pswd: EditText = view.findViewById(R.id.et_pswd)
        val et_email: EditText = view.findViewById(R.id.et_email)
        val btn_login: Button = view.findViewById(R.id.btn_login)
        show_pass_btn.setOnClickListener(View.OnClickListener {

                if(et_pswd.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                    show_pass_btn.setImageResource(R.drawable.pswd_visibility_on)

                    //Show Password
                    et_pswd.transformationMethod = HideReturnsTransformationMethod.getInstance();
                }
                else{
                    show_pass_btn.setImageResource(R.drawable.pswd_visibility_off)

                    //Hide Password
                    et_pswd.transformationMethod = PasswordTransformationMethod.getInstance();

                }
        })

        btn_login.setOnClickListener(View.OnClickListener {
                    mViewModel.login(et_pswd.text.toString(),et_email.text.toString())
        })

    }


    companion object {

    }
}