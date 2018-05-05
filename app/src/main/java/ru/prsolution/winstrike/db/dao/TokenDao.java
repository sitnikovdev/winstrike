package ru.prsolution.winstrike.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import ru.prsolution.winstrike.db.entity.TokenEntity;

@Dao
public interface TokenDao {

    @Query("SELECT * FROM token LIMIT 1")
    TokenEntity getToken();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveToken(TokenEntity token);

    @Query("DELETE FROM token")
    void truncateTable();

}
