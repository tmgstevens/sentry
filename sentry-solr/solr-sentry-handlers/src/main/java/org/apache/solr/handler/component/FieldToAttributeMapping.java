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

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.regex.Pattern;

public class FieldToAttributeMapping {

  private static final Splitter ATTR_NAME_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

  private final String fieldName;
  private final Collection<String> attributes;
  private final FilterType filterType;
  private final boolean acceptEmpty;
  private final String allUsersValue;
  private final Pattern attrValueRegex;
  private final String extraOpts;

  enum FilterType {
    AND,
    OR,
    LTE,
    GTE
  }

  public FieldToAttributeMapping(String fieldName, String ldapAttributeNames, String filterType, boolean acceptEmpty, String allUsersValue, String valueFilterRegex, String extraOpts) {
    this.fieldName = fieldName;
    this.attributes = Sets.newHashSet(ATTR_NAME_SPLITTER.split(ldapAttributeNames));
    this.filterType = FilterType.valueOf(filterType);
    this.acceptEmpty = acceptEmpty;
    this.allUsersValue = allUsersValue;
    this.attrValueRegex = Pattern.compile(valueFilterRegex);
    this.extraOpts = extraOpts;
  }

  public String getFieldName() {
    return fieldName;
  }

  public Collection<String> getAttributes() {
    return attributes;
  }

  public FilterType getFilterType() {
    return filterType;
  }

  public boolean getAcceptEmpty() {
    return acceptEmpty;
  }

  public String getAllUsersValue() {
    return allUsersValue;
  }

  public Pattern getAttrValueRegex() {
    return attrValueRegex;
  }

  public String getExtraOpts() {
    return extraOpts;
  }
}
