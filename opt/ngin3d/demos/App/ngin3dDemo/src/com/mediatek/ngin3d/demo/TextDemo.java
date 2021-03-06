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

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import com.mediatek.ngin3d.Color;
import com.mediatek.ngin3d.Point;
import com.mediatek.ngin3d.Rotation;
import com.mediatek.ngin3d.Stage;
import com.mediatek.ngin3d.Text;
import com.mediatek.ngin3d.android.StageView;
import com.mediatek.ngin3d.animation.PropertyAnimator;

/**
 * A Text demo with double sided
 */
public class TextDemo extends Activity {

    private Stage mStage = new Stage();
    private StageView mStageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStageView = new StageView(this, mStage);
        setContentView(mStageView);

        for (int x = 0; x <= 800; x += 100) {
            for (int y = 0; y <= 480; y += 80) {
                Text text = new Text(String.format("%d,%d", x, y));
                text.setPosition(new Point(x, y));
                text.setTextSize(16);

                if (y == 80) {
                    text.setBackgroundColor(Color.RED);
                } else if (y == 160) {
                    text.setBackgroundColor(Color.GREEN);
                } else if (y == 240) {
                    text.setBackgroundColor(Color.BLUE);
                } else if (y == 320) {
                    text.setTextColor(new Color(255, 0, 255, 128));
                    text.setBackgroundColor(new Color(255, 255, 255, 128));
                }
                text.setDoubleSided(true);
                mStage.add(text);
            }
        }

        mStage.setPosition(new Point(0.5f, 0, 0, true));
        PropertyAnimator rotation = new PropertyAnimator(mStage, "rotation", new Rotation(0, 0, 0), new Rotation(0, 360, 0));
        rotation.setDuration(2000);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.start();
    }

    @Override
    protected void onPause() {
        mStageView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStageView.onResume();
    }

}
