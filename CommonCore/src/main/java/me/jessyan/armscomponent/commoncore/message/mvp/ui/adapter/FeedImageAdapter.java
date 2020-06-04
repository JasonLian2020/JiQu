package me.jessyan.armscomponent.commoncore.message.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

import me.jessyan.armscomponent.commoncore.R;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ImageListBean;
import me.jessyan.armscomponent.commoncore.message.mvp.model.entity.ResourceBean;
import me.jessyan.autosize.utils.ScreenUtils;

public class FeedImageAdapter extends BaseQuickAdapter<ImageListBean, BaseViewHolder> {
    private static final int ITEM_TYPE_ONE = 1;
    private static final int ITEM_TYPE_TWO = 2;
    private static final int ITEM_TYPE_THREE = 3;
    private static final int ITEM_TYPE_FOUR = 4;
    private static final int ITEM_TYPE_FIVE = 5;
    private static final int ITEM_TYPE_SIX = 6;
    private static final int ITEM_TYPE_SEVEN = 7;
    private static final int ITEM_TYPE_EIGHT = 8;
    private static final int ITEM_TYPE_NINE = 9;

    public FeedImageAdapter(@Nullable List<ImageListBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<ImageListBean>() {
            @Override
            protected int getItemType(ImageListBean bean) {
                List<ResourceBean> imageList = bean.getImageList();
                switch (imageList.size()) {
                    case 1:
                        return ITEM_TYPE_ONE;
                    case 2:
                        return ITEM_TYPE_TWO;
                    case 3:
                        return ITEM_TYPE_THREE;
                    case 4:
                        return ITEM_TYPE_FOUR;
                    case 5:
                        return ITEM_TYPE_FIVE;
                    case 6:
                        return ITEM_TYPE_SIX;
                    case 7:
                        return ITEM_TYPE_SEVEN;
                    case 8:
                        return ITEM_TYPE_EIGHT;
                    case 9:
                        return ITEM_TYPE_NINE;
                    default:
                        return ITEM_TYPE_ONE;
                }
            }
        });
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_ONE, R.layout.home_item_image_one);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_TWO, R.layout.home_item_image_two);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_THREE, R.layout.home_item_image_three);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_FOUR, R.layout.home_item_image_four);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_FIVE, R.layout.home_item_image_five);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_SIX, R.layout.home_item_image_six);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_SEVEN, R.layout.home_item_image_seven);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_EIGHT, R.layout.home_item_image_eight);
        getMultiTypeDelegate().registerItemType(ITEM_TYPE_NINE, R.layout.home_item_image_nine);
    }

    private OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public interface OnImageClickListener {
        void onItemClick(ImageView imageView, int position, List<ResourceBean> imageList);
    }

    private int maxWidth;

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    private int maxHeight;

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * 适配宽度
     */
    private int adaptWidth;

    public void setAdaptWidth(int adaptWidth) {
        this.adaptWidth = adaptWidth;
    }

    /**
     * 适配高度
     */
    private int adaptHeight;

    public void setAdaptHeight(int adaptHeight) {
        this.adaptHeight = adaptHeight;
    }

    private int margin;

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageListBean item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_ONE:
                handleOne(helper, item);
                break;
            case ITEM_TYPE_TWO:
                handleTwo(helper, item);
                break;
            case ITEM_TYPE_THREE:
                handleThree(helper, item);
                break;
            case ITEM_TYPE_FOUR:
                handleFour(helper, item);
                break;
            case ITEM_TYPE_FIVE:
                handleFive(helper, item);
                break;
            case ITEM_TYPE_SIX:
                handleSix(helper, item);
                break;
            case ITEM_TYPE_SEVEN:
                handleSeven(helper, item);
                break;
            case ITEM_TYPE_EIGHT:
                handleEight(helper, item);
                break;
            case ITEM_TYPE_NINE:
                handleNine(helper, item);
                break;
        }
    }

    private void loadImage(ImageView imageView, int position, List<ResourceBean> imageList) {
        Glide.with(mContext)
                .load(imageList.get(position).getPath())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.public_placeholder)
                        .error(R.color.public_error))
                .into(imageView);
        //点击事件
        imageView.setOnClickListener(v -> {
            if (onImageClickListener != null)
                onImageClickListener.onItemClick((ImageView) v, position, imageList);
        });
    }

    private void processImageSize(ResourceBean bean, FrameLayout viewParent) {
        if (maxHeight == 0 || maxWidth == 0) return;
        int originalHeight = bean.getHeight();
        int originalWidth = bean.getWidth();
        int realWidth;
        int realHeight;
        if (originalHeight > originalWidth) {
            //高大于宽
            realWidth = adaptWidth;
            realHeight = (int) (originalHeight * 1f / originalWidth * realWidth + 0.5f);
            if (realHeight > maxHeight) {
                realHeight = maxHeight;
                realWidth = (int) (originalWidth * 1f / originalHeight * realHeight + 0.5f);
            }
        } else {
            //宽大于高
            realHeight = adaptHeight;
            realWidth = (int) (originalWidth * 1f / originalHeight * realHeight + 0.5f);
            if (realWidth > maxWidth) {
                realWidth = maxWidth;
                realHeight = (int) (originalHeight * 1f / originalWidth * realWidth + 0.5f);
            }
        }
        ViewGroup.LayoutParams layoutParams = viewParent.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = realWidth;
            layoutParams.height = realHeight;
            viewParent.setLayoutParams(layoutParams);
        }
    }

    private void processImageMargin(FrameLayout viewParent) {
        if (margin == 0) return;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewParent.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMarginStart(margin);
            layoutParams.setMarginEnd(margin);
        }
    }

    private void handleOne(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        List<ResourceBean> imageList = item.getImageList();
        processImageSize(imageList.get(0), (FrameLayout) imageOne.getParent());
        processImageMargin((FrameLayout) imageOne.getParent());
        loadImage(imageOne, 0, imageList);
    }

    private void handleTwo(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
    }

    private void handleThree(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        ImageView imageThree = helper.getView(R.id.imageThree);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
        loadImage(imageThree, 2, imageList);
    }

    private void handleFour(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        ImageView imageThree = helper.getView(R.id.imageThree);
        ImageView imageFour = helper.getView(R.id.imageFour);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
        loadImage(imageThree, 2, imageList);
        loadImage(imageFour, 3, imageList);
    }

    private void handleFive(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        ImageView imageThree = helper.getView(R.id.imageThree);
        ImageView imageFour = helper.getView(R.id.imageFour);
        ImageView imageFive = helper.getView(R.id.imageFive);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
        loadImage(imageThree, 2, imageList);
        loadImage(imageFour, 3, imageList);
        loadImage(imageFive, 4, imageList);
    }

    private void handleSix(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        ImageView imageThree = helper.getView(R.id.imageThree);
        ImageView imageFour = helper.getView(R.id.imageFour);
        ImageView imageFive = helper.getView(R.id.imageFive);
        ImageView imageSix = helper.getView(R.id.imageSix);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
        loadImage(imageThree, 2, imageList);
        loadImage(imageFour, 3, imageList);
        loadImage(imageFive, 4, imageList);
        loadImage(imageSix, 5, imageList);
    }

    private void handleSeven(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        //第一个图片需要动态设置高度
        ViewGroup.LayoutParams layoutParams = imageOne.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenSize(mContext)[0];
        int height = screenWidth / 2;
        if (layoutParams != null && layoutParams.height != height) {
            layoutParams.height = height;
            imageOne.setLayoutParams(layoutParams);
        }
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        ImageView imageThree = helper.getView(R.id.imageThree);
        ImageView imageFour = helper.getView(R.id.imageFour);
        ImageView imageFive = helper.getView(R.id.imageFive);
        ImageView imageSix = helper.getView(R.id.imageSix);
        ImageView imageSeven = helper.getView(R.id.imageSeven);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
        loadImage(imageThree, 2, imageList);
        loadImage(imageFour, 3, imageList);
        loadImage(imageFive, 4, imageList);
        loadImage(imageSix, 5, imageList);
        loadImage(imageSeven, 6, imageList);
    }

    private void handleEight(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        ImageView imageThree = helper.getView(R.id.imageThree);
        ImageView imageFour = helper.getView(R.id.imageFour);
        ImageView imageFive = helper.getView(R.id.imageFive);
        ImageView imageSix = helper.getView(R.id.imageSix);
        ImageView imageSeven = helper.getView(R.id.imageSeven);
        ImageView imageEight = helper.getView(R.id.imageEight);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
        loadImage(imageThree, 2, imageList);
        loadImage(imageFour, 3, imageList);
        loadImage(imageFive, 4, imageList);
        loadImage(imageSix, 5, imageList);
        loadImage(imageSeven, 6, imageList);
        loadImage(imageEight, 7, imageList);
    }

    private void handleNine(BaseViewHolder helper, ImageListBean item) {
        ImageView imageOne = helper.getView(R.id.imageOne);
        ImageView imageTwo = helper.getView(R.id.imageTwo);
        ImageView imageThree = helper.getView(R.id.imageThree);
        ImageView imageFour = helper.getView(R.id.imageFour);
        ImageView imageFive = helper.getView(R.id.imageFive);
        ImageView imageSix = helper.getView(R.id.imageSix);
        ImageView imageSeven = helper.getView(R.id.imageSeven);
        ImageView imageEight = helper.getView(R.id.imageEight);
        ImageView imageNine = helper.getView(R.id.imageNine);
        List<ResourceBean> imageList = item.getImageList();
        loadImage(imageOne, 0, imageList);
        loadImage(imageTwo, 1, imageList);
        loadImage(imageThree, 2, imageList);
        loadImage(imageFour, 3, imageList);
        loadImage(imageFive, 4, imageList);
        loadImage(imageSix, 5, imageList);
        loadImage(imageSeven, 6, imageList);
        loadImage(imageEight, 7, imageList);
        loadImage(imageNine, 8, imageList);
    }
}
