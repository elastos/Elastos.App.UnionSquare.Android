package org.elastos.wallet.ela.ui.Assets;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.elastos.did.DIDDocument;
import org.elastos.wallet.R;
import org.elastos.wallet.ela.ElaWallet.MyWallet;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.bean.BusEvent;
import org.elastos.wallet.ela.db.RealmUtil;
import org.elastos.wallet.ela.db.table.SubWallet;
import org.elastos.wallet.ela.db.table.Wallet;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewBaseViewData;
import org.elastos.wallet.ela.ui.Assets.adapter.AssetskAdapter;
import org.elastos.wallet.ela.ui.Assets.bean.BalanceEntity;
import org.elastos.wallet.ela.ui.Assets.bean.RecieveJwtEntity;
import org.elastos.wallet.ela.ui.Assets.fragment.AddAssetFragment;
import org.elastos.wallet.ela.ui.Assets.fragment.AssetDetailsFragment;
import org.elastos.wallet.ela.ui.Assets.fragment.CreateSignReadOnlyWalletFragment;
import org.elastos.wallet.ela.ui.Assets.fragment.TransferFragment;
import org.elastos.wallet.ela.ui.Assets.fragment.WalletListFragment;
import org.elastos.wallet.ela.ui.Assets.fragment.WalletManageFragment;
import org.elastos.wallet.ela.ui.Assets.fragment.mulsignwallet.CreateMulWalletFragment;
import org.elastos.wallet.ela.ui.Assets.fragment.transfer.SignFragment;
import org.elastos.wallet.ela.ui.Assets.listener.ISubWalletListener;
import org.elastos.wallet.ela.ui.Assets.presenter.AssetsPresenter;
import org.elastos.wallet.ela.ui.Assets.presenter.CommonGetBalancePresenter;
import org.elastos.wallet.ela.ui.Assets.presenter.TransferPresenter;
import org.elastos.wallet.ela.ui.Assets.presenter.WalletManagePresenter;
import org.elastos.wallet.ela.ui.Assets.viewdata.AssetsViewData;
import org.elastos.wallet.ela.ui.Assets.viewdata.CommonBalanceViewData;
import org.elastos.wallet.ela.ui.common.bean.CommmonObjEntity;
import org.elastos.wallet.ela.ui.common.listener.CommonRvListener1;
import org.elastos.wallet.ela.ui.common.viewdata.CommmonStringWithMethNameViewData;
import org.elastos.wallet.ela.ui.did.fragment.AuthorizationFragment;
import org.elastos.wallet.ela.ui.main.MainActivity;
import org.elastos.wallet.ela.ui.mine.bean.MessageEntity;
import org.elastos.wallet.ela.ui.mine.fragment.MessageListFragment;
import org.elastos.wallet.ela.utils.CacheUtil;
import org.elastos.wallet.ela.utils.Constant;
import org.elastos.wallet.ela.utils.Log;
import org.elastos.wallet.ela.utils.MyUtil;
import org.elastos.wallet.ela.utils.QrBean;
import org.elastos.wallet.ela.utils.RxEnum;
import org.elastos.wallet.ela.utils.SPUtil;
import org.elastos.wallet.ela.utils.ScanQRcodeUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * 资产首页
 */
public class AssetskFragment extends BaseFragment implements AssetsViewData, CommonRvListener1, ISubWalletListener, OnRefreshListener, CommonBalanceViewData, CommmonStringWithMethNameViewData, NewBaseViewData {

    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private AssetskAdapter assetskAdapter;
    private AssetsPresenter assetsPresenter;
    private RealmUtil realmUtil;
    private Wallet wallet;
    private CommonGetBalancePresenter commonGetBalancePresenter;
    private Map<String, List<org.elastos.wallet.ela.db.table.SubWallet>> listMap;
    private Map<String, String> transactionMap;
    private String scanResult;

    @Override
    protected int getLayoutId() {
        initClassicsHeader();
        return R.layout.fragment_assetsk;
    }

    @Override

