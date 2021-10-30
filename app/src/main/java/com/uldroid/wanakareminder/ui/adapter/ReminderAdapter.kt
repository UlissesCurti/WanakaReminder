package com.uldroid.wanakareminder.ui.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uldroid.wanakareminder.R
import com.uldroid.wanakareminder.data.model.Product
import com.uldroid.wanakareminder.databinding.ItemViewReminderBinding
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter(
    val delegate: Delegate
) : ListAdapter<ReminderAdapter.Dataset, ReminderAdapter.ReminderAdapterViewHolder>(
    ReminderItemDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderAdapterViewHolder {
        val binding =
            ItemViewReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReminderAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReminderAdapterViewHolder(
        private val binding: ItemViewReminderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataset: Dataset) {
            binding.run {
                tvName.text = dataset.product.name
                imgProduct.setImageResource(
                    when (dataset.product.id) {
                        0L -> {
                            R.drawable.fish
                        }
                        1L -> {
                            R.drawable.apple
                        }
                        2L -> {
                            R.drawable.cow
                        }
                        3L -> {
                            R.drawable.cabbage
                        }
                        else -> R.drawable.ic_launcher_foreground
                    }
                )


                if (dataset.product.nextDates?.isNotEmpty() == true) {
                    if (dataset.product.expanded) {
                        containerStarted.visibility = View.VISIBLE
                        containerExpanded.visibility = View.VISIBLE
                        containerStandby.visibility = View.GONE
                        imgArrow.rotation = 180.0f
                    } else {
                        containerStarted.visibility = View.VISIBLE
                        containerExpanded.visibility = View.GONE
                        containerStandby.visibility = View.GONE
                        imgArrow.rotation = 0.0f
                    }
                    btnStop.setText(R.string.item_view_stop)

                    btnStop.setOnClickListener {
                        delegate.onStopClick(dataset.product)
                    }

                    var nextTimes = ""
                    val now = Calendar.getInstance()
                    var nextTimeFound = false

                    dataset.product.nextDates?.forEachIndexed { index, cal ->
                        if (now <= cal && !nextTimeFound) {
                            tvNextTime.text = formatTime(cal)
                            nextTimeFound = true
                            if (index == dataset.product.nextDates?.size?.minus(1)) {
                                tvNextTitle.setText(R.string.item_view_harvest)
                            } else {
                                tvNextTitle.setText(R.string.item_view_next)
                            }
                        }
                        if (now > cal && index == dataset.product.nextDates?.size?.minus(1)) {
                            tvNextTime.setText(R.string.harvest)
                            btnStop.setText(R.string.item_view_finish)

                            btnStop.setOnClickListener {
                                delegate.onStopClick(dataset.product, true)
                            }
                        }
                        nextTimes = when (index) {
                            0 -> {
                                nextTimes + "Start - ${formatTime(cal)}"
                            }
                            (dataset.product.nextDates?.size?.minus(1)) -> {
                                nextTimes + "<p>Harvest - ${formatTime(cal)}</p>"
                            }
                            else -> {
                                nextTimes + "<p>${index + 1}º - ${formatTime(cal)}</p>"
                            }
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvNextTimes.text = Html.fromHtml(nextTimes, Html.FROM_HTML_MODE_COMPACT);
                    } else {
                        tvNextTimes.text = Html.fromHtml(nextTimes);
                    }

                } else {
                    containerStarted.visibility = View.GONE
                    containerExpanded.visibility = View.GONE
                    containerStandby.visibility = View.VISIBLE
                }

                btnStart.setOnClickListener {
                    delegate.onStartClick(dataset.product)
                }
                imgArrow.setOnClickListener {
                    delegate.onArrowClick(dataset.product)
                }

            }
        }
    }

    data class Dataset(
        val product: Product
    )

    private fun formatTime(cal: Calendar): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return simpleDateFormat.format(cal.time).toString()
    }

    interface Delegate {
        fun onStartClick(product: Product)
        fun onStopClick(product: Product, ignoreDialog: Boolean = false)
        fun onArrowClick(product: Product)
    }
}

internal object ReminderItemDiffCallback : DiffUtil.ItemCallback<ReminderAdapter.Dataset>() {

    override fun areItemsTheSame(
        oldItem: ReminderAdapter.Dataset,
        newItem: ReminderAdapter.Dataset
    ): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(
        oldItem: ReminderAdapter.Dataset,
        newItem: ReminderAdapter.Dataset
    ): Boolean {
        var timesAreTheSame = true
        oldItem.product.nextDates?.forEachIndexed { index, s ->
            if (s != newItem.product.nextDates?.get(index)) {
                timesAreTheSame = false
            }
        }
        if (
            (oldItem.product.nextDates == null && newItem.product.nextDates != null) ||
            (oldItem.product.nextDates != null && newItem.product.nextDates == null)
        ) {
            timesAreTheSame = false
        }
        return oldItem.product.id == newItem.product.id &&
                oldItem.product.expanded == newItem.product.expanded &&
                timesAreTheSame
    }
}
