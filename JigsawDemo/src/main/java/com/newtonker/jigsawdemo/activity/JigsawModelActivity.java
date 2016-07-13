package com.newtonker.jigsawdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.constants.FileConstants;
import com.newtonker.jigsawdemo.event.OnFilterItemClickListener;
import com.newtonker.jigsawdemo.event.OnItemClickListener;
import com.newtonker.jigsawdemo.model.JigsawType;
import com.newtonker.jigsawdemo.model.TemplateEntity;
import com.newtonker.jigsawdemo.utils.FileUtils;
import com.newtonker.jigsawdemo.utils.TemplateUtils;
import com.newtonker.jigsawdemo.widget.JigsawLinearLayout;
import com.newtonker.jigsawdemo.widget.JigsawModelLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * 单个拼图模板修饰界面
 */
public class JigsawModelActivity extends Activity {
    private static final int PHOTO_PICKED_WITH_DATA = 1000;
    // 拼图为正方形，宽高比为1.0f
    private static final float JIGSAW_MODEL_RATIO = 1.0f;

    private int idOfTemplate;
    private JigsawType jigsawType;
    private ArrayList<String> selectedPaths;

    // 当前选中的图片位置
    private int curPosition;

    private int modelAreaParentWidth;
    private int modelAreaParentHeight;
    private JigsawModelLayout modelArea;// 排版的View
    private RelativeLayout modelAreaParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_model);
        // 接受传递的图片地址    和     排版的张数布局信息
        Intent intent = getIntent();
        selectedPaths = intent.getStringArrayListExtra(SelectPhotoActivity.SELECTED_PATHS);
        jigsawType = (JigsawType) intent.getSerializableExtra(SelectPhotoActivity.TYPE_OF_JIGSAW);

        initView();
    }

    private void reDrawModelArea() {
        if (0 != modelAreaParentWidth && 0 != modelAreaParentHeight) {
            float ratio = ((float) modelAreaParentWidth) / modelAreaParentHeight;

            int modelAreaWidth;
            int modelAreaHeight;

            if (JIGSAW_MODEL_RATIO > ratio) {
                modelAreaWidth = modelAreaParentWidth;
                modelAreaHeight = (int) (modelAreaParentWidth / JIGSAW_MODEL_RATIO);
            } else {
                modelAreaHeight = modelAreaParentHeight;
                modelAreaWidth = (int) (modelAreaHeight * JIGSAW_MODEL_RATIO);
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(modelAreaWidth, modelAreaHeight);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            modelArea.setLayoutParams(params);
            modelArea.setModelAreaParentWidthAndHeight(modelAreaWidth, modelAreaHeight);

            modelArea.reDraw();
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // 获取父类布局  根布局
        JigsawLinearLayout rootLayout = (JigsawLinearLayout) findViewById(R.id.root_layout);
        rootLayout.setOnSelectedStateChangedListener(new JigsawLinearLayout.OnSelectedStateChangedListener() {
            @Override
            public void onSelectedStateChanged() {
                // 这里设置隐藏选中边框，不显示组件
                modelArea.setShowSelectedState(false);
            }
        });

        // 拼图区域
        modelArea = (JigsawModelLayout) findViewById(R.id.single_model_area);
        // 创建模板
        TemplateEntity entity = TemplateUtils.getEntity(this, jigsawType, idOfTemplate);
        modelArea.setImagePathList(selectedPaths);
        Log.d("initView", "id =" + entity.getId());
        modelArea.setTemplateEntity(entity);
        //好像是图片的选中状态
        modelArea.setOnPopupSelectListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                curPosition = position;
                // 跳出选图界面
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
            }
        });
        //选中之后添加滤镜
        modelArea.setOnPopupFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GPUImageFilter filter) {
                // 获取当前显示的图片
                Bitmap bitmap = modelArea.getSelectedBitmap();
                // 开启渲染
                new ImageRenderTask(bitmap, filter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        modelAreaParent = (RelativeLayout) findViewById(R.id.model_area_parent);
        final ViewTreeObserver observer = modelAreaParent.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                modelAreaParent.getViewTreeObserver().removeOnPreDrawListener(this);
                modelAreaParentWidth = modelAreaParent.getMeasuredWidth();
                modelAreaParentHeight = modelAreaParent.getMeasuredHeight();
                // redraw model area
                reDrawModelArea();
                return true;
            }
        });

        ImageView backBtn = (ImageView) findViewById(R.id.editor_action_bar_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退出
                JigsawModelActivity.this.finish();
            }
        });

        TextView saveBtn = (TextView) findViewById(R.id.editor_action_bar_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存图片
                new SavePictureTask().execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                String path = FileUtils.getPath(this, data.getData());
                // 替代之前的图片路径
                selectedPaths.add(curPosition, path);
                selectedPaths.remove(curPosition + 1);
                // 设置新的图片
                modelArea.replaceSelectedBitmap(path);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (modelArea.getShowSelectedState()) {
            modelArea.setShowSelectedState(false);
        } else {
            super.onBackPressed();
        }
    }

    // 点击滤镜后的渲染
    private class ImageRenderTask extends AsyncTask<Void, Void, Bitmap> {
        // 用于生成滤镜效果图片
        private GPUImage gpuImage;
        private Bitmap bitmap;
        private GPUImageFilter filter;

        public ImageRenderTask(Bitmap bitmap, GPUImageFilter filter) {
            this.bitmap = bitmap;
            this.filter = filter;
        }

        @Override
        protected void onPreExecute() {
            // 先生成滤镜效果的主类
            gpuImage = new GPUImage(JigsawModelActivity.this);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            if (null == filter) {
                return bitmap;
            }

            gpuImage.setFilter(filter);

            return gpuImage.getBitmapWithFilterApplied(bitmap);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (null != bitmap) {
                // 将获取的bitmap设置给当前的TouchSlotView
                modelArea.renderSelectedBitmap(bitmap);
            }
        }
    }


    // 保存图片的Task
    private class SavePictureTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String filePath = null;
            try {
                //获取图片
                Bitmap targetBitmap = FileUtils.createBitmapFromView(modelArea);
                // 保存图片到本地
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                Date curTime = new Date();
                String fileName = new StringBuilder("Jigsaw_").append(dateFormat.format(curTime)).append(".jpg").toString();

                // 保存文件，返回路径
                filePath = saveImage(FileConstants.JIGSAW_DIR, fileName, targetBitmap);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if (null == path) {
                // 保存失败
                Toast.makeText(JigsawModelActivity.this, "Save Failed!", Toast.LENGTH_SHORT).show();
            } else {
                // 保存成功
                Toast.makeText(JigsawModelActivity.this, "Save succeed! Path: " + path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 根据路径和文件名保存文件
     *
     * @param folderName
     * @param fileName
     * @param bm
     */
    private String saveImage(String folderName, String fileName, Bitmap bm) {
        if (null == bm) {
            // 保存失败，直接返回
            return null;
        }

        // 先判断文件夹是否存在，不存在要先创建
        File dir = new File(folderName);
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }

        File file = new File(dir, fileName);
        OutputStream out = null;
        try {
            // 封装输出流
            out = new BufferedOutputStream(new FileOutputStream(file));

            if (!bm.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                return null;
            }

            // 转换为数据流后回收bitmap
            if (!bm.isRecycled()) {
                bm.recycle();
            }

            // 扫描新增图片文件
            Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri fileContentUri = Uri.fromFile(file);
            mediaScannerIntent.setData(fileContentUri);
            JigsawModelActivity.this.sendBroadcast(mediaScannerIntent);

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    // 关闭输出流
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
