package com.techmonad.es

import java.net.InetAddress

import org.apache.lucene.search.join.ScoreMode
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.transport.client.PreBuiltTransportClient


object EsQueryApp extends App {

  val indexName = "testing"
  val client: Client = {
    val nodes = List("localhost")
    val port = 9300
    val hosts = nodes.map { host => new TransportAddress(InetAddress.getByName(host), port) }
    val settings: Settings = Settings.builder().build()
    new PreBuiltTransportClient(settings).addTransportAddresses(hosts: _*)
  }

  val query = boolQuery()

  val firstClause =
    boolQuery()
      .must(termQuery("languages.id", "1"))
      .must(rangeQuery("languages.level").gte(0))

  val secondClause =
    boolQuery()
      .must(termQuery("languages.id", "3"))
      .must(rangeQuery("languages.level").gte(0))

  val nestedMust = boolQuery()
    .must(nestedQuery("languages", firstClause, ScoreMode.None))
    .must(nestedQuery("languages", secondClause, ScoreMode.None))


  query
    .filter(nestedMust)


  val esQueryApi = new EsQueryApi(client)
  val count = esQueryApi.getCount(indexName, query)
  println(count)


}





