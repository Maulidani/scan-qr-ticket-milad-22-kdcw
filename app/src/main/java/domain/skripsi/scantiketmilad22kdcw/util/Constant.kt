package domain.skripsi.scantiketmilad22kdcw.util

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import domain.skripsi.scantiketmilad22kdcw.R

class Constant {

    companion object {
        const val BASE_URL = "https://milad22.kedai.or.id"

        fun MaterialButton.setShowProgress(showProgress: Boolean?) {

            iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
            isCheckable = showProgress == false
            text = if (showProgress == true) "" else "Coba lagi"

            icon = if (showProgress == true) {
                CircularProgressDrawable(context!!).apply {
                    setStyle(CircularProgressDrawable.DEFAULT)
                    setColorSchemeColors(ContextCompat.getColor(context!!, R.color.white))
                    start()
                }
            } else null

            if (icon != null) { // callback to redraw button icon
                icon.callback = object : Drawable.Callback {
                    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                    }

                    override fun invalidateDrawable(who: Drawable) {
                        this@setShowProgress.invalidate()
                    }

                    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
                    }
                }
            }
        }
    }
}