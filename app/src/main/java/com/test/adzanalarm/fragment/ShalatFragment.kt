package com.test.adzanalarm.fragment

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.adzanalarm.R
import com.test.adzanalarm.databinding.FragmentShalatBinding
import com.test.adzanalarm.utils.NotificationService
import com.test.adzanalarm.utils.NotificationUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class ShalatFragment : Fragment() {

    private lateinit var binding : FragmentShalatBinding
    private var mNotified = false
    private var title = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShalatBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        fun newInstance(): ShalatFragment{
            return ShalatFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val diff = setReminder()

        object : CountDownTimer(diff, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                @SuppressLint("DefaultLocale") val count = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(
                                    millisUntilFinished
                                )
                            ),  // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
                binding.countdown.text = count
            }

            override fun onFinish() {

            }
        }.start()

        if (!mNotified) {
            createNotifChannel()
            activity?.let { NotificationUtils().setNotification(Calendar.getInstance().timeInMillis + diff, it, title) }
        }

    }

    private fun setReminder() : Long{
        val sdf = SimpleDateFormat("dd/M/yyyy kk:mm:ss")
        val currentDate = sdf.format(Date())

        val sdf2 = SimpleDateFormat("dd/M/yyyy")
        val dateNow = sdf2.format(Date())

        val subuh = "04:26:00"
        val reminderSubuh = "$dateNow $subuh"

        val terbit = "05:43:00"
        val reminderTerbit = "$dateNow $terbit"

        val dzuhur = "11:46:00"
        val reminderDzuhur = "$dateNow $dzuhur"

        val ashar = "14:57:00"
        val reminderAshar = "$dateNow $ashar"

        val maghrib = "17:49:00"
        val reminderMaghrib = "$dateNow $maghrib"

        val isya = "18:58:00"
        val reminderIsya = "$dateNow $isya"

        val dateStart = sdf.parse(currentDate)
        val dateSubuh = sdf.parse(reminderSubuh)
        val dateTerbit = sdf.parse(reminderTerbit)
        val dateDzuhur = sdf.parse(reminderDzuhur)
        val dateAshar = sdf.parse(reminderAshar)
        val dateMaghrib = sdf.parse(reminderMaghrib)
        val dateIsya = sdf.parse(reminderIsya)

        var diff = 0L
        if (dateStart<dateSubuh){
            binding.prayer.text = "Subuh 04:26"
            title = "Subuh"
            diff = dateSubuh.time - dateStart.time
            resetColor()
            binding.lySubuh.setBackgroundResource(R.drawable.bg_round_basic)
        }else if (dateStart<dateTerbit){
            binding.prayer.text = "Terbit 05:43"
            title = "Terbit"
            diff = dateTerbit.time - dateStart.time
            resetColor()
            binding.lyTerbit.setBackgroundResource(R.drawable.bg_round_basic)
        }else if (dateStart<dateDzuhur){
            binding.prayer.text = "Dzuhur 11:46"
            title = "Dzuhur"
            diff = dateDzuhur.time - dateStart.time
            resetColor()
            binding.lyDzuhur.setBackgroundResource(R.drawable.bg_round_basic)
        }else if (dateStart<dateAshar){
            binding.prayer.text = "Ashar 14:57"
            title = "Ashar"
            diff = dateAshar.time - dateStart.time
            resetColor()
            binding.lyAsar.setBackgroundResource(R.drawable.bg_round_basic)
        }else if (dateStart<dateMaghrib){
            binding.prayer.text = "Maghrib 17:49"
            title = "Maghrib"
            diff = dateMaghrib.time - dateStart.time
            resetColor()
            binding.lyMaghrib.setBackgroundResource(R.drawable.bg_round_basic)
        }else if (dateStart<dateIsya){
            binding.prayer.text = "Isya 18:58"
            title = "Isya"
            diff = dateIsya.time - dateStart.time
            resetColor()
            binding.lyIsya.setBackgroundResource(R.drawable.bg_round_basic)
        }else {
            binding.prayer.text = "Subuh 04:26"
            title = "Subuh"
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_MONTH, 1)
            val dateNow24 = sdf2.format(cal.time)
            val reminderSubuh24 = "$dateNow24 $subuh"
            val dateSubuh24 = sdf.parse(reminderSubuh24)
            diff = dateSubuh24.time - dateStart.time
            resetColor()
            binding.lySubuh.setBackgroundResource(R.drawable.bg_round_basic)
        }

        return diff
    }

    private fun resetColor(){
        binding.lySubuh.setBackgroundResource(R.drawable.bg_round_basic50)
        binding.lyTerbit.setBackgroundResource(R.drawable.bg_round_basic50)
        binding.lyDzuhur.setBackgroundResource(R.drawable.bg_round_basic50)
        binding.lyAsar.setBackgroundResource(R.drawable.bg_round_basic50)
        binding.lyMaghrib.setBackgroundResource(R.drawable.bg_round_basic50)
        binding.lyIsya.setBackgroundResource(R.drawable.bg_round_basic50)
    }

    private fun createNotifChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library


            val notificationManager:NotificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NotificationService.CHANNEL_ID, NotificationService.CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = getString(R.string.body_notif)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}