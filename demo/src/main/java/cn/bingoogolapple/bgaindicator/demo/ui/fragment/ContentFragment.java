package cn.bingoogolapple.bgaindicator.demo.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;
import cn.bingoogolapple.bgaindicator.demo.R;

@BGAALayout(R.layout.fragment_content)
public class ContentFragment extends BaseFragment {
    @BGAAView(R.id.tv_content_tag)
    private TextView mTagTv;
    public int mPosition;

    public static ContentFragment newInstance(int position) {
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.mPosition = position;
        return contentFragment;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mTagTv.setText("第" + mPosition + "页");
    }
}
