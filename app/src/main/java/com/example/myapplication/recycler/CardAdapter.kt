package com.example.myapplication.recycler

import android.media.AudioManager
import android.media.ToneGenerator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.button.MaterialButton

class CardAdapter(
    private var items: List<CardItem>,
    private val onToggleFavorite: (CardItem) -> Unit,
    private val lockFavoriteButton: Boolean = false
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    fun submitList(newItems: List<CardItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, lockFavoriteButton) {
            if (!lockFavoriteButton) {
                if (item.isFavorite) {
                    tone.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                } else {
                    tone.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                }
                onToggleFavorite(item)
            }
        }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val favButton: MaterialButton = itemView.findViewById(R.id.buttonFav)

        fun bind(item: CardItem, lock: Boolean, onToggle: () -> Unit) {
            image.setImageResource(item.imageResId)
            image.contentDescription = itemView.context.getString(R.string.content_image)
            title.text = item.title
            description.text = item.description

            favButton.isEnabled = !lock
            favButton.isCheckable = true
            favButton.isChecked = item.isFavorite

            favButton.text = if (item.isFavorite) {
                itemView.context.getString(R.string.tab_favorites)
            } else {
                itemView.context.getString(R.string.action_favorite)
            }

            favButton.setOnClickListener { onToggle() }
        }
    }
}
