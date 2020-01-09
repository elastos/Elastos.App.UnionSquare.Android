package org.elastos.wallet.ela.ui.crvote.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.MyApplication;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.bean.ImageBean;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.NodeDotJsonViewData;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.NodeInfoBean;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.SuperNodeListPresenter;
import org.elastos.wallet.ela.utils.GlideApp;
import org.elastos.wallet.ela.utils.GlideRequest;

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
        if (bean.getIndex() <12) {
            helper.setText(R.id.tv_rank, "No." + (bean.getIndex() + 1));
        } else {
            helper.setText(R.id.tv_rank, "" + (bean.getIndex() + 1));
        }

        ImageView iv = helper.getView(R.id.iv_icon);
        iv.setImageResource(R.mipmap.found_vote_initial);
        String baseUrl = bean.getUrl();
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
                map.put(url, "");
            }

            @Override
            public void onGetNodeDotJsonData(ImageView iv1, NodeInfoBean t, String url) {
                //这个时候的iv已经不是那个iv了  所有传递iv试试
                if (iv1.getTag(R.id.error_tag_empty) == null || !(iv1.getTag(R.id.error_tag_empty).toString()).equals(url)) {
                    return;
                }
                if (t == null || t.getOrg() == null || t.getOrg().getBranding() == null || t.getOrg().getBranding().getLogo_256() == null) {
                    map.put(url, "");
                    return;
                }

                String imgUrl = t.getOrg().getBranding().getLogo_256();
                // map.put(url, imgUrl);
                // glideRequest.load(imgUrl).into(iv1);
                presenter.getImage(iv1, url, imgUrl, context, new NodeDotJsonViewData() {
                    @Override
                    public void onError(String url) {
                        map.put(url, "");
                    }

                    @Override
                    public void onGetImage(ImageView iv1, String url, ImageBean imageBean) {
                        if (iv1.getTag(R.id.error_tag_empty) == null || !(iv1.getTag(R.id.error_tag_empty).toString()).equals(url)) {
                            GlideApp.with(mContext).clear(iv1);
                            iv1.setImageResource(R.mipmap.found_vote_initial);
                            return;
                        }
                        String newimgUrl = MyApplication.REQUEST_BASE_URL + "/" + imageBean.getData();
                        map.put(url, newimgUrl);
                        glideRequest.load(newimgUrl).into(iv1);
                    }
                });
                //CustomViewTarget clear不了
         /*       glideRequest.load(imgUrl).into(new CustomViewTarget<ImageView, Bitmap>(iv1) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        //glideRequest.load(R.mipmap.found_vote_initial).into(iv);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (iv1.getTag(R.id.error_tag_empty) != null && (url).equals(iv1.getTag(R.id.error_tag_empty).toString())) {
                            //glideRequest.load(resource).into(iv);
                            iv1.setImageBitmap(resource);
                            map.put(iv1.getTag(R.id.error_tag_empty).toString(), resource);
                        }
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        // glideRequest.load(placeholder).into(iv);
                    }
                });*/


            }
        });

    }


}
