package org.elastos.wallet.ela.ui.Assets.fragment.transfer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.db.table.Wallet;
import org.elastos.wallet.ela.ui.Assets.adapter.SignViewPagetAdapter;
import org.elastos.wallet.ela.ui.Assets.presenter.TransferPresenter;
import org.elastos.wallet.ela.ui.common.viewdata.CommmonStringWithMethNameViewData;
import org.elastos.wallet.ela.utils.Constant;
import org.elastos.wallet.ela.utils.QRCodeUtils;
import org.elastos.wallet.ela.utils.ScreenUtil;
import org.elastos.wallet.ela.utils.ShareUtil;
import org.elastos.wallet.ela.utils.widget.ScaleTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ToSignFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vptitle)
    TextView tvVptitle;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.ll_vp)
    LinearLayout llVp;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tosign;
    }

    @Override
    protected void setExtraData(Bundle data) {
        String attributes = data.getString("attributes");
        String chainId = data.getString("ChainId", "ELA");
        Wallet wallet = data.getParcelable("wallet");

        setData(attributes,wallet.getWalletId(),chainId);
    }

    @Override
    protected void initView(View view) {
        tvTitle.setText(getString(R.string.waitingsign));
        ivTitleRight.setVisibility(View.VISIBLE);
        ivTitleRight.setImageResource(R.mipmap.top_share);


    }

    @OnClick({R.id.iv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_right:
                //截图分享
                ShareUtil.fxPic(getBaseActivity(), mRootView);
                break;
        }
    }


    public void setData(String data,String walletId,String chainID) {
        //encodeTransaction  加密后的结果
        List<Bitmap> images = QRCodeUtils.createMulQrCodeBitmap(data, ScreenUtil.dp2px(getContext(), 170)
                , ScreenUtil.dp2px(getContext(), 170), Constant.SIGN,walletId,chainID);
        if (images.size() == 1) {
            ivQr.setVisibility(View.VISIBLE);
            llVp.setVisibility(View.GONE);
            tvVptitle.setVisibility(View.GONE);
            ivQr.setImageBitmap(images.get(0));
            return;
        }
        ivQr.setVisibility(View.GONE);
        llVp.setVisibility(View.VISIBLE);
        tvVptitle.setVisibility(View.VISIBLE);
        SignViewPagetAdapter signViewPagetAdapter = new SignViewPagetAdapter(images, getContext());
        viewpage.setAdapter(signViewPagetAdapter);
        viewpage.setPageTransformer(true, new ScaleTransformer());
        viewpage.setPageMargin(10);
        tvVptitle.setText(1 + "/" + images.size());
        viewpage.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tvVptitle.setText((position + 1) + "/" + images.size());
            }
        });
    }

}