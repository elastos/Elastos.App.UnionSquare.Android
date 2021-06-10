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

package org.elastos.wallet.ela.ui.committee;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.ElaWallet.MyWallet;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.db.RealmUtil;
import org.elastos.wallet.ela.db.table.SubWallet;
import org.elastos.wallet.ela.db.table.Wallet;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewBaseViewData;
import org.elastos.wallet.ela.ui.Assets.fragment.AddAssetFragment;
import org.elastos.wallet.ela.ui.committee.adaper.PastCtRecAdapter;
import org.elastos.wallet.ela.ui.committee.bean.CtDetailBean;
import org.elastos.wallet.ela.ui.committee.bean.PastCtBean;
import org.elastos.wallet.ela.ui.committee.fragment.CtListFragment;
import org.elastos.wallet.ela.ui.committee.fragment.CtManagerFragment;
import org.elastos.wallet.ela.ui.committee.fragment.SecretaryCtDetailFragment;
import org.elastos.wallet.ela.ui.committee.presenter.CtDetailPresenter;
import org.elastos.wallet.ela.ui.committee.presenter.CtManagePresenter;
import org.elastos.wallet.ela.ui.committee.presenter.PastCtPresenter;
import org.elastos.wallet.ela.ui.common.bean.CommmonStringEntity;
import org.elastos.wallet.ela.ui.common.bean.ISubWalletListEntity;
import org.elastos.wallet.ela.ui.common.listener.CommonRvListener;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.crvote.bean.CrStatusBean;
import org.elastos.wallet.ela.ui.crvote.fragment.CRManageFragment;
import org.elastos.wallet.ela.ui.crvote.presenter.CRlistPresenter;
import org.elastos.wallet.ela.ui.did.presenter.AddDIDPresenter;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.Arith;
import org.elastos.wallet.ela.utils.DialogUtil;
import org.elastos.wallet.ela.utils.NumberiUtil;
import org.elastos.wallet.ela.utils.listener.WarmPromptListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * List of past members 委员会的首页
 */
