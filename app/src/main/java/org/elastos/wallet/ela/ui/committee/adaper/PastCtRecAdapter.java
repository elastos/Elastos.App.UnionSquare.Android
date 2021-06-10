/*
 * Copyright (c) 2019 Elastos Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.elastos.wallet.ela.ui.committee.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.ui.committee.bean.CtDetailBean;
import org.elastos.wallet.ela.ui.committee.bean.PastCtBean;
import org.elastos.wallet.ela.ui.common.listener.CommonRvListener;
import org.elastos.wallet.ela.utils.DateUtil;
import org.elastos.wallet.ela.utils.SPUtil;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastCtRecAdapter extends RecyclerView.Adapter<PastCtRecAdapter.ViewHolder> {
    private Context context;
    private ManagerListener managerListener;
    private CommonRvListener commonRvListener;

    private List<PastCtBean.DataBean> list;
    private String did;
    private Map<String, CtDetailBean.DataBean> dataBeanList;

    public PastCtRecAdapter(Context context, List<PastCtBean.DataBean> list, Map<String, CtDetailBean.DataBean> dataBeanList, String did) {
        this.context = context;
        this.list = list;
        this.dataBeanList = dataBeanList;
        this.did = did;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ct_past_manager, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PastCtBean.DataBean data = list.get(i);
        CtDetailBean.DataBean dataBean = dataBeanList.get(data.getIndex() + "");
        viewHolder.time.setText(
                String.format("%1$s — %2$s", DateUtil.formatTimestamp(data.getStartDate(), "yyyy.MM.dd"), DateUtil.formatTimestamp(data.getEndDate(), "yyyy.MM.dd")));
        String status = data.getStatus();
        String stage = data.getIndex() + "";
        int Language = new SPUtil(context).getLanguage();
        if (Language != 0) {
            switch (data.getIndex()) {
                case 1:
                    stage = "1st";
                    break;
                case 2:
                    stage = "2nd";
                    break;
                case 3:
                    stage = "3rd";
                    break;
                default:
                    stage = stage + "th";
            }
        }


        if (status.equalsIgnoreCase("CURRENT")) {
            //本届
            viewHolder.title.setText(String.format(context.getString(R.string.pastitemtitle), stage, context.getString(R.string.current)));
        } else if (status.equalsIgnoreCase("VOTING")) {
            //选举中
            viewHolder.title.setText(String.format(context.getString(R.string.pastitemtitle), stage, context.getString(R.string.voting)));
        } else {
            viewHolder.title.setText(String.format(context.getString(R.string.amember), stage));
        }
        String type = dataBean.getType();//当前的身份  [委员: 'CouncilMember', 秘书长: 'SecretaryGeneral',未当选委员: 'UnelectedCouncilMember'其他: 'Other']
        switch (type) {
            case "CouncilMember":
                if (!"0".equals(dataBean.getDepositAmount())||status.equalsIgnoreCase("CURRENT")  ) {
                    viewHolder.manager.setText(context.getString(R.string.ctmanager));
                    viewHolder.rlBg.setBackgroundResource(R.drawable.ct_past_item_border);
                    viewHolder.manager.setVisibility(View.VISIBLE);
                }
                break;
            case "UnelectedCouncilMember":
                if (!"0".equals(dataBean.getDepositAmount()) || status.equalsIgnoreCase("VOTING")) {
                    viewHolder.manager.setText(context.getString(R.string.votemanager));
                    viewHolder.rlBg.setBackgroundResource(R.drawable.ct_past_item_border);
                    viewHolder.manager.setVisibility(View.VISIBLE);
                }
                break;
        }


        if (null != managerListener) {
            viewHolder.manager.setOnClickListener(v ->
                    managerListener.onManagerClick(i, data, dataBean)
            );
        }
        if (commonRvListener != null) {
            viewHolder.itemView.setOnClickListener(v -> commonRvListener.onRvItemClick(data.getIndex(), data));
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface ManagerListener {
        void onManagerClick(int position, PastCtBean.DataBean data, CtDetailBean.DataBean dataBean);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.rl_bg)
        RelativeLayout rlBg;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.manager_btn)
        TextView manager;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setManagerListener(ManagerListener listener) {
        this.managerListener = listener;
    }

    public void setCommonRvListener(CommonRvListener commonRvListener) {
        this.commonRvListener = commonRvListener;
    }


}
