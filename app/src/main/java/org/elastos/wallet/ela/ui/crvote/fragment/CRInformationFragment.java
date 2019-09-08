package org.elastos.wallet.ela.ui.crvote.fragment;


import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.blankj.utilcode.util.ToastUtils;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.NodeDotJsonViewData;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.NodeInfoBean;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.SuperNodeListPresenter;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.CacheUtil;
import org.elastos.wallet.ela.utils.ClipboardUtil;
import org.elastos.wallet.ela.utils.GlideApp;
import org.elastos.wallet.ela.utils.NumberiUtil;
import org.elastos.wallet.ela.utils.SPUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 节点信息
 */
public class CRInformationFragment extends BaseFragment {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.sb_jrhxlb)
    SuperButton sbJrhxlb;
    @BindView(R.id.sb_ckhxlb)
    SuperButton sbCkhxlb;
    CRListBean.DataBean.ResultBean.ProducersBean bean;
    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_num_vote)
    TextView tvNumVote;
    @BindView(R.id.tv_zl)
    TextView tvZl;
    @BindView(R.id.tv_addrs)
    TextView tv_addrs;
    List<CRListBean.DataBean.ResultBean.ProducersBean> list;
    String zb;
    public String type;
    @BindView(R.id.tv_node_publickey)
    TextView tvNodePublickey;
    @BindView(R.id.iv_icon)
    AppCompatImageView ivIcon;
    @BindView(R.id.line_info)
    View lineInfo;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.line_intro)
    View lineIntro;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.ll_intro)
    LinearLayout llIntro;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.ll_infodetail)
    LinearLayout llInfodetail;
    @BindView(R.id.tv_intro_detail)
    TextView tvIntroDetail;
    private ArrayList<CRListBean.DataBean.ResultBean.ProducersBean> netlist;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_node_information;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void setExtraData(Bundle data) {
        bean = (CRListBean.DataBean.ResultBean.ProducersBean) data.getSerializable("bean");
        zb = data.getString("zb");
        netlist = (ArrayList<CRListBean.DataBean.ResultBean.ProducersBean>) data.getSerializable("netList");
    }

    @Override
    protected void initView(View view) {
        setToobar(toolbar, toolbarTitle, getString(R.string.node_information));
//        registReceiver();
        String url = bean.getUrl();
        new SuperNodeListPresenter().getUrlJson(url, this, new NodeDotJsonViewData() {
            @Override
            public void onGetNodeDotJsonData(NodeInfoBean t, String url) {
                //获取icon
                if (t == null || t.getOrg() == null || t.getOrg().getBranding() == null || t.getOrg().getBranding().getLogo_256() == null) {
                    return;
                }
                String imgUrl = t.getOrg().getBranding().getLogo_256();
                GlideApp.with(CRInformationFragment.this).load(imgUrl)
                        .error(R.mipmap.found_vote_initial_circle).circleCrop().into(ivIcon);
                //获取节点简介
                NodeInfoBean.OrgBean.CandidateInfoBean infoBean = t.getOrg().getCandidate_info();
                if (infoBean != null) {
                    String info = new SPUtil(CRInformationFragment.this.getContext()).getLanguage() == 0 ? infoBean.getZh() : infoBean.getEn();

                    if (!TextUtils.isEmpty(info)) {
                        llTab.setVisibility(View.VISIBLE);
                        tvIntroDetail.setText(info);
                    }
                }

            }
        });
        tvName.setText(bean.getNickname());
        tvNumVote.setText(bean.getVotes().split("\\.")[0] + " " + getString(R.string.ticket));
        tvNodePublickey.setText(bean.getOwnerpublickey());
        tv_addrs.setText(AppUtlis.getLoc(getContext(), bean.getLocation() + ""));
        tvUrl.setText(bean.getUrl());
        if (bean.getVoterate() != null) {
            tvZl.setText(NumberiUtil.numberFormat(Double.parseDouble(bean.getVoterate()) * 100 + "", 5) + "%");
        }

        if (bean.isActive() == false) {
            sbJrhxlb.setEnabled(false);
            sbJrhxlb.setBackgroundColor(getResources().getColor(R.color.player_grey));
        }

        //  tvZl.setText(bean.getIp());
        list = CacheUtil.getCRProducerList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOwnerpublickey().equals(bean.getOwnerpublickey())) {
                    sbJrhxlb.setText(getString(R.string.remove_candidate_list));
                }
            }
        }
    }


    @OnClick({R.id.tv_url, R.id.sb_jrhxlb, R.id.sb_ckhxlb, R.id.ll_info, R.id.ll_intro})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_info:
                lineInfo.setVisibility(View.VISIBLE);
                lineIntro.setVisibility(View.GONE);
                tvInfo.setTextColor(getResources().getColor(R.color.whiter));
                tvIntro.setTextColor(getResources().getColor(R.color.whiter50));
                llInfodetail.setVisibility(View.VISIBLE);
                tvIntroDetail.setVisibility(View.GONE);
                break;
            case R.id.ll_intro:
                lineInfo.setVisibility(View.GONE);
                lineIntro.setVisibility(View.VISIBLE);
                tvInfo.setTextColor(getResources().getColor(R.color.whiter50));
                tvIntro.setTextColor(getResources().getColor(R.color.whiter));
                llInfodetail.setVisibility(View.GONE);
                tvIntroDetail.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_url:
                ClipboardUtil.copyClipboar(getBaseActivity(), tvUrl.getText().toString().trim());
                break;
            case R.id.sb_jrhxlb:
                //移除
                if (sbJrhxlb.getText().toString().equals(getString(R.string.remove_candidate_list))) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getOwnerpublickey().equals(bean.getOwnerpublickey())) {
                            ToastUtils.showShort(getString(R.string.yi_remove_candidate_list));
                            sbJrhxlb.setText(getString(R.string.candidate_list));
                            list.remove(i);
                            CacheUtil.setCRProducerList(list);
                        }
                    }
                    return;
                }

                if (sbJrhxlb.getText().toString().equals(getString(R.string.candidate_list))) {

                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(bean);
                    CacheUtil.setCRProducerList(list);
                    sbJrhxlb.setText(getString(R.string.remove_candidate_list));
                    ToastUtils.showShort(getString(R.string.candidate_list));
                    return;
                }


                break;
            case R.id.sb_ckhxlb:
                Bundle bundle = new Bundle();
                bundle.putString("zb", zb);
                bundle.putString("type", type);
                bundle.putSerializable("netList", (Serializable) netlist);
                start(CRNodeCartFragment.class, bundle);
                break;
        }
    }

}