    public void onSaveInstanceState(Bundle outState) {
        realmUtil.updateSubWalletDetial(listMap);
        CacheUtil.setUnReadMessage(messageList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void setExtraData(Bundle data) {
        String toValue = data.getString("toValue");
        if ("notice".equals(toValue)) {
            ((BaseFragment) getParentFragment()).start(MessageListFragment.class);
        }

    }

    @Override
    protected void initView(View view) {
        srl.setOnRefreshListener(this);
        assetsPresenter = new AssetsPresenter();
        commonGetBalancePresenter = new CommonGetBalancePresenter();
        realmUtil = new RealmUtil();
        wallet = realmUtil.queryDefauleWallet();
        getMyDID().init(wallet.getWalletId());//初始化mydid
        tvTitle.setText(wallet.getWalletName());
        setWalletView(wallet);
        List<Wallet> wallets = realmUtil.queryUserAllWallet();
        listMap = new HashMap<>();
        transactionMap = new HashMap<>();
        for (Wallet wallet : wallets) {
            assetsPresenter.getAllSubWallets(wallet.getWalletId(), this);
        }
        registReceiver();
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.iv_add, R.id.tv_title, R.id.iv_scan})
    public void onViewClicked(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.iv_title_left:
            case R.id.tv_title:
                //钱包列表
                ((BaseFragment) getParentFragment()).start(new WalletListFragment());
                break;
            case R.id.iv_title_right:
                //钱包管理
                bundle = new Bundle();
                bundle.putParcelable("wallet", wallet);
                bundle.putParcelableArrayList("subWallets", (ArrayList<? extends Parcelable>) listMap.get(wallet.getWalletId()));

                ((BaseFragment) getParentFragment()).start(WalletManageFragment.class, bundle);
                break;
            case R.id.iv_scan:
                //扫一扫
                requstManifestPermission(getString(R.string.needpermission));

                break;
            case R.id.iv_add:
                bundle = new Bundle();
                bundle.putString("walletId", wallet.getWalletId());
                ArrayList<String> chainIds = new ArrayList<>();
                List<org.elastos.wallet.ela.db.table.SubWallet> assetList = listMap.get(wallet.getWalletId());
                for (org.elastos.wallet.ela.db.table.SubWallet iSubWallet : assetList) {
                    chainIds.add(iSubWallet.getChainId());
                }
                bundle.putStringArrayList("chainIds", chainIds);
                ((BaseFragment) getParentFragment()).start(AddAssetFragment.class, bundle);
                break;

        }
    }

