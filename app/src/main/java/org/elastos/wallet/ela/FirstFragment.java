package org.elastos.wallet.ela;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import org.elastos.wallet.R;
import org.elastos.wallet.core.MasterWallet;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.db.RealmUtil;
import org.elastos.wallet.ela.ui.Assets.fragment.HomeWalletFragment;
import org.elastos.wallet.ela.ui.common.viewdata.CommmonObjectWithMethNameViewData;
import org.elastos.wallet.ela.ui.main.MainFragment;
import org.elastos.wallet.ela.utils.SPUtil;

import java.util.ArrayList;

import butterknife.BindView;


public class FirstFragment extends BaseFragment implements CommmonObjectWithMethNameViewData {

    private ArrayList<MasterWallet> data;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initView(View view) {
        int Language = new SPUtil(getContext()).getLanguage();
        int id;
        if (Language == 0) {
            id = MyApplication.chainID == 0 ? R.mipmap.c_flash : R.mipmap.alpha_720_1280;
        } else {
            id = MyApplication.chainID == 0 ? R.mipmap.e_flash : R.mipmap.alpha_e_720_1280;

        }
        imageView.setBackgroundResource(id);

        new FirstPresenter().getAllMasterWallets(this);

    }

    private void onFirst(ArrayList<MasterWallet> data) {
        if (data.size() > 0) {
            // Bundle bundle = new Bundle();
            // bundle.putStringArrayList("ids", (ArrayList<String>) data);
            //对比本地数据库并同步
            new RealmUtil().sync(data);
            startWithPop(MainFragment.class, null);
        } else {
            startWithPop(HomeWalletFragment.class, null);
        }
    }


    public static FirstFragment newInstance() {
        Bundle args = new Bundle();
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }



  /*  @Override
    protected void requstPermissionOk() {
        //申请权限成功
        super.requstPermissionOk();
        deayedSkipActivity();
    }*/

    private void deayedSkipActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onFirst(data);
            }
        }, 800);
    }

    @Override
    public void onGetCommonData(String methodname, Object data) {
        this.data = (ArrayList<MasterWallet>) data;
        //requstManifestPermission(getString(R.string.needpermission));
        deayedSkipActivity();
    }

   /* @Override
    protected void requstPermissionDefeated() {
        //申请权限失败
        super.requstPermissionDefeated();
        deayedSkipActivity();
    }*/
}
