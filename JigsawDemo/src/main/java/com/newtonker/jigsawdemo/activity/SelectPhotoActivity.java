package com.newtonker.jigsawdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.newtonker.jigsawdemo.R;
import com.newtonker.jigsawdemo.adapter.ModelLinearAdapter;
import com.newtonker.jigsawdemo.adapter.PhotoGridAdapter;
import com.newtonker.jigsawdemo.adapter.PopupDirectoryListAdapter;
import com.newtonker.jigsawdemo.event.OnItemCheckListener;
import com.newtonker.jigsawdemo.event.OnPhotoCheckedChangeListener;
import com.newtonker.jigsawdemo.model.JigsawType;
import com.newtonker.jigsawdemo.model.Photo;
import com.newtonker.jigsawdemo.model.PhotoDirectory;
import com.newtonker.jigsawdemo.utils.MediaStoreHelper;
import com.newtonker.jigsawdemo.utils.TemplateUtils;
import com.newtonker.jigsawdemo.widget.JigsawModelLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片生成模版Activity
 */
public class SelectPhotoActivity extends AppCompatActivity {
    private static final int WRITE_REQUEST_CODE = 2909;

    // 拼图最多可用图片张数
    public final static int DEFAULT_MAX_COUNT = 9;
    public final static String SELECTED_PATHS = "selected_paths";
    public final static String TYPE_OF_JIGSAW = "type_of_jigsaw";
    public final static String ID_OF_TEMPLATE = "id_of_template";
    // 当前拼图模板的类型
    private JigsawType jigsawType;
    // 最多可用图片数目
    private int maxCount = DEFAULT_MAX_COUNT;
    // 图片展示组件
    private RecyclerView photoRecyclerView;
    private PhotoGridAdapter photoGridAdapter;
    private PopupDirectoryListAdapter popupListAdapter;
    private ModelLinearAdapter modelLinearAdapter;
    // 图片文件夹
    private List<PhotoDirectory> directories;
    // 已选图片列表
    private ArrayList<String> selectedPhotosPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
            } else {
                // continue with your code
                init();
            }
        } else {
            // continue with your code
            init();
        }

    }

    private void init() {
        // 初始化
        directories = new ArrayList<>();
        selectedPhotosPath = new ArrayList<>();

        MediaStoreHelper.getPhotoDirs(this, new MediaStoreHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<PhotoDirectory> directories) {
                SelectPhotoActivity.this.directories.clear();
                SelectPhotoActivity.this.directories.addAll(directories);
                photoGridAdapter.notifyDataSetChanged();
                popupListAdapter.notifyDataSetChanged();
            }
        });

        initView();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化界面
     */
    Button btn, btn1;

    private void initView() {
//        // 拼图模板展示区
//        RecyclerView modelRecyclerView = (RecyclerView) findViewById(R.id.model_area);
//        // 适配器
//        modelRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        modelRecyclerView.setHasFixedSize(true);
//        modelLinearAdapter = new ModelLinearAdapter(this);
//        modelRecyclerView.setAdapter(modelLinearAdapter);
//        modelLinearAdapter.setOnModelItemClickListener(new OnModelItemClickListener()
//        {
//            @Override
//            public void onClick(View view, int position)
//            {
//                // 跳转到另一个界面去操作
//                Intent intent = new Intent(SelectPhotoActivity.this, JigsawModelActivity.class);
//                intent.putStringArrayListExtra(SELECTED_PATHS, selectedPhotosPath);
//                intent.putExtra(TYPE_OF_JIGSAW, jigsawType);
//                intent.putExtra(ID_OF_TEMPLATE, position);
//                startActivity(intent);
//            }
//        });

        btn = (Button) findViewById(R.id.btn_grid);
        btn1 = (Button) findViewById(R.id.btn_drag);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到另一个界面去操作
                Intent intent = new Intent(SelectPhotoActivity.this, GridAct.class);
                intent.putStringArrayListExtra(SELECTED_PATHS, selectedPhotosPath);
                startActivity(intent);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到另一个界面去操作
                Intent intent = new Intent(SelectPhotoActivity.this, DragAct.class);
                intent.putStringArrayListExtra(SELECTED_PATHS, selectedPhotosPath);
                startActivity(intent);
            }
        });

        // 图片展示区
        photoRecyclerView = (RecyclerView) findViewById(R.id.photo_area);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        photoRecyclerView.setLayoutManager(layoutManager);
        // 图片展示适配器
        photoGridAdapter = new PhotoGridAdapter(this, directories);
        photoRecyclerView.setAdapter(photoGridAdapter);
        photoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean OnItemCheck(int position, Photo path, boolean isCheck, int selectedItemCount) {
                int total = selectedItemCount + (isCheck ? -1 : 1);

                if (total > maxCount) {
                    Toast.makeText(SelectPhotoActivity.this, getString(R.string.over_max_count_tips, maxCount), Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
        photoGridAdapter.setOnPhotoCheckedChangeListener(new OnPhotoCheckedChangeListener() {
            @Override
            public void onCheckedChange(ArrayList<String> paths) {
                selectedPhotosPath.clear();
                // 获取所选图片的路径
                selectedPhotosPath.addAll(paths);
                btn.setText(paths.size() + " , click to grid them");
                btn1.setText(paths.size() + " , click to drag them");
                // 刷新拼图列表
                jigsawType = TemplateUtils.getJigsawType(selectedPhotosPath.size());

                List<JigsawModelLayout> models = TemplateUtils.getSlotLayoutList(SelectPhotoActivity.this, jigsawType, selectedPhotosPath);

//                modelLinearAdapter.setModels(models);
//                modelLinearAdapter.notifyDataSetChanged();
            }
        });

        // 切换图片文件夹按钮
        final Button btSwitchDirectory = (Button) findViewById(R.id.button);
        // 图片列表弹窗
        popupListAdapter = new PopupDirectoryListAdapter(this, directories);
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setAnchorView(btSwitchDirectory);
        listPopupWindow.setAdapter(popupListAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);

        btSwitchDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else if (!isFinishing()) {
                    listPopupWindow.setHeight(Math.round(photoRecyclerView.getHeight()));
                    listPopupWindow.show();
                }
            }
        });

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                PhotoDirectory directory = directories.get(position);
                btSwitchDirectory.setText(directory.getName());
                photoGridAdapter.setCurrentDirectoryIndex(position);
                photoGridAdapter.notifyDataSetChanged();
            }
        });
    }
}
