package com.srainbow.leisureten.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by SRainbow on 2017/6/1.
 */

public class SongMenu extends RealmObject {

    @PrimaryKey
    private long menuId;
    private String menuName;

    public long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
