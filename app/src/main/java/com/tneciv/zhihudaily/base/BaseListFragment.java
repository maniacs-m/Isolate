package com.tneciv.zhihudaily.base;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tneciv.zhihudaily.R;
import com.tneciv.zhihudaily.constants.Constants;
import com.tneciv.zhihudaily.constants.ErrorEntity;
import com.tneciv.zhihudaily.home.model.HomeEventEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.home_container)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    public SwipeRefreshLayout swipeRefresh;

    public SharedPreferences config;

    public BaseListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_list, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        config = getActivity().getSharedPreferences(Constants.PREF_CONFIG_KEY, Context.MODE_PRIVATE);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(R.color.accent, R.color.primary);
        init();
        setRecyclerLayout();
        requestUrl();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void operator(HomeEventEntity.OperatorType type) {
        if (type.getOperatorType().equals("refresh")) {
            swipeRefresh.setRefreshing(true);
            onRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void errorHandler(ErrorEntity errorEntity) {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        requestUrl();
    }

    /**
     * execute by order
     */
    public abstract void init();

    public abstract void setRecyclerLayout();

    public abstract void requestUrl();

}
