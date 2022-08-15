package com.example.tetris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tetris.constants.ImageViewConstants;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DashboardServiceTest {

    public final AnimationService animationService = new AnimationService();
    public final SharedPreferencesService sharedPreferencesService = new SharedPreferencesService();
    public final ViewService viewService = new ViewService(sharedPreferencesService);
    public final DashboardService dashboardService = new DashboardService(animationService, sharedPreferencesService, viewService);

    @Test
    public void test_prepareDashboard() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GridLayout gridLayout = new GridLayout(appContext);
        gridLayout.setRowCount(9);
        gridLayout.setColumnCount(9);

        dashboardService.prepareDashboard(appContext, gridLayout);

        assertEquals(81, gridLayout.getChildCount());
    }

    @Test
    public void test_prepareImageView() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ImageView imageView = dashboardService.prepareImageView(appContext);

        assertNotNull(imageView.getDrawable());

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

        assertEquals(ImageViewConstants.IMAGE_VIEW_WIDTH.intValue(), layoutParams.width);
        assertEquals(ImageViewConstants.IMAGE_VIEW_HEIGHT.intValue(), layoutParams.height);
    }

}
