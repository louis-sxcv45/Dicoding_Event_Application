package com.example.dicodingeventapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    private lateinit var eventViewModel: EventViewModel
    private lateinit var binding: ActivityDetailEventBinding

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Get the event object from the intent
        val eventDetail: ListEventsItem? = intent.getParcelableExtra(EXTRA_EVENT)
        Log.d("DetailEventActivity", "Received Event: $eventDetail")

        if (eventDetail != null) {
            supportActionBar?.title = eventDetail.name ?: "Detail Event"
            // Load event image
            eventViewModel.eventsDetail(eventDetail)
            val imageUrl = eventDetail.imageLogo
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.eventImage)
            } else {
                Log.e("DetailEventActivity", "Image URL is null.")
            }

            // Display event details
            binding.textViewEventName.text = eventDetail.name ?: "Nama Acara Tidak Tersedia"
            binding.textViewOrganizer.text = eventDetail.ownerName ?: "Penyelenggara Tidak Tersedia"
            binding.textViewTime.text = "${eventDetail.beginTime} - ${eventDetail.endTime}"

            val fixedQuota = eventDetail.quota?.minus(eventDetail.registrants!!)
            binding.textViewQuota.text = "Sisa Kuota: ${fixedQuota ?: 0}"
            binding.textViewDescription.text = HtmlCompat.fromHtml(
                eventDetail.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.textViewSummary.text = eventDetail.summary ?: "Ringkasan Tidak Tersedia"
            binding.textViewRegistrants.text = "Pendaftar: ${eventDetail.registrants ?: 0}"
            binding.textViewCity.text = eventDetail.cityName ?: "Kota Tidak Tersedia"
            binding.textViewCategory.text = eventDetail.category ?: "Kategori Tidak Tersedia"

            binding.btnRegister.setOnClickListener {
                val registrationUrl = "https://www.dicoding.com/events/${eventDetail.id}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(registrationUrl))
                startActivity(intent)
            }
            showLoading(false)
        }
        eventViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
