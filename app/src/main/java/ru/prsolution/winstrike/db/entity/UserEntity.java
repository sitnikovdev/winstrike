/*
 * Copyright 2017, The Android Open Source Project
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

package ru.prsolution.winstrike.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import ru.prsolution.winstrike.model.UserDb;


@Entity(tableName = "user")
public class UserEntity implements UserDb {
    @PrimaryKey
    private int id;
    private String name;
    private String phone;
    private String publickId;
    private String token;
    private Boolean confirmed;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublickId() {
        return publickId;
    }

    public void setPublickId(String publickId) {
        this.publickId = publickId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public Boolean getConfirmed() {
        return this.confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }


    @Override
    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity() {
    }

    @Ignore
    public UserEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public UserEntity(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
    }

}
