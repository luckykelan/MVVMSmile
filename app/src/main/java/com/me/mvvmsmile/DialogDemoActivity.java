package com.me.mvvmsmile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kelan.mvvmsmile.widget.dialog.SmileDialog;
import com.kelan.mvvmsmile.widget.dialog.SmileDialogAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogDemoActivity extends AppCompatActivity {


    @BindView(R.id.listview)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_demo);
        ButterKnife.bind(this);
        initListView();
    }

    void initListView() {
        String[] listItems = new String[]{
                "消息类型对话框（蓝色按钮）",
                "消息类型对话框（红色按钮）",
                "菜单类型对话框",
                "带 Checkbox 的消息确认框",
                "单选菜单类型对话框",
                "多选菜单类型对话框",
                "多选菜单类型对话框(item 数量很多)",
                "带输入框的对话框",
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        mListView.setAdapter(new ArrayAdapter<>(this, R.layout.simple_list_item, data));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showMessagePositiveDialog();
                        break;
                    case 1:
                        showMessageNegativeDialog();
                        break;
                    case 2:
                        showMenuDialog();
                        break;
                    case 3:
                        showConfirmMessageDialog();
                        break;
                    case 4:
                        showSingleChoiceDialog();
                        break;
                     case 5:
                        showMultiChoiceDialog();
                        break;
                    case 6:
                        showNumerousMultiChoiceDialog();
                        break;
                    case 7:
                        showEditTextDialog();
                        break;
                }
            }
        });
    }

    /**
     * 消息类型对话框（蓝色按钮）
     */
    private void showMessagePositiveDialog() {
        new SmileDialog.MessageDialogBuilder(this)
                .setTitle("标题")
                .setMessage("确定要发送吗？")
                .addAction("取消", new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        dialog.dismiss();
                        Toast.makeText(DialogDemoActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .create(R.style.SmileUI_Dialog)//可写可不写
                .show();
    }


    /**
     * 消息类型对话框（红色按钮）
     */
    private void showMessageNegativeDialog() {
        new SmileDialog.MessageDialogBuilder(this)
                .setTitle("标题")
                .setMessage("确定要删除吗？")
                .addAction("取消", new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", SmileDialogAction.ACTION_PROP_NEGATIVE, new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        Toast.makeText(DialogDemoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 带 Checkbox 的消息确认框
     */
    private void showConfirmMessageDialog() {
        new SmileDialog.CheckBoxMessageDialogBuilder(this)
                .setTitle("退出后是否删除账号信息?")
                .setMessage("删除账号信息")
                .setChecked(true)
                .addAction("取消", new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("退出", new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 菜单类型对话框
     */
    private void showMenuDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3"};
        new SmileDialog.MenuDialogBuilder(this)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DialogDemoActivity.this, "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create(R.style.SmileUI_Dialog)
                .show();
    }

    /**
     * 单选菜单类型对话框
     */
    private void showSingleChoiceDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3"};
        final int checkedIndex = 1;
        new SmileDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DialogDemoActivity.this, "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 多选菜单类型对话框
     */
    private void showMultiChoiceDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3", "选项4", "选项5", "选项6"};
        final SmileDialog.MultiCheckableDialogBuilder builder = new SmileDialog.MultiCheckableDialogBuilder(this)
                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new SmileDialogAction.ActionListener() {
            @Override
            public void onClick(SmileDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("提交", new SmileDialogAction.ActionListener() {
            @Override
            public void onClick(SmileDialog dialog, int index) {
                String result = "你选择了 ";
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                }
                Toast.makeText(DialogDemoActivity.this, result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 多选菜单类型对话框(item 数量很多)
     */
    private void showNumerousMultiChoiceDialog() {
        final String[] items = new String[]{
                "选项1", "选项2", "选项3", "选项4", "选项5", "选项6",
                "选项7", "选项8", "选项9", "选项10", "选项11", "选项12",
                "选项13", "选项14", "选项15", "选项16", "选项17", "选项18"
        };
        final SmileDialog.MultiCheckableDialogBuilder builder = new SmileDialog.MultiCheckableDialogBuilder(this)
                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new SmileDialogAction.ActionListener() {
            @Override
            public void onClick(SmileDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("提交", new SmileDialogAction.ActionListener() {
            @Override
            public void onClick(SmileDialog dialog, int index) {
                String result = "你选择了 ";
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                }
                Toast.makeText(DialogDemoActivity.this, result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 带输入框的对话框
     */
    private void showEditTextDialog() {
        final SmileDialog.EditTextDialogBuilder builder = new SmileDialog.EditTextDialogBuilder(this);
        builder.setTitle("标题")
                .setPlaceholder("在此输入您的昵称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new SmileDialogAction.ActionListener() {
                    @Override
                    public void onClick(SmileDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            Toast.makeText(DialogDemoActivity.this, "您的昵称: " + text, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(DialogDemoActivity.this, "请填入昵称", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }


}