public class PastCtListFragment extends BaseFragment implements NewBaseViewData, CommonRvListener, PastCtRecAdapter.ManagerListener, OnLoadMoreListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    PastCtRecAdapter adapter;
    List<PastCtBean.DataBean> pastCtDataList;
    CtManagePresenter ctManagePresenter;
    CtDetailBean.DataBean itemCtDetail;//点击项目的届委员信息 other（没有参选）没有详情
    PastCtBean.DataBean itemPasrCtbean;//点击项目的届委员会状态
    CrStatusBean crStatusBean = null;//getRegistInfo获得
    private AddDIDPresenter addDIDPresenter;

    private RealmUtil realmUtil = new RealmUtil();
    private Wallet wallet = realmUtil.queryDefauleWallet();


    private Map<String, CtDetailBean.DataBean> dataBeanList = new HashMap<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ct_past_list;
    }


    @Override
    protected void initView(View view) {
        ivTitleRight.setImageResource(R.mipmap.found_ct_secretary_entrance);
        tvTitle.setText(mContext.getString(R.string.ctmemberlist));
        ctManagePresenter = new CtManagePresenter();
        addDIDPresenter = new AddDIDPresenter();
        new CtDetailPresenter().getCurrentCouncilInfo(this, wallet.getDid().replace("did:elastos:", ""), "crc");
        new PastCtPresenter().getCouncilTerm(this);
    }

    private void setRecycleView() {
        if (adapter == null) {
            adapter = new PastCtRecAdapter(getContext(), pastCtDataList, dataBeanList, wallet.getDid().replace("did:elastos:", ""));
            recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerview.setAdapter(adapter);
            adapter.setCommonRvListener(this);
            adapter.setManagerListener(this);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onGetData(String methodName, BaseEntity baseEntity, Object o) {
        switch (methodName) {
            case "getCouncilInfo":
                CtDetailBean.DataBean ctDetailBean = ((CtDetailBean) baseEntity).getData();
                ctDetailBean.setId((String) o);
                dataBeanList.put((String) o, ctDetailBean);
                if (dataBeanList.size() == pastCtDataList.size()) {
                    //获得了全部数据
                    setRcViewData(pastCtDataList, dataBeanList);
                }
                break;
            case "getCouncilTerm":
                //获得历届委员会信息
                pastCtDataList = ((PastCtBean) baseEntity).getData();
                //获得历届中当前用户的身份
                for (PastCtBean.DataBean dataBean : pastCtDataList) {
                    new CtDetailPresenter().getCouncilInfo(this, dataBean.getIndex() + "", wallet.getDid().replace("did:elastos:", ""));
                }

                break;
            case "getCurrentCouncilInfo":
                needDraw((CtDetailBean) baseEntity);
                setView((CtDetailBean) baseEntity);

                //  new PastCtPresenter().getCouncilTerm(this);
                break;
            case "getRegisteredCRInfo":
                crStatusBean = JSON.parseObject(((CommmonStringEntity) baseEntity).getData(), CrStatusBean.class);
                addDIDPresenter.getAllSubWallets(wallet.getWalletId(), this);
                break;

            case "getAllSubWallets":
                ISubWalletListEntity subWalletListEntity = (ISubWalletListEntity) baseEntity;
                for (SubWallet subWallet : subWalletListEntity.getData()) {
                    if (subWallet.getChainId().equals(MyWallet.IDChain)) {
//                        addDIDPresenter.getAllPublicKeys(wallet.getWalletId(), MyWallet.IDChain, 0, 1, this);
                        // Log.d("??????",status+"////");
                        Bundle bundle = new Bundle();
                        CrStatusBean.InfoBean info = crStatusBean.getInfo();
                        if (itemPasrCtbean.getStatus().equals("VOTING")) {
                            //只有选举中（VOTING）的届(未当选)
                            new CRlistPresenter().getCRlist(1, 1000, "all", this, true);
                        } else if ("UnelectedCouncilMember".equals(itemCtDetail.getType())) {
                            //不是选举期未当选且有质押金getRegisteredCRInfo获得的一定是此届的 只有提取质押金
                            bundle.putString("depositAmount", itemCtDetail.getDepositAmount());
                            bundle.putParcelable("crStatusBean", crStatusBean);
                            bundle.putParcelable("wallet", wallet);
                            bundle.putString("type", "CTLIST");
                            start(CRManageFragment.class, bundle);
                        } else {
                            //本届和历届  历届只有提取质押金  委员管理  只有委员才能进来
                            bundle.putString("name", info.getNickName());
                            bundle.putString("status", itemCtDetail.getStatus());
                            bundle.putString("cid", info.getCID());
                            bundle.putString("depositAmount", itemCtDetail.getDepositAmount());
                            bundle.putString("type", itemPasrCtbean.getStatus());//选中的届的状态  当前 历史 选举中
                            bundle.putString("did", info.getDID());
                            bundle.putString("dpospublickey", itemCtDetail.getDpospublickey());
                            start(CtManagerFragment.class, bundle);
                        }
                        return;
                    }
                }
                showOpenDIDWarm(subWalletListEntity);
                break;

            case "getCRlist":
                //只有选举中且参选才会到这里
                List<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean> curentAllList = ((CRListBean) baseEntity).getData().getResult().getCrcandidatesinfo();
                String did = wallet.getDid().replace("did:elastos:", "");
                String totalvotes = ((CRListBean) baseEntity).getData().getResult().getTotalvotes();
                //重置信息  获得当前节点详情  剔除非active数据
                resetData(curentAllList, totalvotes, did);
                break;
        }
    }

    /**
     * 重置信息  获得当前节点详情  剔除非active数据
     *
     * @param list
     * @param totalvotes
     */
    private void resetData(List<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean> list, String totalvotes, String did) {


        for (int i = 0; i < list.size(); i++) {
            //筛选当前节点
            CRListBean.DataBean.ResultBean.CrcandidatesinfoBean bean = list.get(i);
            bean.setIndex(i);
            setVoterate(bean, totalvotes);
            if (bean.getCid().equals(did)) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("crStatusBean", crStatusBean);
                bundle.putParcelable("curentNode", bean);
                start(CRManageFragment.class, bundle);
            }
        }


    }

    private void setVoterate(CRListBean.DataBean.ResultBean.CrcandidatesinfoBean bean, String totalvotes) {
        BigDecimal voterateDecimal = Arith.div(bean.getVotes(), totalvotes, 5);
        if (voterateDecimal.compareTo(new BigDecimal(0.01)) < 0) {
            bean.setVoterate("< 1");
        } else {
            String voterate = NumberiUtil.numberFormat(Arith.mul(voterateDecimal, 100), 2);
            bean.setVoterate(voterate);
        }


    }

    private void needDraw(CtDetailBean ctDetailBean) {
        CtDetailBean.DataBean dataBean = ctDetailBean.getData();
        String type = dataBean.getType();
        String name = dataBean.getDidName();
        String did = dataBean.getDid();
        String cid = dataBean.getCid();
        String status = dataBean.getStatus();
        String depositAmount = dataBean.getDepositAmount();
        String dpospublickey = dataBean.getDpospublickey();

        // 需要提取质押金的情况
        if (dataBean != null) {
            if (!AppUtlis.isNullOrEmpty(status)
                    && !AppUtlis.isNullOrEmpty(depositAmount)
                    && !depositAmount.equalsIgnoreCase("0")) {
                if (status.equals("Terminated")
                        || status.equals("Impeached")
                        || status.equals("Returned")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    bundle.putString("name", name);
                    bundle.putString("did", did);
                    bundle.putString("cid", cid);
                    bundle.putString("status", status);
                    bundle.putString("depositAmount", depositAmount);
                    bundle.putString("dpospublickey", dpospublickey);
                    start(CtManagerFragment.class, bundle);
                }
            }
        }

        //TODO test code
//        Bundle bundle = new Bundle();
//        bundle.putString("type", "other");
//        bundle.putString("name", "test");
//        bundle.putString("did", "xxxxxxxxxxxxxxxx");
//        bundle.putString("cid", "xxxxxxxxxxxxxxxx");
//        bundle.putString("status", "Impeached");
//        bundle.putString("depositAmount", "10000");
//        start(CtManagerFragment.class, bundle);
    }

    private void showOpenDIDWarm(ISubWalletListEntity subWalletListEntity) {
        new DialogUtil().showCommonWarmPrompt(getBaseActivity(), getString(R.string.noidchainopenornot),
                getString(R.string.toopen), getString(R.string.cancel), false, new WarmPromptListener() {
                    @Override
                    public void affireBtnClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("walletId", wallet.getWalletId());
                        ArrayList<String> chainIds = new ArrayList<>();
                        for (SubWallet iSubWallet : subWalletListEntity.getData()) {
                            chainIds.add(iSubWallet.getChainId());
                        }
                        bundle.putStringArrayList("chainIds", chainIds);
                        start(AddAssetFragment.class, bundle);
                    }
                });
    }

    private void setRcViewData(List<PastCtBean.DataBean> datas, Map<String, CtDetailBean.DataBean> dataBeans) {
        if (datas == null || datas.size() <= 0) return;
        if (dataBeans == null || dataBeans.size() <= 0) return;
        Collections.reverse(pastCtDataList);
        setRecycleView();
    }

    @Override
    public void onRvItemClick(int index, Object o) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        start(CtListFragment.class, bundle);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    @Override
    public void onManagerClick(int position, PastCtBean.DataBean data, CtDetailBean.DataBean dataBean) {

        this.itemCtDetail = dataBean;
        this.itemPasrCtbean = data;
        ctManagePresenter.getRegisteredCRInfo(wallet.getWalletId(), MyWallet.ELA, this);
    }

    @OnClick({R.id.iv_title_right})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("id", "");
        bundle.putString("did", wallet.getDid().replace("did:elastos:", ""));
        start(SecretaryCtDetailFragment.class, bundle);
    }

    private void setView(CtDetailBean ctDetailBean) {
        CtDetailBean.DataBean dataBean = ctDetailBean.getData();
        String type = dataBean.getType();
        if (!AppUtlis.isNullOrEmpty(type) && type.equalsIgnoreCase("SecretaryGeneral")) {
            ivTitleRight.setVisibility(View.VISIBLE);
        }

    }
}
