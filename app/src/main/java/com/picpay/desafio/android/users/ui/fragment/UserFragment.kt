package com.picpay.desafio.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.picpay.desafio.android.databinding.FragmentUsersBinding
import com.picpay.desafio.android.users.domain.model.User
import com.picpay.desafio.android.ui.adapter.users.UserListAdapter
import com.picpay.desafio.android.users.viewmodel.UserViewModel
import com.picpay.desafio.android.users.viewmodel.model.UserState
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UserViewModel by viewModel()
    private val adapter by lazy { UserListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
//       viewModel.init()
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserState.SuccessState -> stateSuccess(state.users, false)
                is UserState.SuccessLocalState -> stateSuccess(state.users, true)
                is UserState.ErrorState -> stateError(state)
                is UserState.LoadingState -> stateLoading(state.load)
            }
        }

    }

    private fun setupViews() {
        with(binding) {
            recyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener {
                viewModel.tapOnRetry()
            }
            binding.contentError.btnRetry.setOnClickListener {
                viewModel.tapOnRetry()
            }
        }
    }

    private fun stateLoading(loading: Boolean) {
        with(binding) {
            pbLoad.isVisible = loading
            swipeRefresh.isRefreshing = false
        }
    }

    private fun stateError(errorState: UserState.ErrorState) {
        with(binding.contentError) {
            body.isVisible = true
            tvMessageError.text = errorState.messageError
            btnRetry.text = errorState.retryMessage
        }
    }

    private fun stateSuccess(users: List<User>, showContentError: Boolean) {
        with(binding) {
            adapter.users = users
            contentError.body.isVisible = showContentError
        }
    }

}