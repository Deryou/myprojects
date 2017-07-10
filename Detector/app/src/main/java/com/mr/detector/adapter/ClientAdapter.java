package com.mr.detector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mr.detector.R;
import com.mr.detector.util.TcpClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MR on 2017/5/4.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder>{

    private Context mContext;
    private List<TcpClient> mData;
    private TcpClient mClient;

    public ClientAdapter(Context context, List<TcpClient> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_client, parent, false);
        ClientViewHolder viewHolder = new ClientViewHolder(view);
        ButterKnife.bind(viewHolder, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        mClient = mData.get(position);
        holder.setData("");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ClientViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_view)
        TextView mClientName;

        public ClientViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(String data) {
            mClientName.setText(data);
        }
    }
}
