package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.UserAdapter
import com.loguito.brainmove.ext.dismissKeyboard
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.models.remote.Measure
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.UserListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_generic_user_list.*
import java.util.concurrent.TimeUnit

class AddMeasureUserSelectionFragment : Fragment() {
    private val args: AddMeasureUserSelectionFragmentArgs by navArgs()
    private var measure: Measure? = null
    private lateinit var viewModel: UserListViewModel
    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val navOption =
                NavOptions.Builder().setPopUpTo(R.id.addMeasureUserSelectionFragment, true).build()
            findNavController()
                .navigate(
                    AddMeasureUserSelectionFragmentDirections.actionGlobalAdminMainFragment3(),
                    navOption
                )
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_generic_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        measure = args.measure
        initViews()
        observeData()
        bindListeners()
    }

    private fun initViews() {
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.adapter = adapter
        userRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
    }

    private fun observeData() {
        viewModel.users.observe(viewLifecycleOwner, Observer {
            emptyUserList.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            userRecyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            adapter.users = it
        })

        viewModel.usersError.observe(viewLifecycleOwner, Observer {
            showDialog(getString(R.string.fetch_users_error), R.string.accept_button_text)
        })

        viewModel.loadingVisibility.observe(viewLifecycleOwner, Observer {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_users_message))
            } else {
                requireActivity().hideLoadingSpinner()
            }
        })

        adapter.selectedUser.observe(viewLifecycleOwner, Observer { user ->
            measure?.let {
                dismissKeyboard()
                val action =
                    AddMeasureUserSelectionFragmentDirections.actionAddMeasureUserSelectionFragmentToReviewUserMeasureFragment3(
                        user,
                        it
                    )
                findNavController().navigate(action)
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        searchUserEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchButton.isEnabled = it.isNotEmpty()
            }

        searchButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.getUserByKeyword(searchUserEditText.text.toString())
            }
    }
}