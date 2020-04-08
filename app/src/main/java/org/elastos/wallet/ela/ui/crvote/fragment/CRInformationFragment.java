package org.elastos.wallet.ela.ui.crvote.fragment;


import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperButton;
import com.blankj.utilcode.util.ToastUtils;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewBaseViewData;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.crvote.presenter.CRManagePresenter;
import org.elastos.wallet.ela.ui.did.entity.CredentialSubjectBean;
import org.elastos.wallet.ela.ui.did.entity.GetJwtRespondBean;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.CacheUtil;
import org.elastos.wallet.ela.utils.ClipboardUtil;
import org.elastos.wallet.ela.utils.svg.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 节点信息
 */
public class CRInformationFragment extends BaseFragment implements NewBaseViewData {


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
    CRListBean.DataBean.ResultBean.CrcandidatesinfoBean bean;
    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_num_vote)
    TextView tvNumVote;
    @BindView(R.id.tv_zl)
    TextView tvZl;
    @BindView(R.id.tv_addrs)
    TextView tv_addrs;
    List<String> list;
    public String type;
    @BindView(R.id.tv_did)
    TextView tvDid;
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
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    private String DID;
    private CredentialSubjectBean credentialSubjectBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cr_information;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void setExtraData(Bundle data) {
        bean = (CRListBean.DataBean.ResultBean.CrcandidatesinfoBean) data.getSerializable("curentBean");
    }

    @Override
    protected void initView(View view) {
        setToobar(toolbar, toolbarTitle, getString(R.string.crinfor));
        tvName.setText(bean.getNickname());
        tvNumVote.setText(bean.getVotes().split("\\.")[0] + " " + getString(R.string.ticket));
        if (!TextUtils.isEmpty(bean.getCid())) {
            DID = "did:elastos:" + bean.getCid();
            tvDid.setText(DID);
            //从服务器获得凭证信息
            new CRManagePresenter().jwtGet(DID, this);
        } else {
            tvDid.setText(getString(R.string.unactive));
            tvDid.setCompoundDrawables(null, null, null, null);
        }
        tv_addrs.setText(AppUtlis.getLoc(getContext(), bean.getLocation() + ""));
        tvUrl.setText(bean.getUrl());
        tvZl.setText(bean.getVoterate() + "%");
        list = CacheUtil.getCRProducerListString();
        if (list.contains(bean.getDid())) {
            sbJrhxlb.setText(getString(R.string.remove_candidate_list));
        }
        tvIntroDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    @OnClick({R.id.tv_url, R.id.sb_jrhxlb, R.id.sb_ckhxlb, R.id.ll_info, R.id.ll_intro, R.id.iv_detail, R.id.tv_did})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_did:
                if (!TextUtils.isEmpty(DID))
                    ClipboardUtil.copyClipboar(getBaseActivity(), tvDid.getText().toString());
                break;
            case R.id.iv_detail:
                Bundle bundle = new Bundle();
                bundle.putParcelable("credentialSubjectBean", credentialSubjectBean);
                start(CredentialInfoFragemnt.class, bundle);
                break;
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
                if (list.contains(bean.getDid())) {
                    //移除
                    list.remove(bean.getDid());
                    ToastUtils.showShort(getString(R.string.yi_remove_candidate_list));
                    sbJrhxlb.setText(getString(R.string.candidate_list));
                } else {
                    list.add(bean.getDid());
                    ToastUtils.showShort(getString(R.string.candidate_list));
                    sbJrhxlb.setText(getString(R.string.remove_candidate_list));
                }
                CacheUtil.setCRProducerListString(list);
                break;
            case R.id.sb_ckhxlb:
                start(CRNodeCartFragment.class, getArguments());
                break;
        }
    }

    @Override
    public void onGetData(String methodName, BaseEntity baseEntity, Object o) {
        switch (methodName) {
            case "jwtGet":
                GetJwtRespondBean getJwtRespondBean = (GetJwtRespondBean) baseEntity;
                String jwt = getJwtRespondBean.getData().getJwt();
                if (!TextUtils.isEmpty(jwt)) {
                    String[] jwtParts = jwt.split("\\.");
                    String payload = new String(Base64.decode(jwtParts[1], Base64.URL_SAFE));
                    String pro = getMyDID().getCredentialProFromJson(payload);
                    credentialSubjectBean = JSON.parseObject(pro, CredentialSubjectBean.class);
                    if (credentialSubjectBean == null || credentialSubjectBean.whetherEmpty()) {
                        return;
                    }
                    GlideApp.with(CRInformationFragment.this).load(credentialSubjectBean.getAvatar())
                            .error(R.mipmap.found_vote_initial_circle).circleCrop().into(ivIcon);
                    ivDetail.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(credentialSubjectBean.getIntroduction())) {
                        llTab.setVisibility(View.VISIBLE);
                        tvIntroDetail.setText(credentialSubjectBean.getIntroduction());
                    }
                }
                break;
        }
    }
}
