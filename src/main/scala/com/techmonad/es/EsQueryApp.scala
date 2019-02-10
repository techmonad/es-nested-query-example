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
  val nestedMust1 = boolQuery()
    .must(nestedQuery("languages", termQuery("languages.id", "1"), ScoreMode.None))
    .must(nestedQuery("languages", rangeQuery("languages.level").gte(0), ScoreMode.None))

  val nestedMust2 = boolQuery()
    .must(nestedQuery("languages", termQuery("languages.id", "3"), ScoreMode.None))
    .must(nestedQuery("languages", rangeQuery("languages.level").gte(0), ScoreMode.None))

  query
    .filter(
      boolQuery()
        .must(nestedMust1)
        .must(nestedMust2)

    )


  val esQueryApi = new EsQueryApi(client)
  val count = esQueryApi.getCount(indexName, query)
  println(count)





}





