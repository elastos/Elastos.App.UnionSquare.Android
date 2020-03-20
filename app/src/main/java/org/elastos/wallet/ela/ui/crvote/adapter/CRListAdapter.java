package org.elastos.wallet.ela.ui.crvote.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseViewHolder;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.did.entity.CredentialSubjectBean;
import org.elastos.wallet.ela.ui.did.entity.GetJwtRespondBean;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.NodeDotJsonViewData;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.SuperNodeListPresenter;
import org.elastos.wallet.ela.utils.svg.GlideApp;
import org.elastos.wallet.ela.utils.svg.GlideRequest;

import java.util.List;

public class CRListAdapter extends CRListAdapterFather {

    private final GlideRequest<Bitmap> glideRequest;
    private SuperNodeListPresenter presenter;

    public CRListAdapter(BaseFragment context, @Nullable List<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean> data, boolean is) {
        super(R.layout.item_cr_list, context, data, is);
        glideRequest = GlideApp.with(context).asBitmap().error(R.mipmap.found_vote_initial).placeholder(R.mipmap.found_vote_initial);


    }

    @Override
    protected void convert(BaseViewHolder helper, CRListBean.DataBean.ResultBean.CrcandidatesinfoBean bean) {
        helper.setBackgroundColor(R.id.ll, mContext.getResources().getColor(R.color.a26ffffff));
        super.convert(helper, bean);

        helper.setGone(R.id.view, !showCheckbox);
  /*      int no = helper.getLayoutPosition();

        if (is && 0 == helper.getLayoutPosition()) {
            no = pos;
        } else if (is) {
            no = (bean.getIndex() < data.get(0).getIndex()) ? no - 1 : no ;
        }*/
        if (bean.getIndex() < 12) {
            helper.setText(R.id.tv_rank, "No." + (bean.getIndex() + 1));
        } else {
            helper.setText(R.id.tv_rank, "" + (bean.getIndex() + 1));
        }

        ImageView iv = helper.getView(R.id.iv_icon);
        iv.setImageResource(R.mipmap.found_vote_initial);
        String baseUrl = bean.getCid();
        iv.setTag(R.id.error_tag_empty, baseUrl);
        GlideApp.with(mContext).clear(iv);
        if (baseUrl == null) {
            return;
        }
        if (map.get(baseUrl) != null) {
            if ("".equals(map.get(baseUrl))) {
                return;
            }
            glideRequest.load(map.get(baseUrl)).into(iv);
            return;
        }
        if (presenter == null) {
            presenter = new SuperNodeListPresenter();
        }
        presenter.getCRUrlJson(iv, baseUrl, context, new NodeDotJsonViewData() {
            @Override
            public void onError(String url) {
                GlideApp.with(mContext).clear(iv);
                iv.setImageResource(R.mipmap.found_vote_initial);
                map.put(url, "");
            }

            @Override
            public void onGetNodeDotJsonData(ImageView iv1, BaseEntity baseEntity, String url) {    //这个时候的iv已经不是那个iv了  所有传递iv试试
                if (iv1.getTag(R.id.error_tag_empty) == null || !(iv1.getTag(R.id.error_tag_empty).toString()).equals(url)) {
                    GlideApp.with(mContext).clear(iv1);
                    iv1.setImageResource(R.mipmap.found_vote_initial);
                    return;
                }
                GetJwtRespondBean getJwtRespondBean = (GetJwtRespondBean) baseEntity;
                String jwt = getJwtRespondBean.getData().getJwt();
                if (!TextUtils.isEmpty(jwt)) {
                    String[] jwtParts = jwt.split("\\.");
                    String payload = new String(Base64.decode(jwtParts[1], Base64.URL_SAFE));
                    String pro = context.getMyDID().getCredentialProFromJson(payload);
                    CredentialSubjectBean credentialSubjectBean = JSON.parseObject(pro, CredentialSubjectBean.class);
                    if (credentialSubjectBean == null || credentialSubjectBean.getAvatar() == null) {
                        map.put(url, "");
                        return;
                    }
                    String imgUrl = credentialSubjectBean.getAvatar();
                    map.put(url, imgUrl);
                    glideRequest.load(imgUrl).into(iv1);
                }

            }
        });

    }


}
