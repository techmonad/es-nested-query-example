package com.techmonad.es

import org.elasticsearch.client.Client
import org.elasticsearch.index.query.BoolQueryBuilder

class EsQueryApi(client: Client) {

  def getCount(indexName: String, query: BoolQueryBuilder): Long = {
    println("Query : " + query)
    client.prepareSearch(indexName)
      .setQuery(query)
      .setSize(0)
      .get()
      .getHits.getTotalHits
  }


}
