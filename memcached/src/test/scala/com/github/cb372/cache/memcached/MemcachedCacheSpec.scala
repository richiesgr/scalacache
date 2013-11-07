package com.github.cb372.cache.memcached

import org.scalatest.{ShouldMatchers, FlatSpec}
import net.spy.memcached.{AddrUtil, MemcachedClient}

/**
 *
 * Author: c-birchall
 * Date:   13/11/07
 */
class MemcachedCacheSpec extends FlatSpec with ShouldMatchers {

  val client = new MemcachedClient(AddrUtil.getAddresses("localhost:11211"))

  def memcachedIsRunning = {
    try {
      client.get("foo")
      true
    } catch { case _: Exception => false }
  }

  if (!memcachedIsRunning) {
    alert("Skipping tests because Memcached does not appear to be running on localhost.")
  } else {

    behavior of "get"

    it should "return the value stored in Memcached" in {
      client.set("key1", 0, 123)
      MemcachedCache(client).get("key1") should be(Some(123))
    }

    it should "return None if the given key does not exist in the underlying cache" in {
      MemcachedCache(client).get("non-existent-key") should be(None)
    }

    behavior of "put"

    it should "store the given key-value pair in the underlying cache" in {
      MemcachedCache(client).put("key1", 123)
      client.get("key1") should be(123)
    }

  }


}