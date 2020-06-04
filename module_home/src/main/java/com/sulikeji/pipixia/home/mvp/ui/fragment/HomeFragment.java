package com.sulikeji.pipixia.home.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sulikeji.pipixia.home.R;
import com.sulikeji.pipixia.home.R2;
import com.sulikeji.pipixia.home.di.component.DaggerHomeComponent;
import com.sulikeji.pipixia.home.mvp.contract.HomeContract;
import com.sulikeji.pipixia.home.mvp.model.entity.CategoryListBean;
import com.sulikeji.pipixia.home.mvp.presenter.HomePresenter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.jason.autopagelayout.AutoPageLayout;
import me.jessyan.armscomponent.commonsdk.base.BaseFragmentPagerAdapter;
import me.jessyan.armscomponent.commonservice.EventBusHub;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.magicindicator.titles.ScaleTransitionPagerTitleView;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.autosize.utils.AutoSizeUtils;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/11/2019 11:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.HOME_HOMEFRAGMENT)
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {
    @BindView(R2.id.contentLayout)
    View contentLayout;
    @BindView(R2.id.tabLayout)
    MagicIndicator tabLayout;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.searchLayout)
    ImageView searchLayout;

    private BaseFragmentPagerAdapter pagerAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        searchLayout.setOnClickListener(v -> clickSearch());
        initViewPager();
        initPageLayout();
        mPresenter.getCategoryList();
    }

    private void clickSearch() {
        showMessage("搜索功能未开发");
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ToastUtils.normal(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        getActivity().finish();
    }

    @Override
    public void updateSuccess(List<CategoryListBean> categoryList) {
        if (categoryList.isEmpty()) {
            //数据为空
            if (pageLayout != null) pageLayout.showEmpty();
        } else {
            initFragmentList(categoryList);
            tabLayout.setNavigator(initNaviagtor());
            pagerAdapter.setNewList(mFragmentList, mTitleList);
            if (pageLayout != null) pageLayout.showContent();
        }
    }

    @Override
    public void updateFail(String message) {
        if (pageLayout != null) pageLayout.showError();
    }

    private String[] mTitleList;
    private List<Fragment> mFragmentList;

    private List<Fragment> initFragmentList(List<CategoryListBean> categoryList) {
        if (mFragmentList == null) mFragmentList = new ArrayList<>();
        else mFragmentList.clear();
        mTitleList = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            CategoryListBean bean = categoryList.get(i);
            mTitleList[i] = bean.getValue();
            Fragment fragment = (Fragment) ARouter.getInstance()
                    .build(RouterHub.HOME_HOMEDETAILFRAGMENT)
                    .withInt(RouterHub.HOME_HOMEDETAILFRAGMENT_KEY_POSITION, i)
                    .withInt(RouterHub.HOME_HOMEDETAILFRAGMENT_KEY_ID, bean.getId())
                    .navigation();
            mFragmentList.add(fragment);
        }
        return mFragmentList;
    }

    private void initViewPager() {
        pagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setNavigator(initNaviagtor());
        ViewPagerHelper.bind(tabLayout, viewPager);
    }

    private IPagerNavigator initNaviagtor() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleList == null ? 0 : mTitleList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                int padding = AutoSizeUtils.dp2px(mContext, 20);
                simplePagerTitleView.setPadding(padding, 0, padding, 0);
                simplePagerTitleView.setText(mTitleList[index]);
                simplePagerTitleView.setTextSize(44);
                simplePagerTitleView.setMinScale(0.68f);
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#FF0200"));
                simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        return commonNavigator;
    }

    private AutoPageLayout pageLayout;

    private void initPageLayout() {
        pageLayout = new AutoPageLayout.Builder(this)
                .setTarget(contentLayout)
                .setLoadingLayout(R.layout.public_layout_loading, null)
                .setEmptyLayout(R.layout.public_layout_empty, null)
                .setErrorLayout(R.layout.public_layout_error, null)
                .showType(AutoPageLayout.SHOW_TYPE_LOADING)
                .build();
    }

    @Subscriber(tag = EventBusHub.PUBLISH_PUBLISHSUCCESS, mode = ThreadMode.MAIN)
    private void publishSuccess(Object bean) {
        //TODO:
        Timber.tag("jason").d("publishSuccess");
    }
}
