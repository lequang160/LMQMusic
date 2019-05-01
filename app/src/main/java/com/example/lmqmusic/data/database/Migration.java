package com.example.lmqmusic.data.database;

import android.support.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {
            RealmObjectSchema songRealmObject = schema.get("SongRealmObject");
            if(songRealmObject ==null) return;
            songRealmObject
                    .addField("streamType", int.class);
            if(songRealmObject.hasField("playing")){
                songRealmObject.removeField("playing");
            }
            oldVersion++;
        }
    }
}
