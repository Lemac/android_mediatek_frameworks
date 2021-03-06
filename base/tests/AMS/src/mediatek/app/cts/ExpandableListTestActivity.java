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

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mediatek.app.cts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.android.internal.R;
import com.android.internal.view.menu.ContextMenuBuilder;
import com.google.android.collect.Lists;

public class ExpandableListTestActivity extends ExpandableListActivity {
    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";
    private boolean mOnContentChangedCalled = false;
    private boolean mOnCreateContextMenuCalled = false;
    private boolean mOnGroupCollapseCalled = false;
    private boolean mOnGroupExpandCalled = false;
    private ExpandableListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<Map<String, String>> groupData = Lists.newArrayList();
        final List<List<Map<String, String>>> childData = Lists.newArrayList();
        for (int i = 0; i < 20; i++) {
            final Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, "Group " + i);
            curGroupMap.put(IS_EVEN, (i % 2 == 0) ? "This group is even" : "This group is odd");

            final List<Map<String, String>> children = Lists.newArrayList();
            for (int j = 0; j < 15; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, "Child " + j);
                curChildMap.put(IS_EVEN, (j % 2 == 0) ? "This child is even" : "This child is odd");
            }
            childData.add(children);
        }

        // Set up our adapter
        mAdapter = new SimpleExpandableListAdapter(this, groupData,
                R.layout.simple_expandable_list_item_1,
                new String[] { NAME, IS_EVEN }, new int[] { R.id.text1, R.id.text2 }, childData,
                R.layout.simple_expandable_list_item_2,
                new String[] { NAME, IS_EVEN }, new int[] { R.id.text1, R.id.text2 });
        setListAdapter(mAdapter);

    }

    private int testCallback() {
        final ExpandableListView v = getExpandableListView();
        final ExpandableListAdapter a = getExpandableListAdapter();
        final View convertView = new View(this);
        final View gv = a.getGroupView(0, true, convertView, v);
        v.setOnCreateContextMenuListener(this);
        v.createContextMenu(new ContextMenuBuilder(this));
        for (int i = 0; i < 20; i++) {
            v.expandGroup(i);
            v.performClick();
            v.performLongClick();
            for (int k = 0; k < 15; k++) {
                v.performItemClick(gv, i, k);
            }
            v.collapseGroup(i);
        }
        if (mOnContentChangedCalled && mOnCreateContextMenuCalled
                && mOnGroupCollapseCalled && mOnGroupExpandCalled)
            return RESULT_OK;

        return RESULT_CANCELED;
    }

    private int testView() {
        final ExpandableListView currentView = getExpandableListView();
        for (int i = 0; i < 20; i++) {
            if (!currentView.expandGroup(i))
                return RESULT_CANCELED;
            if (!currentView.collapseGroup(i))
                return RESULT_CANCELED;
        }
        final View otherView = findViewById(android.R.id.list);
        setContentView(otherView);
        if (!otherView.equals(getExpandableListView()))
            return RESULT_CANCELED;
        setContentView(currentView);
        return RESULT_OK;
    }

    private int testSelecte() {
        final ExpandableListView v = getExpandableListView();
        for (int i = 0; i < 20; i++) {
            v.expandGroup(i);
            setSelectedGroup(i);
            for (int k = 0; k < 15; k++) {
                setSelectedChild(i, k, false);
                if (ExpandableListView.getPackedPositionForChild(i, k) != getSelectedPosition())
                    return RESULT_CANCELED;
            }

            for (int k = 0; k < 15; k++) {
                setSelectedChild(i, k, true);
                if (ExpandableListView.getPackedPositionForChild(i, k) != getSelectedPosition())
                    return RESULT_CANCELED;
            }
            v.collapseGroup(i);
        }
        return RESULT_OK;
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String action = getIntent().getAction();
        if (LaunchpadActivity.EXPANDLIST_SELECT.equals(action)) {
            setResult(testSelecte());
        } else if (LaunchpadActivity.EXPANDLIST_VIEW.equals(action)) {
            setResult(testView());
        } else if (LaunchpadActivity.EXPANDLIST_CALLBACK.equals(action)) {
            setResult(testCallback());
        }
        Looper.myQueue().addIdleHandler(new Idler());
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onContentChanged() {
        mOnContentChangedCalled = true;
        super.onContentChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        mOnCreateContextMenuCalled = true;
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        mOnGroupCollapseCalled = true;
        super.onGroupCollapse(groupPosition);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        mOnGroupExpandCalled = true;
        super.onGroupExpand(groupPosition);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onStop() {
        super.onStop();
    }

    private class Idler implements MessageQueue.IdleHandler {
        public final boolean queueIdle() {
            finish();
            return false;
        }
    }

}
