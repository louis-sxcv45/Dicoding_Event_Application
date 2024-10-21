package com.example.dicodingeventapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.databinding.FragmentHomeBinding
import com.example.dicodingeventapplication.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var upComingEventAdapter: EventAdapter
    private lateinit var pasEventAdapter: EventAdapter
    private val binding get() = _binding!!
    private val eventViewModel by viewModels<EventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        upComingEventAdapter = EventAdapter()
        val upcominglayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvupcomingevent.layoutManager = upcominglayoutManager
        binding.rvupcomingevent.adapter = upComingEventAdapter

        pasEventAdapter = EventAdapter()
        val paseventlayoutmanager = LinearLayoutManager(requireContext())
        binding.rvpastEvent.layoutManager = paseventlayoutmanager
        binding.rvpastEvent.adapter = pasEventAdapter

        eventViewModel.upComingEvents()
        eventViewModel.pastEvents()
        homeData()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun homeData(){
        eventViewModel.upComingEvent.observe(viewLifecycleOwner,{ listEvents->
            upComingEventAdapter.submitList(listEvents.take(5))
        })
        eventViewModel.finishedEvent.observe(viewLifecycleOwner,{ listEvents->
            pasEventAdapter.submitList(listEvents.take(5))
        })
        eventViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        showLoading(false)
    }
    private fun showLoading(isLoading: Boolean){
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}