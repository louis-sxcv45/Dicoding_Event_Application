package com.example.dicodingeventapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.databinding.FragmentUpComingEventBinding
import com.example.dicodingeventapplication.ui.EventAdapter

class UpComingEventFragment : Fragment() {

    private var _binding : FragmentUpComingEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val eventViewModel by viewModels<EventViewModel>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpComingEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        eventAdapter = EventAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvent.layoutManager = layoutManager
        binding.rvEvent.adapter = eventAdapter

        eventViewModel.upComingEvent.observe(viewLifecycleOwner,{ listEvents->
            eventAdapter.submitList(listEvents)
            showLoading(false)
        })

        eventViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        eventViewModel.upComingEvents()
        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}