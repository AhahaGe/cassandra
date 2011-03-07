package org.apache.cassandra.io;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.io.IOException;
import java.security.MessageDigest;

import org.apache.cassandra.db.DecoratedKey;
import org.apache.cassandra.io.util.PageCacheInformer;

/**
 * a CompactedRow is an object that takes a bunch of rows (keys + columnfamilies)
 * and can write a compacted version of those rows to an output stream.  It does
 * NOT necessarily require creating a merged CF object in memory.
 */
public abstract class AbstractCompactedRow
{
    public final DecoratedKey key;
    protected boolean hasColumnsInPageCache = false;

    public AbstractCompactedRow(DecoratedKey key)
    {
        this.key = key;
    }

    /**
     * write the row (size + column index + filter + column data, but NOT row key) to @param out
     */
    public abstract void write(PageCacheInformer out) throws IOException;

    /**
     * update @param digest with the data bytes of the row (not including row key or row size)
     */
    public abstract void update(MessageDigest digest);

    /**
     * @return true if there are no columns in the row AND there are no row-level tombstones to be preserved
     */
    public abstract boolean isEmpty();

    /**
     * @return the number of columns in the row
     */
    public abstract int columnCount();

    /**
     * @return if any columns in this row are in the OS Page Cache
     */
    public boolean hasColumnsInPageCache()
    {
        return hasColumnsInPageCache;
    }

    public void setHasColumnsInPageCache(boolean hasColumnsInPageCache)
    {
        this.hasColumnsInPageCache = hasColumnsInPageCache;
    }

}
