package com.example.myapplication.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentContactBinding

class ContactFragment : Fragment() {
    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(binding.root.context.getString(com.example.myapplication.R.string.contact_phone_uri))
            startActivity(intent)
        }
        binding.btnWhatsApp.setOnClickListener {
            val url = binding.root.context.getString(com.example.myapplication.R.string.contact_whatsapp_url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        binding.btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse(binding.root.context.getString(com.example.myapplication.R.string.contact_email_uri))
            intent.putExtra(Intent.EXTRA_SUBJECT, binding.root.context.getString(com.example.myapplication.R.string.contact_email_subject))
            startActivity(intent)
        }
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
        val sampleUrl = "https://www.html5rocks.com/en/tutorials/video/basics/devstories.webm"
        binding.videoView.setVideoURI(Uri.parse(sampleUrl))
        binding.videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            binding.videoView.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
