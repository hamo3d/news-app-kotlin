package com.example.mynewsappwithretrofit.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mynewsappwithretrofit.R
import com.example.mynewsappwithretrofit.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // إعداد التنقل
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemReselectedListener {
            // تجاهل إعادة تحميل الـ Fragment
        }

        // إعداد ConnectivityManager
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // تعريف NetworkCallback
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    snackbar?.dismiss()
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    showNoInternetSnackbar()
                }
            }
        }

        // تسجيل الـ NetworkCallback
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // تحقق أولي من حالة الإنترنت
        if (!isInternetAvailable()) {
            showNoInternetSnackbar()
        }
    }

    private fun showNoInternetSnackbar() {
        if (snackbar == null || !snackbar!!.isShown) {
            snackbar = Snackbar.make(
                binding.main,
                "لا يوجد اتصال بالإنترنت",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("إعادة المحاولة") {
                if (isInternetAvailable()) {
                    snackbar?.dismiss()
                } else {
                    // لا نفعل شيئًا لأن Snackbar ظاهر بالفعل
                }
            }
            snackbar?.show()
        }
    }


    private fun isInternetAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}