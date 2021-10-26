package gb.android.nasapi.presentation.pager.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import gb.android.nasapi.databinding.FragmentMarsBinding

class MarsFragment : Fragment() {

    //===========================================================================================
    // BINDING

    private var _binding: FragmentMarsBinding? = null
    private val binding: FragmentMarsBinding
        get() = _binding!!

    //===========================================================================================
    // VIEW MODEL

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, MarsViewModelFactory())
            .get(MarsViewModel::class.java)
    }

    //===========================================================================================
    // RECYCLER VIEW ADAPTER

    private val adapter: MarsAdapter by lazy {
        MarsAdapter()
    }

    //===========================================================================================
    // COMPANION

    companion object {
        fun newInstance() = MarsFragment()
    }

    //===========================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMars.adapter = adapter

        viewModel.requestCuriosityPics()
        viewModel.liveDataToObserve.observe(requireActivity()) {
            render(it)
        }

        binding.chipGroupMarsRover.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.chipMarsRoverCuriosity.id -> {
                    viewModel.requestCuriosityPics()
                }
                binding.chipMarsRoverOpportunity.id -> {
                    viewModel.requestOpportunityPics()
                }
                binding.chipMarsRoverSpirit.id -> {
                    viewModel.requestSpiritPics()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //===========================================================================================
    // RENDER

    private fun render(marsState: MarsState) {
        when (marsState) {
            is MarsState.Error -> {
                binding.rvMars.visibility = View.GONE
                Snackbar
                    .make(
                        binding.root,
                        "${marsState.error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            MarsState.Loading -> {
                Snackbar
                    .make(
                        binding.root,
                        "LOADING....",
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
            is MarsState.Success -> {
                binding.rvMars.visibility = View.VISIBLE
                adapter.setMarsPics(marsState.marsDomainDataModelList)
            }
        }
    }
}