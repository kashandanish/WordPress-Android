package org.wordpress.android.ui.plans;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.wordpress.android.R;
import org.wordpress.android.WordPress;
import org.wordpress.android.ui.ActivityLauncher;
import org.wordpress.android.ui.themes.ThemeWebActivity;
import org.wordpress.android.util.AppLog;
import org.wordpress.android.util.DisplayUtils;

/**
 * single page within the post-purchase activity's ViewPager
 */
public class PlanPostPurchaseFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";
    private int mPageNumber;

    static PlanPostPurchaseFragment newInstance(int pageNumber) {
        PlanPostPurchaseFragment fragment = new PlanPostPurchaseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mPageNumber = args.getInt(ARG_PAGE_NUMBER);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_PAGE_NUMBER, mPageNumber);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPageNumber = savedInstanceState.getInt(ARG_PAGE_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.plan_post_purchase_fragment, container, false);

        ImageView image = (ImageView) rootView.findViewById(R.id.image);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.text_title);
        TextView txtDescription = (TextView) rootView.findViewById(R.id.text_description);
        Button button = (Button) rootView.findViewById(R.id.button);

        int titleResId;
        int textResId;
        int buttonResId;
        int imageResId;
        boolean adjustImageForLandscape;
        switch (mPageNumber) {
            case PlanPostPurchaseActivity.PAGE_NUMBER_INTRO:
                titleResId = R.string.plans_post_purchase_title_intro;
                textResId = R.string.plans_post_purchase_text_intro;
                buttonResId = 0;
                imageResId = R.drawable.plans_business_active;
                adjustImageForLandscape = false;
                break;
            case PlanPostPurchaseActivity.PAGE_NUMBER_CUSTOMIZE:
                titleResId = R.string.plans_post_purchase_title_customize;
                textResId = R.string.plans_post_purchase_text_customize;
                buttonResId = R.string.plans_post_purchase_button_customize;
                imageResId = R.drawable.plans_customize;
                adjustImageForLandscape = true;
                break;
            case PlanPostPurchaseActivity.PAGE_NUMBER_VIDEO:
                titleResId = R.string.plans_post_purchase_title_video;
                textResId = R.string.plans_post_purchase_text_video;
                buttonResId = R.string.plans_post_purchase_button_video;
                imageResId = R.drawable.plans_video_upload;
                adjustImageForLandscape = false;
                break;
            case PlanPostPurchaseActivity.PAGE_NUMBER_THEMES:
                titleResId = R.string.plans_post_purchase_title_themes;
                textResId = R.string.plans_post_purchase_text_themes;
                buttonResId = R.string.plans_post_purchase_button_themes;
                imageResId = R.drawable.plans_premium_themes;
                adjustImageForLandscape = true;
                break;
            default:
                AppLog.w(AppLog.T.PLANS, "invalid plans post-purchase page");
                throw new IllegalArgumentException("invalid plans post-purchase page");
        }

        txtTitle.setText(titleResId);
        txtDescription.setText(textResId);
        image.setImageResource(imageResId);

        if (buttonResId != 0) {
            button.setVisibility(View.VISIBLE);
            button.setText(buttonResId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleButtonClick();
                }
            });
        } else {
            button.setVisibility(View.GONE);
        }

        // hide image in landscape if it would take up too much horizontal space
        if (adjustImageForLandscape && DisplayUtils.isLandscape(getActivity())) {
            image.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void handleButtonClick() {
        switch (mPageNumber) {
            case PlanPostPurchaseActivity.PAGE_NUMBER_CUSTOMIZE:
                ThemeWebActivity.openCurrentTheme(getActivity(), ThemeWebActivity.ThemeWebActivityType.PREVIEW);
                break;
            case PlanPostPurchaseActivity.PAGE_NUMBER_THEMES:
                ActivityLauncher.viewCurrentBlogThemes(getActivity());
                break;
            case PlanPostPurchaseActivity.PAGE_NUMBER_VIDEO:
                ActivityLauncher.addNewBlogPostOrPageForResult(getActivity(), WordPress.currentBlog, false);
                break;
        }

        // the user launched another activity, so we close this one
        getActivity().finish();
    }
}
