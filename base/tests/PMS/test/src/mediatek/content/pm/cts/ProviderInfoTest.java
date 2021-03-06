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

package mediatek.content.pm.cts;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcel;
import android.test.AndroidTestCase;

import java.util.Iterator;
import java.util.List;

public class ProviderInfoTest extends AndroidTestCase {
    private static final String PACKAGE_NAME = "com.mediatek.cts.pms.stub";
    private static final String PROVIDER_NAME = "android.content.cts.MockContentProvider";

    public void testProviderInfo() throws NameNotFoundException {
        PackageManager pm = getContext().getPackageManager();
        Parcel p = Parcel.obtain();
        // Test ProviderInfo()
        new ProviderInfo();
        // Test other methods
        ApplicationInfo appInfo = pm.getApplicationInfo(PACKAGE_NAME, 0);
        List<ProviderInfo> providers = pm.queryContentProviders(PACKAGE_NAME, appInfo.uid, 0);
        Iterator<ProviderInfo> providerIterator = providers.iterator();
        ProviderInfo current;
        while (providerIterator.hasNext()) {
            current = providerIterator.next();
            if (current.name.equals(PROVIDER_NAME)) {
                checkProviderInfoMethods(current, p);
                break;
            }
        }
    }

    private void checkProviderInfoMethods(ProviderInfo providerInfo, Parcel p) {
        // Test toString, describeContents
        assertNotNull(providerInfo.toString());
        assertEquals(0, providerInfo.describeContents());

        // Test ProviderInfo(ProviderInfo orig)
        ProviderInfo infoFromExisted = new ProviderInfo(providerInfo);
        checkInfoSame(providerInfo, infoFromExisted);

        // Test writeToParcel
        providerInfo.writeToParcel(p, 0);
        p.setDataPosition(0);
        ProviderInfo infoFromParcel = ProviderInfo.CREATOR.createFromParcel(p);
        checkInfoSame(providerInfo, infoFromParcel);
        p.recycle();
    }

    private void checkInfoSame(ProviderInfo expected, ProviderInfo actual) {
        assertEquals(expected.name, actual.name);
        assertEquals(expected.authority, actual.authority);
        assertEquals(expected.readPermission, actual.readPermission);
        assertEquals(expected.writePermission, actual.writePermission);
        assertEquals(expected.grantUriPermissions, actual.grantUriPermissions);
        assertEquals(expected.uriPermissionPatterns, actual.uriPermissionPatterns);
        assertEquals(expected.multiprocess, actual.multiprocess);
        assertEquals(expected.initOrder, actual.initOrder);
        assertEquals(expected.isSyncable, actual.isSyncable);
    }
}
