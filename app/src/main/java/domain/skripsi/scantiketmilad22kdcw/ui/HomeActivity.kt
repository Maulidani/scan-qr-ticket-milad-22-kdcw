package domain.skripsi.scantiketmilad22kdcw.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import domain.skripsi.scantiketmilad22kdcw.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.SmoothBottomBar

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav: SmoothBottomBar
    private val REQUEST_CODE_CAMERA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNav = findViewById(R.id.bottomBar)

        loadFragment(HomeFragment())

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
        }
    }

    override fun onResume() {
        super.onResume()

        bottomNav.setOnItemSelectedListener {
            when (it) {
                0 -> {
                    loadFragment(HomeFragment())
                    bottomNav.visibility = View.VISIBLE
                }
                1 -> {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(
                            applicationContext,
                            "Perizina kamera tidak aktif",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
//                        val animation =
//                            AnimationUtils.loadAnimation(this, R.anim.fade_out_bottom_nav)

                        CoroutineScope(Dispatchers.Main).launch {
                            loadFragment(ScannerFragment())
//                            delay(150)
//                            bottomNav.startAnimation(animation)
//                            delay(600)
//                            bottomNav.visibility = View.GONE
                        }
                    }
                }
                2 -> {
                    loadFragment(InfoFragment())
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, fragment)
            commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
            } else {
                //
            }
        }
    }

//    override fun onBackPressed() {
//
//        if (bottomNav.isVisible) {
//            super.onBackPressed()
//        } else {
//            loadFragment(HomeFragment())
//            bottomNav.visibility = View.VISIBLE
//            bottomNav.itemActiveIndex = 0
//        }
//
//    }

}