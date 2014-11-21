/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehcache.resilience;

import java.util.Map;

import org.ehcache.Cache;
import org.ehcache.exceptions.BulkCacheLoaderException;
import org.ehcache.exceptions.BulkCacheWriterException;
import org.ehcache.exceptions.CacheAccessException;
import org.ehcache.exceptions.CacheWriterException;

/**
 * A strategy for providing cache resilience in the face of failure.
 * <p>
 * An implementation of this interface is used by a cache to decide how to
 * recover after internal components of the cache fail.  Implementations of
 * these methods are expected to take suitable recovery steps.  They can then
 * choose between allowing the operation to terminate successfully, or throw an
 * exception which will be propagated to the thread calling in to the cache.
 * 
 * @author Chris Dennis
 */
public interface ResilienceStrategy<K, V> {
  
  /**
   * Called when a {@link Cache#get(java.lang.Object)} fails on a cache without
   * a cache loader due to an underlying store failure.
   * 
   * @param key the key being retrieved
   * @param e the triggered failure
   * @return the value to return from the operation
   */
  V getFailure(K key, CacheAccessException e);
  
  /**
   * Called when a {@link Cache#get(java.lang.Object)} fails on a cache with a
   * cache loader due to an underlying store failure.
   * 
   * @param key the key being retrieved
   * @param loaded the value from the loader
   * @param e the triggered failure
   * @return the value to return from the operation
   */
  V getFailure(K key, V loaded, CacheAccessException e);
  
  /**
   * Called when a {@link Cache#containsKey(java.lang.Object)} fails due to an
   * underlying store failure.
   * 
   * @param key the key being queried
   * @param e the triggered failure
   * @return the value to return from the operation
   */
  boolean containsKeyFailure(K key, CacheAccessException e);
  
  /**
   * Called when a {@link Cache#put(java.lang.Object, java.lang.Object)} fails
   * due to an underlying store failure.
   * 
   * @param key the key being put
   * @param value the value being put
   * @param e the triggered failure
   */
  void putFailure(K key, V value, CacheAccessException e);

  /**
   * Called when a {@link Cache#put(java.lang.Object, java.lang.Object)} fails
   * due to an underlying store failure, and the associated cache write
   * operation also failed.
   * 
   * @param key the key being put
   * @param value the value being put
   * @param e the cache failure
   * @param f the writer failure
   */
  void putFailure(K key, V value, CacheAccessException e, CacheWriterException f);

  /**
   * Called when a {@link Cache#remove(java.lang.Object)} fails due to an
   * underlying store failure.
   * 
   * @param key the key being removed
   * @param e the triggered failure
   */
  void removeFailure(K key, CacheAccessException e);

  /**
   * Called when a {@link Cache#remove(java.lang.Object)} fails
   * due to an underlying store failure, and the associated cache write
   * operation also failed.
   * 
   * @param key the key being removed
   * @param e the cache failure
   * @param f the writer failure
   */
  void removeFailure(K key, CacheAccessException e, CacheWriterException f);
 
  /**
   * Called when a {@link Cache#clear()} fails due to an underlying store
   * failure.
   * 
   * @param e the triggered failure
   */
  void clearFailure(CacheAccessException e);

  /**
   * Called when a cache iterator advancement fails due to an underlying store
   * failure.
   * 
   * @param e the triggered failure
   */
  void iteratorFailure(CacheAccessException e);

  /**
   * Called when a {@link Cache#putIfAbsent(java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure.
   * 
   * @param key the key being put
   * @param value the value being put
   * @param e the triggered failure
   * @param knownToBeAbsent {@code true} if the value is known to be absent
   * @return the value to return from the operation
   */
  V putIfAbsentFailure(K key, V value, CacheAccessException e, boolean knownToBeAbsent);

  /**
   * Called when a {@link Cache#putIfAbsent(java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure, and the associated cache write
   * operation also failed.
   * 
   * @param key the key being put
   * @param value the value being put
   * @param e the cache failure
   * @param f the writer failure
   * @return the value to return from the operation
   */
  V putIfAbsentFailure(K key, V value, CacheAccessException e, CacheWriterException f);
  
  /**
   * Called when a {@link Cache#remove(java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure.
   * 
   * @param key the key being removed
   * @param value the value being removed
   * @param e the triggered failure
   * @param knownToBePresent {@code true} if the value is known to be present
   * @return the value to return from the operation
   */
  boolean removeFailure(K key, V value, CacheAccessException e, boolean knownToBePresent);