    public static AssetskFragment newInstance() {
        Bundle args = new Bundle();
        AssetskFragment fragment = new AssetskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void requstPermissionOk() {
        new ScanQRcodeUtil().scanQRcode(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK && requestCode == ScanQRcodeUtil.SCAN_QR_REQUEST_CODE && data != null) {
            scanResult = data.getStringExtra("result");//&& matcherUtil.isMatcherAddr(result)
            if (!TextUtils.isEmpty(scanResult) /*&& matcherUtil.isMatcherAddr(result)*/) {
                if (scanResult.startsWith("elastos:")) {
                    //兼容elastos:
                    scanElastos(scanResult);
                } else {
                    scanQrBean(scanResult);
                }

            }
        }

    }

    private void scanQrBean(String result) {
        try {
            QrBean qrBean = JSON.parseObject(result, QrBean.class);
            int type = qrBean.getExtra().getType();
            Bundle bundle = new Bundle();

            switch (type) {
                case Constant.TRANSFER:
                    //扫描联系人到转账页面
                    bundle.putParcelable("wallet", wallet);
                    bundle.putString("ChainID", qrBean.getExtra().getSubWallet());
                    bundle.putString("address", qrBean.getData());
                    ((BaseFragment) getParentFragment()).start(TransferFragment.class, bundle);
                    break;
                case Constant.CREATEREADONLY:
                    //创建只读钱包
                    bundle.putString("result", result);
                    ((BaseFragment) getParentFragment()).start(CreateSignReadOnlyWalletFragment.class, bundle);
                    break;
                case Constant.CREATEMUL:
                    //创建多签钱包
                    bundle.putString("result", result);
                    ((BaseFragment) getParentFragment()).start(CreateMulWalletFragment.class, bundle);
                    break;
                case Constant.SIGN:
                    //去签名
                    //数据完整后跳转//如果是其他数据  用新的数据
                    String attribute = getData(qrBean, Constant.SIGN);
                    if (!TextUtils.isEmpty(attribute)) {
                        bundle.putParcelable("wallet", wallet);
                        bundle.putInt("transType", qrBean.getExtra().getTransType());
                        bundle.putString("attributes", attribute);
                        ((BaseFragment) getParentFragment()).start(SignFragment.class, bundle);
                    }
                    break;
                default:
                    toErroScan(result);
                    break;
            }
        } catch (Exception e) {
            toErroScan(result);
        }
    }

    private void scanElastos(String result) {
        result = result.replace("elastos:", "");
        if (result.startsWith("//credaccess/")) {
            //did
            decodeJwt(result);

        } else {
            //钱包转账地址情况 elastos:EJQcgWDazveSy436TauPJ3R8PCYpifp6HA?amount=6666.00000000
            String[] parts = result.split("\\?");
            diposeElastosCaode(new TransferPresenter().analyzeElastosData(parts, wallet.getWalletId(), this), parts);

        }
    }

    private void decodeJwt(String result) {
        try {
            ////0 普通单签 1单签只读 2普通多签 3多签只读
            if (wallet.getType() != 0) {
                toErroScan(scanResult);
            }
            result = result.replace("//credaccess/", "");
            String[] jwtParts = result.split("\\.");
            // String base64Header = jwtParts[0];
            String base64Payload = jwtParts[1];
            // String unSignature = base64Header + "." + base64Payload;
            //  String signature = jwtParts[2];
            //  String header = new String(org.elastos.did.util.Base64.decode(base64Header));
            String payload = new String(Base64.decode(base64Payload, Base64.DEFAULT));
            RecieveJwtEntity recieveJwtEntity = JSON.parseObject(payload, RecieveJwtEntity.class);
            String elaString = recieveJwtEntity.getIss();
            elaString = elaString.contains("did:elastos:") ? elaString : "did:elastos:" + elaString;
            // Log.e("Base64", "Base64---->" + header + "\n" + payload + "\n" + signature);.0
            new WalletManagePresenter().DIDResolve(elaString, this);
        } catch (Exception e) {
            toErroScan(scanResult);
        }


    }

    private void setWalletView(Wallet wallet) {
        tvTitle.setText(wallet.getWalletName());
        switch (wallet.getType()) {
            //0 普通单签 1单签只读 2普通多签 3多签只读
            case 0:
                ivTitleLeft.setImageResource(R.mipmap.single_wallet);
                break;
            case 1:
                ivTitleLeft.setImageResource(R.mipmap.single_walllet_readonly);
                break;
            case 2:
                ivTitleLeft.setImageResource(R.mipmap.multi_wallet);
                break;
            case 3:
                ivTitleLeft.setImageResource(R.mipmap.multi_wallet_readonly);
                break;
        }
    }

    /**
     * 这里调用getAllSubWallets会刷新recycleview
     *
     * @param wallet
     */
    private void setWalletViewNew(Wallet wallet) {
        setWalletView(wallet);
        assetsPresenter.getAllSubWallets(wallet.getWalletId(), this);
    }


    private void setRecycleView() {
        List<org.elastos.wallet.ela.db.table.SubWallet> assetList = listMap.get(wallet.getWalletId());
        if (assetList == null || assetList.size() == 0) {
            return;
        }
        if (assetskAdapter == null) {
            recyclerview.setAdapter(assetskAdapter = new AssetskAdapter(getContext(), assetList));
            recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerview.setHasFixedSize(true);
            recyclerview.setNestedScrollingEnabled(false);
            recyclerview.setFocusableInTouchMode(false);
            assetskAdapter.setCommonRvListener(this);
        } else {
            assetskAdapter.setData(assetList);
            assetskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public synchronized void onGetAllSubWallets(List<SubWallet> data, int type) {
        if (data == null || data.size() == 0) {
            return;
        }
        String currentBelongId = data.get(0).getBelongId();
        List<SubWallet> assetList = listMap.get(currentBelongId);//原来的数据
        listMap.put(currentBelongId, data);
        for (SubWallet newSubWallet : data) {

            First:
            {
                if (assetList != null && assetList.size() != 0) {
                    for (SubWallet oldSubWallet : assetList) {
                        if (newSubWallet.equals(oldSubWallet)) {
                            //原有的数据保留
                            newSubWallet.setProgress(oldSubWallet.getProgress());
                            newSubWallet.setSyncTime(oldSubWallet.getSyncTime());
                            break First;

                        }

                    }
                    assetsPresenter.syncStart(currentBelongId, newSubWallet.getChainId(), this);
                    SubWallet subWallet = realmUtil.querySubWallet(newSubWallet.getBelongId(), newSubWallet.getChainId());
                    newSubWallet.setProgress(subWallet.getProgress());
                    newSubWallet.setSyncTime(subWallet.getSyncTime());
                    assetsPresenter.registerWalletListener(currentBelongId, newSubWallet.getChainId(), this);
                } else {
                    assetsPresenter.syncStart(currentBelongId, newSubWallet.getChainId(), this);
                    SubWallet subWallet = realmUtil.querySubWallet(newSubWallet.getBelongId(), newSubWallet.getChainId());
                    newSubWallet.setProgress(subWallet.getProgress());
                    newSubWallet.setSyncTime(subWallet.getSyncTime());
                    assetsPresenter.registerWalletListener(currentBelongId, newSubWallet.getChainId(), this);
                }
            }

        }


        if (wallet.getWalletId().equals(currentBelongId)) {
            setRecycleView();
        }
    }

    @Override
    public void onBalance(BalanceEntity data) {

        List<org.elastos.wallet.ela.db.table.SubWallet> assetList = listMap.get(data.getMasterWalletId());
        for (org.elastos.wallet.ela.db.table.SubWallet assetsItemEntity : assetList) {
            if (assetsItemEntity.getChainId().equals(data.getChainId())) {
                assetsItemEntity.setBalance(data.getBalance());
                if (wallet.getWalletId().equals(data.getMasterWalletId())) {
                    post(RxEnum.BALANCECHANGE.ordinal(), null, assetsItemEntity);
                }
            }

        }
    }

    @Override
    public void onRvItemClick(View view, int position, Object o) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("wallet", wallet);
        for (SubWallet subWallet : listMap.get(wallet.getWalletId())) {
            if (subWallet.getChainId().equals((String) o)) {
                bundle.putParcelable("subWallet", subWallet);
            }
        }

        ((BaseFragment) getParentFragment()).start(AssetDetailsFragment.class, bundle);
    }

    /***************ISubWalletListener子钱包同步的监听*****************/
    @Override
    public void OnTransactionStatusChanged(JSONObject jsonObject) {
        try {
            int confirms = jsonObject.getInt("confirms");
            String hash = jsonObject.getString("txId");
            if (confirms == 0) {
                //did发布和更新
                if (hash.equals(getMyDID().getMyDIDAdapter().getTxId())) {
                    getMyDID().getMyDIDAdapter().getCallback().accept(hash, 0, null);
                }

                return;
            }
            String transferType = transactionMap.get(hash);
            if (TextUtils.isEmpty(transferType)) {
                return;
            }
            //1002是投票覆盖其他投票的特殊投票类型  在此成功时候给出提醒
            String reason;
            if ("1002".equals(transferType)) {
                reason = String.format(getString(R.string.specialvotesucessreason), getString(R.string.crcvote));

            } else if ("1003".equals(transferType)) {
                reason = String.format(getString(R.string.specialvotesucessreason), getString(R.string.supernode_election));

            } else {
                return;
            }
            //只需要一次成功的提醒
            transactionMap.remove(hash);

            String chainId = jsonObject.getString("ChainID");
            String masterWalletID = jsonObject.getString("MasterWalletID");
            String transferTypeDes = getTransferDes(transferType, chainId);
            String walleName = realmUtil.queryUserWallet(masterWalletID).getWalletName();
            showNotification(reason, transferTypeDes, walleName);
            addToMessageCenter(hash, transferType, chainId, walleName, reason);

        } catch (Exception e) {
            Log.i(getClass().getSimpleName(), e.getMessage());
        }

    }

    @Override
    public void OnBlockSyncStarted(JSONObject jsonObject) {

        //   Log.e("???", jsonObject.toString());
    }

    @Override
    public synchronized void OnBlockSyncProgress(JSONObject jsonObject) {

        try {
            long BytesPerSecond = jsonObject.getLong("BytesPerSecond");
            String DownloadPeer = jsonObject.getString("DownloadPeer");
            String MasterWalletID = jsonObject.getString("MasterWalletID");
            long lastBlockTime = jsonObject.getLong("LastBlockTime");
            String ChainID = jsonObject.getString("ChainID");
            //同步进行中
            int progress = jsonObject.getInt("Progress");
            List<org.elastos.wallet.ela.db.table.SubWallet> assetList = listMap.get(MasterWalletID);
            if (assetList == null) {
                return;
            }
            for (org.elastos.wallet.ela.db.table.SubWallet subWallet : assetList) {
                if (ChainID.equals(subWallet.getChainId())) {
                    subWallet.setBytesPerSecond(BytesPerSecond);
                    subWallet.setDownloadPeer(DownloadPeer);
                    subWallet.setProgress(progress);
                    if (lastBlockTime != 0) {
                        // String curentTime = DateUtil.time(lastBlockTime);
                        subWallet.setSyncTime(lastBlockTime + "");
                        subWallet.setFiled1("Connected");
                    }
                    subWallet.setProgress(progress);
                    if (progress == 100) {
                        subWallet.setFiled2("true");
                    }
                    if (wallet.getWalletId().equals(MasterWalletID)) {
                        post(RxEnum.UPDATAPROGRESS.ordinal(), null, subWallet);
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnBlockSyncStopped(JSONObject jsonObject) {
        //同步完成  记录时间并更新
        // Log.e("???", jsonObject.toString());
    }

    @Override
    public synchronized void OnBalanceChanged(JSONObject jsonObject) {

        try {
            String MasterWalletID = jsonObject.getString("MasterWalletID");
            String ChainID = jsonObject.getString("ChainID");
            long balance = jsonObject.getLong("Balance");
            List<org.elastos.wallet.ela.db.table.SubWallet> assetList = listMap.get(MasterWalletID);
            if (assetList == null) {
                return;
            }
            for (org.elastos.wallet.ela.db.table.SubWallet assetsItemEntity : assetList) {
                if (ChainID.equals(assetsItemEntity.getChainId())) {
                    assetsItemEntity.setBalance(balance + "");
                    if (wallet.getWalletId().equals(MasterWalletID)) {
                        post(RxEnum.BALANCECHANGE.ordinal(), null, assetsItemEntity);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static List<MessageEntity> messageList;

    @Override
    public synchronized void OnTxPublished(JSONObject jsonObject) {

        try {
            String hash = jsonObject.getString("hash");
            String transferType = transactionMap.get(hash);
            if (TextUtils.isEmpty(transferType)) {
                return;
            }
            String resultString = jsonObject.getString("result");
            JSONObject result = new JSONObject(resultString);
            int code = result.getInt("Code");
            String reason = result.getString("Reason");
            //只捕捉失败的回调
            if (code == 0 || (code == 18 && reason.contains("uplicate"))) {
                return;
            }
            if (hash.equals(getMyDID().getMyDIDAdapter().getTxId())) {
                getMyDID().getMyDIDAdapter().getCallback().accept(hash, code, reason);
            }
            //只需要捕捉一次失败
            transactionMap.remove(hash);
            String chainId = jsonObject.getString("ChainID");
            String masterWalletID = jsonObject.getString("MasterWalletID");
            String transferTypeDes = getTransferDes(transferType, chainId);
            String walleName = realmUtil.queryUserWallet(masterWalletID).getWalletName();
            reason = getString(R.string.commonfailereason);
            showNotification(reason, transferTypeDes, walleName);
            addToMessageCenter(hash, transferType, chainId, walleName, reason);

        } catch (Exception e) {
            Log.i(getClass().getSimpleName(), e.getMessage());
        }
    }

    private String getTransferDes(String transferType, String chainId) {
        String transferTypeDes = getString(R.string.transfertype13);
        try {
            if (chainId.equals(MyWallet.IDChain)) {
                transferTypeDes = getString(getContext().getResources().getIdentifier("sidetransfertype" + transferType, "string",
                        getContext().getPackageName()));
            } else {
                transferTypeDes = getString(getContext().getResources().getIdentifier("transfertype" + transferType, "string",
                        getContext().getPackageName()));
            }
        } catch (Exception e) {
            Log.i("transferTypeDes", e.getMessage());
        }
        return transferTypeDes;
    }

    @Override
    public void OnTxDeleted(JSONObject jsonObject) {
        //  Log.e("???", jsonObject.toString());
    }

    @Override
    public void OnAssetRegistered(JSONObject jsonObject) {

    }

    @Override
    public synchronized void OnConnectStatusChanged(JSONObject jsonObject) {
        //"Connecting", "Connected", "Disconnected"
        // jsonObject.put("status", status);
        //            jsonObject.put("MasterWalletID", mMasterWalletID);
        //            jsonObject.put("ChainID", mSubWalletID);
        //            jsonObject.put("Action", "OnConnectStatusChanged");
        try {
            String MasterWalletID = jsonObject.getString("MasterWalletID");
            String ChainID = jsonObject.getString("ChainID");
            String status = jsonObject.getString("status");
            List<org.elastos.wallet.ela.db.table.SubWallet> assetList = listMap.get(MasterWalletID);
            if (assetList == null) {
                return;
            }
            for (org.elastos.wallet.ela.db.table.SubWallet subWallet : assetList) {
                if (ChainID.equals(subWallet.getChainId())) {
                    subWallet.setFiled1(status);
                    if (wallet.getWalletId().equals(MasterWalletID)) {
                        post(RxEnum.UPDATAPROGRESS.ordinal(), null, subWallet);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        onErrorRefreshLayout(srl);
        List<org.elastos.wallet.ela.db.table.SubWallet> assetList = listMap.get(wallet.getWalletId());
        for (org.elastos.wallet.ela.db.table.SubWallet assetsItemEntity : assetList) {
            commonGetBalancePresenter.getBalance(wallet.getWalletId(), assetsItemEntity.getChainId(), 2, this);
            assetsPresenter.syncStart(wallet.getWalletId(), assetsItemEntity.getChainId(), this);
        }

    }

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        return closeApp();
    }

    /**
     * eventbus监听
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BusEvent result) {
        int integer = result.getCode();
        if (integer == RxEnum.WALLETUPDATE.ordinal()) {
            //切换钱包
            Wallet temp = (Wallet) result.getObj();
            if (!wallet.getWalletId().equals(temp.getWalletId())) {
                wallet = temp;
                getMyDID().init(wallet.getWalletId());//初始化mydid
                setWalletView(wallet);
                setRecycleView();
                if (dataMap != null && dataMap.size() > 0) {
                    dataMap.clear();
                }
            }

        }
        if (integer == RxEnum.ONE.ordinal()) {
            //创建钱包
            Wallet temp = (Wallet) result.getObj();
            if (!wallet.getWalletId().equals(temp.getWalletId())) {
                wallet = temp;
                setWalletViewNew(wallet);
            }

        }
        if (integer == RxEnum.UPDATA_WALLET_NAME.ordinal()) {
            String walletId = (String) result.getObj();
            if (wallet.getWalletId().equals(walletId)) {
                wallet.setWalletName(result.getName());
                tvTitle.setText(result.getName());
            }
        }
        if (integer == RxEnum.DELETE.ordinal()) {
            //删除
            String walletId = (String) result.getObj();
            listMap.remove(walletId);
            if (wallet.getWalletId().equals(walletId)) {
                wallet = realmUtil.queryDefauleWallet();
                getMyDID().init(wallet.getWalletId());//初始化mydid
                setWalletView(wallet);
                setRecycleView();
            }
        }
        if (integer == RxEnum.UPDATAPROPERTY.ordinal()) {
            //子钱包改变  创建或删除
            setWalletViewNew(wallet);
        }
        if (integer == RxEnum.UPDATAPROGRESS.ordinal()) {
            //progress改变
            setRecycleView();
        }
        if (integer == RxEnum.BALANCECHANGE.ordinal()) {
            //资产改变
            setRecycleView();
        }
        if (integer == RxEnum.TRANSFERSUCESS.ordinal()) {
            //交易发生出去

            transactionMap.put((String) result.getObj(), result.getName());
        }

    }


    @Override
    public void onDestroy() {
        //存储所有同步状态
        realmUtil.updateSubWalletDetial(listMap);
        CacheUtil.setUnReadMessage(messageList);
        super.onDestroy();
    }


    private int currentType = -1;
    private Map<Integer, String> dataMap;//用于存储二维码信息

    private String getData(QrBean qrBean, int type) {
        if (dataMap == null) {
            dataMap = new TreeMap<>();
        }
        if (type != currentType) {
            currentType = type;
            dataMap.clear();
        }

        String mydata = qrBean.getData();
        int max = qrBean.getTotal();
        int current = qrBean.getIndex();
        dataMap.put(current, mydata);
        if (dataMap.size() == max) {
            StringBuilder signData = new StringBuilder();
            for (String s : dataMap.values()) {
                signData.append(s);
            }
            currentType = -1;
            dataMap.clear();
            return signData.toString();
        }
        String msg = String.format(getContext().getString(R.string.scanprocess), dataMap.size() + "/" + max);
        showToast(msg);
        requstManifestPermission(getString(R.string.needpermission));

        return null;
    }

    private void toErroScan(String result) {
        if (dataMap != null) {
            dataMap.clear();
        }
        currentType = -1;
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        ((BaseFragment) getParentFragment()).start(ErrorScanFragment.class, bundle);
    }

    private void diposeElastosCaode(int analyzeElastosData, String[] parts) {
        Bundle bundle = new Bundle();
        switch (analyzeElastosData) {
            case 0:
                toErroScan("elastos:" + parts[0] + (parts.length > 1 ? "" : ("?" + parts[1])));
                break;
            case 2:
                bundle.putString("amount", parts[1].replace("amount=", ""));
            case 1:
                bundle.putParcelable("wallet", wallet);
                bundle.putString("ChainID", MyWallet.ELA);
                bundle.putString("address", parts[0]);
                ((BaseFragment) getParentFragment()).start(TransferFragment.class, bundle);
                break;
        }
    }

    private void addToMessageCenter(String hash, String transferType, String chainId, String walleName, String reason) {
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setReason(reason);
        messageEntity.setTransferType(transferType);
        messageEntity.setWalletName(walleName);
        messageEntity.setTime(new Date().getTime() / 1000);
        messageEntity.setHash(hash);
        messageEntity.setChainId(chainId);
        messageList.remove(messageEntity);
        messageList.add(messageEntity);
        post(RxEnum.NOTICE.ordinal(), null, null);
    }

    private void showNotification(String reason, String transferTypeDes, String walleName) {
        SPUtil sp = new SPUtil(getContext());
        if (!sp.isOpenSendMsg()) {
            return;
        }
        Intent intent = new Intent(getContext(),
                MainActivity.class);//代表fragment所绑定的activity，这个需要写全路径
        intent.putExtra("toValue", "notice");//传递参数，然后根据参数进行判断需要跳转的fragment界面

        PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        //需添加的代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
        Notification notification = new NotificationCompat.Builder(getContext(), "default")
                .setContentTitle(MyUtil.getAppName(getContext()))
                .setContentText("【" + walleName + getString(R.string.wallet) + "】" + transferTypeDes + " - " + getString(R.string.transactionfinish) + ", " + reason + ".")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.icon_ela)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_ela))
                .build();
        manager.notify((int) System.currentTimeMillis(), notification);
    }

    @Override
    public void onGetData(String methodName, BaseEntity baseEntity, Object o) {
        switch (methodName) {
            case "DIDResolve":
                //未传递paypas网站提供的did验签
                verifyDID((DIDDocument) ((CommmonObjEntity) baseEntity).getData());


                break;
            case "DIDResolveWithTip":
                //传递paypas是判断当前钱包有没有did
                curentHasDID((DIDDocument) ((CommmonObjEntity) baseEntity).getData());
                break;
        }
    }

    private void verifyDID(DIDDocument didDocument) {
        if (didDocument == null) {
            toErroScan(scanResult);
        } else {
            //验签
            String result = scanResult.replace("elastos://credaccess/", "");
            String[] jwtParts = result.split("\\.");
            String unSignature = jwtParts[0] + "." + jwtParts[1];
            String signature = jwtParts[2];
            if (didDocument.verify(signature, unSignature.getBytes())) {
                // 验签成功
                // 验签成功
                //先判断本地是否有did
                new WalletManagePresenter().DIDResolveWithTip(wallet.getDid(), this);
            } else {
                //验签失败
                toErroScan(scanResult);
            }
        }
    }

    private void curentHasDID(DIDDocument didDocument) {
        if (didDocument != null) {
            //  getMyDID().getDidStore().storeDid(didDocument);//存储本地
            Bundle bundle = new Bundle();
            bundle.putString("scanResult", scanResult);
            bundle.putString("walletId", wallet.getWalletId());
            ((BaseFragment) getParentFragment()).start(AuthorizationFragment.class, bundle);

        }
    }

    @Override
    public void onGetCommonData(String methodname, String data) {

    }
}