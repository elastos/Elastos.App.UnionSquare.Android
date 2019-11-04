package org.elastos.wallet.ela.ui.did.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.ui.common.listener.CommonRvListener;
import org.elastos.wallet.ela.ui.did.entity.DIDInfoEntity;
import org.elastos.wallet.ela.utils.DateUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 资产页面的rv
 */

public class DIDRecordRecAdapetr extends RecyclerView.Adapter<DIDRecordRecAdapetr.ViewHolder> {


    public void setCommonRvListener(CommonRvListener commonRvListener) {
        this.commonRvListener = commonRvListener;
    }

    private CommonRvListener commonRvListener;
    private List<DIDInfoEntity> list;

    private Context context;

    public DIDRecordRecAdapetr(Context context, List<DIDInfoEntity> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_did_record, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DIDInfoEntity didInfoEntity = list.get(position);
        holder.tvDidname.setText(didInfoEntity.getDidName());
        ////Pending 确认中   Confirmed已确认  Unpublished 未发布(草稿  这个api不提供,保存草稿时候自己设置)
        switch (didInfoEntity.getStatus()) {
            case "Pending":
                holder.tvDid.setVisibility(View.GONE);
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.c7DF17B));
                holder.tvStatus.setText(context.getString(R.string.Pending));
                break;
            case "Unpublished":
                holder.tvDid.setVisibility(View.GONE);
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.whiter50));
                holder.tvStatus.setText(context.getString(R.string.lastedit) + DateUtil.timeNYR(didInfoEntity.getIssuanceDate(), context));
                break;
            default:
                holder.tvDid.setVisibility(View.VISIBLE);
                holder.tvDid.setText("did:ela:" + didInfoEntity.getId());
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.whiter50));
                if (didInfoEntity.getExpires() > new Date().getTime()) {
                    //过期
                    String str = "<font color='#FBAD42'>" + context.getString(R.string.expired) + "</font> " +
                            context.getString(R.string.expireddate) + DateUtil.timeNYR(didInfoEntity.getIssuanceDate(), context);
                    holder.tvStatus.setText(Html.fromHtml(str));
                    break;
                }
                holder.tvStatus.setText(context.getString(R.string.validdate) + DateUtil.timeNYR(didInfoEntity.getIssuanceDate(), context)
                        + "至" + DateUtil.timeNYR(didInfoEntity.getExpires(), context));

                break;
        }

        if (commonRvListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonRvListener.onRvItemClick(position, didInfoEntity);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_didname)
        TextView tvDidname;
        @BindView(R.id.tv_did)
        TextView tvDid;
        @BindView(R.id.tv_status)
        TextView tvStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
