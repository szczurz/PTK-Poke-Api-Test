package com.pkurkowski.pokeapi.presentation.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.domain.SpriteDescription

class SpritesAdapter(map: Map<SpriteDescription, String>) :
    RecyclerView.Adapter<SpritesAdapter.SpriteHolder>() {

    val list: List<Pair<SpriteDescription, String>> = map.toList()

    class SpriteHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val spriteImageView: ImageView = view.findViewById(R.id.spriteImageView)
        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

        fun bind(description: SpriteDescription, url: String) {
            descriptionTextView.text =
                "${description.side}\n${description.gender}\n${description.style}"

            Glide.with(spriteImageView)
                .load(url)
                .placeholder(R.drawable.ic_baseline_sync_24)
                .error(R.drawable.ic_baseline_sync_problem_24)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(spriteImageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpriteHolder {
        return SpriteHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokomon_sprite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SpriteHolder, position: Int) {
        holder.bind(list[position].first, list[position].second)
    }

    override fun getItemCount() = list.size
}