  /**
   * Called when a {@link Cache#remove(java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure, and the associated cache write
   * operation also failed.
   * 
   * @param key the key being removed
   * @param value the value being removed
   * @param e the cache failure
   * @param f the writer failure
   * @return the value to return from the operation
   */
  boolean removeFailure(K key, V value, CacheAccessException e, CacheWriterException f);
  
  /**
   * Called when a {@link Cache#replace(java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure.
   * 
   * @param key the key being replaced
   * @param value the value being replaced
   * @param e the triggered failure
   * @return the value to return from the operation
   */
  V replaceFailure(K key, V value, CacheAccessException e);

  /**
   * Called when a {@link Cache#replace(java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure, and the associated cache write
   * operation also failed.
   * 
   * @param key the key being replaced
   * @param value the value being replaced
   * @param e the cache failure
   * @param f the writer failure
   * @return the value to return from the operation
   */
  V replaceFailure(K key, V value, CacheAccessException e, CacheWriterException f);
  
  /**
   * Called when a {@link Cache#replace(java.lang.Object, java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure.
   * 
   * @param key the key being replaced
   * @param value the expected value
   * @param newValue the replacement value
   * @param e the triggered failure
   * @param knownToMatch {@code true} if the value is known to match
   * @return the value to return from the operation
   */
  boolean replaceFailure(K key, V value, V newValue, CacheAccessException e, boolean knownToMatch);

  /**
   * Called when a {@link Cache#replace(java.lang.Object, java.lang.Object, java.lang.Object)}
   * fails due to an underlying store failure, and the associated cache write
   * operation also failed.
   * 
   * @param key the key being replaced
   * @param value the expected value
   * @param newValue the replacement value
   * @param e the cache failure
   * @param f the writer failure
   * @return the value to return from the operation
   */
  boolean replaceFailure(K key, V value, V newValue, CacheAccessException e, CacheWriterException f);
  
  /**
   * Called when a {@link Cache#getAll(java.lang.Iterable)} fails on a cache
   * without a cache loader due to an underlying store failure.
   * 
   * @param keys the keys being retrieved
   * @param e the triggered failure
   * @return the value to return from the operation
   */
  Map<K, V> getAllFailure(Iterable<? extends K> keys, CacheAccessException e);

  /**
   * Called when a {@link Cache#getAll(java.lang.Iterable)} fails on a cache
   * with a cache loader due to an underlying store failure.
   * 
   * @param keys the keys being retrieved
   * @param loaded the values from the loader
   * @param e the triggered failure
   * @return the value to return from the operation
   */
  Map<K, V> getAllFailure(Iterable<? extends K> keys, Map<K, V> loaded, CacheAccessException e);
  
  /**
   * Called when a {@link Cache#getAll(java.lang.Iterable)} fails on a cache
   * with a cache loader due to an underlying store failure, and the associated
   * cache write operation also failed.
   * 
   * @param keys the keys being retrieved
   * @param e the cache failure
   * @param f the writer failure
   * @return the value to return from the operation
   */
  Map<K, V> getAllFailure(Iterable<? extends K> keys, CacheAccessException e, BulkCacheLoaderException f);
  
  /**
   * Called when a {@link Cache#putAll(java.lang.Iterable)} fails due to an
   * underlying store failure.
   * 
   * @param entries the entries being put
   * @param e the triggered failure
   */
  void putAllFailure(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, CacheAccessException e);

  /**
   * Called when a {@link Cache#putAll(java.lang.Iterable)} fails due to an
   * underlying store failure, and the associated cache write operation also
   * failed.
   * 
   * @param entries the entries being put
   * @param e the cache failure
   * @param f the writer failure
   */
  void putAllFailure(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, CacheAccessException e, BulkCacheWriterException f);

  /**
   * Called when a {@link Cache#removeAll(java.lang.Iterable)} fails due to an
   * underlying store failure.
   * 
   * @param keys the keys being removed
   * @param e the triggered failure
   * @return the value to return from the operation
   */
  Map<K, V> removeAllFailure(Iterable<? extends K> keys, CacheAccessException e);

  /**
   * Called when a {@link Cache#removeAll(java.lang.Iterable)} fails
   * due to an underlying store failure, and the associated cache write
   * operation also failed.
   * 
   * @param keys the keys being removed
   * @param e the cache failure
   * @param f the writer failure
   * @return the value to return from the operation
   */
  Map<K, V> removeAllFailure(Iterable<? extends K> keys, CacheAccessException e, BulkCacheWriterException f);
}
