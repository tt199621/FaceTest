package com.example.facetest.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.facetest.R;

import java.util.List;

public class AlertDialogUtils {

    public static AlertDialogUtils getInstance() {
        return new AlertDialogUtils();
    }

    /**
     * 弹出自定义样式的AlertDialog
     *
     * @param context 上下文
     * @param title   AlertDialog的标题
     * @param tv      点击弹出框选择条目后，要改变文字的TextView
     * @param args    作为弹出框中item显示的字符串数组
     */
    public void showAlertDialog(Context context, String title, final TextView tv, final List<String> args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        dialog.show();

        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_salary, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title_alert_dialog_salary);
        ListView list = (ListView) view.findViewById(R.id.lv_alert_dialog_salary);
        tvTitle.setText(title);
        ListAdapter adpter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, args);
        list.setAdapter(adpter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = args.get(position);
                tv.setText(str);
                if (onDialogItemSelectListener != null) {
                    onDialogItemSelectListener.onItemSelect(str);
                }
                dialog.dismiss();
            }
        });

        dialog.getWindow().setContentView(view);
    }

    private OnDialogItemSelectListener onDialogItemSelectListener;

    public void setOnDialogItemSelectListener(AlertDialogUtils.OnDialogItemSelectListener onDialogItemSelectListener) {
        this.onDialogItemSelectListener = onDialogItemSelectListener;
    }

    /**
     * item选中回调接口
     */
    public interface OnDialogItemSelectListener {
        /**
         * item选中回调方法
         *
         * @param str 选中的item中的String
         */
        void onItemSelect(String str);
    }


    /**
     * 带有确认取消按钮的自定义dialog
     *
     * @param context 上下文
     * @param message 显示的信息
     */

    public static AlertDialog.Builder builder1;
    public static AlertDialog alertDialog;
    public static void showConfirmDialog(Context context, String message) {
         builder1 = new AlertDialog.Builder(context);
         alertDialog = builder1.create();
        alertDialog.show();

        View view = View.inflate(context, R.layout.view_alert_dialog_confirm, null);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_message_dialog);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel_dialog);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm_dialog);

        tvMsg.setText(message);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onNegativeButtonClick(alertDialog);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onPositiveButtonClick(alertDialog);
            }
        });

        alertDialog.getWindow().setContentView(view);

    }

    public void closeDialog(){
        alertDialog.dismiss();
    }


    private static OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    /**
     * 按钮点击回调接口
     */
    public interface OnButtonClickListener {
        /**
         * 确定按钮点击回调方法
         *
         * @param dialog 当前 AlertDialog，传入它是为了在调用的地方对 dialog 做操作，比如 dismiss()
         *               也可以在该工具类中直接  dismiss() 掉，就不用将 AlertDialog 对象传出去了
         */
        void onPositiveButtonClick(AlertDialog dialog);

        /**
         * 取消按钮点击回调方法
         *
         * @param dialog 当前AlertDialog
         */
        void onNegativeButtonClick(AlertDialog dialog);
    }

}
