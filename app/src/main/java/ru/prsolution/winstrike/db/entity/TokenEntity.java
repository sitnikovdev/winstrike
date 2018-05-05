package ru.prsolution.winstrike.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ru.prsolution.winstrike.model.TokenDb;

@Entity(tableName = "token")
public class TokenEntity implements TokenDb {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "apiToken")
    public String apiToken;

    public TokenEntity(String token) {
        this.apiToken = apiToken;
    }

    public String getToken() {
        return apiToken;
    }

    public TokenEntity() {
    }
}
