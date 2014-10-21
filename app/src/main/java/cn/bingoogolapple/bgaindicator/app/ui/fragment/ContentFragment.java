package cn.bingoogolapple.bgaindicator.app.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import cn.bingoogolapple.bgaindicator.app.R;
import cn.bingoogolapple.loon.library.LoonLayout;
import cn.bingoogolapple.loon.library.LoonView;

/**
 * Created by bingoogolapple on 14/10/21.
 */
@LoonLayout(id = R.layout.fragment_content)
public class ContentFragment extends BaseFragment {
    @LoonView(id = R.id.tv_content_tag)
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
