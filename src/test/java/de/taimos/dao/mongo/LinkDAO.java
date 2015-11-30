package de.taimos.dao.mongo;

/*
 * #%L
 * Spring DAO Mongo
 * %%
 * Copyright (C) 2013 - 2015 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class LinkDAO extends AbstractMongoDAO<LinkObject> {
	
	@Override
	protected Class<LinkObject> getEntityClass() {
		return LinkObject.class;
	}
	
	public LinkObject findByName(String name) {
		return this.findFirstByQuery("{name:#}", null, name);
	}
	
}
