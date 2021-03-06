package com.fangxu.dota2helper.presenter;

import android.app.Activity;

import com.fangxu.dota2helper.bean.NewsList;
import com.fangxu.dota2helper.callback.INewsView;
import com.fangxu.dota2helper.callback.NewsCallback;
import com.fangxu.dota2helper.interactor.NewsInteractor;
import com.fangxu.dota2helper.ui.Fragment.NewsFragment;
import com.fangxu.dota2helper.ui.Fragment.UpdateFragment;

import java.util.List;

/**
 * Created by lenov0 on 2016/4/9.
 */
public class NewsPresenter extends BasePresenter implements NewsCallback{
    public static final int NEWS = 0;
    public static final int UPDATES = 1;

    private INewsView mCallback;
    private int mType;

    public NewsPresenter(Activity activity, INewsView iNewsView) {
        mInteractor = new NewsInteractor(activity, this);
        mCallback = iNewsView;
        if (iNewsView instanceof NewsFragment){
            mType = NEWS;
        } else if (iNewsView instanceof UpdateFragment) {
            mType = UPDATES;
        }
    }

    public void loadNewsCache() {
        ((NewsInteractor)mInteractor).getCachedNews();
    }

    public void getUpdateCache () {
        ((NewsInteractor)mInteractor).getCachedUpdates();
    }

    public void doRefresh() {
        if (mType == NEWS) {
            ((NewsInteractor)mInteractor).queryNews();
        } else {
            ((NewsInteractor)mInteractor).queryUpdates();
        }
    }

    public void doLoadMore() {
        if (mType == NEWS) {
            ((NewsInteractor)mInteractor).queryMoreNews();
        } else {
            ((NewsInteractor)mInteractor).queryMoreUpdates();
        }
    }

    @Override
    public void onGetCache(List<NewsList.NewsEntity> bannerEntityList, List<NewsList.NewsEntity> newsEntityList, boolean updateNews) {
        if (!updateNews) {
            mCallback.setBanner(bannerEntityList);
        }
        mCallback.setNewsList(newsEntityList, false);
        mCallback.onCacheLoaded();
    }

    @Override
    public void onCacheEmpty() {
        mCallback.onCacheLoaded();
    }

    @Override
    public void onGetBanner(List<NewsList.NewsEntity> bannerEntityList) {
        mCallback.setBanner(bannerEntityList);
    }

    @Override
    public void onUpdateFailed(boolean loadmore) {
        mCallback.setRefreshFailed(loadmore);
        mCallback.hideProgress(loadmore);
    }

    @Override
    public void onUpdateSuccessed(List<NewsList.NewsEntity> newsEntityList, boolean loadmore) {
        mCallback.setNewsList(newsEntityList, loadmore);
        mCallback.hideProgress(loadmore);
    }
}
