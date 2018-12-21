/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.handler.component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;
import org.apache.solr.common.SolrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class CachingUserAttributeSource implements UserAttributeSource {

    private static Logger log = LoggerFactory.getLogger(CachingUserAttributeSource.class);
    private static final String DEFAULT_CACHE_BUILDER_SPEC = "maximumSize=1000";
    private final LoadingCache<String, Multimap<String,String>> cache;

    public CachingUserAttributeSource(final UserAttributeSource userAttributeSource) {
        this(userAttributeSource, null);
    }

    public CachingUserAttributeSource(final UserAttributeSource userAttributeSource, String cacheSpec) {
        String spec = (cacheSpec != null) ? cacheSpec : DEFAULT_CACHE_BUILDER_SPEC;
        log.debug("Creating cached user attribute source, userAttributeSource={}, cacheSpec={}", userAttributeSource, spec);
        CacheLoader<String, Multimap<String, String>> cacheLoader = new CacheLoader<String, Multimap<String, String>>() {
            public Multimap<String, String> load(String userName) {
                log.debug("User attribute cache miss for user: {}", userName);
                return userAttributeSource.getAttributesForUser(userName);
            }
        };
        cache = CacheBuilder.from(spec).build(cacheLoader);
    }

    @Override
    public Multimap<String, String> getAttributesForUser(String userName) {
        try {
            return cache.get(userName);
        } catch (ExecutionException e) {
            throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,
                    "Error getting user attributes from cache", e);
        }
    }

    @Override
    public Class<? extends UserAttributeSourceParams> getParamsClass() {
        return null;
    }

    @Override
    public void init(UserAttributeSourceParams params, Collection<String> attributes) {

    }

}
