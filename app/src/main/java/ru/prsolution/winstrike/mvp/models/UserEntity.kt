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

package ru.prsolution.winstrike.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import ru.prsolution.winstrike.model.UserDb


@Entity(tableName = "user")
class UserEntity : UserDb {
    @PrimaryKey
    private var id: Int = 0
    private var name: String? = null
    private var phone: String? = null
    private var publickId: String? = null
    private var token: String? = null
    private var confirmed: Boolean? = null

    override fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    override fun getPublickId(): String? {
        return publickId
    }

    fun setPublickId(publickId: String) {
        this.publickId = publickId
    }

    override fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }


    override fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    override fun getConfirmed(): Boolean? {
        return this.confirmed
    }

    fun setConfirmed(confirmed: Boolean?) {
        this.confirmed = confirmed
    }


    override fun getToken(): String? {
        return token
    }


    fun setToken(token: String) {
        this.token = token
    }

    constructor() {}

    @Ignore
    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

    @Ignore
    constructor(user: UserEntity) {
        this.id = user.getId()
        this.name = user.getName()
    }

}
