package com.sulikeji.pipixia.publish.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.sulikeji.pipixia.publish.R;
import com.sulikeji.pipixia.publish.R2;
import com.sulikeji.pipixia.publish.di.component.DaggerPublishDetailComponent;
import com.sulikeji.pipixia.publish.mvp.contract.PublishDetailContract;
import com.sulikeji.pipixia.publish.mvp.presenter.PublishDetailPresenter;
import com.sulikeji.pipixia.publish.mvp.ui.DetailGridInset;
import com.sulikeji.pipixia.publish.mvp.ui.adapter.PublishDetailAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.jason.imagepicker.ImagePicker;
import me.jason.imagepicker.IntentHub;
import me.jason.imagepicker.internal.entity.Item;
import me.jessyan.armscomponent.commoncore.MediaUtils;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ContentBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.MediaBean;
import me.jessyan.armscomponent.commonservice.RouterHub;
import me.jessyan.armscomponent.commonui.easyat.FormatHelper;
import me.jessyan.armscomponent.commonui.easyat.KeyCodeDeleteHelper;
import me.jessyan.armscomponent.commonui.easyat.NoCopySpanEditableFactory;
import me.jessyan.armscomponent.commonui.easyat.SpanFactory;
import me.jessyan.armscomponent.commonui.easyat.watcher.SelectionSpanWatcher;
import me.jessyan.armscomponent.commonui.util.ToastUtils;
import me.jessyan.autosize.AutoSize;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/24/2019 17:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.PUBLISH_PUBLISHDETAILACTIVITY)
public class PublishDetailActivity extends BaseActivity<PublishDetailPresenter> implements PublishDetailContract.View, TextWatcher {

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.chooseVideo)
    ImageView chooseVideo;
    @BindView(R2.id.chooseImage)
    ImageView chooseImage;
    @BindView(R2.id.chooseAt)
    ImageView chooseAt;
    @BindView(R2.id.chooseSend)
    Button chooseSend;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    BaseQuickAdapter mAdapter;
    @Inject
    Dialog mDialog;

    private EditText topContent;

    private int fromType;
    private List<MediaBean> selectedList;
    private String videoThumbPath;

    public static final int REQUEST_CODE_CHOOSE_IMAGE = 1;
    public static final int REQUEST_CODE_CHOOSE_VIDEO = 2;
    public static final int REQUEST_CODE_CHOOSE_AT = 3;
    public static final int REQUEST_CODE_CHECK_AT = 4;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPublishDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.popup_down_in, R.anim.popup_down_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        topContent.removeTextChangedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_IMAGE:
                if (data == null) return;
                processResultForChooseImage(data);
                break;
            case REQUEST_CODE_CHOOSE_VIDEO:
                if (data == null) return;
                processResultForChooseVideo(data);
                break;
            case REQUEST_CODE_CHOOSE_AT:
                if (data == null) return;
                processResultForChooseAt(data);
                break;
            case REQUEST_CODE_CHECK_AT:
                if (data == null) return;
                processResultForCheckAt(data);
                break;
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.publish_activity_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        parseIntent();
        initStatusBar();
        initRecyclerView();
        initBottomLayout();
    }

    private void parseIntent() {
        if (getIntent() == null) return;
        fromType = getIntent().getIntExtra(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_FROMTYPE, IntentHub.FROM_NONE);
        ArrayList<Item> selectedItems = getIntent().getParcelableArrayListExtra(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_SELECTEDITEM);
        ArrayList<String> selectedPaths = getIntent().getStringArrayListExtra(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_SELECTEDPATH);
        ArrayList<Uri> selectedUris = getIntent().getParcelableArrayListExtra(RouterHub.PUBLISH_PUBLISHDETAILACTIVITY_KEY_SELECTEDURI);
        selectedList = getSelectedList(selectedItems, selectedPaths, selectedUris);
        switch (fromType) {
            case IntentHub.FROM_NONE:
                break;
            case IntentHub.FROM_IMAGE:
                //是否需要加上添加按钮
                processAddBean();
                break;
            case IntentHub.FROM_VDIEO:
                if (selectedList != null && !selectedList.isEmpty()) checkVideoInfo();
                break;
        }
    }

    private void initStatusBar() {
        int statusBarColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarColor = getResources().getColor(R.color.public_colorPrimary);
            BarUtils.setStatusBarLightMode(this, true);
        } else {
            statusBarColor = getResources().getColor(R.color.public_colorPrimary_compatibility);
        }
        //状态栏颜色
        BarUtils.setStatusBarColor(this, statusBarColor);
    }

    private void initRecyclerView() {
        //赋值
        ((PublishDetailAdapter) mAdapter).setFromImage(isFromImage());
        ((PublishDetailAdapter) mAdapter).setList(selectedList);
        //设置
        ArmsUtils.configRecyclerView(recyclerView, mLayoutManager);
        int spacing = getResources().getDimensionPixelSize(R.dimen.publish_detail_spacing);
        recyclerView.addItemDecoration(new DetailGridInset(getSpanCount(), spacing, true));
        mAdapter.addHeaderView(initHeaderView(recyclerView));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MediaBean item = (MediaBean) adapter.getItem(position);
            if (item == null) return;
            if (MediaBean.NONE_PATH.equals(item.getPath())) {
                mPresenter.clickChooseImage();
            } else {
                //TODO:预览视频或者图片
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MediaBean item = (MediaBean) adapter.getItem(position);
            if (item == null) return;
            if (view.getId() == R.id.imageDelete) {
                //删除图片
                mPresenter.clickDeleteImage(item, position);
            } else if (view.getId() == R.id.videoDelete) {
                //删除视频
                mPresenter.clickDeleteVideo(item, position);
            }
        });
        mAdapter.bindToRecyclerView(recyclerView);
    }

    private void initBottomLayout() {
        chooseVideo.setOnClickListener(v -> mPresenter.clickChooseVideo());
        chooseImage.setOnClickListener(v -> mPresenter.clickChooseImage());
        chooseAt.setOnClickListener(v -> mPresenter.clickChooseAt());
        chooseSend.setOnClickListener(v -> mPresenter.clickPublish());
        chooseSend.setEnabled(false);
        updateChooseImage();
    }

    private View initHeaderView(RecyclerView recyclerView) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.publish_layout_top_input, recyclerView, false);
        topContent = headerView.findViewById(R.id.topContent);
        //解决EditText无法上下滚动
        topContent.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        });
        topContent.setEditableFactory(new NoCopySpanEditableFactory(new SelectionSpanWatcher()));
        topContent.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                return KeyCodeDeleteHelper.onDelDown(((EditText) v).getText());
            }
            return false;
        });
        topContent.setOnFocusChangeListener((v, hasFocus) -> {
            Timber.tag("jason").d("onFocusChange " + hasFocus);
            if (hasFocus) AutoSize.cancelAdapt(this);
            else AutoSize.autoConvertDensityOfGlobal(this);
        });
        topContent.addTextChangedListener(this);
        topContent.setMovementMethod(LinkMovementMethod.getInstance());
        topContent.postDelayed(this::requestFocusAndShowSoftInput, 250);
        return headerView;
    }

    @Override
    public void onBackPressed() {
        KeyboardUtils.hideSoftInput(this);
        super.onBackPressed();
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
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
        KeyboardUtils.hideSoftInput(this);
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getSpanCount() {
        return 3;
    }

    @Override
    public List<MediaBean> getSelectedList() {
        return selectedList;
    }

    @Override
    public boolean isFromImage() {
        if (fromType == IntentHub.FROM_IMAGE) return true;
        else return false;
    }

    @Override
    public boolean isFromVideo() {
        if (fromType == IntentHub.FROM_VDIEO) return true;
        else return false;
    }

    @Override
    public void updateChooseImage() {
        chooseImage.setEnabled(isCanChooseImage());
    }

    @Override
    public String getJsonTextContent() {
        Editable editable = topContent.getText();
        return FormatHelper.editableToJson(editable, ContentBean.class);
    }

    @Override
    public String getVideoThumbPath() {
        return videoThumbPath;
    }

    /**
     * 是否可以选择图片
     *
     * @return true表示可以选中，false表示不可选择
     */
    private boolean isCanChooseImage() {
        if (selectedList != null && selectedList.size() == 9
                && !MediaBean.NONE_PATH.equals(selectedList.get(8).getPath())) {
            return false;
        }
        return true;
    }

    private List<MediaBean> getSelectedList(ArrayList<Item> selectedItems, ArrayList<String> selectedPaths, ArrayList<Uri> selectedUris) {
        List<MediaBean> selectedList = null;
        if (selectedItems != null && selectedPaths != null && selectedUris != null) {
            selectedList = new ArrayList<>();
            for (int i = 0; i < selectedItems.size(); i++) {
                Item item = selectedItems.get(i);
                String path = selectedPaths.get(i);
                Uri contentUri = selectedUris.get(i);
                selectedList.add(MediaBean.valueOf(item, path, contentUri));
            }
        }
        return selectedList;
    }

    private void processAddBean() {
        if (selectedList == null) return;
        if (selectedList.size() > 0 && selectedList.size() < 9) {
            //0<x<9
            selectedList.add(MediaBean.emptyBean());
        }
    }

    @SuppressLint("CheckResult")
    private void checkVideoInfo() {
        if (selectedList.get(0).getId() != -1) {
            //非拍照的
            processVideoInfo();
            return;
        }
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> processVideoInfo());
    }

    private void processVideoInfo() {
        MediaBean mediaBean = selectedList.get(0);
        if (mediaBean.getHeight() == 0 || mediaBean.getWidth() == 0)
            //获取视频信息
            MediaUtils.getVideoInfo(mediaBean, result -> {
                if (result == null) return;
                mediaBean.setId(result.getId());
                mediaBean.setSize(result.getSize());
                mediaBean.setDuration(result.getDuration());
                mediaBean.setWidth(result.getWidth());
                mediaBean.setHeight(result.getHeight());
            });
        //获取视频封面
        MediaUtils.getVideoThumb(mediaBean, result -> {
            videoThumbPath = result;
        });
    }

    private void processResultForChooseImage(Intent data) {
        ArrayList<Item> selectedItems = ImagePicker.obtainItemResult(data);
        ArrayList<String> selectedPaths = ImagePicker.obtainPathResult(data);
        ArrayList<Uri> selectedUris = ImagePicker.obtainUriResult(data);
        List<MediaBean> tempList = getSelectedList(selectedItems, selectedPaths, selectedUris);
        if (selectedList == null) {
            selectedList = new ArrayList<>();
        } else if (!selectedList.isEmpty()) {
            if (isFromVideo()) selectedList.clear();
            else selectedList.remove(selectedList.size() - 1);
        }
        selectedList.addAll(tempList);
        fromType = ImagePicker.obtainFromType(data);
        //是否需要加上添加按钮
        processAddBean();
        //更新底部图片选择按钮
        updateChooseImage();
        //赋值
        ((PublishDetailAdapter) mAdapter).setFromImage(isFromImage());
        mAdapter.setNewData(selectedList);
    }

    private void processResultForChooseVideo(Intent data) {
        ArrayList<Item> selectedItems = ImagePicker.obtainItemResult(data);
        ArrayList<String> selectedPaths = ImagePicker.obtainPathResult(data);
        ArrayList<Uri> selectedUris = ImagePicker.obtainUriResult(data);
        selectedList = getSelectedList(selectedItems, selectedPaths, selectedUris);
        fromType = ImagePicker.obtainFromType(data);
        checkVideoInfo();
        //赋值
        ((PublishDetailAdapter) mAdapter).setFromImage(isFromImage());
        mAdapter.setNewData(selectedList);
    }

    private void processResultForChooseAt(Intent data) {
        int userId = data.getIntExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERID, -1);
        String userName = data.getStringExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERNAME);
        ContentBean bean = new ContentBean();
        bean.setTag(ContentBean.TAG_AT);
        bean.setUserId(userId);
        bean.setUserName(userName);
        // 添加数据到编辑框
        topContent.getText().append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
        topContent.postDelayed(this::requestFocusAndShowSoftInput, 250);
    }

    private void processResultForCheckAt(Intent data) {
        int userId = data.getIntExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERID, -1);
        String userName = data.getStringExtra(RouterHub.SEARCH_SEARCHCONTACTSACTIVITY_KEY_USERNAME);
        ContentBean bean = new ContentBean();
        bean.setTag(ContentBean.TAG_AT);
        bean.setUserId(userId);
        bean.setUserName(userName);
        // 先删除@，再添加数据到编辑框
        int length = topContent.getText().length();
        topContent.getText().delete(length - 1, length);
        topContent.getText().append(SpanFactory.newSpannable(bean.getSpannedName(), bean));
        topContent.postDelayed(this::requestFocusAndShowSoftInput, 250);
    }

    private void requestFocusAndShowSoftInput() {
        // 获取焦点
        topContent.setFocusable(true);
        topContent.setFocusableInTouchMode(true);
        topContent.requestFocus();
        topContent.setSelection(topContent.getText().length());
        KeyboardUtils.showSoftInput(topContent);
    }

    /*==================TextWatcher Start==================*/
    private int beforeTextlength;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextlength = s.length();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int afterTextLength = s.length();
        // @人检测功能
        if (afterTextLength != 0 && beforeTextlength < afterTextLength) {
            char at = s.charAt(s.length() - 1);
            if ("@".equals(String.valueOf(at))) {
                //检测到用户有@操作
                mPresenter.checkChooseAt();
            }
        }
        // 字数限制
        chooseSend.setEnabled(afterTextLength >= 10);
    }
    /*==================TextWatcher End==================*/
}
