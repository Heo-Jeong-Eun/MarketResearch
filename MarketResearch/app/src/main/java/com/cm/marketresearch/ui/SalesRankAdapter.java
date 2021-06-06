package com.cm.marketresearch.ui;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import com.cm.marketresearch.R;
import com.cm.marketresearch.base.BaseRecyclerAdapter;
import com.cm.marketresearch.base.BaseViewHolder;
import com.cm.marketresearch.databinding.ItemCommonUsageBinding;
import com.cm.marketresearch.remote.response.RankItem;
import com.cm.marketresearch.remote.response.RankScoreItem;


public class SalesRankAdapter extends BaseRecyclerAdapter<RankScoreItem, SalesRankAdapter.ViewHolder> {


    public SalesRankAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends BaseViewHolder<ItemCommonUsageBinding, RankScoreItem> {

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_common_usage);
        }

        @Override
        protected void bind(int position, RankScoreItem data) {
            binding.tvTitle.setText((position + 1) + "순위");
            binding.subTitle.setText(data.getRankItem().getTRDAR_CD_NM());
        }


    }
}
