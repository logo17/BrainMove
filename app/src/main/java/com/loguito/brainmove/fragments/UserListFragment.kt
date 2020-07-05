package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.UserAdapter
import com.loguito.brainmove.ext.dismissKeyboard
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.UserListViewModel
import kotlinx.android.synthetic.main.fragment_generic_user_list.*
import java.util.concurrent.TimeUnit

class UserListFragment : Fragment() {
    private lateinit var viewModel: UserListViewModel
    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)
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
        initViews()
        observeData()
        bindListeners()
    }

    private fun initViews() {
        toolbar.setOnMenuItemClickListener {
            viewModel.logoutUser()
            true
        }
        toolbar.inflateMenu(R.menu.toolbar_logout_menu)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.adapter = adapter
        userRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
    }

    private fun observeData() {
        viewModel.isLoggedOutUser.observe(viewLifecycleOwner) {
            val navOption = NavOptions.Builder().setPopUpTo(R.id.adminMainFragment2, true).build()
            requireActivity().findNavController(R.id.fragment)
                .navigate(MainFragmentDirections.actionGlobalLauncherFragment(), navOption)
        }

        viewModel.usersError.observe(viewLifecycleOwner, Observer {
            showDialog(getString(R.string.fetch_users_error), R.string.accept_button_text)
        })

        viewModel.users.observe(viewLifecycleOwner, Observer {
            emptyUserList.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            userRecyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            adapter.users = it
        })

        viewModel.loadingVisibility.observe(viewLifecycleOwner, Observer {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_users_message))
            } else {
                requireActivity().hideLoadingSpinner()
            }
        })

        adapter.selectedUser.observe(viewLifecycleOwner, Observer {
            dismissKeyboard()
            val action = UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(it)
            findNavController().navigate(action)
        })
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        searchUserEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.getUserByKeyword(it.toString()) }
    }
}