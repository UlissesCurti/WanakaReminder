package com.uldroid.wanakareminder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uldroid.wanakareminder.R
import com.uldroid.wanakareminder.data.model.Product
import com.uldroid.wanakareminder.data.model.toReminderAdapterDataset
import com.uldroid.wanakareminder.databinding.FragmentReminderBinding
import com.uldroid.wanakareminder.ui.adapter.ReminderAdapter
import com.uldroid.wanakareminder.ui.viewmodel.ReminderViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ReminderFragment : Fragment(), ReminderAdapter.Delegate {

    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReminderViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderBinding.inflate(inflater, container, false)

        observeProducts()
        viewModel.getProducts()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            rvReminders.run {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = ReminderAdapter(this@ReminderFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeProducts() {
        viewModel.productList.observe(viewLifecycleOwner, {
            (binding.rvReminders.adapter as ReminderAdapter).submitList(it.toReminderAdapterDataset())
        })
    }

    override fun onStartClick(product: Product) {
        viewModel.start(product.copy())
    }

    override fun onStopClick(product: Product, ignoreDialog: Boolean) {
        if(ignoreDialog) {
            viewModel.stop(product.copy())
        } else {
            buildStopConfirmationDialog(product.copy())
        }
    }

    override fun onArrowClick(product: Product) {
        viewModel.expand(product.copy())
    }

    private fun buildStopConfirmationDialog(product: Product) {
        AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.yes) { dialog, _ ->
                viewModel.stop(product)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .setMessage(R.string.stop_dialog_message)
            ?.show()
    }
}