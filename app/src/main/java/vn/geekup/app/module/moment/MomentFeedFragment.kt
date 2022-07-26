package vn.geekup.app.module.moment

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.base.list.PagingLoadStateAdapter
import vn.geekup.app.databinding.FragmentMomentFeedBinding
import vn.geekup.app.module.main.MainFragment
import vn.geekup.app.utils.*

class MomentFeedFragment : BaseFragment<MomentViewModel, FragmentMomentFeedBinding>() {

    private lateinit var adapterPaging: MomentFeedPagingAdapter

    override val viewModel: MomentViewModel by viewModel()

    override fun initViewModelByActivityLifecycle(): Boolean = true

    override fun provideViewBinding(parent: ViewGroup): FragmentMomentFeedBinding {
        return FragmentMomentFeedBinding.inflate(layoutInflater, parent, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar(R.color.color_white)
        (parentFragment?.parentFragment as? MainFragment)?.bottomNavigationState(true)
        initRecyclerView()
        initRefreshLayout()

        fragmentBinding.tvHtml.setText(
            HtmlCompat.fromHtml(
                "Great job! Looks like you will have a <(%x)> higher GOP than Forecast due to an increase in <(%d)>",
                FROM_HTML_MODE_COMPACT,
                null,
            ) { opening, tag, output, xml ->
                Log.e("Moment Feed Fragment", " $opening - $tag - $output - $xml")
                if (opening) {
                    if (tag.equals("x")) {
                        output.append("<(%x)>")
                    } else if (tag.equals("d")) {
                        output.append("<%d>")
                    }
                }

            }
        )
    }

    override fun bindViewModel() {
        super.bindViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.fetchPagingNetworkFlow().distinctUntilChanged().collectLatest {
                adapterPaging.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapterPaging.loadStateFlow.collect { loadStates ->
                if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                    fragmentBinding.isMomentEmpty = adapterPaging.itemCount == 0
                }
                fragmentBinding.lnRetry.visible(loadStates.refresh is LoadState.Error)
                fragmentBinding.swMoments.isRefreshing =
                    loadStates.refresh is LoadState.Loading && adapterPaging.itemCount > 0
            }
        }
    }

    private fun initAdapter() {
        adapterPaging = MomentFeedPagingAdapter()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragmentBinding.rvMoments.layoutManager = layoutManager
        fragmentBinding.rvMoments.adapter =
            adapterPaging.withLoadStateFooter(
                footer = PagingLoadStateAdapter(retryFunc = {
                    adapterPaging.retry()
                }),
            )
    }

    private fun initRefreshLayout() {
        fragmentBinding.swMoments.setOnRefreshListener {
            adapterPaging.refresh()
        }

        fragmentBinding.btnRetry.setOnClickListener {
            adapterPaging.retry()
        }
    }

}
