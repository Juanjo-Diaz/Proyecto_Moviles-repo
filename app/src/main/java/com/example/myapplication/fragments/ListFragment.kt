package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentListBinding
import com.example.myapplication.recycler.CardAdapter
import com.example.myapplication.recycler.CardItem
import com.example.myapplication.viewmodels.ListViewModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListViewModel by activityViewModels()
    private lateinit var adapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CardAdapter(emptyList(), onToggleFavorite = { item ->
            viewModel.toggleFavorite(item.id)
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.setItems(initialItems())

        viewModel.items.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    private fun initialItems(): List<CardItem> = listOf(
        CardItem(1, getString(R.string.mtg_title_1), getString(R.string.mtg_desc_1), R.drawable.ic_mtg_bolt),
        CardItem(2, getString(R.string.mtg_title_2), getString(R.string.mtg_desc_2), R.drawable.ic_mtg_counterspell),
        CardItem(3, getString(R.string.mtg_title_3), getString(R.string.mtg_desc_3), R.drawable.ic_mtg_llanowar),
        CardItem(4, getString(R.string.mtg_title_4), getString(R.string.mtg_desc_4), R.drawable.ic_mtg_shivan)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
