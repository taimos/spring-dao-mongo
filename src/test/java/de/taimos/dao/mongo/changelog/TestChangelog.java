package de.taimos.dao.mongo.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.DB;

import de.taimos.dao.mongo.ChangelogUtil;

@ChangeLog
public class TestChangelog {
	
	@ChangeSet(order = "001", id = "index1", author = "thoeger")
	public void index1(DB db) {
		ChangelogUtil.addIndex(db.getCollection("TestObject"), "name", true, true);
	}
}
