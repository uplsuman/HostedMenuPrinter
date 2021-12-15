package app.hmprinter.com.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.hmprinter.com.Constants.AppConstant.FAILED_LOGGED_IN
import app.hmprinter.com.Constants.AppConstant.SUCCESSFULLY_LOGGED_IN
import app.hmprinter.com.Helpers.DataStoreManager
import app.hmprinter.com.Models.RestaurantResponse
import app.hmprinter.com.R
import app.hmprinter.com.ViewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch


class LoginScreenFragment : Fragment() {
    private val TAG = LoginScreenFragment::class.java.name
    private lateinit var mViewModel: LoginViewModel
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var rlLoader: ConstraintLayout
    private lateinit var btnLogin: Button
    private lateinit var btnShowPassword: ImageView
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_login_screen, container, false)
        initUi(view)
        onClicks()
        initViewModel(view)
        return view
    }

    /**
     *  This method initialize UI views
     */
    private fun initUi(view: View) {
        btnShowPassword = view.findViewById(R.id.show_pass_btn)
        etPassword = view.findViewById(R.id.et_pswd)
        etEmail = view.findViewById(R.id.et_email)
        btnLogin = view.findViewById(R.id.btn_login)
        rlLoader = view.findViewById(R.id.loader)
    }

    /**
     *  This method handles onclicks
     */
    private fun onClicks() {
        btnShowPassword.setOnClickListener {

            if (etPassword.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
                btnShowPassword.setImageResource(R.drawable.pswd_visibility_on)

                //Show Password
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                btnShowPassword.setImageResource(R.drawable.pswd_visibility_off)

                //Hide Password
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }

        btnLogin.setOnClickListener {
            rlLoader.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
            mViewModel.login(etPassword.text.toString(), etEmail.text.toString())

        }
    }

    /**
     *  This method initialize viewmodel &
     *  observe viewmodel data
     *  Redirects based on login response
     */
    @SuppressLint("ShowToast")
    private fun initViewModel(view: View) {
        val restaurantObserver: Observer<RestaurantResponse> = Observer { restaurant ->
            run {
                Log.d(TAG, restaurant.message)
                if (restaurant.error) {
                    showSnackBar(view, FAILED_LOGGED_IN, Color.DKGRAY)
                    Log.d(TAG, restaurant.message)
                    rlLoader.visibility = View.GONE
                    btnLogin.visibility = View.VISIBLE

                } else {
                    showSnackBar(view, SUCCESSFULLY_LOGGED_IN, Color.DKGRAY)
                    Log.d(TAG, restaurant.message)
                    lifecycleScope.launch {
                        val gson = Gson()
                        val parsedRestaurantData = gson.toJson(restaurant)
                        dataStoreManager.updateDataToDataStore(
                            true,
                            parsedRestaurantData.toString()
                        )
                    }
                    rlLoader.visibility = View.GONE
                    findNavController()
                        .navigate(R.id.action_login_screen_fragment_to_printer_selection_screen_fragment)

                }
            }

        }
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mViewModel.restaurantData.observe(requireActivity(), restaurantObserver)
    }

    /**
     * Function for snackBar
     */
    private fun showSnackBar(view: View, message: String, color: Int) {
        val snackBar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null)
        with(snackBar) {
            snackBar.view.setBackgroundColor(color)
            show()
        }
    }

    companion object
}