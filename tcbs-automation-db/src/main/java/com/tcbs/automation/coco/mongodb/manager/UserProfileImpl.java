package com.tcbs.automation.coco.mongodb.manager;

import com.mongodb.MongoClient;
import com.tcbs.automation.coco.mongodb.model.UserProfile;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.tcbs.automation.config.coco.CocoServiceConfig.MONGO_DB_NAME;

@Component
public class UserProfileImpl implements UserProfileManager {
  private Datastore database;

  public UserProfileImpl(MongoClient client) {
    Morphia morphia = new Morphia();
    this.database = morphia.createDatastore(client, MONGO_DB_NAME);
  }

  @Override
  public Optional<UserProfile> findRatingUserById(String copierRatingTcbsId) {
    return Optional.ofNullable(this.database.createQuery(UserProfile.class)
      .field("id")
      .equal(copierRatingTcbsId)
      .get());
  }
}
