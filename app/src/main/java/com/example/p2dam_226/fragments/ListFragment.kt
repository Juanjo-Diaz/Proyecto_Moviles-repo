package com.example.p2dam_226.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p2dam_226.R
import com.example.p2dam_226.databinding.FragmentListBinding
import com.example.p2dam_226.recycler.CardAdapter
import com.example.p2dam_226.recycler.CardItem
import com.example.p2dam_226.viewmodels.ListViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.items.collect { list ->
                    adapter.submitList(list)
                }
            }
        }

        binding.fabAdd.setOnClickListener {
            showAddBookDialog()
        }
    }

    private fun showAddBookDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_book, null)
        val editTitle = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editDialogTitle)
        val editDesc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editDialogDesc)
        val checkFav = dialogView.findViewById<android.widget.CheckBox>(R.id.checkDialogFav)

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title)
            .setView(dialogView)
            .setPositiveButton(R.string.dialog_add) { _, _ ->
                val title = editTitle.text?.toString().orEmpty()
                val desc = editDesc.text?.toString().orEmpty()
                val isFav = checkFav.isChecked
                if (title.isNotBlank()) {
                    viewModel.addBook(title, desc, isFav)
                }
            }
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
