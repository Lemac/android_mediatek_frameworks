/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.mediatek.ngin3d.demo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.mediatek.ngin3d.Container;
import com.mediatek.ngin3d.Point;
import com.mediatek.ngin3d.Rotation;
import com.mediatek.ngin3d.Scale;
import com.mediatek.ngin3d.Sphere;
import com.mediatek.ngin3d.android.StageActivity;
import com.mediatek.ngin3d.animation.PropertyAnimation;

/**
 * A demo for show you how to enable stereoscopic 3D effect.
 * Note stereoscopic 3d effect need stereo display hardware support.
 */
public class StereoSpace3DDemo extends StageActivity {

    private static final int EARTH_SIZE = 200;
    private static final int MOON_SIZE = (int) (EARTH_SIZE / 3.66);

    private static final int LOCAL_S3D_2D = 0x00000000;
    private static final int LOCAL_S3D_3D = 0x00080000;
    private static final int LOCAL_S3D_SIDE_BY_SIDE = 0x00200000;
    private static final int LOCAL_S3D_MASK = 0x00FF0000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final Sphere earth = Sphere.createFromResource(getResources(), R.drawable.earth_atmos_2048);
        final Sphere moon = Sphere.createFromResource(getResources(), R.drawable.moon_1024);

        Container container = new Container();
        container.add(moon);

        earth.setScale(new Scale(EARTH_SIZE, EARTH_SIZE, EARTH_SIZE));
        moon.setScale(new Scale(MOON_SIZE, MOON_SIZE, MOON_SIZE));

        container.setPosition(new Point(0.5f, 0.5f, true));
        earth.setPosition(new Point(0.5f, 0.5f, true));
        moon.setPosition(new Point(0.34f, 0f, 134.16f, true));

        mStage.add(earth, container);

        new PropertyAnimation(earth, "rotation", new Rotation(0, 0, 0), new Rotation(0, -360, 0))
            .setDuration(24000).setLoop(true).start();

        new PropertyAnimation(container, "rotation", new Rotation(0, 0, 0), new Rotation(0, -360, 0))
            .setDuration(24000).setLoop(true).start();

        // Enable stereoscopic 3d and set the focal length to be the same as
        // the distance from camera to the center of the model, which is 1111
        // by default.
        mStage.setStereo3D(true, 1111);

        // Set the S3D display rendering flags as defined in
        // android.view.WindowManager. We use hard coded values here as S3D is
        // not supported on all ALPS branches
        //   WindowManager.LayoutParams.FLAG_EX_S3D_3D |
        //   WindowManager.LayoutParams.FLAG_EX_S3D_SIDE_BY_SIDE,
        //   WindowManager.LayoutParams.FLAG_EX_S3D_MASK);
        mStageView.setStereoscopic3dFlags(LOCAL_S3D_3D | LOCAL_S3D_SIDE_BY_SIDE,
                                          LOCAL_S3D_MASK);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stereo3d_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        switch (item_id) {
            case R.id.stereo3d_turnon:
                mStage.setStereo3D(true, 1111);
                mStageView.setStereoscopic3dFlags(LOCAL_S3D_3D | LOCAL_S3D_SIDE_BY_SIDE,
                                                  LOCAL_S3D_MASK);
                break;

            case R.id.stereo3d_turnoff:
                mStage.setStereo3D(false, 0);
                mStageView.setStereoscopic3dFlags(LOCAL_S3D_2D, LOCAL_S3D_MASK);
                break;

            default:
                return false;
        }
        return true;
    }
}
